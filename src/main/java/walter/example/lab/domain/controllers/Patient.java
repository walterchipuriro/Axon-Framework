package walter.example.lab.domain.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import walter.example.lab.service.EventDTO;
import walter.example.lab.service.PatientService;
import walter.example.lab.service.RegisterPatientDTO;
import walter.example.lab.service.ResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class Patient {
    private final PatientService patientService;

    public Patient(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO add(@Valid @RequestBody RegisterPatientDTO patientDTO) {
        return patientService.register(patientDTO);
    }

    @GetMapping
    public List<walter.example.lab.repository.Patient> list() {
        return patientService.getPatients();
    }

    @GetMapping("/{id}/events")
    public List<EventDTO> events(@PathVariable("id") String id) {
        return patientService.getEvents(id);
    }
}
