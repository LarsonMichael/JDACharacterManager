package commands;

public class CommandRegex {
    public static String CHARACTER_COMMAND_MATCHING_REGEX = "^(?i)!CHARACTER(?-i) .*";
    public static String CHARACTER_COMMAND_STRIP_REGEX = "^(?i)!CHARACTER(?-i)";
    public static String HELP_COMMAND_MATCHING_REGEX = "^(?i)!HELP(?-i).*";
    public static String STATS_COMMAND_MATCHING_REGEX = "^(?i)!STATS(?-i).*";
    public static String STATS_COMMAND_STRIPPING_REGEX = "^(?i)!STATS(?-i)";
}
