package course;

/**
 * One assessment belonging to a course. An Assessment is only constructed
 * after Course has validated the proposed details.
 */
public class Assessment {
    private final String name;
    private final String description;
    private final int points;
    private final String submissionType;
    private final boolean group;
    private final String groupingName;
    private final String publishDateTime;
    private final String dueDateTime;
    private final String hideDateTime;

    public Assessment(String name, String description, int points,
                      String submissionType, boolean group, String groupingName,
                      String publishDateTime, String dueDateTime, String hideDateTime) {
        // Initialise every attribute using the already-validated parameters.
        this.name = name;
        this.description = description;
        this.points = points;
        this.submissionType = submissionType;
        this.group = group;
        this.groupingName = groupingName;
        this.publishDateTime = publishDateTime;
        this.dueDateTime = dueDateTime;
        this.hideDateTime = hideDateTime;
    }

    // Read-only access lets unit tests verify that the constructor saved
    // exactly the parameters provided without exposing writable fields.
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPoints() { return points; }
    public String getSubmissionType() { return submissionType; }
    public boolean isGroup() { return group; }
    public String getGroupingName() { return groupingName; }
    public String getPublishDateTime() { return publishDateTime; }
    public String getDueDateTime() { return dueDateTime; }
    public String getHideDateTime() { return hideDateTime; }
}
