package Misc;

import java.util.regex.Pattern;

/**
 * Regex patterns to match and filter data
 */
public class Patterns {
    public static final String regexRecordDataSplit = "\\*";
    public static final String regexRecordNameSplit = "(?<=[^/]{3,})\\/(?=[^\\d/]{3,})";
    public static final Pattern regexTime = Pattern.compile("^\\d{1,2}:\\d{1,2}$");
    public static final Pattern regexInt = Pattern.compile("^\\d+$");
    public static final Pattern regexDay = Pattern.compile("^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday$");
    //public static final Pattern regexWeekPattern = Pattern.compile("^((\\d{1,2}-\\d{1,2}|\\d{1,2})\\s*,\\s*)*(\\d{1,2}-\\d{1,2}|\\d{1,2})$");
}
