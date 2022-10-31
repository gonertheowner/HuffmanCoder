import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {
    private final String path;
    public Analyzer(String path) {
        this.path = path;
    }

    public byte[] readFromFile() {
        byte[] data = new byte[0];
        try (var input = new FileInputStream(path)) {
            data = input.readAllBytes();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    public List<Byte> getDistinctSymbols() {
        ArrayList<Byte> symbols = new ArrayList<>();
        byte[] data = readFromFile();

        for (byte datum : data) {
            symbols.add(datum);
        }
        return symbols.stream().distinct().sorted().collect(Collectors.toList());
    }

    public ArrayList<Integer> getAmountOfDistinctSymbols() {
        var list = getDistinctSymbols();
        var contList = new ArrayList<Integer>();
        byte[] data = readFromFile();

        for (int i = 0; i < list.size(); i++) {
            int count = 0;
            for (byte datum : data) {
                if (list.get(i) == datum) {
                    count++;
                }
            }
            contList.add(i, count);
        }
        return contList;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private HashMap<Byte, Integer> getSymbolsMap(int sorting) {
        HashMap<Byte, Integer> map = new HashMap<>();
        var byteList = getDistinctSymbols();
        var countList = getAmountOfDistinctSymbols();

        for (int i = 0; i < byteList.size(); i++) {
            map.put(byteList.get(i), countList.get(i));
        }

        if (sorting == -1) {
            map = (HashMap<Byte, Integer>) sortByValue(map);
        }
        return map;
    }

    public void getInfo(int sorting) {
        var symbolsMap = getSymbolsMap(sorting);
        ArrayList<Byte> distinctSymbols = new ArrayList<>(symbolsMap.keySet());
        ArrayList<Integer> amountOfDistinctSymbols = new ArrayList<>(symbolsMap.values());
        var lengthOfFile = readFromFile().length;

        ArrayList<Double> p = new ArrayList<>();
        for (var amount : amountOfDistinctSymbols) {
            p.add((double) amount / (double) lengthOfFile);
        }

        ArrayList<Double> Ix = new ArrayList<>();
        for (var pi : p) {
            Ix.add(Math.log((double) 1 / pi) / Math.log(2));
        }

        double I = 0.0;
        for (int i = 0; i < Ix.size(); i++) {
            I += Ix.get(i);
        }

        System.out.println("File length: " + lengthOfFile);
        System.out.println("Symbol, v, p, I(a):");
        for (int i = 0; i < symbolsMap.size(); i++) {
            System.out.println(Integer.toHexString(distinctSymbols.get(i)) + "\t\t" + amountOfDistinctSymbols.get(i)+ "\t\t" + p.get(i) + "\t\t" + Ix.get(i));
        }
        System.out.println("Summary information: " + I);
    }

    public void start() {
        getInfo(0);
        System.out.println();
        getInfo(-1);
    }
}
