package walter.example.lab.service.handler;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import walter.example.lab.en.PatientRegistered;
import walter.example.lab.repository.Patient;
import walter.example.lab.repository.PatientRepository;

@Component
@RequiredArgsConstructor
public class PatientEventHandler {

    private final PatientRepository patientRepository;

    @EventHandler
    @Transactional
    public void on(PatientRegistered event) {
        Patient patient = new Patient(
                event.getPatientId().toString(),
                event.getLastname(),
                event.getFirstname(),
                event.getSex(),
                event.getBirthdate()
        );
        patientRepository.save(patient);
    }
}

