package uk.gov.dvla.poc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dvla.poc.events.LicenceCreated;
import uk.gov.dvla.poc.forms.BicycleLicenceCreateForm;
import uk.gov.dvla.poc.forms.BicycleLicenceQueryForm;
import uk.gov.dvla.poc.forms.BicycleLicenceUpdateForm;
import uk.gov.dvla.poc.model.BicycleLicence;
import uk.gov.dvla.poc.repository.BicycleLicenceDynamoDBRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Controller
public class HomeController {

    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    private BicycleLicenceDynamoDBRepository licenceDynamoDBRepository;

    public HomeController(BicycleLicenceDynamoDBRepository licenceDynamoDBRepository) {
        this.licenceDynamoDBRepository = licenceDynamoDBRepository;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

    @GetMapping("/licenceMgmt")
    public String createLicenceForm(HttpServletRequest request, Model model) {
        addLicencesToModel(request, model);
        model.addAttribute("message", "");
        initForms(model);
        return "licenceMgmt";
    }

    @PostMapping("/submitLicenceForm")
    public String submitLicenceForm(HttpServletRequest request, @ModelAttribute BicycleLicenceCreateForm form, Model model) {
        Iterable<BicycleLicence> licences = addLicencesToModel(request, model);
        BicycleLicence existingLicence = StreamSupport.stream(licences.spliterator(), false)
                .filter(licence -> form.getEmail().equals(licence.getEmail()))
                .findAny()
                .orElse(null);

        if(existingLicence != null) {
            model.addAttribute("message", "Email already exists - please try again");
            initForms(model);
            return "licenceMgmt";
        }
        BicycleLicence licence = new BicycleLicence();
        licence.setId(UUID.randomUUID().toString());
        licence.setEmail(form.getEmail());
        licence.setName(form.getName());
        licence.setTelephone(form.getTelephone());
        licence.setPenaltyPoints(0);
        licence.addEvent(new LicenceCreated());
        BicycleLicence committedLicence = licenceDynamoDBRepository.save(licence);
        model.addAttribute("message", "Bicycle Licence Added: Doc Id(" + committedLicence.getId() + ")");
        addLicencesToModel(request, model);

        return "licenceMgmt";
    }



    private void initForms(Model model) {
        BicycleLicenceCreateForm form = new BicycleLicenceCreateForm();
        model.addAttribute("bicycleLicenceCreateForm", form);

        BicycleLicenceQueryForm queryForm = new BicycleLicenceQueryForm();
        model.addAttribute("bicycleLicenceQueryForm", queryForm);

        BicycleLicenceUpdateForm licenceUpdateForm = new BicycleLicenceUpdateForm();
        model.addAttribute("bicycleLicenceUpdateForm", licenceUpdateForm);
    }

    private Iterable<BicycleLicence> addLicencesToModel(HttpServletRequest request, Model model) {
        Iterable<BicycleLicence> licences = licenceDynamoDBRepository.findAll();
        model.addAttribute("licences", licences);
        request.getSession().setAttribute("licences", licences);
        return licences;
    }
}