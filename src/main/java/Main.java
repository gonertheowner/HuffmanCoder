public class Main {
    public static void main(String[] args) {
        Signature signature = new Signature("SHRK", "1.02", "10000000");
        String XFilePath = "C:\\Users\\g0n3r\\java_projects\\HuffmanCoder\\HuffmanCoder\\src\\main\\resources\\test.txt";
        new Analyzer(XFilePath).start();
        System.out.println();
        Coder coder = new Coder(XFilePath, signature);
        String YFilePath = coder.createYFile();
        Decoder decoder = new Decoder(YFilePath, signature.getBasedExtension(XFilePath), signature.getExtension(), coder);
        decoder.createXFile();
    }
}
