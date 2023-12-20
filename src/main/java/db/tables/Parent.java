package db.tables;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="parent_id")
    private int id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String surname;
    private String middleName;
    @Column(nullable=false)
    private String mail;
    private String pass;

    @ManyToMany(mappedBy = "parents")
    public Set<Student> children = new HashSet<>();

    public Parent(){}

    public Parent(String name, String surname, String middleName, String mail){
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.mail = mail;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public String getMiddleName() {
        return middleName;
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public Set<Student> getChildren() {
        return children;
    }
}