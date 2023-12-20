package db.tables;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private int id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String surname;
    private String middleName;
    @Column(name="class_name")
    private String className;
    @Column(nullable=false)
    private String mail;
    private String pass;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="student_parent",
            joinColumns = { @JoinColumn(name="student_id")},
            inverseJoinColumns = { @JoinColumn(name="parent_id") }
    )
    public Set<Parent> parents = new HashSet<>();

    public Student(){}
    public Student(Student student){
        id = student.id;
        name = student.name;
        surname = student.surname;
        middleName = student.middleName;
        className = student.className;
        mail = student.mail;
    }

    public Student(String name, String surname, String middleName, String className, String mail){
        this.className = className;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.mail = mail;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getClassName() {
        return className;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Parent> getParents() {
        return parents;
    }

    public String getMail() {
        return mail;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurname() {
        return surname;
    }
}
