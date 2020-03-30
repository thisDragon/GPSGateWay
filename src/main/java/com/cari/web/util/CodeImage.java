package com.cari.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;


/**
 * @author 卓诗垚 
 * 登录页验证码图片生成器
 */
public class CodeImage {
	
	public static final String CONTENT_TYPE = "image/jpeg";
	
	private String randValue;//产生随机数的值
	private int width = 50;//图片宽度
	private int height = 20;//图片高度
	private Color bgColor = Color.WHITE;//图片背景颜色
	private Color fontColor = Color.BLACK;//字体颜色
	private boolean isDisturb = true;//是否产生干扰线
	
	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isDisturb() {
		return isDisturb;
	}

	public void setDisturb(boolean isDisturb) {
		this.isDisturb = isDisturb;
	}

	public String getRandValue() {
		return randValue;
	}

	public void setRandValue(String randValue) {
		this.randValue = randValue;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 生成图片
	 * @return
	 */
	public BufferedImage getImage() {
		//在内存中创建图象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		//生成随机类
		Random random = new Random();
		// 设定背景色
		g.setColor(bgColor);
		g.fillRect(0, 0, width, height);

		//设定字体
		g.setFont(new Font("Courier New",Font.BOLD,18));

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		if (isDisturb) {
			g.setColor(getRandColor(160,200));
			for (int i=0;i<155;i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(8);
				int yl = random.nextInt(8);
				g.drawLine(x,y,x+xl-4,y+yl-4);
			}
		}

		//画边框
		g.setColor(Color.BLACK);
		g.drawRect(0,0,width-1,height-1);

		// 取随机产生的认证码(4位数字)
		StringBuffer sbRand = new StringBuffer();
		String sTemp = null;
		for (int i = 0; i < 4; i++) {
			sTemp = "" + random.nextInt(10);
			sbRand.append(sTemp);
			g.setColor(fontColor);
			g.drawString(sTemp,i*12+2,15);		//让数字“上窜下跳”
		}
		randValue = sbRand.toString();

		// 图象生效
		g.dispose();
		return image;
	}

	/**
	 * 输出图片
	 * @param response
	 * @param image
	 */
	public static void outPut(OutputStream out, BufferedImage image) {
		try {
			ImageIO.write(image, "JPEG", out);
		} catch (IOException e) {
		//	e.printStackTrace();
		}finally{
			try{
				if (out != null){
					out.close();
				}
			}catch(Exception e){
			}
		}
	}
	
	/**
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	public Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if(fc>255) fc=255;
		if(bc>255) bc=255;
		int r=fc+random.nextInt(bc-fc);
		int g=fc+random.nextInt(bc-fc);
		int b=fc+random.nextInt(bc-fc);
		return new Color(r,g,b);
	}
}
