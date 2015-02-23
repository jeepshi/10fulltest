package org.jeep.stock;


public interface StockChangeListener {

	public void stockChange(String code, Stock stock);
	
}
