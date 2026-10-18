package course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;


/**
 * Shared course implementation for Assessment 4.
 * Abbas Sheriffdeen is responsible for conditions 1-4.
 * Nhua Lam Tam is responsible for conditions 5-8.
 * Imath Wickramarachchi is responsible for conditions 9 to 11
 */
public class Course {
    // strict format so fake dates like 29/02/2026 get rejected
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    // Common course state required for the individual tests to compile and run.
    private final List<Assessment> assessments = new ArrayList<>();

    /** Read-only access helps JUnit verify that rejected data changes nothing. */
    public List<Assessment> getAssessments() {
        return Collections.unmodifiableList(assessments);
    }

    public boolean createNewAssessment(String name, String description, int points,
                                       String submissionType, boolean group, String groupingName,
                                       String publishDateTime, String dueDateTime,
                                       String hideDateTime) {
        // THIS MEMBER'S IMPLEMENTATION: Conditions 1–4.
        if (!validConditions1To4(name, description, points)) {
            return false;
        }

        // Validate conditions 5-8 before creating or storing an Assessment.
        if (!validConditions5To8(points, submissionType, group, groupingName)) {
            return false;
        }

        // check conditions 9 to 11
        if (!validConditions9To11(group, groupingName, publishDateTime, dueDateTime, hideDateTime)) {
            return false;
        }

        // Add an Assessment only after all implemented checks have passed.
        Assessment newAssessment = new Assessment(name, description, points,
                submissionType, group, groupingName,
                publishDateTime, dueDateTime, hideDateTime);
        assessments.add(newAssessment);
        return true;
    }

    /**
     * Condition 1: Assessment name must be unique within the course.
     * Condition 2: Assessment name must not be blank (including whitespace).
     * Condition 3: Description must contain at least three words.
     * Condition 4: Points must be strictly greater than zero.
     *
     * Validation is read-only: nothing is created or modified on rejection.
     */
    private boolean validConditions1To4(String name, String description, int points) {
        // C2: Reject empty/whitespace-only names before checking uniqueness.
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // C1: Existing course assessments cannot share exactly the same name.
        for (Assessment existing : assessments) {
            if (existing.getName().equals(name)) {
                return false;
            }
        }

        // C3: Split on one or more whitespace characters to count words.
        if (description == null || description.trim().isEmpty()
                || description.trim().split("\\s+").length < 3) {
            return false;
        }

        // C4: Zero and negative point values must not be accepted.
        return points > 0;
    }

    /**
     * Condition 5: Points for one assessment must not exceed 50.
     * Condition 6: Total assessment points must not exceed 100.
     * Condition 7: Submission type must be pdf, zip, or doc.
     * Condition 8: Group assessments must have a nonblank grouping name.
     *
     * Validation is read-only: nothing is created or modified on rejection.
     */
    private boolean validConditions5To8(int points, String submissionType,
                                       boolean group, String groupingName) {
        // C5: Exactly 50 points is allowed; higher values must be rejected.
        if (points > 50) {
            return false;
        }

        // C6: Include the new assessment when checking the course total.
        int totalPoints = points;
        for (Assessment existing : assessments) {
            totalPoints += existing.getPoints();
        }
        if (totalPoints > 100) {
            return false;
        }

        // C7: Only the three specified submission types are allowed.
        if (!"pdf".equals(submissionType) && !"zip".equals(submissionType)
                && !"doc".equals(submissionType)) {
            return false;
        }

        // C8: A grouping name is required only for group assessments.
        if (group && (groupingName == null || groupingName.trim().isEmpty())) {
            return false;
        }
        return true;
    }

    // conditions 9, 10 and 11
    private boolean validConditions9To11(boolean group, String groupingName,
                                         String publishDateTime, String dueDateTime,
                                         String hideDateTime) {
        // C9: grouping name can't be used by another assessment
        if (group) {
            for (Assessment a : assessments) {
                if (groupingName.equals(a.getGroupingName())) {
                    return false;
                }
            }
        }

        // C10: turn the strings into dates (null means the format was wrong)
        LocalDateTime publish = parseDateTime(publishDateTime);
        LocalDateTime due = parseDateTime(dueDateTime);
        LocalDateTime hide = parseDateTime(hideDateTime);
        if (publish == null || due == null || hide == null) {
            return false;
        }

        // C11: publish < due < hide (same time is not allowed)
        if (!due.isAfter(publish)) {
            return false;
        }
        if (!hide.isAfter(due)) {
            return false;
        }
        return true;
    }

    // reads the date, gives back null if it is not valid
    private LocalDateTime parseDateTime(String text) {
        if (text == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(text, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            return null; // e.g. 29/02/2026 fails here
        }
    }
}
