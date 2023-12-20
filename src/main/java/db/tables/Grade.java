package db.tables;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="grade_id")
    private int id;
    private float value;
    @Column(name="student_id")
    private int studentId;
    @Column(name="teacher_id")
    private int teacherId;
    @Column(name="subject_id")
    private int subjectId;
    private String workName;
    @Temporal(TemporalType.DATE)
    private Date workDate;


    public Grade(){}

    public Grade(float value, Integer studentId, Integer teacherId, Integer subjectId, String workName, Date workDate){
        this.value = value;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.workName = workName;
        this.workDate = workDate;
    }


    public float getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getWorkName() {
        return workName;
    }
}
