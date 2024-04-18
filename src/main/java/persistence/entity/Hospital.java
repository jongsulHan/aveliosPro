package persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hospital")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hospital_id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "department")
    private String department;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "hospital_patient",
            joinColumns = @JoinColumn(name = "hospital_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private List<Patient> registeredPatients;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "treatment_id")
    private List<Treatment> treatments;
}
