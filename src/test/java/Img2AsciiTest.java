import com.bladejava.xtools.utils.Img2Ascii;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by oushaku on 16/9/3.
 */
public class Img2AsciiTest {


    public static void main(String[] args) {
        Img2Ascii img2Ascii = new Img2Ascii("/Users/oushaku/Documents/5c7f5c6034a85edfb825426649540923dc54759c.jpg");
        //String html = img2Ascii.imageWidth(130).toHtml();
        String html = img2Ascii.imageWidth(130).grayscale(1).toHtml();
        try {
            FileWriter fileWriter = new FileWriter(new File("test.html"));
            fileWriter.write(html);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
