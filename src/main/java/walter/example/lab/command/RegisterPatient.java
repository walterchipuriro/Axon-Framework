package walter.example.lab.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import walter.example.lab.en.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class RegisterPatient {

    @TargetAggregateIdentifier
    UUID patientId;
    String lastname;
    String firstname;
    Gender sex;
    LocalDate birthdate;
}
