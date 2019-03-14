import Entries.Entry;
import Entries.EntryFactory;
import Entries.EntryType;
import Misc.MultiException;
import Misc.MyTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Manage entries and display them in a GUI
 */
class EntryManager {
    private List<Entry> entries = new ArrayList<>();
    private JTable table;
    private DefaultTableModel model;
    private String[] columns = new String[]{"Name", "Type", "Day", "Start time", "End time", "Week pattern", "Location",
        "Room size", "Class size", "Staff", "Department", "Comment"};

    EntryManager(JTable table) {
        this.table = table;
        model = new MyTableModel();
        Arrays.stream(columns).forEach(model::addColumn);
        table.setModel(model);
        model.fireTableDataChanged();
    }

    // unique: name, day, start time, end time, week pattern
    private boolean exists(Entry e) {
        return entries.stream().anyMatch(e2 ->
            e.getName().equals(e2.getName()) &&
                e.getDay().equals(e2.getDay()) &&
                e.getStartTime().equals(e2.getStartTime()) &&
                e.getEndTime().equals(e2.getEndTime()) &&
                e.getLocation().equals(e2.getLocation()) &&
                e.getWeekPattern().equals(e2.getWeekPattern()));
    }

    /**
     * Add an entry to the collection and update the GUI
     *
     * @param type the entry type
     * @param data raw entry data
     * @throws Exception
     */
    void add(EntryType type, String[] data) throws Exception {
        data[0] += "." + Entry.getTypeCode(type);
        add(data);
    }

    private void add(String[] data) throws Exception {
        List<Entry> entries;
        try {
            entries = EntryFactory.parse(data);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage() + " at:\n" + Arrays.toString(data));
        }
        for (Entry entry : entries) {
            if (exists(entry)) {
                throw new Exception("Entry already exists:\n" + entries);
            }
            this.entries.add(entry);
        }
        fillTable();
    }

    /**
     * Read a file and parse all raw records in it, updating the GUI
     *
     * @param fileName file to read
     * @throws Exception
     */
    void read(String fileName) throws Exception {
        MultiException multiE = new MultiException();
        Iterator<String> iter = Files.lines(Paths.get(fileName)).skip(1).iterator();
        while (iter.hasNext()) {
            try {
                add(iter.next().split("\t"));
            } catch (Exception e) {
                multiE.exceptionList.add(e);
            }
        }
        System.out.printf("Read file: %s\n", fileName);
        fillTable();
        if (multiE.exceptionList.size() > 0) {
            throw multiE;
        }
    }

    /**
     * Filter table by module name
     *
     * @param name case-insensitive partial string to match
     * @throws Exception
     */
    void filterTable(String name) throws Exception {
        fillTable(entries.stream().filter(e -> e.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList()));
    }

    private void fillTable() throws Exception {
        fillTable(entries);
    }

    private void fillTable(List<Entry> entries) throws Exception {
        model.setRowCount(0);
        for (Entry e : entries) {
            model.addRow(e.toRawData());
        }
        model.fireTableDataChanged();
    }

    private boolean tableContainsName(String name) {
        return IntStream.range(0, table.getRowCount()).anyMatch(i -> table.getValueAt(i, 0).equals(name));
    }

    /**
     * Save currently displayed data as an html table
     *
     * @param fileName Output file
     * @throws Exception
     */
    void saveHtml(String fileName) throws Exception {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.write("<table><tr>");
        for (String column : columns) {
            printWriter.write(("<th>" + column + "</th>"));
        }
        printWriter.write("</tr>");
        for (Entry entry : entries) {
            if (!tableContainsName(entry.getName())) {
                continue;
            }
            printWriter.write("<tr>");
            Object[] data = entry.toRawData();
            for (Object item : data) {
                printWriter.write(("<td>" + item + "</td>"));
            }
            printWriter.write("</tr>");
        }
        printWriter.write("</table>");
        printWriter.close();
    }
}
