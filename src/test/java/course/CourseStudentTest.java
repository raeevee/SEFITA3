package course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Member A's JUnit 5 tests: TC01–TC04, validation conditions 1–4 only. */
class CourseStudentTest {

    // Valid baseline, with exactly one field changed for each invalid test.
    private static class Data {
        String name = "quiz one";
        String description = "write three paragraphs";
        int points = 1;
        String submissionType = "pdf";
        boolean group = false;
        String groupingName = "";
        String publish = "20/11/2026 09:00";
        String due = "23/11/2026 09:00";
        String hide = "24/11/2026 09:00";

        boolean addTo(Course course) {
            return course.createNewAssessment(name, description, points,
                    submissionType, group, groupingName, publish, due, hide);
        }
    }

    @BeforeEach
    void verifyPreconditionValidBaseline() {
        // Every negative test assumes that the unchanged baseline is valid.
        Course check = new Course();
        assertTrue(check.getAssessments().isEmpty(), "new course must be empty");
        assertTrue(new Data().addTo(check), "baseline data must be valid");
        assertEquals(1, check.getAssessments().size());
    }

    private static Course newEmptyCourse() {
        Course course = new Course();
        assertTrue(course.getAssessments().isEmpty(), "precondition: course is empty");
        return course;
    }

    private static void expectRejectedUnchanged(Course course, Data invalid) {
        List<Assessment> original = new ArrayList<>(course.getAssessments());
        assertFalse(invalid.addTo(course), "invalid creation must return false");
        assertEquals(original.size(), course.getAssessments().size(),
                "invalid creation must not add an assessment");
        for (int i = 0; i < original.size(); i++) {
            assertSame(original.get(i), course.getAssessments().get(i),
                    "pre-existing assessment object must remain unchanged");
        }
    }

    @Test
    void tc01DuplicateNameRejected() {
        Course course = newEmptyCourse();
        Data first = new Data();
        assertTrue(first.addTo(course), "precondition: first name already exists");
        assertEquals(1, course.getAssessments().size());

        // C1: Exact duplicate of an existing assessment name.
        Data duplicate = new Data();
        expectRejectedUnchanged(course, duplicate);
        Assessment retained = course.getAssessments().get(0);
        assertEquals(first.name, retained.getName());
        assertEquals(first.description, retained.getDescription());
        assertEquals(first.points, retained.getPoints());
        assertEquals(first.submissionType, retained.getSubmissionType());
        assertEquals(first.group, retained.isGroup());
        assertEquals(first.groupingName, retained.getGroupingName());
        assertEquals(first.publish, retained.getPublishDateTime());
        assertEquals(first.due, retained.getDueDateTime());
        assertEquals(first.hide, retained.getHideDateTime());
    }

    @Test
    void tc02WhitespaceOnlyNameRejected() {
        Course course = newEmptyCourse();
        Data invalid = new Data();
        invalid.name = " ";  // C2: whitespace is not a valid name.
        expectRejectedUnchanged(course, invalid);
    }

    @Test
    void tc03DescriptionWithTwoWordsRejected() {
        Course course = newEmptyCourse();
        Data invalid = new Data();
        invalid.description = "two words";  // C3: one below the 3-word minimum.
        expectRejectedUnchanged(course, invalid);
    }

    @Test
    void tc04ZeroPointsRejected() {
        Course course = newEmptyCourse();
        Data invalid = new Data();
        invalid.points = 0;  // C4: zero is the boundary below minimum 1.
        expectRejectedUnchanged(course, invalid);
    }
}
