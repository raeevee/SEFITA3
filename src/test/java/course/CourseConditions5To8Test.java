package course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
        assertStoredDetails(assessment, name, points, SUBMISSION_TYPE);
    }

    private static void assertStoredDetails(Assessment assessment, String name, int points,
                                            String submissionType) {
        assertStoredDetails(assessment, name, points, submissionType, false, "");
    }

    private static void assertStoredDetails(Assessment assessment, String name, int points,
                                            String submissionType, boolean group, String groupingName) {
        assertEquals(name, assessment.getName());
        assertEquals(DESCRIPTION, assessment.getDescription());
        assertEquals(points, assessment.getPoints());
        assertEquals(submissionType, assessment.getSubmissionType());
        assertEquals(group, assessment.isGroup());
        assertEquals(groupingName, assessment.getGroupingName());
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

    @Test
    void tc06TotalAboveOneHundredRejectedWithoutChangingExistingAssessments() {
        Course course = new Course();
        assertTrue(course.getAssessments().isEmpty(), "precondition: course is empty");
        assertTrue(addAssessment(course, "first assessment", 50));
        assertTrue(addAssessment(course, "second assessment", 49));
        assertEquals(2, course.getAssessments().size());
        assertEquals(99, course.getAssessments().stream().mapToInt(Assessment::getPoints).sum(),
                "precondition: existing assessments total 99 points");

        // C6 valid boundary: 50 + 49 + 1 = 100 must be accepted.
        assertTrue(addAssessment(course, "third assessment", 1),
                "condition 6: exactly 100 total points must be accepted");
        assertEquals(3, course.getAssessments().size());
        List<Assessment> original = List.copyOf(course.getAssessments());
        assertStoredDetails(original.get(0), "first assessment", 50);
        assertStoredDetails(original.get(1), "second assessment", 49);
        assertStoredDetails(original.get(2), "third assessment", 1);

        // C6 invalid boundary: 100 + 1 = 101. The new name is unique,
        // and one point satisfies C4 and C5, so only C6 causes rejection.
        assertFalse(addAssessment(course, "fourth assessment", 1),
                "condition 6: 101 total points must be rejected");
        assertEquals(original.size(), course.getAssessments().size(),
                "rejection must not add an assessment");
        for (int i = 0; i < original.size(); i++) {
            assertSame(original.get(i), course.getAssessments().get(i),
                    "rejection must preserve every existing assessment object");
        }
        assertStoredDetails(course.getAssessments().get(0), "first assessment", 50);
        assertStoredDetails(course.getAssessments().get(1), "second assessment", 49);
        assertStoredDetails(course.getAssessments().get(2), "third assessment", 1);
        assertEquals(100, course.getAssessments().stream().mapToInt(Assessment::getPoints).sum());
    }

    @Test
    void tc07UnsupportedSubmissionTypesRejectedWithoutChangingExistingAssessments() {
        Course course = new Course();
        assertTrue(course.getAssessments().isEmpty(), "precondition: course is empty");
        String[] validTypes = {"pdf", "zip", "doc"};

        // Precondition: every permitted type is accepted and stored correctly.
        for (String type : validTypes) {
            assertTrue(course.createNewAssessment(type + " assessment", DESCRIPTION, 1,
                    type, false, "", PUBLISH, DUE, HIDE));
        }
        assertEquals(3, course.getAssessments().size());
        List<Assessment> original = List.copyOf(course.getAssessments());
        for (int i = 0; i < validTypes.length; i++) {
            assertStoredDetails(original.get(i), validTypes[i] + " assessment", 1, validTypes[i]);
        }

        // C7: Unsupported and missing types fail. All other inputs are valid;
        // the name is unique and the proposed total is only four points.
        for (String invalidType : new String[] {"txt", "docx", "", null}) {
            assertFalse(course.createNewAssessment("invalid assessment", DESCRIPTION, 1,
                    invalidType, false, "", PUBLISH, DUE, HIDE),
                    "condition 7: unsupported or missing submission type must be rejected");
            assertEquals(original.size(), course.getAssessments().size(),
                    "rejection must not add an assessment");
            for (int i = 0; i < original.size(); i++) {
                assertSame(original.get(i), course.getAssessments().get(i));
                assertStoredDetails(course.getAssessments().get(i),
                        validTypes[i] + " assessment", 1, validTypes[i]);
            }
        }
    }

    @Test
    void tc08GroupWithoutGroupingNameRejectedWithoutChangingExistingAssessments() {
        Course course = new Course();
        assertTrue(course.getAssessments().isEmpty(), "precondition: course is empty");

        // Precondition: an individual assessment can have no grouping name.
        assertTrue(addAssessment(course, "individual assessment", 1));
        // A group assessment with a nonblank grouping name must be accepted.
        assertTrue(course.createNewAssessment("group assessment", DESCRIPTION, 1,
                SUBMISSION_TYPE, true, "team alpha", PUBLISH, DUE, HIDE));
        assertEquals(2, course.getAssessments().size());
        Assessment individual = course.getAssessments().get(0);
        Assessment grouped = course.getAssessments().get(1);
        assertStoredDetails(individual, "individual assessment", 1);
        assertStoredDetails(grouped, "group assessment", 1, SUBMISSION_TYPE, true, "team alpha");

        // C8: Empty, whitespace-only and null names are missing grouping names.
        // All other inputs are valid; the proposed course total is only 3.
        for (String missingName : new String[] {"", " ", null}) {
            assertFalse(course.createNewAssessment("invalid assessment", DESCRIPTION, 1,
                    SUBMISSION_TYPE, true, missingName, PUBLISH, DUE, HIDE),
                    "condition 8: group assessments need a grouping name");
            assertEquals(2, course.getAssessments().size(),
                    "rejection must not add an assessment");
            assertSame(individual, course.getAssessments().get(0));
            assertSame(grouped, course.getAssessments().get(1));
            assertStoredDetails(course.getAssessments().get(0), "individual assessment", 1);
            assertStoredDetails(course.getAssessments().get(1),
                    "group assessment", 1, SUBMISSION_TYPE, true, "team alpha");
        }
    }
}
