import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Image {
    private BufferedImage img = null;
    private File f = null;
    private PixelInImage pixel;
    private String message;

    public Image(String path){
        try{
            f = new File(path);
            img = ImageIO.read(f);
        } catch(IOException e){
            System.out.println(e);
        }
    }


    public void encode(String message){
        this.message = message;
        if (img != null){
            pixel = new PixelInImage(img);
            char c;
            for (int i = 0; i < message.length(); i++) {
                c = message.charAt(i);
                pixel.encodeR(c, 0);
                pixel.encodeG(c, 2);
                pixel.nextPixel();
                pixel.encodeR(c, 4);
                pixel.encodeG(c, 8);
                pixel.nextPixel();
            }
        }
    }



    public String decode(){
        if (img != null){
            pixel = new PixelInImage(img);
            char mess[] = new char[message.length()];
            int first;
            int second;
            int third;
            int fourth;
            for (int i = 0; i < message.length(); i++) {
                first = pixel.decodeR();
                second = pixel.decodeB();
                pixel.nextPixel();
                third = pixel.decodeR();
                fourth = pixel.decodeB();
                pixel.nextPixel();
                int c_int = (first << 6) | (second<<4) | (third<<2) | fourth;
                mess[i] = (char) c_int;
            }
            return mess.toString();

        }
        return "ERROR: Image not found.";
    }

    public void save(String newPath){
        try{
            f = new File(newPath);
            ImageIO.write(img, "bmp", f);
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
