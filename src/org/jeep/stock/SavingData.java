package org.jeep.stock;

import java.io.*;

public class SavingData {
    private static String dataFile = "data.txt";
    private static boolean starting = false;
    private static RandomAccessFile raf = null;
    private static File file = null;

    public static void startSaving()
    {
	if(starting)
	    return;
	
	
	try{
	    file = new File(dataFile);
	    raf = new RandomAccessFile(file, "rw");
	} catch (Exception e) {
	    System.err.println("SavingData: new RandomAccessFile error:"+e.getMessage());
	}
	    
	starting = true;
    }

    public static void stopSaving()
    {
	try{
	    if(raf!=null)
	    	raf.close();
	} catch (Exception e) {
	    System.err.println("SavingData: close raf error:"+e.getMessage());
	}
	starting = false;
    }

    public static void save(String str)
    {
    	if(str!=null)
    		save(str, 0, str.length());
    }

    public static void save(String str, int from, int to)
    {
	if(str==null)
	    return;

	String s = str.substring(from, to);
	s = s.concat("\n");
	try{
	    raf.seek(file.length());
	    raf.write (s.getBytes());
        } catch (Exception e) {
            System.err.println("SavingData: write error:"+e.getMessage());                    
        }

    }

    

}