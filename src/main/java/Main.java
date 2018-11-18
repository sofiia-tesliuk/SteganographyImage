public class Main {
    public static void main(String[] args){
        String pathToImage = "examples/image_without_message.bmp";
        Image img_test = new Image(pathToImage);
        String message = "This is very cute image!";
        img_test.encode(message);
        img_test.save("examples/image_with_message.bmp");

        String got_message = img_test.decode();
        System.out.println(got_message);
    }
}
