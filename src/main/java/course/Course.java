package course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Individual segment for Assessment 4.
 * Student member owns validation conditions 1–4 only.
 * The rest of the team's rules must be integrated before final submission.
 */
public class Course {
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

        // GROUP INTEGRATION REQUIRED BEFORE FINAL SUBMISSION:
        // Teammate 1: reject inputs failing conditions 5–8 here.
        // Teammate 2: reject inputs failing conditions 9–11 here.
        // Do not claim this individual file validates all 11 conditions.

        // Add an Assessment only after this member's checks have passed.
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
}
