# StudentScholar Assessment 4 – Individual Student Contribution


## Contribution summary

- Conditions 1–4 of `Course.createNewAssessment()`: unique assessment name, nonblank name, description of at least three words, and points greater than zero.
- Four JUnit 5 cases in `src/test/java/course/CourseStudentTest.java` (TC01–TC04), with precondition assertions, return-value checks, and checks that rejected inputs leave the assessment list unchanged.
- `docs/Student_Test_Cases.docx`: test-case documentation.
- `docs/Student_User_Story.docx`: Student user story and three acceptance criteria.

## Included support classes

`Course.java` and `Assessment.java` provide a runnable partial implementation and assessment storage so the Student tests can execute. `pom.xml` provides Eclipse Maven/JUnit 5 configuration.

**Group integration warning:** This `Course.java` validates only conditions 1–4. It does *not* check conditions 5–11 and should not overwrite a teammate's work without merging. A complete group submission still needs all 11 rules, the remaining tests, and the video.

## Run

1. Eclipse: File → Import → Maven → Existing Maven Projects, select the project root.
2. Run `CourseStudentTest.java` as JUnit Test, or run `mvn test` if Maven is installed.
3. Record actual JUnit results in the test-case document. Do not mark them passed before running them in the IDE.

## Ownership and academic integrity

This is a suggested Student contribution prepared for review. The individual must understand, review, adapt, and independently validate the material before submitting it as their own work. The course instructions prohibit directly copying and pasting AI-generated answers.
