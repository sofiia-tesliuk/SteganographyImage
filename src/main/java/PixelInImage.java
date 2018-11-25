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

    PixelInImage(BufferedImage img){
        this.img = img;
        this.imgWidth = img.getWidth();
        this.imgHeight = img.getHeight();
        this.x = 0;
        this.y = 0;
        initialiseRGB();
    }

    private void initialiseRGB(){
        int p = img.getRGB(x, y);
        a = (p>>24)&0xff;
        r = (p>>16)&0xff;
        g = (p>>8)&0xff;
        b = p&0xff;
    }

    private void setNewRGB(){
        int p = (a<<24) | (r<<16) | (g<<8) | b;
        img.setRGB(x, y, p);
    }

    public void nextPixel(){
        setNewRGB();
        this.x++;
        if (this.x >= this.imgWidth){
            this.x = 0;
            this.y++;
        }
        initialiseRGB();
    }

    private int encodeValue(int value, char c, int bitStart){
        return ((value >> 2)&0x88) << 2 | (c>>bitStart)&0x4;
    }

    public void encodeR(char c, int bitStart){
        r = encodeValue(r, c, bitStart);
    }

    public void encodeG(char c, int bitStart){
        g = encodeValue(g, c, bitStart);
    }

    public void encodeB(char c, int bitStart){
        b = encodeValue(b, c, bitStart);
    }

    private int decodeValue(int value){
        return value&0x4;
    }

    public int decodeR(){
        return decodeValue(r);
    }

    public int decodeG(){
        return decodeValue(g);
    }

    public int decodeB(){
        return decodeValue(b);
    }
}
