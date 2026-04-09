package walter.example.lab.repository;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import walter.example.lab.en.Gender;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
public class Patient {

    @Id
    private String personId;

    @NotNull
    private String lastname;

    @NotNull
    private String firstname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender sex;

    @NotNull
    private LocalDate birthdate;
}
