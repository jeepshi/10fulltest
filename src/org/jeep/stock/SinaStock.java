package org.jeep.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

public class SinaStock implements IStock {

    private String name;// n

    private String code;//

    private double lastTrade;// l

    private Date time;// l

    private double changeAmt;// c2

    private String changePercentage;// c2

    private double open;// o

    private double prevClose;// p

    private String dayRange;// m

    private String yearRange;// w

    // ----- runtime

    //    private final static String prefixUrl = "http://quote.yahoo.com/d/quotes.csv?s=";

    //this url is faster than previous url.
    private final static String prefixSinaUrl = "http://hq.sinajs.cn/list=";
    
    /***********************************
     * display order:
     * 0: stock name
     * 1: open price today
     * 2: last pre close price
     * 3: current price
     * 4: highest price today
     * 5: lowest price today
     * 6: 1st ask price
     * 7: 1st bid price
     * 8: volume of stock
     * 9: volume of money
     * 10: volume stock of 1st ask
     * 11: price of 1st ask 
     * ...
     * 18: volume stock of 5th ask
     * 19: price of 5th ask
     * 20: volume stock of 1st bid
     * 21: price of 1st ask
     * ...
     * 30: date
     * 31: time
     */

    private String stockInfos[];

    public SinaStock(String code) {
        if (code == null || "".equals(code.trim())) {
            // TODO add error log
        } else {
            String urlStr = null;
            if (code.startsWith("0")) {
                urlStr = prefixSinaUrl + "sz" + code;

            }
            if (code.startsWith("6")) {
                urlStr = prefixSinaUrl + "sh" + code;
            }
            if (urlStr == null) {
                // TODO add error log
            }
                        
            //test 
            // urlStr = "http://download.finance.yahoo.com/d/quotes.csv?s=000039.SZ,000001.SZ&f=slc2opmwvn";
            try {
                this.code = code;
                URL url = new URL(urlStr);
                URLConnection connection = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                String msgLine = in.readLine();
                //msgLine result is "000039.SZ","10:13pm - <b>22.88</b>","+0.24 - +1.06%",22.70,22.64,"22.60 - 22.99","14.61 - 36.50",1892125,"CIMC"
                System.out.println("msg:"+msgLine);
                stockInfos = msgLine.split(",");
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO add log
            }
        }
    }

    public SinaStock(String[] codes) {
        String mixCodes = new String("");
        if(codes!=null && codes.length>0){
            for(int i = 0; i < codes.length; i++){
                String c = codes[i];
                if (c.startsWith("0")) {
                    mixCodes += c + ".SZ," ;
                }
                if (c.startsWith("6")) {
                    mixCodes += c + ".SS," ;
                }
            }
        }
        if(!"".equals(mixCodes.trim())){
            System.out.println("mix codes:"+mixCodes);
            mixCodes = mixCodes.substring(0, mixCodes.length()-1);
            String urlStr = null;
            urlStr = prefixSinaUrl + mixCodes;
            try {
               // this.code = code;
                URL url = new URL(urlStr);
                URLConnection connection = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                String msgLine = null;
                while((msgLine = in.readLine())!=null){
                    System.out.println("msg2:"+msgLine);
                    stockInfos = msgLine.split(",");
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO add log
            }
        }
    }

    public double getChangeAmt() {
        this.parseChange();
        return changeAmt;
    }

    private void parseChange(){
        if(this.changePercentage != null){
            return;
        }
        if(this.stockInfos != null && this.stockInfos.length > 0){
            String change = this.stockInfos[2];
            String[] tempStr = change.substring(1, change.length() - 1).split(" - ");
            this.changeAmt = Double.parseDouble(tempStr[0]);
            this.changePercentage = tempStr[1];
        }
    }

    public String getChangePercentage() {
        this.parseChange();
        return changePercentage;
    }

    public String getCode() {
        return code;
    }

    public String getDayRange() {
        this.dayRange = this.stockInfos[5].substring(1, stockInfos[5].length() - 1);
        return dayRange;
    }

    private void parseLastStatus(){
        if(this.time != null || this.stockInfos == null){
            return;
        }
        int index = this.stockInfos[1].indexOf('m');
        String temp = this.stockInfos[1].substring(1, index);//eg. 0:00a
        int hour = Integer.parseInt(temp.substring(0, temp.length() - 4));
        if(temp.indexOf('p') > -1){
            //for pm
            hour += 12;
        }
        //for time zone
        hour += 13;

        int min = Integer.parseInt(temp.substring(temp.length() - 3, temp.length() - 1));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        this.time = calendar.getTime();

        //for last trade
        temp = this.stockInfos[1].substring(this.stockInfos[1].indexOf("<b>") + 3, this.stockInfos[1].indexOf("</b>"));
        this.lastTrade = Double.parseDouble(temp);
    }


    public double getLastTrade() {
        this.parseLastStatus();
        return lastTrade;
    }

    public Date getTime() {
        this.parseLastStatus();
        return time;
    }


    public String getName() {
        this.name = this.stockInfos[8].substring(1, this.stockInfos[8].length() - 1);
        return name;
    }

    public double getOpen() {
        this.open = Double.parseDouble(this.stockInfos[3]);
        return open;
    }

    public double getPrevClose() {
        this.prevClose = Double.parseDouble(this.stockInfos[4]);
        return prevClose;
    }

    public String getVolume() {
        return this.stockInfos[7];
    }

    public String getYearRange() {
        if(this.yearRange == null){
            yearRange = this.stockInfos[6].substring(1, this.stockInfos[6].length() - 1);
        }
        return this.yearRange;
    }

	@Override
	public double getChange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrentPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getCurrentTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getPreClose() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStockCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStockName() {
		// TODO Auto-generated method stub
		return null;
	}

}
