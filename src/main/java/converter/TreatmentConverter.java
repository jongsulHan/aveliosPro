package converter;

import persistence.entity.Patient;
import persistence.entity.Treatment;
import proto.common.Common;

public class TreatmentConverter {

    public static Treatment convertTreatmentCommonToEntity(Common.Treatment treatment) {
        return Treatment.builder()
                .id(treatment.getId())
                .patient(PatientConverter.convertPatientCommonToEntity(treatment.getPatient()))
                .diagnosis(treatment.getDiagnosis())
                .treatmentDate(treatment.getTreatmentDate())
                .hospital(HospitalConverter.convertHospitalCommonToEntity(treatment.getHospital()))
                .build();
    }

    public static Common.Treatment convertTreatmentEntityToCommon(Treatment treatment) {
        return Common.Treatment.newBuilder()
                .setId(treatment.getId())
                .setPatient(PatientConverter.convertPatientEntityToCommon(treatment.getPatient()))
                .setDiagnosis(treatment.getDiagnosis())
                .setTreatmentDate(treatment.getTreatmentDate())
                .setHospital(HospitalConverter.convertHospitalEntityToCommon(treatment.getHospital()))
                .build();
    }
}
