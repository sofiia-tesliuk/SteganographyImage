import java.nio.ByteBuffer;
import java.util.BitSet;

public class Main {
    public static int bitsPrint(BitSet bbb){
        for (int i = 0; i < 8; i++) {
            if (bbb.get(i)){
                System.out.print('1');
            } else{
                System.out.print('0');
            }
        }
        //System.out.println();
        return 0;
    }
    public static void main(String[] args) {
        String pathToImage = "examples/image_without_message.bmp";
        SteganographyImage img_test = new SteganographyImage(pathToImage);
        String message = "This is very cute image!";
        img_test.encode(message);
        img_test.save("examples/image_with_message.bmp");

//        BitSet newBitSet = BitSet.valueOf(ByteBuffer.allocate(1));
//        System.out.println(newBitSet.size());
//        System.out.println(newBitSet.toString());

        String got_message = img_test.decode();
        System.out.println(got_message);

//        for (int i = 0; i < message.length(); i++) {
//            System.out.print(String.format("%c: ", message.charAt(i)));
//            bitsPrint(BitSet.valueOf(new byte[]{(byte) message.charAt(i)}));
//            System.out.print(String.format("  |  %c: ", got_message.charAt(i)));
//            bitsPrint(BitSet.valueOf(new byte[]{(byte) got_message.charAt(i)}));
//            System.out.println();
//        }

//
//        char c = 'a';
//        int a = 134;
//        int b = 45;
//        BitSet aBitSet = BitSet.valueOf(new byte[]{(byte) a});
//        BitSet bBitSet = BitSet.valueOf(new byte[]{(byte) b});
//        BitSet cBitSet = BitSet.valueOf(new byte[]{(byte) c});
//
//        System.out.print("a, before: ");
//        bitsPrint(aBitSet);
//        System.out.print("b, before: ");
//        bitsPrint(bBitSet);
//        System.out.print("c, before: ");
//        bitsPrint(cBitSet);
//        BitSet output = BitSet.valueOf(ByteBuffer.allocate(1));
//        if (cBitSet.get(0)){
//            output.set(0);
//        }
//        if (cBitSet.get(1)){
//            output.set(1);
//        }
//        if (cBitSet.get(2)){
//            output.set(2);
//        }
//        if (cBitSet.get(3)){
//            output.set(3);
//        }
//        for (int i = 4; i < 8; i++) {
//            if (aBitSet.get(i)){
//                output.set(i);
//            }
//        }
//        aBitSet = output;
//
//        output = BitSet.valueOf(ByteBuffer.allocate(1));
//        if (cBitSet.get(4)){
//            output.set(0);
//        }
//        if (cBitSet.get(5)){
//            output.set(1);
//        }
//        if (cBitSet.get(6)){
//            output.set(2);
//        }
//        if (cBitSet.get(7)){
//            output.set(3);
//        }
//        for (int i = 4; i < 8; i++) {
//            if (bBitSet.get(i)){
//                output.set(i);
//            }
//        }
//        bBitSet = output;
//
//        output = BitSet.valueOf(ByteBuffer.allocate(1));
//        for (int i = 0; i < 4; i++) {
//            if (aBitSet.get(i)){
//                output.set(i);
//            }
//            if (bBitSet.get(i)){
//                output.set(i + 4);
//            }
//        }
//
//        cBitSet = output;
//        System.out.print("a, after: ");
//        bitsPrint(aBitSet);
//        System.out.print("b, after: ");
//        bitsPrint(bBitSet);
//        System.out.print("c, after: ");
//        bitsPrint(cBitSet);
//        System.out.println((char) output.toByteArray()[0]);
    }
}
