package db.tables;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class SchoolSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="subjectId")
    private int id;
    @Column(nullable=false)
    private String name;

    @OneToOne
    @JoinColumn(name = "subjectId")
    private Grade grade;

    @ManyToMany(mappedBy = "teacherSchoolSubjects")
    public Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(mappedBy = "classSchoolSubjects")
    public Set<SchoolClass> schoolClasses = new HashSet<>();


    public SchoolSubject(){}

    public SchoolSubject(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Set<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }
}
