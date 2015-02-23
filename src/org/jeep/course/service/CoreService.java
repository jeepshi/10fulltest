package org.jeep.course.service;
import java.util.Date;  
import java.util.Map;  

import javax.servlet.http.HttpServletRequest;  

import org.jeep.course.message.resp.TextMessage;  
import org.jeep.course.util.MessageUtil;  
import org.jeep.stock.Stock;

public class CoreService {
    /** 
     * ����΢�ŷ��������� 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;
        String txtContent = null;
        try {  
            // Ĭ�Ϸ��ص��ı���Ϣ����  
            String respContent = "�������쳣�����Ժ��ԣ�";  
  
            // xml�������  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
  
            // ���ͷ��ʺţ�open_id��  
            String fromUserName = requestMap.get("FromUserName");  
            // �����ʺ�  
            String toUserName = requestMap.get("ToUserName");  
            // ��Ϣ����  
            String msgType = requestMap.get("MsgType");  
  
            // �ظ��ı���Ϣ  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
            textMessage.setFuncFlag(0);  
            
            
            
            // �ı���Ϣ  
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
            	txtContent = requestMap.get("Content");
            	System.out.println(txtContent);
            	if (txtContent.equals("��")){
            		respContent = getMainMenu();
            	}
            	else if (txtContent.startsWith("1")) {
            		
            	}
            	else if (txtContent.startsWith("2")) {
            		txtContent = txtContent.substring(2, txtContent.length());
            		Stock stock;
            		stock = new Stock(txtContent);
            		//respContent = stock.getName();
            		respContent = stock.getWeiXinMessage();
            		System.out.println(respContent);
            	}
            	else {
            		respContent = "�����͵����ı���Ϣ��";
            	}
            	
            }  
            // ͼƬ��Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "�����͵���ͼƬ��Ϣ!��";  
            }  
            // ����λ����Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
                respContent = "�����͵��ǵ���λ����Ϣ��";  
            }  
            // ������Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
                respContent = "�����͵���������Ϣ��";  
            }  
            // ��Ƶ��Ϣ  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
                respContent = "�����͵�����Ƶ��Ϣ��";  
            }  
            // �¼�����  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
                // �¼�����  
                String eventType = requestMap.get("Event");  
                // ����  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "лл���Ĺ�ע��";  
                }  
                // ȡ������  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO ȡ�����ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ  
                }  
                // �Զ���˵�����¼�  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
                    // TODO �Զ���˵�Ȩû�п��ţ��ݲ����������Ϣ  
                }  
            }  
  
            //respContent = respContent + fromUserName + "->" + toUserName;
            
            textMessage.setContent(respContent);  
            respMessage = MessageUtil.textMessageToXml(textMessage);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }  
    
    public static String getMainMenu() {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("��ظ�����ѡ�����").append("\n\n");
    	buffer.append("1. ����Ԥ��").append("\n");
    	buffer.append("2. ��Ʊ��ѯ").append("\n");
    	
    	buffer.append("\n");
    	buffer.append("�ظ���?����ʾ�˰����˵�"); 
    	
    	return buffer.toString();
    }
    
   
    
}
