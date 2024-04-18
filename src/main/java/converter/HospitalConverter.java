package converter;

import persistence.entity.Hospital;
import proto.common.Common;

public class HospitalConverter {
    public static Hospital convertHospitalCommonToEntity(Common.Hospital hospital) {
        var list = hospital.getRegisteredPatientsList();
        return Hospital.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .department(hospital.getDepartment())
                .registeredPatients(list.stream().map(PatientConverter::convertPatientCommonToEntity).toList())
                .build();
    }

    public static Common.Hospital convertHospitalEntityToCommon(Hospital hospital) {
        var list = hospital.getRegisteredPatients();
        return Common.Hospital.newBuilder()
                .setId(hospital.getId())
                .setDepartment(hospital.getDepartment())
                .setName(hospital.getName())
                .addAllRegisteredPatients(list.stream().map(PatientConverter::convertPatientEntityToCommon).toList())
                .build();
    }
}
