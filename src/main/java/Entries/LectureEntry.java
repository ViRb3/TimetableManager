package Entries;

import java.time.LocalTime;

public class LectureEntry extends Entry {
    LectureEntry(String name, String day, LocalTime startTime, LocalTime endTime, String weekPattern, String location,
                 int roomSize, int classSize, String staff, String department) {
        super(name, day, startTime, endTime, weekPattern, location, roomSize, classSize, staff, department);
    }

    @Override
    public EntryType getType() {
        return EntryType.Lecture;
    }

    @Override
    public Issues checkIssues() {
        return Issues.None;
    }

    @Override
    String outputTransformer(String str) {
        return str.toUpperCase();
    }
}
