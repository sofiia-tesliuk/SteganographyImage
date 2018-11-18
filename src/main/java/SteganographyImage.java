import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;


public class SteganographyImage {
    private BufferedImage img = null;
    private File f = null;
    private int imgWidth;
    private int imgHeight;

    public static int bitsPrint(String str, BitSet bbb){
        System.out.print(str);
        for (int i = 0; i < 8; i++) {
            if (bbb.get(i)){
                System.out.print('1');
            } else{
                System.out.print('0');
            }
        }
        System.out.println();
        return 0;
    }

    public SteganographyImage(String path){
        try{
            f = new File(path);
            img = ImageIO.read(f);
            imgWidth = img.getWidth();
            imgHeight = img.getHeight();
        } catch(IOException e){
            System.out.println(e);
        }
    }

    private void encodePixel(int x, int y, BitSet encodeBits){
        int p = img.getRGB(x,y);

        int a = (p>>24)&0xff;
        int[] rgb = new int[3];
        rgb[0] = (p>>16)&0xff;
        rgb[1] = (p>>8)&0xff;
        rgb[2] = p&0xff;

        bitsPrint("encode Bits: ", encodeBits);
        try {
            for (int i = 0; i < 3; i++) {
                rgb[i] = encodeValue(rgb[i], encodeBits.get(2 * i, 2 * i + 1));
            }
        }catch (ArrayIndexOutOfBoundsException e){
            // bits for encoding ended
        }
        p = (a<<24) | (rgb[0]<<16) | (rgb[1]<<8) | rgb[2];
        img.setRGB(x, y, p);
    }

    private int encodeValue(int value, BitSet encodeBits){
        BitSet valueBitSet = BitSet.valueOf(new byte[]{(byte) value});
        BitSet newValueBitSet = BitSet.valueOf(ByteBuffer.allocate(1));
        for (int i = 0; i < encodeBits.size(); i++) {
            if (encodeBits.get(i)){
                newValueBitSet.set(i);
            }
        }
        for (int i = encodeBits.size(); i < 8; i++) {
            if (valueBitSet.get(i)){
                newValueBitSet.set(i);
            }
        }
        bitsPrint("r: ", newValueBitSet);

        return (int) newValueBitSet.toByteArray()[0];
    }

    public void encode(String message){
        if (img != null){
            byte byteMessage[]= new byte[message.length() + 1];
            byteMessage[0] = (byte) message.length();
            for (int i = 1; i < message.length() + 1; i++) {
                byteMessage[i] = (byte) message.charAt(i - 1);
            }
            BitSet bitMessage = BitSet.valueOf(byteMessage);
            bitsPrint("first char: ", bitMessage.get(0, 8));
            //System.out.println(bitMessage.toString());
            int x = 0, y = 0;
            for (int i = 0; i < 8 * (message.length()); i += 6) {
                encodePixel(x, y, bitMessage.get(i, i + 6));
                x += 1;
                if (x == imgWidth){
                    x = 0;
                    y += 1;
                }
            }
        }
    }

    private BitSet decodePixel(int x, int y){
        int p = img.getRGB(x,y);

        int a = (p>>24)&0xff;
        int[] rgb = new int[3];
        rgb[0] = (p>>16)&0xff;
        rgb[1] = (p>>8)&0xff;
        rgb[2] = p&0xff;

        BitSet resBitSet = BitSet.valueOf(ByteBuffer.allocate(1));
        BitSet rgbBitSet;
        for (int i = 0; i < 3; i++) {
            rgbBitSet = decodeValue(rgb[i]);
            for (int j = 0; j < 2; j++) {
                if (rgbBitSet.get(j)){
                    resBitSet.set(i * 2 + j);
                }
            }
        }
        return resBitSet;
    }

    private BitSet decodeValue(int value){
        BitSet valueBitSet = BitSet.valueOf(new byte[]{(byte) value});
        return valueBitSet.get(0, 2);
    }

    public String decode(){
        if (img != null){
            BitSet length1 = decodePixel(0, 0);
            BitSet length2 = decodePixel(1, 0);
            BitSet lengthBitSet = BitSet.valueOf(ByteBuffer.allocate(1));
            for (int i = 0; i < 6; i++) {
                if (length1.get(i)){
                    lengthBitSet.set(i);
                }
            }
            for (int i = 0; i < 2; i++) {
                if (length2.get(i)){
                    lengthBitSet.set(i + 6);
                }
            }
            int length = (int) lengthBitSet.toByteArray()[0];

            BitSet message = BitSet.valueOf(ByteBuffer.allocate(1));
            for (int i = 2; i < 8; i++) {
                if (length2.get(i)){
                    message.set(i - 2);
                }
            }
            int counter = 4;
            int x = 2;
            int y = 0;
            while (counter < 8 * length){
                BitSet newBitSet = decodePixel(x, y);
                for (int i = 0; i < 6; i++) {
                    if (newBitSet.get(i)){
                        message.set(counter);
                    }
                    counter += 1;
                }
                x += 1;
                if (x == imgWidth){
                    x = 0;
                    y += 1;
                }
            }
            //System.out.println(message.toString());
            return message.toByteArray().toString();
        }
        return "ERROR: No image found!";
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
