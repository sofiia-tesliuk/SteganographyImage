package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Image {
    private BufferedImage img = null;
    private File f = null;
    private PixelInImage pixel;

    public Image(String path){
        try{
            f = new File(path);
            img = ImageIO.read(f);
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public void encode(String message){
        if (img != null){
            pixel = new PixelInImage(img);
            for (int i = 0; i < 15; i++) {
                pixel.encodeNextValue(message.length(), i*2);
            }

            char c;
            for (int i = 0; i < message.length(); i++) {
                c = message.charAt(i);
                for (int j = 0; j < 4; j++) {
                    pixel.encodeNextValue(c, j*2);
                }
                pixel.setNewRGB();
            }
        }
    }

    public String decode(){
        if (img != null){
            pixel = new PixelInImage(img);
            int length = 0;
            for (int i = 0; i < 15; i++) {
                length |= pixel.decodeNextValue(i*2);
            }
            char message[] = new char[length];
            char c;
            for (int i = 0; i < length; i++) {
                c = 0;
                for (int j = 0; j < 4; j++) {
                    c |= pixel.decodeNextValue(j*2);
                }
                message[i] = c;
            }
            return String.valueOf(message);

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
