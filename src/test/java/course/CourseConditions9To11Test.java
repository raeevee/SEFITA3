package course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseConditions9To11Test {
    // valid dates used in most tests
    private static final String PUBLISH = "20/11/2026 09:00";
    private static final String DUE = "23/11/2026 09:00";
    private static final String HIDE = "24/11/2026 09:00";

    private Course course;

    @BeforeEach
    void setUp() {
        course = new Course();
    }

    // shortcut so each test is shorter
    private boolean add(String name, boolean group, String groupingName,
                        String publish, String due, String hide) {
        return course.createNewAssessment(name, "write three paragraphs", 10, "pdf",
                group, groupingName, publish, due, hide);
    }

    @Test
    void tc09DuplicateGroupingName() {
        // precondition: course is empty, then add the first group one
        assertTrue(course.getAssessments().isEmpty());
        assertTrue(add("group one", true, "team alpha", PUBLISH, DUE, HIDE));

        // same grouping name again should fail
        assertFalse(add("group two", true, "team alpha", PUBLISH, DUE, HIDE));
        assertEquals(1, course.getAssessments().size()); // nothing new added
    }

    @Test
    void tc10aWrongFormat() {
        assertEquals(0, course.getAssessments().size()); // precondition
        // date written the wrong way round
        assertFalse(add("test", false, "", "2026-11-20 09:00", DUE, HIDE));
        assertEquals(0, course.getAssessments().size());
    }

    @Test
    void tc10bImpossibleDate() {
        // 2026 is not a leap year so 29/02 doesn't exist
        assertFalse(add("test", false, "", "29/02/2026 20:00", DUE, HIDE));
        assertEquals(0, course.getAssessments().size());
        // 2028 is a leap year so this one is fine
        assertTrue(add("test", false, "", "29/02/2028 20:00", "01/03/2028 20:00", "02/03/2028 20:00"));
    }

    @Test
    void tc11aDueBeforePublish() {
        // due same as publish (boundary) so it fails
        assertFalse(add("test", false, "", PUBLISH, PUBLISH, HIDE));
        // one minute after publish is ok
        assertTrue(add("test", false, "", PUBLISH, "20/11/2026 09:01", HIDE));
    }

    @Test
    void tc11bHideBeforeDue() {
        // hide same as due so it fails
        assertFalse(add("test", false, "", PUBLISH, DUE, DUE));
        // one minute after due is ok
        assertTrue(add("test", false, "", PUBLISH, DUE, "23/11/2026 09:01"));
    }

    @Test
    void tc12AllValid() {
        // three different valid ones
        assertTrue(add("one", false, "", PUBLISH, DUE, HIDE));
        assertTrue(add("two", true, "team beta", PUBLISH, DUE, HIDE));
        assertTrue(add("three", false, "", PUBLISH, DUE, HIDE));
        assertEquals(3, course.getAssessments().size());

        // check the attributes were saved properly
        Assessment a = course.getAssessments().get(1);
        assertEquals("two", a.getName());
        assertEquals("team beta", a.getGroupingName());
        assertEquals(DUE, a.getDueDateTime());
    }
}
