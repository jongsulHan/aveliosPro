package persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "treatment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "treatment_id")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;
    @Column(name = "diagnosis")
    private String diagnosis;
    @Column(name = "treatment_date")
    private String treatmentDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
