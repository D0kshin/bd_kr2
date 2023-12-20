package db.tests;

import org.junit.jupiter.api.*;
import db.SchoolDB;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchoolDBTests {
    @Test
    public void studentRegistrationTest(){
        SchoolDB db = new SchoolDB();
        db.addSchoolClass("9А");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "9A", "Ivan@mail.ru");
        Assertions.assertTrue(db.getStudentByMail("Ivan@mail.ru").getMail().equals("Ivan@mail.ru"));
        Assertions.assertTrue(db.getStudentByMail("Ivan@mail.ru").getName().equals("Ivan"));
        Assertions.assertTrue(db.getStudentByMail("Ivan@mail.ru").getId() == 1);
    }

    @Test
    public void listOfSubjectByClass(){
        SchoolDB db = new SchoolDB();
        db.addSchoolClass("2А");
        db.addSchoolClass("4Б");
        db.addSchoolClass("9Г");
        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");
        db.addSchoolSubject("физкультура");


        Set<String> firstClassSet = new HashSet();
        firstClassSet.add("география");
        firstClassSet.add("математика");
        db.addSubjectsToClass("2А", "география", "математика");

        Set<String> secondClassSet = new HashSet();
        secondClassSet.add("математика");
        secondClassSet.add("история");
        secondClassSet.add("физика");
        db.addSubjectsToClass("4Б",  "математика","история", "физика");

        Set<String> thirdClassSet = new HashSet();
        thirdClassSet.add("физкультура");
        thirdClassSet.add("история");
        thirdClassSet.add("физика");
        thirdClassSet.add("математика");
        db.addSubjectsToClass("9Г",  "физкультура","история", "физика", "математика", "математика");

        Set<String> firstTestClassSet = new HashSet();
        Set<String> secondTestClassSet = new HashSet();
        Set<String> thirdTestClassSet = new HashSet();
        db.getSchoolClassSubjects(db.getClassId("2А")).forEach(schoolSubject -> firstTestClassSet.add(schoolSubject.getName()));
        db.getSchoolClassSubjects(db.getClassId("4Б")).forEach(schoolSubject -> secondTestClassSet.add(schoolSubject.getName()));
        db.getSchoolClassSubjects(db.getClassId("9Г")).forEach(schoolSubject -> thirdTestClassSet.add(schoolSubject.getName()));

        Assertions.assertTrue(firstTestClassSet.equals(firstClassSet));
        Assertions.assertTrue(secondTestClassSet.equals(secondClassSet));
        Assertions.assertTrue(thirdTestClassSet.equals(thirdClassSet));
    }

    @Test
    public void viewGradesBySubject(){
        SchoolDB db = new SchoolDB();

        db.addSchoolClass("2А");
        db.addSchoolClass("3А");
        db.addSchoolClass("4Б");

        db.addStudent("Ivan2", "Ivanov2", "Ivanovich2", "3A", "Ivan@mail2.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "2A", "Ivan@mail.ru");

        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");
        db.addSubjectsToClass("2А", "география", "математика");
        db.addSubjectsToClass("4Б",  "математика","история", "физика");
        db.addTeacher("Валентин", "Ульянов", "", "Valentin@mail.ru");
        db.addTeacher("Алексей", "Калмыков", "Генадьевич", "Kalmik@mail.ru");
        db.addGrade(4, db.getStudentByMail("Ivan@mail.ru").getId(),
                db.getTeacherByMail("Valentin@mail.ru").getId(),
                db.getSchoolSubjectId("география"),
                "контрольная", new Date());
        db.addGrade(2, db.getStudentByMail("Ivan@mail.ru").getId(),
                db.getTeacherByMail("Kalmik@mail.ru").getId(),
                db.getSchoolSubjectId("физика"),
                "контрольная", new Date());
        db.addGrade(2, db.getStudentByMail("Ivan@mail.ru").getId(),
                db.getTeacherByMail("Kalmik@mail.ru").getId(),
                db.getSchoolSubjectId("физика"),
                "контрольная", new Date());
        db.addGrade(2, db.getStudentByMail("Ivan@mail.ru").getId(),
                db.getTeacherByMail("Kalmik@mail.ru").getId(),
                db.getSchoolSubjectId("физика"),
                "контрольная", new Date());
        db.addGrade(5, db.getStudentByMail("Ivan@mail.ru").getId(),
                db.getTeacherByMail("Valentin@mail.ru").getId(),
                db.getSchoolSubjectId("география"),
                "контрольная", new Date());

        Assertions.assertTrue(db.getGradesBySubject(db.getSchoolSubjectId("география")).size() == 2);
        Assertions.assertTrue(db.getGradesBySubject(db.getSchoolSubjectId("физика")).size() == 3);
    }

    @Test
    public void viewTeacherBySubject(){
        SchoolDB db = new SchoolDB();
        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");
        db.addTeacher("Валентин", "Ульянов", "", "Valentin@mail.ru");
        db.addTeacher("Алексей", "Калмыков", "Генадьевич", "Kalmik@mail.ru");
        db.addSubjectsToTeacher(db.getTeacherByMail("Kalmik@mail.ru").getId(), "география", "физика", "история");
        db.addSubjectsToTeacher(db.getTeacherByMail("Valentin@mail.ru").getId(), "математика", "физика");
        Assertions.assertTrue(db.getTeachersBySubject(db.getSchoolSubjectId("география")).get(0).getName().equals("Алексей"));
        Assertions.assertTrue(db.getTeachersBySubject(db.getSchoolSubjectId("математика")).get(0).getName().equals("Валентин"));
        Assertions.assertTrue(db.getTeachersBySubject(db.getSchoolSubjectId("физика")).size() == 2);
    }

    @Test
    public  void createTeachers(){
        SchoolDB db = new SchoolDB();
        db.addTeacher("Валентин", "Ульянов", "", "Valentin@mail.ru");
        db.addTeacher("Алексей", "Калмыков", "Генадьевич", "Kalmik@mail.ru");

        Assertions.assertTrue(db.getTeacherByMail("Valentin@mail.ru").getName().equals("Валентин"));
        Assertions.assertTrue(db.getTeacherByMail("Kalmik@mail.ru").getName().equals("Алексей"));
    }

    @Test
    public void createSchoolSubjects(){
        SchoolDB db = new SchoolDB();
        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");

        Assertions.assertTrue(db.getSchoolSubjects().size() == 4);
    }

    @Test
    public void viewSchoolSubjects(){
        SchoolDB db = new SchoolDB();
        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");
        db.addSchoolSubject("физика");

        Assertions.assertTrue(db.getSchoolSubjects().size() == 4);
    }

    @Test
    public void viewStutentsByTeacher(){
        SchoolDB db = new SchoolDB();

        db.addSchoolClass("9А");
        db.addSchoolClass("2А");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "9А", "Ivan@mail.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "9А", "Ivan@mail2.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "2А", "Ivan@mail3.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "10А", "Ivan@mail5.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "2А", "Ivan@mail4.ru");

        db.addStudent("Ivan", "Ivanov", "Ivanovich", "7А", "Ivan@mail6.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "7А", "Ivan@mail7.ru");

        db.addTeacher("Валентин", "Ульянов", "", "Valentin@mail.ru");
        db.addTeacher("Алексей", "Калмыков", "Генадьевич", "Kalmik@mail.ru");

        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");

        db.addSubjectsToTeacher(db.getTeacherByMail("Valentin@mail.ru").getId(), "география", "математика");
        db.addSubjectsToClass("9А", "география");
        db.addSubjectsToClass("2А", "география");

        Assertions.assertTrue(db.getStudentsByTeacher(db.getTeacherByMail("Valentin@mail.ru").getId()).size() == 4);
    }



    @Test
    public void viewSchoolClasses(){
        SchoolDB db = new SchoolDB();
        db.addSchoolClass("9А");
        db.addSchoolClass("2А");
        db.addSchoolClass("2А");
        db.addSchoolClass("5А");
        db.addSchoolClass("9А");
        db.addSchoolClass("9А");
        db.addSchoolClass("2А");

        Assertions.assertTrue(db.getSchoolClasses().size() == 3);
    }

    @Test
    public void viewSchoolSubjectsAndClassesInIt(){
        SchoolDB db = new SchoolDB();
        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");

        db.addSchoolClass("9А");
        db.addSchoolClass("2А");
        db.addSchoolClass("5А");

        db.addSubjectsToClass("9А", "математика","история", "физика");
        db.addSubjectsToClass("2А",  "география", "математика");

        db.getSchoolSubjectAndClassesId().forEach(list -> {
            list.forEach(item -> {
                System.out.println(item);
            });
        });
        Assertions.assertTrue(db.getSchoolSubjectAndClassesId().get(0).size() == 2);
        Assertions.assertTrue(db.getSchoolSubjectAndClassesId().get(1).size() == 3);
        Assertions.assertTrue(db.getSchoolSubjectAndClassesId().get(2).size() == 2);
        Assertions.assertTrue(db.getSchoolSubjectAndClassesId().get(3).size() == 2);
    }

    @Test
    public void viewStudentsByClass(){
        SchoolDB db = new SchoolDB();

        db.addSchoolClass("9А");
        db.addSchoolClass("2А");
        db.addSchoolClass("5А");

        db.addStudent("Ivan", "Ivanov", "Ivanovich", "9А", "Ivan@mail.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "9А", "Ivan@mail2.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "2А", "Ivan@mail3.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "5А", "Ivan@mail5.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "2А", "Ivan@mail4.ru");

        Assertions.assertTrue(db.getStudentsByClass(db.getClassId("9А")).size() == 2);
    }

    @Test
    public void viewGradesByStudent(){
        SchoolDB db = new SchoolDB();

        db.addSchoolClass("2А");
        db.addSchoolClass("3А");
        db.addSchoolClass("4Б");

        db.addStudent("Ivan2", "Ivanov2", "Ivanovich2", "3A", "Ivan@mail2.ru");
        db.addStudent("Ivan", "Ivanov", "Ivanovich", "2A", "Ivan@mail.ru");

        db.addSchoolSubject("география");
        db.addSchoolSubject("математика");
        db.addSchoolSubject("история");
        db.addSchoolSubject("физика");
        db.addSubjectsToClass("2А", "география", "математика");
        db.addSubjectsToClass("4Б",  "математика","история", "физика");
        db.addTeacher("Валентин", "Ульянов", "", "Valentin@mail.ru");
        db.addTeacher("Алексей", "Калмыков", "Генадьевич", "Kalmik@mail.ru");

        db.addSubjectsToTeacher(db.getTeacherByMail("Valentin@mail.ru").getId(),"география" );
        db.addSubjectsToTeacher(db.getTeacherByMail("Kalmik@mail.ru").getId(),"физика" );


        db.addGrade(4, db.getStudentByMail("Ivan@mail.ru").getId(),
                db.getTeacherByMail("Valentin@mail.ru").getId(),
                db.getSchoolSubjectId("география"),
                "контрольная", new Date());
        db.addGrade(4, db.getStudentByMail("Ivan@mail2.ru").getId(),
                db.getTeacherByMail("Kalmik@mail.ru").getId(),
                db.getSchoolSubjectId("физика"),
                "контрольная", new Date());
        db.addGrade(4, db.getStudentByMail("Ivan@mail.ru").getId(),
                db.getTeacherByMail("Kalmik@mail.ru").getId(),
                db.getSchoolSubjectId("физика"),
                "контрольная", new Date());

        Assertions.assertTrue(db.getGradesByStudent(db.getStudentByMail("Ivan@mail2.ru").getId()).size() == 1);
        Assertions.assertTrue(db.getGradesByStudent(db.getStudentByMail("Ivan@mail.ru").getId()).size() == 2);
    }
}
