import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class Coder {
    private final String XFilePath;
    private final Signature signature;
    private CodeTreeNode tree;

    public Coder(String xFilePath, Signature signature) {
        XFilePath = xFilePath;
        this.signature = signature;
    }

    private ArrayList<Character> readFromFile() {
        ArrayList<Character> data = new ArrayList<>();
        try (var reader = new FileReader(XFilePath)) {
            while (reader.ready()) {
                data.add((char) reader.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public CodeTreeNode getCodeTree() {
        return tree;
    }

    public String createYFile() {
        String YFilePath = signature.createOutputFilePath(new File(XFilePath));
        try (var writer = new FileWriter(YFilePath)) {
            // Записываем шапку
            String header = (signature.createHeader(new File(XFilePath)) + "~");
            for (var c : header.toCharArray()) {
                writer.write(c);
            }

            if (signature.getAlgorithms().charAt(0) == '1') {
                // Записываем считанные из файла символы в строку
                StringBuilder builder = new StringBuilder();
                for (var c : readFromFile()) {
                    builder.append(c);
                }
                String text = builder.toString();

                // Рассчитываем частоту для каждого символа
                TreeMap<Character, Integer> frequencies = Huffman.countFrequency(text);

                // Генерируем список листьев дерева
                ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();
                for(Character c: frequencies.keySet()) {
                    codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
                }

                // Строим кодовое дерево с помощью алгоритма Хаффмана
                tree = Huffman.huffman(codeTreeNodes);

                // Генерируем таблицу префиксных кодов для кодируемых символов с помощью кодового дерева
                TreeMap<Character, String> codes = new TreeMap<>();
                for(Character c: frequencies.keySet()) {
                    codes.put(c, tree.getCodeForCharacter(c, ""));
                }
                System.out.println("Таблица префиксных кодов: " + codes);

                // Кодируем текст, заменяем сиволы соответствующими кодами
                StringBuilder encoded = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    encoded.append(codes.get(text.charAt(i)));
                }

                System.out.println("Размер исходного файла: " + text.getBytes().length * 8 + " бит");
                System.out.println("Размер сжатого файла: " + encoded.length() + " бит");
                System.out.println("Биты сжатого файла: " + encoded);

                // Записываем закодированные данные из файла X в файл Y
                for (var c : encoded.toString().toCharArray()) {
                    writer.write(c);
                }
            } else {
                for (var c : readFromFile()) {
                    writer.write(c);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return YFilePath;
    }
}
