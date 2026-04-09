package walter.example.lab.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import walter.example.lab.en.Gender;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientDTO {

    private String lastname;

    private String firstname;

    private Gender sex;

    private LocalDate birthdate;

}
