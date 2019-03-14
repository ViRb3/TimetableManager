package Entries;

import java.time.LocalTime;

/**
 * A timetable record entry
 */
public abstract class Entry {
    private String name;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String weekPattern;
    private String location;
    private int roomSize;
    private int classSize;
    private String staff;
    private String department;

    Entry(String name, String day, LocalTime startTime, LocalTime endTime, String weekPattern, String location,
          int roomSize, int classSize, String staff, String department) {
        this.name = outputTransformer(name);
        this.day = outputTransformer(day);
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekPattern = outputTransformer(weekPattern);
        this.location = outputTransformer(location);
        this.roomSize = roomSize;
        this.classSize = classSize;
        this.staff = outputTransformer(staff);
        this.department = outputTransformer(department);
    }

    /**
     * @param et an entry type
     * @return the entry type's code
     * @throws Exception
     */
    public static String getTypeCode(EntryType et) throws Exception {
        switch (et) {
            case Lecture:
                return "L";
            case ComputerLab:
                return "CL";
            case Practical:
                return "P";
            case Seminar:
                return "S";
        }
        throw new Exception("Unknown entry type: " + et);
    }

    private String getIssueString() throws Exception {
        Issues issue = checkIssues();
        switch (issue) {
            case ClassExceedsRoom:
                return "Class exceeds room";
            case Class10PercentLargerThanRoom:
                return "Class 10% larger than room";
            case ClassSmallerThanHalfRoom:
                return "Class smaller than half the room";
            case None:
                return "";
        }
        throw new Exception("Unknown issue type: " + issue);
    }

    /**
     * @return this entry's type code
     * @throws Exception
     */
    public String getTypeCode() throws Exception {
        return getTypeCode(getType());
    }

    /**
     * @return the type of this entry
     */
    public abstract EntryType getType();

    /**
     * @return this entry's type full string
     */
    public String getTypeString() {
        return outputTransformer(getType().toString());
    }

    @Override
    public String toString() {
        return "Entry{" + "name='" + name + '\'' + "type='" + getTypeString() + '\'' + ", day='" + day + '\''
            + ", startTime=" + startTime + ", endTime=" + endTime + ", weekPattern='" + weekPattern + '\''
            + ", location='" + location + '\'' + ", roomSize=" + roomSize + ", classSize=" + classSize + ", staff='"
            + staff + '\'' + ", department='" + department + '\'' + '}';
    }

    /**
     * @return entry issues, if any
     */
    public abstract Issues checkIssues();

    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getWeekPattern() {
        return weekPattern;
    }

    public String getLocation() {
        return location;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public int getClassSize() {
        return classSize;
    }

    public String getStaff() {
        return staff;
    }

    public String getDepartment() {
        return department;
    }

    /**
     * Transform data strings depending on entry type
     *
     * @param str input data string to be transformed
     * @return transformed data string
     */
    abstract String outputTransformer(String str);

    /**
     * @return raw data of this entry
     * @throws Exception
     */
    public Object[] toRawData() throws Exception {
        return new Object[]{getName(), getTypeString(), getDay(), getStartTime(), getEndTime(),
            getWeekPattern(), getLocation(), getRoomSize(), getClassSize(), getStaff(), getDepartment(),
            getIssueString()};
    }
}
