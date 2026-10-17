package course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shared course implementation for Assessment 4.
 * Member A is responsible for conditions 1-4.
 * Member B is responsible for conditions 5-8.
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

        // Validate conditions 5-8 before creating or storing an Assessment.
        if (!validConditions5To8(points, submissionType, group, groupingName)) {
            return false;
        }

        // GROUP INTEGRATION REQUIRED BEFORE FINAL SUBMISSION:
        // Teammate 2: reject inputs failing conditions 9–11 here.
        // Do not claim this individual file validates all 11 conditions.

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
}
