package persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Treatment {
    @Id
    private long id;
    private String diagnosis;
    private LocalDate treatmentDate;
    private String physicianID;
    private String notes;
    @OneToOne
    private Hospital hospital;
}
