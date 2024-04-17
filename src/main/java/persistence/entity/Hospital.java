package persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hospital {
    @Id
    private long id;
    private String name;
    private String department;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Patient> registeredPatients;
}
