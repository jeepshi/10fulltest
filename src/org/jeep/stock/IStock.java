package org.jeep.stock;

import java.util.Date;

public interface IStock {
//��ù�Ʊ����
public String getStockCode();
//��ù�Ʊ����
public String getStockName();
//��õ�ǰʱ��
public Date getCurrentTime();
//��õ�ǰ�۸�
public double getCurrentPrice();
//��ÿ��̼۸�
public double getOpen();
//���ǰһ���̼۸�
public double getPreClose();
//����ǵ���
public double getChange();

}