package org.jeep.stock;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class StockKImage extends JPanel{
	private String code;
	private String preUrl = "http://image.sinajs.cn/newchart/daily/n/";
	
	Image img;
	
	public StockKImage (String code)
	{
		super();
		this.code = code;
/*		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setTitle("View Test");*/
	}
	
	public void update()
	{
		if(this.code == null)
			return;
		String urlStr = preUrl;
		
		if (code.startsWith("6")) {
			urlStr += "sh" + this.code +".gif";
		}else{
			urlStr += "sz" + this.code +".gif";
		}
		
		try {
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();
			String type = connection.getContentType();
			String encode = connection.getContentEncoding();
			Object obj = connection.getContent();
			
			System.out.println("type:"+type+", encode:"+encode+", obj:"+obj);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
            img = Toolkit.getDefaultToolkit().createImage(url);
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForAll();
            tracker.removeImage(img,0);
            System.out.println("width:"+img.getWidth(null)+", height:"+img.getHeight(null));
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	public void paint(Graphics g){
		super.paint(g);
		System.out.println("img:"+img);
		g.drawImage(img, 0, 80, this);
//		g.drawImage(img, 80, 80, 900, 530, 0, 0, img.getWidth(null),img.getHeight(null),  this);
	} 
	
	public void update(Graphics g) {
		System.out.println(".update() img:"+img);
	}
	
	public static void main(String[] args)
	{

		StockKImage k = new StockKImage("600200");
		JFrame frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().setLayout(null);
		frame.setTitle("View Test");
		frame.getContentPane().add(k);
		k.update();
		frame.setBounds(50, 50, 800, 600);
		frame.setBackground(Color.RED);
		frame.setVisible(true);
	}
}
