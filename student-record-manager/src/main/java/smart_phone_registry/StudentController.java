package smart_phone_registry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

// ==========================================
// 1. STUDENT MODEL DATA ENTITY
// ==========================================
@Entity
@Table(name = "student_records")
class StudentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String studentClass;
    private Integer age;

    // --- PUBLIC GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStudentClass() { return studentClass; }
    public void setStudentClass(String studentClass) { this.studentClass = studentClass; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}

// ==========================================
// 2. DATA ACCESS LAYER REPOSITORY
// ==========================================
interface StudentRepository extends JpaRepository<StudentModel, Long> {
    // Custom Search Query: Automatically executes SQL LIKE query filter matching case-insensitive input text
    List<StudentModel> findByNameContainingIgnoreCase(String name);
}

// ==========================================
// 3. CENTRAL WEB CONTROLLER
// ==========================================
@Controller
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // A. View All & Handle Student Name Filtering
    @GetMapping("/students")
    public String viewStudentsPortal(@RequestParam(value = "search", required = false) String search, Model model) {
        List<StudentModel> records;
        if (search != null && !search.trim().isEmpty()) {
            records = studentRepository.findByNameContainingIgnoreCase(search);
            model.addAttribute("queryValue", search);
        } else {
            records = studentRepository.findAll();
        }
        model.addAttribute("studentCollection", records);
        return "student-list";
    }

    // B. Launch Fresh Data Forms
    @GetMapping("/student/new")
    public String launchRegistrationForm(Model model) {
        model.addAttribute("student", new StudentModel());
        model.addAttribute("panelTitle", "Add New Student Record");
        return "student-form";
    }

    // C. Save Entries & Update Active Records
    @PostMapping("/student/save")
    public String commitStudentRecord(@ModelAttribute("student") StudentModel student) {
        studentRepository.save(student);
        return "redirect:/students";
    }

    // D. Fetch Existing Record & Launch Edit View Block
    @GetMapping("/student/edit/{id}")
    public String launchModificationForm(@PathVariable("id") Long id, Model model) {
        StudentModel student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tracking ID: " + id));
        model.addAttribute("student", student);
        model.addAttribute("panelTitle", "Modify Student Details");
        return "student-form";
    }

    // E. Handle Destructive Record Deletions
    @GetMapping("/student/delete/{id}")
    public String purgeStudentRecord(@PathVariable("id") Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }
}
