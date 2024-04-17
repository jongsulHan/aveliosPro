package converter;

import lombok.AllArgsConstructor;
import persistence.entity.Hospital;
import proto.HospitalServiceOuterClass;

public class HospitalConverter {
    public static Hospital convertHospitalToHospital(HospitalServiceOuterClass.Hospital hospital) {
        return Hospital.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .department(hospital.getDepartment())
                .registeredPatients(PatientConverter.convertPatientToPatient(hospital.getRegisteredPatientIdsList()))
                .build();
    }

    public static HospitalServiceOuterClass.Hospital convertHospitalToGeneratedHospital(Hospital hospital) {
        return HospitalServiceOuterClass.Hospital.newBuilder()
                .setId(hospital.getId())
                .setDepartment(hospital.getDepartment())
                .setName(hospital.getName())
                .addAllRegisteredPatientIds(PatientConverter.convertPatientFromPatient(hospital.getRegisteredPatients()))
                .build();
    }
}
