package converter;

import lombok.AllArgsConstructor;
import persistence.entity.Patient;
import proto.common.Common;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class PatientConverter {

//    public static List<Patient> convertPatientCommonToEntity(List<Common.Patient> patientList) {
//        return patientList.stream().map(patient ->
//                Patient.builder()
//                        .id(patient.getId())
//                        .firstName(patient.getFirstName())
//                        .lastName(patient.getLastName())
//                        .dateOfBirth(LocalDate.parse(patient.getDateOfBirth()))
//                        .gender(patient.getGender())
//                        .address(patient.getAddress())
//                        .phoneNumber(patient.getPhoneNumber())
//                        .build()).toList();
//    }
//
//    public static List<Common.Patient> convertPatientEntityToCommon(List<Patient> patientList) {
//        return patientList.stream().map(patient ->
//                Common.Patient.newBuilder()
//                        .setId(patient.getId())
//                        .setFirstName(patient.getFirstName())
//                        .setLastName(patient.getLastName())
//                        .setDateOfBirth(patient.getDateOfBirth().toString())
//                        .setGender(patient.getGender())
//                        .setAddress(patient.getAddress())
//                        .setPhoneNumber(patient.getPhoneNumber())
//                        .build()).toList();
//    }

    public static Patient convertPatientCommonToEntity(Common.Patient patient) {
        return
                Patient.builder()
                        .id(patient.getId())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .dateOfBirth(LocalDate.parse(patient.getDateOfBirth()))
                        .gender(patient.getGender())
                        .address(patient.getAddress())
                        .phoneNumber(patient.getPhoneNumber())
                        .build();
    }

    public static Common.Patient convertPatientEntityToCommon(Patient patient) {
        return
                Common.Patient.newBuilder()
                        .setId(patient.getId())
                        .setFirstName(patient.getFirstName())
                        .setLastName(patient.getLastName())
                        .setDateOfBirth(patient.getDateOfBirth().toString())
                        .setGender(patient.getGender())
                        .setAddress(patient.getAddress())
                        .setPhoneNumber(patient.getPhoneNumber())
                        .build();
    }
}
