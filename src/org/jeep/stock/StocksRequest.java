package org.jeep.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class StocksRequest implements Runnable{
	
private static StocksRequest request=null;
//新浪接口
private static String prefixSinaUrl = "http://hq.sinajs.cn/list=";
private String reqCodes;
private static long REQ_INTERVAL = 3*60*1000L; //间隔为3分钟

private Object obj = new Object();

public StocksRequest()
{
	reqCodes = new String("");
	new Thread(this).start();
}

public static synchronized StocksRequest getInstance()
{
	if(request == null)
		request = new StocksRequest();
	
	return request;
}

public void add(String code)
{
	synchronized(obj){
		if(code != null && code.length() == 6){
			if (code.startsWith("0")) {  //深市
				reqCodes += "sz" + code + ",";
			}else if(code.startsWith("6")){  //沪市
				reqCodes += "sh" + code + ",";
			}else{
				//error wrong code
			}
				
		}
	}
}

public void add(String[] codes)
{
	if(codes!=null && codes.length>0){
		for(int i=0; i<codes.length; i++){
			add(codes[i]);
		}
	}
}

public void remove(String code)
{
	
}

public void remove(String[] codes)
{
	
}

public void clear()
{
	
}

public void addStockChangeListener(StockChangeListener l)
{
	
}

public void removeStockChangeListener(StockChangeListener l)
{
	
}

public void run()
{
	String urlStr = null;
	String[] stockInfos;
	String mixCodes;
	long start, end, t;
	do {
		synchronized(obj){
			mixCodes = reqCodes;
		}
		start = System.currentTimeMillis();
		if(!"".equals(mixCodes.trim())){
	        System.out.println("mix codes:"+mixCodes);
	        mixCodes = mixCodes.substring(0, mixCodes.length()-1);
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
		end = System.currentTimeMillis();
		
		t = end - start;
		if(t < REQ_INTERVAL){
			try{
				Thread.sleep(REQ_INTERVAL - t);
			}catch(InterruptedException e){
				
			}
		}
		
	}while(true);
}


}
