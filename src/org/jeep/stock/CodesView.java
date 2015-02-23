package org.jeep.stock;

import java.io.*;
import java.util.*;

public class CodesView {
    private static String codesfile = "./data/codes";
    private ArrayList ownedCodes = new ArrayList();
    private ArrayList monitorCodes = new ArrayList();
    private boolean debug = false;


    public CodesView()
    {
        parse();
    }

    public String[] getAllCodes()
    {
        ArrayList lists = new ArrayList();
        lists.addAll(ownedCodes);
        lists.addAll(monitorCodes);
        String[] codes = new String[lists.size()];
        lists.toArray(codes);
        return codes;
    }

    public String[] getOwnCodes()
    {
        String[] codes = new String[ownedCodes.size()];
        ownedCodes.toArray(codes);
        return codes;
    }

    public String[] getMonitorCodes()
    {
        String[] codes = new String[monitorCodes.size()];
        monitorCodes.toArray(codes);
        return codes;
    }

    private void parse()
    {
        try{
            BufferedReader in 
                = new BufferedReader(new FileReader(codesfile));
            String line;
            boolean isOwn = false;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if(line.startsWith("#") || line.isEmpty()) {
                    if(debug)
                        System.out.println("CodesView.parse <"+codesfile+"> coment: "
                            + line);
                    continue;
                }
                if(line.equals("[OWN]")) {
                    isOwn = true;
                }else if (line.equals("[MON]")){
                    isOwn = false;
                }else{
                    if(isOwn) {
                        if(debug)
                            System.out.println("CodesView.parse <"+codesfile+"> OWN: "
                                + line);
                        ownedCodes.add(line);
                    } else {
                        if(debug)
                            System.out.println("CodesView.parse <"+codesfile+"> MON: "
                                + line);
                        monitorCodes.add(line);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
