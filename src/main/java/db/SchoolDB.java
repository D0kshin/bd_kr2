package db;

import db.tables.*;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import db.tables.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.security.auth.Subject;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class SchoolDB {

    private SessionFactory sessionFactory;
    private Session openSession() {
        if (sessionFactory == null) {
            final Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Grade.class);
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Teacher.class);
            configuration.addAnnotatedClass(Parent.class);
            configuration.addAnnotatedClass(SchoolSubject.class);
            configuration.addAnnotatedClass(SchoolClass.class);
            sessionFactory = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().build());
        }
        return sessionFactory.openSession();
    }
    public void addStudent(String name, String surname, String middleName, String className, String mail){
        Session session = openSession();
        session.getTransaction().begin();
        Student student = new Student(name, surname, middleName, className, mail);
        session.persist(student);

        session.getTransaction().commit();
    }

    public void addGrade(float value, Integer studentId, Integer teacherId, Integer subjectId, String workName, Date workDate){
        Session session = openSession();
        session.getTransaction().begin();
        Grade grade = new Grade(value, studentId, teacherId, subjectId, workName, workDate);
        session.persist(grade);
        session.getTransaction().commit();
    }

    public void addParent(String name, String surname, String middleName, String mail, Integer childId){
        Session session = openSession();
        session.getTransaction().begin();
        Parent parent = new Parent(name, surname, middleName, mail);
        session.persist(parent);
        Student child = session.find(Student.class, childId);
        if(child == null){
            System.out.println("вернуть ошибку неверный айди");
        }
        child.parents.add(parent);

        session.getTransaction().commit();
    }

    public void addTeacher(String name, String surname, String middleName, String mail){
        Session session = openSession();
        session.getTransaction().begin();
        Teacher teacher = new Teacher(name, surname, middleName, mail);
        session.persist(teacher);

        session.getTransaction().commit();
    }

    public void addSchoolClass(String schoolClassName){
        Session session = openSession();
        session.getTransaction().begin();
        if(getClassId(schoolClassName) == -1){
            SchoolClass schoolClass = new SchoolClass(schoolClassName);
            session.persist(schoolClass);
        }
        session.getTransaction().commit();
    }

    public void addSchoolSubject(String schoolSubjectName){
        Session session = openSession();
        session.getTransaction().begin();

        if(getSchoolSubjectId(schoolSubjectName) == -1){
            SchoolSubject schoolSubject = new SchoolSubject(schoolSubjectName);
            session.persist(schoolSubject);
        }
        session.getTransaction().commit();
    }

    public void addSubjectsToClass(String className, String... subjectNames){
        Session session = openSession();
        session.getTransaction().begin();
        int classId = getClassId(className);
        SchoolClass schoolClass = session.find(SchoolClass.class, classId);
        for(int i = 0; i < subjectNames.length; i++) {
            String subjectName = subjectNames[i];
            int schoolSubjectId = getSchoolSubjectId(subjectName);
            if(schoolSubjectId == -1){
                continue;
            }
            SchoolSubject subject = session.find(SchoolSubject.class, schoolSubjectId);
            schoolClass.classSchoolSubjects.add(subject);
        }
        session.getTransaction().commit();
    }

    public void addSubjectsToTeacher(Integer teacherId, String... subjectName){
        Session session = openSession();
        session.getTransaction().begin();
        for(int i = 0; i < subjectName.length; i++){
            Integer schoolSubjectId = getSchoolSubjectId(subjectName[i]);
            if(schoolSubjectId == -1){
                continue;
            }
            Teacher teacher = session.get(Teacher.class, teacherId);
            SchoolSubject schoolSubject = session.find(SchoolSubject.class, schoolSubjectId);
            teacher.teacherSchoolSubjects.add(schoolSubject);
        }
        session.getTransaction().commit();
    }

    public List<SchoolSubject> getSchoolSubjects(){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from SchoolSubject", SchoolSubject.class);
        List queryList = query.list();
        List subjectList = new ArrayList<>();
        for (Object object: queryList) {
            SchoolSubject subject = (SchoolSubject) object;
            subjectList.add(subject);
        }
        session.getTransaction().commit();
        return subjectList;
    }

    public List<SchoolClass> getSchoolClasses(){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from SchoolClass", SchoolClass.class);
        List queryList = query.list();
        List classesList = new ArrayList<>();
        for (Object object: queryList) {
            SchoolClass schoolClass = (SchoolClass) object;
            classesList.add(schoolClass);
        }
        session.getTransaction().commit();
        return classesList;
    }

    public int getSchoolSubjectId(String subjectName){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("select id from SchoolSubject where name = :subjectName", SchoolSubject.class);
        query.setParameter("subjectName", subjectName);
        List queryList = query.list();
        session.getTransaction().commit();
        if(queryList.isEmpty()){
            return -1;
        }
        return Integer.parseInt(queryList.get(0).toString());
    }

    public List<List<String>> getSchoolSubjectAndClassesId(){
        Session session = openSession();
        session.getTransaction().begin();
        List<SchoolSubject> subjectsList = getSchoolSubjects();
        List<List<String>> resultList = new ArrayList<>();
        subjectsList.forEach(subject -> {
            List<String> classList = new ArrayList<>();
            classList.add(subject.getName());
            subject.schoolClasses.forEach(schoolClass -> {
                classList.add(schoolClass.getName());
            });
            resultList.add(classList);
        });
        session.getTransaction().commit();
        return resultList;
    }



        public int getClassId(String className){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("select id from SchoolClass where name = :paramName", SchoolClass.class);
        query.setParameter("paramName", className);
        List queryList = query.list();
        session.getTransaction().commit();
        if(queryList.isEmpty()){
            return -1;
        }
        return Integer.parseInt(queryList.get(0).toString());
    }

    public Student getStudentByMail(String studentMail){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Student where mail = :paramName", Student.class);
        query.setParameter("paramName", studentMail);
        session.getTransaction().commit();

        return (Student) query.uniqueResult();
    }

    public List<Student> getStudentsByClass(Integer schoolClassId){
        Session session = openSession();
        session.getTransaction().begin();
        SchoolClass schoolClass = session.find(SchoolClass.class, schoolClassId);
        String schoolClassName = schoolClass.getName();
        Query query = session.createQuery("from Student where className = :paramName", Student.class);
        query.setParameter("paramName", schoolClassName);
        List<Student> studentList = query.list();
        session.getTransaction().commit();

        return studentList;
    }

    public Teacher getTeacherByMail(String teacherMail){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Teacher where mail = :teacherMail", Teacher.class);
        query.setParameter("teacherMail", teacherMail);
        session.getTransaction().commit();

        return (Teacher) query.uniqueResult();
    }

    public Parent getParentByMail(String parentMail){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Parent where mail = :teacherMail", Parent.class);
        query.setParameter("teacherMail", parentMail);
        session.getTransaction().commit();

        return (Parent) query.uniqueResult();
    }

    public Student getStudentById(Integer studentId){
        Session session = openSession();
        session.getTransaction().begin();
        Student student = session.get(Student.class, studentId);
        session.getTransaction().commit();

        return student;
    }

    public Parent getParentById(Integer parentId){
        Session session = openSession();
        session.getTransaction().begin();
        Parent parent = session.get(Parent.class, parentId);
        session.getTransaction().commit();

        return parent;
    }

    public Teacher getTeacherById(Integer teacherId){
        Session session = openSession();
        session.getTransaction().begin();
        Teacher teacher = session.get(Teacher.class, teacherId);
        session.getTransaction().commit();

        return teacher;
    }

    public List<SchoolSubject> getSchoolClassSubjects(Integer classId){
        Session session = openSession();
        session.getTransaction().begin();
        SchoolClass schoolClass = session.get(SchoolClass.class, classId);
        List<SchoolSubject> subjectsList = schoolClass.getSchoolSubjects().stream().toList();
        session.getTransaction().commit();
        return subjectsList;
    }

    public List<Grade> getGradesBySubject(Integer subjectId){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Grade where subjectId = :subjectId", Grade.class);
        query.setParameter("subjectId", subjectId);
        List<Grade> queryList = query.list();
        session.getTransaction().commit();

        return queryList;
    }

    public List<Grade> getGradesByStudent(Integer studentId){
        Session session = openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from Grade where studentId = :studentId", Grade.class);
        query.setParameter("studentId", studentId);
        List<Grade> queryList = query.list();
        session.getTransaction().commit();

        return queryList;
    }

    public Grade getGradesById(Integer gradeId){
        Session session = openSession();
        session.getTransaction().begin();

        Grade grade = session.get(Grade.class, gradeId);
        session.getTransaction().commit();

        return grade;
    }

    public List<Teacher> getTeachersBySubject(Integer subjectId){
        Session session = openSession();
        session.getTransaction().begin();
        SchoolSubject schoolSubject = session.get(SchoolSubject.class, subjectId);
        List<Teacher> teachers = schoolSubject.teachers.stream().toList();
        session.getTransaction().commit();
        return teachers;
    }

    public List<Student> getStudentsByTeacher(Integer teacherId){
        Session session = openSession();
        session.getTransaction().begin();
        List<SchoolClass> schoolClasses = new ArrayList<>();
        Teacher teacher = session.get(Teacher.class, teacherId);
        teacher.teacherSchoolSubjects.forEach(schoolSubject -> {
            schoolSubject.schoolClasses.forEach(schoolClass -> {
                schoolClasses.add(schoolClass);
            });
        });
        List<Student> resultStudentList = new ArrayList<>();
        schoolClasses.forEach(schoolClass -> {
            Query query = session.createQuery("from Student where className = :className", Student.class);
            query.setParameter("className", schoolClass.getName());
            List<Student> queryList = query.list();
            resultStudentList.addAll(queryList);
        });
        session.getTransaction().commit();
        return resultStudentList;
    }

    public void deleteGrade(Integer gradeId){
        Session session = openSession();
        session.getTransaction().begin();
        Grade grade = session.get(Grade.class, gradeId);
        session.delete(grade);
        session.getTransaction().commit();
    }
    public void deleteTeacher(Integer teacherId){
        Session session = openSession();
        session.getTransaction().begin();
        Teacher teacher = session.get(Teacher.class, teacherId);
        session.delete(teacher);
        session.getTransaction().commit();
    }
    public void deleteParent(Integer parentId){
        Session session = openSession();
        session.getTransaction().begin();
        Parent parent = session.get(Parent.class, parentId);
        session.delete(parent);
        session.getTransaction().commit();
    }
    public void deleteStudent(Integer studentId){
        Session session = openSession();
        session.getTransaction().begin();
        Student student = session.get(Student.class, studentId);
        session.delete(student);
        session.getTransaction().commit();
    }
    public void deleteSubject(Integer subjectId){
        Session session = openSession();
        session.getTransaction().begin();
        Subject subject = session.get(Subject.class, subjectId);
        session.delete(subject);
        session.getTransaction().commit();
    }
    public void deleteClass(Integer classId){
        Session session = openSession();
        session.getTransaction().begin();
        SchoolClass schoolClass = session.get(SchoolClass.class, classId);
        session.delete(schoolClass);
        session.getTransaction().commit();
    }

}
