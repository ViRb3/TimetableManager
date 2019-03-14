package Entries;

import java.time.LocalTime;

public class SeminarEntry extends Entry {
    SeminarEntry(String name, String day, LocalTime startTime, LocalTime endTime, String weekPattern, String location,
                 int roomSize, int classSize, String staff, String department) {
        super(name, day, startTime, endTime, weekPattern, location, roomSize, classSize, staff, department);
    }

    @Override
    public EntryType getType() {
        return EntryType.Seminar;
    }

    @Override
    public Issues checkIssues() {
        if (getClassSize() < getRoomSize() / 2) {
            return Issues.ClassSmallerThanHalfRoom;
        }
        if (getClassSize() > getRoomSize() * 1.1) {
            return Issues.Class10PercentLargerThanRoom;
        }
        return Issues.None;
    }

    @Override
    String outputTransformer(String str) {
        return str.toLowerCase();
    }
}
