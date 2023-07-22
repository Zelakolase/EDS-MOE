package lib;

/**
 * This enum has all of data paths to be used.
 * @author Morad A.
 */
public enum FilePaths {
    ParentDirectory("./data/"),
    ConfigurationDirectory("./data/conf/"),
    DocumentsDirectory("./data/docs/");

    private final String value;

    FilePaths(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
