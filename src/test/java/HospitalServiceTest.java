import converter.HospitalConverter;
import converter.PatientConverter;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.HospitalRepository;
import persistence.PatientRepository;
import persistence.entity.Hospital;
import persistence.entity.Patient;
import proto.HospitalServiceOuterClass;
import proto.common.Common;
import service.HospitalService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HospitalServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private HospitalService hospitalService;

    @Test
    void testAddHospital() {
        long hospitalId = 1L;
        String hospitalName = "Test Hospital";
        String department = "Cardiology";
        Hospital hospital = new Hospital(hospitalId, hospitalName, department, Collections.emptyList());
        HospitalServiceOuterClass.AddHospitalRequest request = HospitalServiceOuterClass.AddHospitalRequest.newBuilder()
                .setHospital(Common.Hospital.newBuilder()
                        .setId(hospitalId)
                        .setName(hospitalName)
                        .setDepartment(department)
                        .build())
                .build();

        HospitalServiceOuterClass.AddHospitalResponse expectedResponse = HospitalServiceOuterClass.AddHospitalResponse.newBuilder()
                .setHospital(Common.Hospital.newBuilder()
                        .setId(hospitalId)
                        .setName(hospitalName)
                        .setDepartment(department)
                        .build())
                .build();

        when(hospitalRepository.existsById(hospitalId)).thenReturn(false); // does not exist
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospital);

        StreamObserver<HospitalServiceOuterClass.AddHospitalResponse> observer = mock(StreamObserver.class);
        hospitalService.addHospital(request, observer);

        verify(hospitalRepository).existsById(hospitalId);
        verify(hospitalRepository).save(argThat(hospital1 ->
                hospital1.getId() == hospital.getId() &&
                        hospital1.getName().equals(hospital.getName()) &&
                        hospital1.getDepartment().equals(hospital.getDepartment()) &&
                        hospital1.getRegisteredPatients().equals(hospital.getRegisteredPatients())));
        verify(observer).onNext(expectedResponse);
        verify(observer).onCompleted();
    }

    @Test
    public void testDeleteHospital_Success() {
        long hospitalId = 1L;

        when(hospitalRepository.existsById(hospitalId)).thenReturn(true);

        StreamObserver<HospitalServiceOuterClass.DeleteHospitalResponse> observer = mock(StreamObserver.class);
        hospitalService.deleteHospital(HospitalServiceOuterClass.DeleteHospitalRequest.newBuilder().setId(hospitalId).build(), observer);

        verify(hospitalRepository).existsById(hospitalId);
        verify(hospitalRepository).deleteById(hospitalId);
        verify(observer).onNext(argThat(response -> response.getSuccess() && response.getMessage().equals("Successfully deleted hospital")));
        verify(observer).onCompleted();
    }

    @Test
    public void testDeleteHospital_NotFound() {
        long hospitalId = 1L;

        when(hospitalRepository.existsById(hospitalId)).thenReturn(false);

        StreamObserver<HospitalServiceOuterClass.DeleteHospitalResponse> observer = mock(StreamObserver.class);
        hospitalService.deleteHospital(HospitalServiceOuterClass.DeleteHospitalRequest.newBuilder().setId(hospitalId).build(), observer);

        verify(hospitalRepository).existsById(hospitalId);
        verify(hospitalRepository, times(0)).deleteById(hospitalId);
        verify(observer).onNext(argThat(response -> !response.getSuccess() && response.getMessage().equals("This hospital does not exist")));
        verify(observer).onCompleted();
    }

    @Test
    public void testRegisterPatient_Success() {
        long hospitalId = 1L;
        long patientId = 2L;
        String firstName = "Thomas";
        String lastName = "Müller";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String gender = "Male";
        String address = "address";
        String phoneNumber = "123";
        String hospitalName = "Test Hospital";
        String department = "Cardiology";

        Common.Hospital hospitalCommon = HospitalConverter.convertHospitalEntityToCommon(
                new Hospital(hospitalId, hospitalName, department, new ArrayList<>()));
        Common.Patient patientCommon = PatientConverter.convertPatientEntityToCommon(
                new Patient(patientId, firstName, lastName, birthDate, gender, address, phoneNumber, new ArrayList<>()));
        Common.Hospital updatedHospitalCommon = HospitalConverter.convertHospitalEntityToCommon(
                new Hospital(hospitalId, hospitalName, department,List.of(PatientConverter.convertPatientCommonToEntity(patientCommon))));
        HospitalServiceOuterClass.RegisterPatientRequest request = HospitalServiceOuterClass.RegisterPatientRequest.newBuilder()
                .setHospital(hospitalCommon)
                .setPatient(patientCommon)
                .build();

        when(hospitalRepository.existsById(hospitalId)).thenReturn(true);
        when(patientRepository.existsById(patientId)).thenReturn(false);
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(HospitalConverter.convertHospitalCommonToEntity(hospitalCommon));

        StreamObserver<HospitalServiceOuterClass.RegisterPatientResponse> observer = mock(StreamObserver.class);
        hospitalService.registerPatient(request, observer);

        verify(hospitalRepository).existsById(hospitalId);
        verify(patientRepository).existsById(patientId);
        verify(observer).onNext(argThat(response -> response.getHospital().getId() == updatedHospitalCommon.getId()));
        verify(observer).onCompleted();
    }
}
