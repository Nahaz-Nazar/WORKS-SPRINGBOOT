package student_management;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;

// 1. DATA MODEL
class Student {
    private int rollNo;
    private String name;
    private double marks;

    public Student(int rollNo, String name, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
    }

    public int getRollNo() { return rollNo; }
    public String getName() { return name; }
    public double getMarks() { return marks; }
}

// 2. CONTROLLER
@Controller
public class StudentController {

    @GetMapping("/student-info")
    public String getStudentInfo(Model model) {
        Student student = new Student(101, "Anjali Sharma", 92.5);
        model.addAttribute("student", student);
        return "student-info";
    }

    @GetMapping("/student-list")
    public String getStudentList(Model model) {
        List<Student> students = Arrays.asList(
            new Student(101, "Anjali Sharma", 92.5),
            new Student(102, "Rohit Mehta", 85.0),
            new Student(103, "Sneha Iyer", 78.6)
        );
        model.addAttribute("students", students);
        return "student-list";
    }
}
