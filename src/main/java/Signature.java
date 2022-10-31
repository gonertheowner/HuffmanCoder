import java.io.File;

class Signature {
    private final String extension;
    private final String version;
    private final String algorithms;

    Signature(String extension, String version, String algorithms) {
        this.extension = extension;
        this.version = version;
        this.algorithms = algorithms;
    }

    private String signatureToString() {
        return extension + " " + version + " " + algorithms;
    }

    public String createHeader(File inputFile) {
        return this.signatureToString() + " " + inputFile.getName() + " " + inputFile.length();
    }

    protected String createOutputFilePath(File inputFile) {
        return inputFile.getParent() + "/" + inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")) + "." + extension;
    }

    public String getAlgorithms() {
        return algorithms;
    }

    public String getExtension() {
        return extension;
    }

    public String getBasedExtension(String XFilePath) {
        File XFile = new File(XFilePath);
        String fileName = XFile.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
