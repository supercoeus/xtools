package com.bladejava.xtools.utils;

import com.blade.kit.StringKit;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Img2Ascii {

    private static final Logger LOGGER = LoggerFactory.getLogger(Img2Ascii.class);

    private BufferedImage image;
    private int imageWidth = 100;
    private int textType_count = -1; // Start at the beginning
    private String textType = "sequence";
    private String browser = "ie";
    private String bgColor = "black";
    private int fontSize = -3;
    private char[] textChars = { '0', '1'};
    private int grayscale = 0;

    private String fontHTMLstart = "<font color=";
    private String fontHTMLend = "</font>";

    public Img2Ascii(String filePath){
        this(new File(filePath));
    }

    public Img2Ascii(File file){
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Img2Ascii textChars(char...textChars){
        this.textChars = textChars;
        return this;
    }

    public Img2Ascii textType(String textType){
        this.textType = textType;
        return this;
    }

    public Img2Ascii grayscale(int grayscale){
        this.grayscale = grayscale;
        return this;
    }

    public Img2Ascii browser(String browser){
        this.browser = browser;
        return this;
    }

    public Img2Ascii imageWidth(int imageWidth){
        if (imageWidth < 1 || imageWidth > 500) {
            this.imageWidth = 100;
        } else {
            this.imageWidth = imageWidth;
        }
        return this;
    }

    public String toHtml(){

        int[] colours = {0, 0, 0};
        int[] oldcolours = { -1 };

        try {
            // 压缩
            image = Thumbnails.of(image).width(imageWidth).keepAspectRatio(true).asBufferedImage();

            int width = image.getWidth();
            int height = image.getHeight();

            if(StringKit.equals("ie", browser)){
                height = (int) (height * 0.65);
                image = Thumbnails.of(image).forceSize(width, height).asBufferedImage();
            }
            if(StringKit.equals("firefox", browser)){
                height = (int) (height * 0.43);
                image = Thumbnails.of(image).forceSize(width, height).asBufferedImage();
            }

            if(grayscale == 1){
                this.gray();
            }
            StringBuilder html = new StringBuilder();
            html.append("<table align='center' cellpadding='10'>" +
                    "   <tr bgcolor='" + bgColor + "'>" +
                    "       <td><font size=\"" + fontSize + "\">\n<pre>");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    colours = getRGB(x, y);
                    if (!Arrays.equals(colours, oldcolours)) {
                        String hex = String.format("#%02x%02x%02x", colours[0], colours[1], colours[2]);
                        if (x == 0)
                            html.append(fontHTMLstart + hex + ">" + nextCharacter());
                        else
                            html.append(fontHTMLend + fontHTMLstart + hex + ">" + nextCharacter());
                    } else {
                        html.append(nextCharacter());
                    }
                    oldcolours = colours;
                }
                oldcolours= new int[]{-1};
                html.append(fontHTMLend + "<br>");
            }

            html.append("\n</pre></font>");
            html.append("\n<!-- IMAGE ENDS HERE -->\n");

            return html.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private char nextCharacter() {
        if (textChars.length == 1) {
            return textChars[0];
        }
        if (textType == "random") {
            return textChars[(int) (Math.random() * 10)];
        } else if (textType == "sequence") {
            textType_count++;
            if (textType_count >= textChars.length) {
                textType_count = 0;
                return textChars[0];
            }
            return textChars[textType_count];
        }
        return ' ';
    }
    /**
     * 获取图片RGB数组
     *
     * @param x
     * @param y
     * @return
     */
    public int[] getRGB(int x, int y) {
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

    public void gray() {
        for (int x = 0, width = image.getWidth(); x < width; ++x){
            for (int y = 0; y < image.getHeight(); ++y) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                image.setRGB(x, y, gray);
            }
        }
    }

    /**
     * 将彩色图像转换为灰度图。
     * @param hints 重新绘图使用的 RenderingHints 对象。
     * @return 目标灰度图。
     */
    public BufferedImage gray(RenderingHints hints) {
        BufferedImage dstImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        if (hints == null ) {
            Graphics2D g2 = dstImage.createGraphics();
            hints = g2.getRenderingHints();
            g2.dispose();
            g2 = null ;
        }

        ColorSpace grayCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp colorConvertOp = new ColorConvertOp(grayCS, hints);
        colorConvertOp.filter(image, dstImage);
        return dstImage;
    }

}
