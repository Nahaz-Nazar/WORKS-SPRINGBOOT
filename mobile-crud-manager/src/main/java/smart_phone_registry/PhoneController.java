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
import java.util.List;

// ==========================================
// 1. PHONE MODEL DATA ENTITY (Creates DB Table)
// ==========================================
@Entity
@Table(name = "shop_phones")
class PhoneModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;

    // --- PUBLIC GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}

// ==========================================
// 2. DATABASE ACCESS REPOSITORY LAYER
// ==========================================
interface PhoneRepository extends JpaRepository<PhoneModel, Long> {
}

// ==========================================
// 3. MVC CORE WEB ROUTING CONTROLLER
// ==========================================
@Controller
public class PhoneController {

    private final PhoneRepository phoneRepository;

    public PhoneController(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    // A. READ: View list of all phones in stock
    @GetMapping("/phones")
    public String viewAllPhones(Model model) {
        List<PhoneModel> allPhones = phoneRepository.findAll();
        model.addAttribute("phoneCollection", allPhones);
        return "phone-list";
    }

    // B. CREATE: Launch dynamic text form framework
    @GetMapping("/phone/new")
    public String showNewPhoneForm(Model model) {
        model.addAttribute("phone", new PhoneModel());
        model.addAttribute("panelTitle", "Register New Phone Model");
        return "phone-form";
    }

    // C. SAVE: Persist fresh item inserts or updates to active records
    @PostMapping("/phone/save")
    public String commitPhoneRecord(@ModelAttribute("phone") PhoneModel phone) {
        phoneRepository.save(phone);
        return "redirect:/phones";
    }

    // D. UPDATE: Fetch existing target record and launch form pre-filled
    @GetMapping("/phone/edit/{id}")
    public String showEditPhoneForm(@PathVariable("id") Long id, Model model) {
        PhoneModel existingPhone = phoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid phone data node link ID: " + id));
        model.addAttribute("phone", existingPhone);
        model.addAttribute("panelTitle", "Edit Existing Phone Specifications");
        return "phone-form";
    }

    // E. DELETE: Remove single entry structure completely
    @GetMapping("/phone/delete/{id}")
    public String purgePhoneRecord(@PathVariable("id") Long id) {
        phoneRepository.deleteById(id);
        return "redirect:/phones";
    }
}
