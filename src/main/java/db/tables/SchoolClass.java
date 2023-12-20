package db.tables;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="class_id")
    private int id;
    @Column(nullable=false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="class_subject",
            joinColumns = { @JoinColumn(name="class_id")},
            inverseJoinColumns = { @JoinColumn(name="subject_id") }
    )
    public Set<SchoolSubject> classSchoolSubjects = new HashSet<>();

    public SchoolClass(){}

    public SchoolClass(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<SchoolSubject> getSchoolSubjects() {
        return classSchoolSubjects;
    }

    public int getId() {
        return id;
    }
}
