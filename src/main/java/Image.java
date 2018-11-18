import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Image {
    private BufferedImage img = null;
    private File f = null;
    private int imgWidth;
    private int imgHeight;

    public Image(String path){
        try{
            f = new File(path);
            img = ImageIO.read(f);
            imgWidth = img.getWidth();
            imgHeight = img.getHeight();
        } catch(IOException e){
            System.out.println(e);
        }
    }

    private int encodeValue(int value, char[] encodeBits){
        char[] valueArray = Integer.toBinaryString(value).toCharArray();
        for (int i = 0; i < encodeBits.length; i++) {
            valueArray[i] = encodeBits[i];
        }
        return Integer.parseInt(new String(valueArray), 2);
    }

    private void encodePixel(int x, int y, char[] encodeBits){
        int p = img.getRGB(x, y);

        int a = (p>>24)&0xff;
        int[] rgb = new int[3];
        rgb[0] = (p>>16)&0xff;
        rgb[1] = (p>>8)&0xff;
        rgb[2] = p&0xff;

        for (int i = 0; i < encodeBits.length; i++) {
            rgb[i] = encodeValue(rgb[i], Arrays.copyOfRange(encodeBits, i * 2, i * 2 + 1));
        }

        p = (a<<24) | (rgb[0]<<16) | (rgb[1]<<8) | rgb[2];
        img.setRGB(x, y, p);
    }

    private void encodeByte(int x1, int y1, int x2, int y2, char[] encodeBits) {
        encodePixel(x1, y1, Arrays.copyOfRange(encodeBits, 0, 4));
        encodePixel(x2, y2, Arrays.copyOfRange(encodeBits, 4, 8));
    }

    public void encode(String message){
        if (img != null){
            char[] lenghtBinary = Integer.toBinaryString(message.length()).toCharArray();
            for (int i = 0; i < 4; i++) {
                encodeByte(i, 0, imgWidth - i, imgHeight, Arrays.copyOfRange(lenghtBinary, i * 8, i * 8 + 8));
            }
            int x = 4;
            int y = 0;
            char[] charBinary;
            char c;
            for (int i = 4; i < message.length() + 4; i++) {
                c = message.charAt(i - 4);
                charBinary = Integer.toBinaryString(c).toCharArray();
                encodeByte(x, y, imgWidth - x, imgHeight - y, charBinary);
                x += 1;
                if (x == imgWidth){
                    x = 0;
                    y += 1;
                }
            }
        }
    }

    private char[] decodeValue(int value, int numberOfEncodedBits){
        char[] valueArray = Integer.toBinaryString(value).toCharArray();
        return Arrays.copyOfRange(valueArray, 0, numberOfEncodedBits);
    }

    private char[] decodePixel(int x, int y, int numberOfEncodedValues, int numberOfEncodedBitsInValue){
        char[] decodedBits = new char[numberOfEncodedBitsInValue * numberOfEncodedValues];
        int p = img.getRGB(x, y);

        int a = (p>>24)&0xff;
        int[] rgb = new int[3];
        rgb[0] = (p>>16)&0xff;
        rgb[1] = (p>>8)&0xff;
        rgb[2] = p&0xff;

        char[] decodedValue;
        for (int i = 0; i < numberOfEncodedValues; i++) {
             decodedValue = decodeValue(rgb[i], numberOfEncodedBitsInValue);
            for (int j = 0; j < numberOfEncodedBitsInValue; j++) {
                decodedBits[i * numberOfEncodedBitsInValue + j] = decodedValue[j];
            }
        }

        return decodedBits;
    }

    private char[] decodeByte(int x1, int y1, int x2, int y2){
        char[] decodedBits = new char[8];
        char[] decodedPixel;
        decodedPixel = decodePixel(x1, y1, 2, 2);
        for (int i = 0; i < 4; i++) {
            decodedBits[i] = decodedPixel[i];
        }
        decodedPixel = decodePixel(x2, y2, 2, 2);
        for (int i = 0; i < 4; i++) {
            decodedBits[i + 4] = decodedPixel[i];
        }
        return decodedBits;
    }

    public String decode(){
        if (img != null){
            char[] lengthChar = new char[32];
            char[] lengthChar1;
            for (int i = 0; i < 4; i++) {
                lengthChar1 = decodeByte(i, 0, imgWidth - i, imgHeight);
                for (int j = 0; j < 8; j++) {
                    lengthChar[i * 8 + j] = lengthChar1[j];
                }
            }
            int lenght = Integer.parseInt(new String(lengthChar), 2);
            char[] message = new char[lenght];
            int x = 4;
            int y = 0;
            for (int i = 4; i < lenght + 4; i++) {
                message[i - 4] = (char) Integer.parseInt(new String(decodeByte(x, y, imgWidth - x, imgHeight - y)));
                x += 1;
                if (x == imgWidth){
                    x = 0;
                    y += 1;
                }
            }
            return new String(message);
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
