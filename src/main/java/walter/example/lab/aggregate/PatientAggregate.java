package walter.example.lab.aggregate;


import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import walter.example.lab.command.RegisterPatient;
import walter.example.lab.en.PatientRegistered;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
public class PatientAggregate {

    @AggregateIdentifier
    private UUID patientId;

    @CommandHandler
    public PatientAggregate(RegisterPatient command) {
        PatientRegistered event = new PatientRegistered(
                command.getPatientId(),
                command.getLastname(),
                command.getFirstname(),
                command.getSex(),
                command.getBirthdate()
        );
        apply(event);
    }

    @EventSourcingHandler
    public void on(PatientRegistered event) {
        this.patientId = event.getPatientId();
    }
}
