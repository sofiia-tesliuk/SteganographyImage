package image;

import java.awt.image.BufferedImage;


public class PixelInImage {
    private int x;
    private int y;
    private BufferedImage img;
    private int imgWidth;
    private int imgHeight;
    private int a;
    private int r;
    private int g;
    private int b;
    private int currentValue;

    PixelInImage(BufferedImage img){
        this.img = img;
        this.imgWidth = img.getWidth();
        this.imgHeight = img.getHeight();
        this.x = 0;
        this.y = 0;
        initialiseRGB();
        currentValue = 0;
    }

    private void initialiseRGB(){
        int p = img.getRGB(x, y);
        a = (p>>24)&0xff;
        r = (p>>16)&0xff;
        g = (p>>8)&0xff;
        b = p&0xff;
    }

    public void setNewRGB(){
        int p = (a<<24) | (r<<16) | (g<<8) | b;
        img.setRGB(x, y, p);
    }

    private void nextPixel(){
        setNewRGB();
        this.x++;
        if (this.x >= this.imgWidth){
            this.x = 0;
            this.y++;
        }
        initialiseRGB();
    }

    public void encodeNextValue(int c, int bitStart){
        if (currentValue == 0){
            encodeR(c, bitStart);
        } else if (currentValue == 1){
            encodeG(c, bitStart);
        } else if (currentValue == 2){
            encodeB(c, bitStart);
        }
        currentValue += 1;
        if (currentValue == 3){
            currentValue = 0;
            this.nextPixel();
        }
    }

    private int encodeValue(int value, int c, int bitStart){
        int shiftedValue = (value >> 2)&0x3f;
        if (shiftedValue != 0){
            shiftedValue <<= 2;
        }
        return shiftedValue | (c>>bitStart)&0x3;
    }

    private void encodeR(int c, int bitStart) { r = encodeValue(r, c, bitStart);}

    private void encodeG(int c, int bitStart) {g = encodeValue(g, c, bitStart);}

    private void encodeB(int c, int bitStart) {b = encodeValue(b, c, bitStart);}

    private int decodeValue(int value, int bitStart){
        int shiftedValue = value&0x3;
        if ((shiftedValue != 0) && (bitStart > 0)){
            shiftedValue <<= bitStart;
        }
        return shiftedValue;
    }

    private int decodeR(int bitStart) { return decodeValue(r, bitStart); }

    private int decodeG(int bitStart){
        return decodeValue(g, bitStart);
    }

    private int decodeB(int bitStart){
        return decodeValue(b, bitStart);
    }

    public int decodeNextValue(int bitStart){
        int decodedValue = 0;
        if (currentValue == 0){
            decodedValue = decodeR(bitStart);
        } else if (currentValue == 1){
            decodedValue = decodeG(bitStart);
        } else if (currentValue == 2){
            decodedValue = decodeB(bitStart);
        }
        currentValue += 1;
        if (currentValue == 3){
            currentValue = 0;
            this.nextPixel();
        }
        return decodedValue;
    }
}
