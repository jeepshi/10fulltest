package org.jeep.course.message.req;

public class ImageMessage extends BaseMessage {
	private String picUrl;
	
	public String getPicUrl() {
		return picUrl;
	}
	
	public void setPicUrl(String url) {
		picUrl = url;
	}
}
