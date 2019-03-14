package Entries;

import Misc.Patterns;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Parse entries from raw strings
 *
 * @see Entry
 */
public class EntryFactory {
    /**
     * Parse a single raw record to entries
     *
     * @param rawInput raw record to parse
     * @return parsed list of entries
     * @throws Exception
     */
    public static List<Entry> parse(String[] rawInput) throws Exception {
        ArrayList<Entry> result = new ArrayList<>();
        String[] names = rawInput[0].split(Patterns.regexRecordNameSplit);
        String[] locations = rawInput[5].split(Patterns.regexRecordDataSplit);
        String[] times = rawInput[6].split(Patterns.regexRecordDataSplit);

        // handle multiple module names in single record, e.g.:
        // BUS/HRM/MAN/MKT/RMK.CL/02
        if (names.length > 1) {
            // loop backwards to begin with module that has type code,
            // append it to the rest
            for (int i = names.length - 1; i >= 0; i--) {
                String[] newInput = rawInput.clone();
                newInput[0] = names[i];
                if (!newInput[0].contains(".")) {
                    newInput[0] += "." + result.get(0).getTypeCode();
                }
                result.addAll(parse(newInput));
            }
        }
        // handle multiple locations+times in single record, e.g.:
        // C.3X11*C.2B42	16*20
        else if (locations.length == times.length && locations.length > 1) {
            for (int i = 0; i < locations.length; i++) {
                String[] newInput = rawInput.clone();
                newInput[5] = locations[i];
                newInput[6] = times[i];
                result.addAll(parse(newInput));
            }
        }
        // handle standard case
        else {
            result.add(parseOne(rawInput));
        }
        return result;
    }

    private static Entry parseOne(String[] rawInput) throws Exception {
        // check for malformed input
        checkInputLength(rawInput);
        checkInputFilled(rawInput);
        checkInputTime(rawInput);
        checkInputIntegers(rawInput);
        checkInputDay(rawInput[1]);

        Object[] params = new Object[]{rawInput[0], rawInput[1],
            LocalTime.parse(rawInput[2], DateTimeFormatter.ofPattern("HH:mm")),
            LocalTime.parse(rawInput[3], DateTimeFormatter.ofPattern("HH:mm")), rawInput[4], rawInput[5],
            Integer.parseInt(rawInput[6]), Integer.parseInt(rawInput[7]), rawInput[8], rawInput[9]};

        // create appropriate entry type; uses reflection for clarity
        char entryType = getTypeChar(rawInput[0]);
        switch (entryType) {
            case 'C':
                return (Entry) ComputerLabEntry.class.getDeclaredConstructors()[0].newInstance(params);
            case 'L':
                return (Entry) LectureEntry.class.getDeclaredConstructors()[0].newInstance(params);
            case 'P':
                return (Entry) PracticalEntry.class.getDeclaredConstructors()[0].newInstance(params);
            case 'S':
                return (Entry) SeminarEntry.class.getDeclaredConstructors()[0].newInstance(params);
        }
        throw new Exception("Unknown entry type: " + entryType);
    }

    /**
     * Get the {@link Entry}'s type character
     *
     * @param s Input module name
     * @return Type character
     */
    private static char getTypeChar(String s) {
        return s.charAt(s.indexOf('.') + 1);
    }

    private static void checkInputDay(String input) throws Exception {
        if (!Patterns.regexDay.matcher(input).matches()) {
            throw new Exception("Invalid day! Got " + input);
        }
    }

    private static void checkInputIntegers(String[] rawInput) throws Exception {
        if (!Patterns.regexInt.matcher(rawInput[6]).matches() || !Patterns.regexInt.matcher(rawInput[7]).matches()) {
            throw new Exception("Invalid integer! Got " + rawInput[6] + " and " + rawInput[7]);
        }
    }

    private static void checkInputTime(String[] rawInput) throws Exception {
        if (!Patterns.regexTime.matcher(rawInput[2]).matches() || !Patterns.regexTime.matcher(rawInput[3]).matches()) {
            throw new Exception("Invalid time! Expected HH:mm format, got " + rawInput[2] + " and " + rawInput[3]);
        }
    }

    private static void checkInputFilled(String[] rawInput) throws Exception {
        if (IntStream.range(0, 10).filter(i -> i != 8).mapToObj(i -> rawInput[i]).anyMatch(i -> i.trim().equals(""))) {
            throw new Exception("Invalid input! All fields must be filled.");
        }
    }

    private static void checkInputLength(String[] rawInput) throws Exception {
        if (rawInput.length != 10) {
            throw new Exception("Invalid length! expected 10, got " + rawInput.length);
        }
    }
}
