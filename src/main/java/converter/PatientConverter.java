package converter;

import lombok.AllArgsConstructor;
import persistence.entity.Patient;
import proto.HospitalServiceOuterClass;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class PatientConverter {

    public static List<Patient> convertPatientToPatient(List<HospitalServiceOuterClass.Patient> patientList) {
        return patientList.stream().map(patient ->
                Patient.builder()
                        .id(patient.getId())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .dateOfBirth(LocalDate.parse(patient.getDateOfBirth()))
                        .gender(patient.getGender())
                        .address(patient.getAddress())
                        .phoneNumber(patient.getPhoneNumber())
                        .build()).toList();
    }

    public static List<HospitalServiceOuterClass.Patient> convertPatientFromPatient(List<Patient> patientList) {
        return patientList.stream().map(patient ->
                HospitalServiceOuterClass.Patient.newBuilder()
                        .setId(patient.getId())
                        .setFirstName(patient.getFirstName())
                        .setLastName(patient.getLastName())
                        .setDateOfBirth(patient.getDateOfBirth().toString())
                        .setGender(patient.getGender())
                        .setAddress(patient.getAddress())
                        .setPhoneNumber(patient.getPhoneNumber())
                        .build()).toList();
    }
}
