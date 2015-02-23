package org.jeep.stock;

import java.util.Date;

public interface IStock {
//获得股票代码
public String getStockCode();
//获得股票名称
public String getStockName();
//获得当前时间
public Date getCurrentTime();
//获得当前价格
public double getCurrentPrice();
//获得开盘价格
public double getOpen();
//获得前一收盘价格
public double getPreClose();
//获得涨跌率
public double getChange();

}