import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Decoder {
    private final String YFilePath;
    private final String basedExtension;
    private final String extension;
    private final Coder coder;

    public Decoder(String YFilePath, String basedExtension, String extension, Coder coder) {
        this.YFilePath = YFilePath;
        this.basedExtension = basedExtension;
        this.extension = extension;
        this.coder = coder;
    }

    private ArrayList<Character> readFromFile() {
        ArrayList<Character> data = new ArrayList<>();
        try (var reader = new FileReader(YFilePath)) {
            while (reader.ready()) {
                data.add((char) reader.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private int getTildaPosition() {
        var data = readFromFile();
        int pos = 0;
        for (var c : data) {
            if (c == '~') {
                return pos;
            }
            pos++;
        }
        return -1;
    }

    private ArrayList<Character> getDataWithoutHeader() {
        var data = readFromFile();
        var dataWithoutHeader = new ArrayList<Character>();
        for (int i = getTildaPosition() + 1; i < data.size(); i++) {
            dataWithoutHeader.add(data.get(i));
        }
        return dataWithoutHeader;
    }

    private String getHeader() {
        var data = readFromFile();
        StringBuilder header = new StringBuilder();
        for (var c : data) {
            header.append(c);
        }
        return header.toString();
    }

    private String getExtension() {
        return getHeader().split(" ")[0];
    }

    private int getAlgorithm() {
        String algorithms = getHeader().split(" ")[2];
        return Integer.parseInt(String.valueOf(algorithms.charAt(0)));
    }

    private String createOutputFilePath() {
        return YFilePath.substring(0, YFilePath.lastIndexOf(".")) + "(copy)." + basedExtension;
    }

    public void createXFile() {
        StringBuilder encoded = new StringBuilder();
        for (var c : getDataWithoutHeader()) {
            encoded.append(c);
        }
        String decoded = encoded.toString();

        if (getExtension().equals(extension)) {
            if (getAlgorithm() == 1) {
                decoded = Huffman.huffmanDecode(encoded.toString(), coder.getCodeTree());
                System.out.println("Расшифровано: " + decoded);
            }
        }

        try (var writer = new FileWriter(createOutputFilePath())) {
            writer.write(decoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
