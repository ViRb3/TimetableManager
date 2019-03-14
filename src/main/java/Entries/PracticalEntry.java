package Entries;

import java.time.LocalTime;

public class PracticalEntry extends Entry {
    PracticalEntry(String name, String day, LocalTime startTime, LocalTime endTime, String weekPattern, String location,
                   int roomSize, int classSize, String staff, String department) {
        super(name, day, startTime, endTime, weekPattern, location, roomSize, classSize, staff, department);
    }

    @Override
    public EntryType getType() {
        return EntryType.Practical;
    }

    @Override
    public Issues checkIssues() {
        if (getClassSize() > getRoomSize()) {
            return Issues.ClassExceedsRoom;
        }
        return Issues.None;
    }

    @Override
    String outputTransformer(String str) {
        return str.toLowerCase();
    }
}
