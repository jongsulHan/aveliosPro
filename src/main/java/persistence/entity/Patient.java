package persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patient_id")
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birth_date")
    private LocalDate dateOfBirth;
    @Column(name = "gender")
    private String gender;
    @Column(name = "address")
    private String address;
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hospital_id")
    private List<Hospital> registeredHospitals;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "treatment_id")
    private List<Treatment> treatments;
}
