package walter.example.lab.service;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import walter.example.lab.command.RegisterPatient;
import walter.example.lab.repository.Patient;
import walter.example.lab.repository.PatientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final CommandGateway gateway;
    private final PatientRepository patientRepository;
    private final EventStore eventStore;

    @Transactional
    public ResponseDTO register(RegisterPatientDTO patientDTO) {

        UUID patientId = UUID.randomUUID();

        RegisterPatient command = new RegisterPatient(
                patientId,
                patientDTO.getLastname(),
                patientDTO.getFirstname(),
                patientDTO.getSex(),
                patientDTO.getBirthdate()
        );

        gateway.sendAndWait(command);

        return new ResponseDTO(command.getPatientId().toString());
    }

    @Transactional(readOnly = true)
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getEvents(String patientId) {
        List<EventDTO> events = new ArrayList<>();
        var stream = eventStore.readEvents(patientId);
        while (stream.hasNext()) {
            var eventMessage = stream.next();
            events.add(new EventDTO(
                    eventMessage.getSequenceNumber(),
                    eventMessage.getPayloadType().getSimpleName(),
                    String.valueOf(eventMessage.getPayload()),
                    eventMessage.getTimestamp().toString()
            ));
        }
        return events;
    }
}
