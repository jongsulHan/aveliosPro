package service;

import converter.HospitalConverter;
import converter.PatientConverter;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.HospitalRepository;
import persistence.PatientRepository;
import persistence.entity.Hospital;
import persistence.entity.Patient;
import proto.HospitalServiceGrpc;

import proto.HospitalServiceOuterClass.AddHospitalRequest;
import proto.HospitalServiceOuterClass.AddHospitalResponse;
import proto.HospitalServiceOuterClass.DeleteHospitalRequest;
import proto.HospitalServiceOuterClass.DeleteHospitalResponse;
import proto.HospitalServiceOuterClass.UpdateHospitalRequest;
import proto.HospitalServiceOuterClass.UpdateHospitalResponse;
import proto.HospitalServiceOuterClass.RegisterPatientRequest;
import proto.HospitalServiceOuterClass.RegisterPatientResponse;
import proto.HospitalServiceOuterClass.GetHospitalPatientsRequest;
import proto.HospitalServiceOuterClass.GetHospitalPatientsResponse;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GrpcService
@Transactional
public class HospitalService extends HospitalServiceGrpc.HospitalServiceImplBase {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void addHospital(AddHospitalRequest request, StreamObserver<AddHospitalResponse> responseObserver) {
        if (hospitalRepository.existsById(request.getHospital().getId())) {
            AddHospitalResponse response = AddHospitalResponse.newBuilder()
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Hospital hospital = HospitalConverter.convertHospitalCommonToEntity(request.getHospital());

        Hospital tmp = hospitalRepository.save(hospital);
        AddHospitalResponse response = AddHospitalResponse.newBuilder()
                .setHospital(HospitalConverter.convertHospitalEntityToCommon(tmp))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteHospital(DeleteHospitalRequest request, StreamObserver<DeleteHospitalResponse> responseObserver) {
        if (!hospitalRepository.existsById(request.getId())) {
            DeleteHospitalResponse response = DeleteHospitalResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("This hospital does not exist")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        hospitalRepository.deleteById(request.getId());
        DeleteHospitalResponse response = DeleteHospitalResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Successfully deleted hospital")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateHospital(UpdateHospitalRequest request, StreamObserver<UpdateHospitalResponse> responseObserver) {
        if (!hospitalRepository.existsById(request.getHospital().getId())) {
            UpdateHospitalResponse response = UpdateHospitalResponse.newBuilder()
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Hospital hospital = HospitalConverter.convertHospitalCommonToEntity(request.getHospital());

        Hospital tmp = hospitalRepository.save(hospital);
        UpdateHospitalResponse response = UpdateHospitalResponse.newBuilder()
                .setUpdatedHospital(HospitalConverter.convertHospitalEntityToCommon(tmp))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerPatient(RegisterPatientRequest request, StreamObserver<RegisterPatientResponse> responseObserver) {
        if (!hospitalRepository.existsById(request.getHospital().getId()) || patientRepository.existsById(request.getPatient().getId())) {
            RegisterPatientResponse response = RegisterPatientResponse.newBuilder()
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        List<Patient> toBeAddedList = new ArrayList<>(HospitalConverter.convertHospitalCommonToEntity(request.getHospital()).getRegisteredPatients());
        toBeAddedList.add(PatientConverter.convertPatientCommonToEntity(request.getPatient()));
        Hospital hospital = HospitalConverter.convertHospitalCommonToEntity(request.getHospital());
        hospital.setRegisteredPatients(toBeAddedList);

        Hospital responseHospital = hospitalRepository.save(hospital);

        RegisterPatientResponse response = RegisterPatientResponse.newBuilder()
                .setHospital(HospitalConverter.convertHospitalEntityToCommon(responseHospital)).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getHospitalPatients(GetHospitalPatientsRequest request, StreamObserver<GetHospitalPatientsResponse> responseObserver) {
        Optional<Hospital> hospital = hospitalRepository.findById(request.getHospital().getId());
        if (hospital.isEmpty()) {
            GetHospitalPatientsResponse response = GetHospitalPatientsResponse.newBuilder()
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        var list = hospital.get().getRegisteredPatients();
        GetHospitalPatientsResponse response = GetHospitalPatientsResponse.newBuilder()
                .addAllPatients(list.stream().map(PatientConverter::convertPatientEntityToCommon).toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
