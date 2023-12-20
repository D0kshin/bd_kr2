package db.tables;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_id")
    private int id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String surname;
    private String middleName;
    private boolean headTeacher = false;
    @Column(nullable=false)
    private String mail;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Grade grade;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="teacher_subject",
            joinColumns = { @JoinColumn(name="teacher_id")},
            inverseJoinColumns = { @JoinColumn(name="subject_id") }
    )
    public Set<SchoolSubject> teacherSchoolSubjects = new HashSet<>();

    private String pass;

    public Teacher(){}

    public Teacher(String name, String surname, String middleName, String mail){
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.mail = mail;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    public void appointHeadTeacher(){
        this.headTeacher = true;
    }
    public String getMiddleName() {
        return middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getMail() {
        return mail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<SchoolSubject> getSchoolSubjects() {
        return teacherSchoolSubjects;
    }
}

