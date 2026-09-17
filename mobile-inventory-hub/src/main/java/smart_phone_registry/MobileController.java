package smart_phone_registry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

// ==========================================
// 1. MOBILE MODEL DATA ENTITY
// ==========================================
@Entity
@Table(name = "mobile_phones")
class MobileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String type;

    // --- PUBLIC GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

// ==========================================
// 2. DATA REPOSITORY WITH CUSTOM QUERIES
// ==========================================
interface MobileRepository extends JpaRepository<MobileModel, Long> {

    // Custom Query 1: Fetch all phone records using explicit JPQL select
    @Query("SELECT m FROM MobileModel m")
    List<MobileModel> findAllPhonesCustom();

    // Custom Query 2: Fetch only phones that cost strictly less than ₹20,000
    @Query("SELECT m FROM MobileModel m WHERE m.price < :cutoffPrice")
    List<MobileModel> findBudgetPhonesCustom(@Param("cutoffPrice") Double cutoffPrice);

    // Custom Query 3: Count total phones grouped by type
    @Query("SELECT m.type, COUNT(m) FROM MobileModel m GROUP BY m.type")
    List<Object[]> countPhonesGroupedByTypeCustom();
}

// ==========================================
// 3. MVC WEB CONTROLLER
// ==========================================
@Controller
public class MobileController {

    private final MobileRepository mobileRepository;

    public MobileController(MobileRepository mobileRepository) {
        this.mobileRepository = mobileRepository;
    }

    // A. Endpoint to display collection data form layout
    @GetMapping("/mobile/new")
    public String showMobileForm(Model model) {
        model.addAttribute("mobile", new MobileModel());
        return "mobile-form";
    }

    // B. Commits values submitted into the internal H2 database table
    @PostMapping("/mobile/save")
    public String saveMobile(@ModelAttribute("mobile") MobileModel mobile) {
        mobileRepository.save(mobile);
        return "redirect:/mobiles";
    }

    // C. Evaluates individual custom queries and renders distinct view zones
    @GetMapping("/mobiles")
    public String displayInventoryDashboard(Model model) {
        // Run Custom Query 1: Full catalog data slice
        model.addAttribute("catalogItems", mobileRepository.findAllPhonesCustom());

        // Run Custom Query 2: Budget filtered slice below 20,000
        model.addAttribute("budgetItems", mobileRepository.findBudgetPhonesCustom(20000.0));

        // Run Custom Query 3: Aggregated data projection array blocks
        model.addAttribute("typeAggregation", mobileRepository.countPhonesGroupedByTypeCustom());

        return "mobile-list";
    }
}
