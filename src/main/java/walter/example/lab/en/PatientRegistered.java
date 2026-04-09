package walter.example.lab.en;


import lombok.Value;
import org.axonframework.serialization.Revision;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Revision("1.0")
public class PatientRegistered {

    UUID patientId;

    String lastname;

    String firstname;

    Gender sex;

    LocalDate birthdate;
}
