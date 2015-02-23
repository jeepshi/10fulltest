package org.jeep.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import org.jeep.course.util.MessageUtil;

public class Stock {//implements IStock {

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
    private final static String prefixUrl = "http://download.finance.yahoo.com/d/quotes.csv?s=";

    private final static String prefixSinaUrl = "http://hq.sinajs.cn/list=";

    private final static String format = "&f=slc2opmwvn";

    private String stockInfos[];

    private boolean useYahooSite = false;

    private long SLEEP_TIME = 1000 * 300L;
    
    private boolean saveFlag = false;
    
    private boolean loopFlag = false;

    
    
    public Stock(String code) {
    	if(saveFlag)
    		SavingData.startSaving();
    	
        if (code == null || "".equals(code.trim())) {
            // TODO add error log
        } else {
            String urlStr = null;
            if (code.startsWith("0")) {
                if(useYahooSite)
                    urlStr = prefixUrl + code + ".SZ" + format;
                else
                    urlStr = prefixSinaUrl + "sz" + code;

            }
            if (code.startsWith("6")) {
                if(useYahooSite)
                    urlStr = prefixUrl + code + ".SS" + format;
                else
                    urlStr = prefixSinaUrl + "sh" + code;
            }
            if (urlStr == null) {
                // TODO add error log
            }
                        
            //test 
            // urlStr = "http://download.finance.yahoo.com/d/quotes.csv?s=000039.SZ,000001.SZ&f=slc2opmwvn";
            try {
                this.code = code;
                System.out.println("code:" + this.code);
                URL url = new URL(urlStr);
                int loop=0;
                do{
                    URLConnection connection = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                connection.getInputStream()));
                    String msgLine = in.readLine();
                    //yahoo msgLine result is "000039.SZ","10:13pm - <b>22.88</b>","+0.24 - +1.06%",22.70,22.64,"22.60 - 22.99","14.61 - 36.50",1892125,"CIMC"
                    //sina msg:var hq_str_sh600415="D??隆陇,31.01,30.48,31.50,33.45,31.01,31.48,31.50,27619162,888326720,1400,31.48,2000,31.47,10700,31.45,200,31.43,1000,31.42,50066,31.50,100,31.52,1200,31.54,25400,31.55,7290,31.56,2010-11-08,14:39:37";
                    System.out.println("msg:"+msgLine);
                	if(saveFlag)
                		SavingData.save(msgLine, 11, msgLine.length()-2);
                    stockInfos = msgLine.split(",");
                    in.close();
                    loop++;
                    if (loopFlag)
                    	Thread.sleep(SLEEP_TIME);
               } while(loop < 10 && loopFlag);
            } catch (Exception e) {
                e.printStackTrace();
                // TODO add log
            }
        }
    }

    public Stock(String[] codes) {
    	if(saveFlag)
    		SavingData.startSaving();
        String mixCodes = new String("");
        String codesDisplay = "";
        String codesName = "";
        String[] openValues = null;
        String[] preCloseValues = null;
        if(codes!=null && codes.length>0){
            openValues = new String[codes.length]; 
            preCloseValues = new String[codes.length]; 
            for(int i = 0; i < codes.length; i++){
                String c = codes[i];
                if (c.equals("000001")) {
                    if(useYahooSite)
                        mixCodes += c + ".SS," ;
                    else
                        mixCodes += "sh" + c ;
                } else
                if (c.startsWith("0")) {
                    if(useYahooSite)
                        mixCodes += c + ".SZ," ;
                    else
                        mixCodes += "sz" + c ;
                }
                if (c.startsWith("6")) {
                    if(useYahooSite)
                        mixCodes += c + ".SS," ;
                    else
                        mixCodes += "sh" + c ;
                }
                mixCodes += ",";
                codesDisplay += c +"\t";
            }
        }
        if(!"".equals(mixCodes.trim())){
            System.out.println("mix codes:"+mixCodes);
            mixCodes = mixCodes.substring(0, mixCodes.length()-1);
            String urlStr = null;
            if(useYahooSite)
                urlStr = prefixUrl + mixCodes + format;
            else
                urlStr = prefixSinaUrl + mixCodes;
            try {
               // this.code = code;
                int loop = 0;
                boolean first = true;
                String[] curValues = new String[codes.length]; 
                System.out.println(codesDisplay);
                do{
                    URL url = new URL(urlStr);
                    URLConnection connection = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                connection.getInputStream()));
                    //System.out.println("code\tcurrent\topen\tpre close");
                    String msgLine = null;
                    String msgDisplay = "";
                    String msgOpen = "";
                    String msgPreClose = "";
                    int ii = 0;
                    double rate=0.0;
                    while((msgLine = in.readLine())!=null){
                        //System.out.println("msg2:"+msgLine);
                    	if(saveFlag)
                    		SavingData.save(msgLine, 11, msgLine.length()-2);
                        stockInfos = msgLine.split(",");
                        //System.out.println(stockInfos[0].substring(13, 19)+"\t"+stockInfos[3]+"\t"
                        //        +stockInfos[1]+"\t"+stockInfos[2]+"\t"+stockInfos[31]);
                        if(first){
                           codesName +=stockInfos[0].substring(21)+"\t"; 
                           msgOpen +=stockInfos[1]+"\t"; 
                           msgPreClose +=stockInfos[2]+"\t"; 
                           if(openValues!=null){
                                openValues[ii] = stockInfos[1];
                           }
                           if(preCloseValues!=null){
                                preCloseValues[ii] = stockInfos[2];
                           }
                        }
                        curValues[ii] = stockInfos[3];
                        if(preCloseValues!=null){
                            rate = (Double.valueOf(stockInfos[3]) - Double.valueOf(preCloseValues[ii]))*100/Double.valueOf(preCloseValues[ii]);
                        }
                        //msgDisplay +=stockInfos[3]+"\t";
                        msgDisplay +=stockInfos[3]+"("+String.format("%.2f",rate)+"%)";
                        ii++;
                    }
                    msgDisplay +=stockInfos[31]+"\t";
                    in.close();
                    if(first){
                    	System.out.println(codesName);
                        System.out.println("--------------------------------------------------------------------");
                        System.out.println(msgOpen);
                        System.out.println("--------------------------------------------------------------------");
                        System.out.println(msgPreClose);
                        System.out.println("--------------------------------------------------------------------");
                        first = false;
                    }
                    System.out.println(msgDisplay);
                    if (loopFlag)
                    	Thread.sleep(SLEEP_TIME);
                    loop++;
                } while(loop<100 && loopFlag);
            } catch (Exception e) {
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
        //this.name = this.stockInfos[8].substring(1, this.stockInfos[8].length() - 1);
    	this.name = this.stockInfos[0].substring(21);
        return name;
    }

    public double getOpen() {
        this.open = Double.parseDouble(this.stockInfos[1]);
        return open;
    }

    public double getPrevClose() {
        this.prevClose = Double.parseDouble(this.stockInfos[2]);
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
    
    public double getRate() {
    	
    		return  (Double.valueOf(stockInfos[3]) - Double.valueOf(stockInfos[2]))*100/Double.valueOf(stockInfos[2]);
    }
    
    public String getWeiXinMessage() {
    	/*
    	 * 微信格式： 股票名称(代码): 当前价， 涨跌率，成交量,开盘价，昨收盘价
    	 * 
    	 */
    	String msg=null;
    	
    	if (this.code != null) {
    		double rate;
    		String rateMsg;
    		rate = getRate();
    		rateMsg = String.format("%.2f",getRate())+"%";
    		if (rate > 0) {
    			rateMsg = rateMsg + MessageUtil.emoji(0x1F44D);
    		} else {
    			rateMsg = rateMsg + MessageUtil.emoji(0x1F44E);
    		}
    		msg = this.getName()+"("+this.code+") "+ stockInfos[3] + " " + rateMsg
    				+" \n开盘价: "+stockInfos[1]+"  昨收盘价: "+stockInfos[2];
    		
    	}
    	
    	return msg;
    }
    
    public void finalize()
    {
    	if(saveFlag)
    		SavingData.stopSaving();
    }

}
