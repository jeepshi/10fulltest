package org.jeep.course.service;
import java.util.Date;  
import java.util.Map;  

import javax.servlet.http.HttpServletRequest;  

import org.jeep.course.message.resp.TextMessage;  
import org.jeep.course.util.MessageUtil;  
import org.jeep.stock.Stock;

public class CoreService {
    /** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;
        String txtContent = null;
        try {  
            // 默认返回的文本消息内容  
            String respContent = "请求处理异常，请稍候尝试！";  
  
            // xml请求解析  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
  
            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");  
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");  
            // 消息类型  
            String msgType = requestMap.get("MsgType");  
  
            // 回复文本消息  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
            textMessage.setFuncFlag(0);  
            
            
            
            // 文本消息  
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
            	txtContent = requestMap.get("Content");
            	System.out.println(txtContent);
            	if (txtContent.equals("？")){
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
            		respContent = "您发送的是文本消息！";
            	}
            	
            }  
            // 图片消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "您发送的是图片消息!！";  
            }  
            // 地理位置消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
                respContent = "您发送的是地理位置消息！";  
            }  
            // 链接消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
                respContent = "您发送的是链接消息！";  
            }  
            // 音频消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
                respContent = "您发送的是音频消息！";  
            }  
            // 事件推送  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
                // 事件类型  
                String eventType = requestMap.get("Event");  
                // 订阅  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "谢谢您的关注！";  
                }  
                // 取消订阅  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
                }  
                // 自定义菜单点击事件  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
                    // TODO 自定义菜单权没有开放，暂不处理该类消息  
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
    	buffer.append("请回复数字选择服务：").append("\n\n");
    	buffer.append("1. 天气预报").append("\n");
    	buffer.append("2. 股票查询").append("\n");
    	
    	buffer.append("\n");
    	buffer.append("回复“?”显示此帮助菜单"); 
    	
    	return buffer.toString();
    }
    
   
    
}
