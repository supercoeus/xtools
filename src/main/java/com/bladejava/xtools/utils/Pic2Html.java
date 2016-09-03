package com.bladejava.xtools.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;


import net.coobird.thumbnailator.Thumbnails;

public class Pic2Html {

	static long timestart = System.currentTimeMillis() / 1000; // Check when
																// script
																// started

	static int textType_count = -1; // Start at the beginning
	static String textType = "sequence"; // Default to sequence
	static String[] HTMLcharacter = { "0", "1" }; // Default to 0's and 1's
	static String bgColor = "black"; // Default to black background
	static int fontSize = -3; // Default to font size -3
	static int grayscale = 0; // Default to colour
	static String browser = "ie"; // Default to Internet Explorer
	static int contrast = 0; // Apply contrast curve
	
	// Some more strings that will be used

	static String imageURL;
	static String HTMLheader, beginHTML, endHTML, fontHTMLstart, fontHTMLend;

	static int[] contrastcurve = {0,0,0,0,0,0,0,0,1,1,1,1,1,2,2,2,2,3,3,3,4,4,5,5,6,6,6,7,8,8,9,9,10,10,11,12,12,13,14,14,15,16,17,17,18,19,20,21,22,23,23,24,25,26,27,28,29,30,31,32,33,34,35,37,38,39,40,41,42,43,45,46,47,48,49,51,52,53,54,56,57,58,60,61,62,64,65,66,68,69,71,72,73,75,76,78,79,81,82,84,85,87,88,90,91,93,94,96,97,99,100,102,103,105,106,108,109,111,113,114,116,117,119,120,122,124,125,127,128,130,131,133,135,136,138,139,141,142,144,146,147,149,150,152,153,155,156,158,159,161,162,164,165,167,168,170,171,173,174,176,177,179,180,182,183,184,186,187,189,190,191,193,194,195,197,198,199,201,202,203,204,206,207,208,209,210,212,213,214,215,216,217,218,220,221,222,223,224,225,226,227,228,229,230,231,232,232,233,234,235,236,237,238,238,239,240,241,241,242,243,243,244,245,245,246,246,247,247,248,249,249,249,250,250,251,251,252,252,252,253,253,253,253,254,254,254,254,254,255,255,255,255,255,255,255,255};
	
	static void setHTMLvars() {
		HTMLheader = "<!-- Generated on " + new Date().toLocaleString() + " using pic2html.cgi, by Patrik Roos -->\n\n";
		beginHTML = "<html>\n\t<head>\n\t\t<title>Text image</title>\n\t</head>\n<body>\n";
		endHTML = "</td></tr></table></td></tr></table></BODY></HTML>";
		fontHTMLstart = "<font color=";
		fontHTMLend = "</font>";
	}
	
	// Function to write an HTML error page

	static void werr(String errorMessage) {
		setHTMLvars();
		write(HTMLheader + "\n" + beginHTML + "\n");
		write("<table align=center><tr bgcolor=black><td><font color=lightblue size=4>Error: " + errorMessage
				+ "</font></td></tr></table>\n");
		write(endHTML + "\n");
		return;
	}

	// Function to choose next character for output

	static String nextCharacter() {
		if (HTMLcharacter.length == 1) {
			return HTMLcharacter[0];
		}
		if (textType == "random") {
			return HTMLcharacter[(int) (Math.random() * 10)];
		} else if (textType == "sequence") {
			textType_count++;
			if (textType_count >= HTMLcharacter.length) {
				textType_count = 0;
				return HTMLcharacter[0];
			}
			return HTMLcharacter[textType_count];
		}
		return "";
	}
	
	static StringBuffer stringBuffer = new StringBuffer();
	
	public static void write(String content) {
		stringBuffer.append(content);
	}

	/**
	 * 获取图片RGB数组
	 *
	 * @return
	 */
	public static int[] getRGB(BufferedImage image, int x, int y) {
		int[] rgb = null;

		if (image != null && x < image.getWidth() && y < image.getHeight()) {
			rgb = new int[3];
			int pixel = image.getRGB(x, y);
			rgb[0] = (pixel & 0xff0000) >> 16;
			rgb[1] = (pixel & 0xff00) >> 8;
			rgb[2] = (pixel & 0xff);
		}

		return rgb;
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis() / 1000;
		
		setHTMLvars();
		int[] colours = {0, 0, 0};
		int[] oldcolours = { -1 };

		BufferedImage image = ImageIO
				.read(new File("/Users/oushaku/Documents/5c7f5c6034a85edfb825426649540923dc54759c.jpg"));
		
		
		//Thumbnails.of(image).size(image.getWidth(), image.getHeight()).outputQuality(0.9).toFile(new File("a1.jpeg"));
//		image = Thumbnails.of(image).size(image.getWidth(), image.getHeight()).outputQuality(0.9).asBufferedImage();
		
		int imageWidth = 130;
		
		// Check if width is within limits, and force to 100 if not
        if (imageWidth < 1 || imageWidth > 500) { imageWidth = 100; }
        
        //Thumbnails.of(image).width(imageWidth).keepAspectRatio(true).toFile(new File("a2.jpg"));
        
        image = Thumbnails.of(image).width(imageWidth).keepAspectRatio(true).asBufferedImage();
        
        int width = image.getWidth();
		int height = (int) (image.getHeight() * 0.65);
        
        //Thumbnails.of(image).forceSize(width, height).toFile(new File("a3.jpg"));
        image = Thumbnails.of(image).forceSize(width, height).asBufferedImage();
        
		write(beginHTML);
		write("<table width=\"80%\"><tr><td><h2>Result</h2><P>");
		
        write("\t<table align=\"center\" cellpadding=\"10\">\n\t\t<tr bgcolor=\"" + bgColor + "\">\n\t\t\t<td>\n\n<!-- IMAGE BEGINS HERE -->\n<font size=\"" + fontSize + "\">\n<pre>");
        
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				colours = getRGB(image, x, y);
				if (!Arrays.equals(colours, oldcolours)) {
					String hex = String.format("#%02x%02x%02x", colours[0], colours[1], colours[2]);
					if (x == 0)
						write(fontHTMLstart + hex + ">" + nextCharacter());
					else
						write(fontHTMLend + fontHTMLstart + hex + ">" + nextCharacter());
				} else {
					write(nextCharacter());
				}

				oldcolours = colours;
			}
			oldcolours= new int[]{-1};
			write(fontHTMLend + "<br>");
		}
		
		// We're done! Finish up the HTML page, and tell the user how long it took
        write("\n</pre></font>");
        write("\n<!-- IMAGE ENDS HERE -->\n");
        long end = System.currentTimeMillis() / 1000;
        write("\n<P><FONT COLOR=LIGHTBLUE SIZE=2>Rendering time: "+ (end - start) +" seconds.</FONT><BR>\n");
        
        write(endHTML);
        
        FileWriter writer=new FileWriter("a.html");
        writer.write(stringBuffer.toString());
        writer.close();
        
	}

	/** 
     * 将彩色图像转换为灰度图。
     * @param srcImage 源彩色图像。
     * @param hints 重新绘图使用的 RenderingHints 对象。
     * @return 目标灰度图。
     */ 
     public static BufferedImage transformGrayJ2D(BufferedImage srcImage,RenderingHints hints) {
        BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());
       
        if (hints == null ) {
            Graphics2D g2 = dstImage.createGraphics();
            hints = g2.getRenderingHints();
            g2.dispose();
            g2 = null ;
        } 
       
        ColorSpace grayCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp colorConvertOp = new ColorConvertOp(grayCS, hints);
        colorConvertOp.filter(srcImage, dstImage);
       
        return dstImage;
    }
}
