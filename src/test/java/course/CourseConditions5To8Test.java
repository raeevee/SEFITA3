package course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Member B: JUnit 5 test class for conditions 5-8. */
class CourseConditions5To8Test {

    private static final String DESCRIPTION = "write three paragraphs";
    private static final String SUBMISSION_TYPE = "pdf";
    private static final String PUBLISH = "20/11/2026 09:00";
    private static final String DUE = "23/11/2026 09:00";
    private static final String HIDE = "24/11/2026 09:00";

    private static boolean addAssessment(Course course, String name, int points) {
        return course.createNewAssessment(name, DESCRIPTION, points,
                SUBMISSION_TYPE, false, "", PUBLISH, DUE, HIDE);
    }

    private static void assertStoredDetails(Assessment assessment, String name, int points) {
        assertEquals(name, assessment.getName());
        assertEquals(DESCRIPTION, assessment.getDescription());
        assertEquals(points, assessment.getPoints());
        assertEquals(SUBMISSION_TYPE, assessment.getSubmissionType());
        assertFalse(assessment.isGroup());
        assertEquals("", assessment.getGroupingName());
        assertEquals(PUBLISH, assessment.getPublishDateTime());
        assertEquals(DUE, assessment.getDueDateTime());
        assertEquals(HIDE, assessment.getHideDateTime());
    }

    @BeforeEach
    void verifyFiftyPointBoundaryIsValid() {
        // Precondition: 50 is allowed. Use a separate course so this baseline
        // cannot make the rejection test accidentally fail the 100-point rule.
        Course baseline = new Course();
        assertTrue(baseline.getAssessments().isEmpty());
        assertTrue(addAssessment(baseline, "boundary assessment", 50),
                "precondition: exactly 50 points must be accepted");
        assertEquals(1, baseline.getAssessments().size());
        assertStoredDetails(baseline.getAssessments().get(0), "boundary assessment", 50);
    }

    @Test
    void tc05FiftyOnePointsRejectedWithoutChangingExistingAssessment() {
        Course course = new Course();
        assertTrue(course.getAssessments().isEmpty(), "precondition: course is empty");
        assertTrue(addAssessment(course, "existing assessment", 1),
                "precondition: a valid assessment is already stored");
        assertEquals(1, course.getAssessments().size());
        Assessment existing = course.getAssessments().get(0);
        assertStoredDetails(existing, "existing assessment", 1);

        // 51 is the first invalid integer above 50. The new name is unique;
        // total points would be only 52, so condition 6 cannot cause rejection.
        assertFalse(addAssessment(course, "boundary assessment", 51),
                "condition 5: an assessment above 50 points must be rejected");
        assertEquals(1, course.getAssessments().size(),
                "rejection must not add an assessment");
        assertSame(existing, course.getAssessments().get(0),
                "rejection must preserve the existing assessment object");
        assertStoredDetails(existing, "existing assessment", 1);
    }
}
