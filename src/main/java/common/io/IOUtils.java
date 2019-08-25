package common.io;

public final class IOUtils {
    private static final String underscore = "_";
    private static final String doubleUnderscore = "__";
    private static final String quadUnderscore = "____";

    public static final  String commentToken = "#";
    public static final String globalFileSeparator = "\t";
    public static final String lineSeparator = " ";


    public static String removeUnderscore(String input) {
        return input.replace(quadUnderscore, lineSeparator).
                replace(doubleUnderscore, lineSeparator).
                replace(underscore, lineSeparator);
    }


}
