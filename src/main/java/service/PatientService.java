package service;


import converter.HospitalConverter;
import converter.PatientConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import persistence.PatientRepository;

import persistence.entity.Patient;
import proto.PatientServiceGrpc;
import proto.PatientServiceOuterClass.AddPatientRequest;
import proto.PatientServiceOuterClass.AddPatientResponse;
import proto.PatientServiceOuterClass.UpdatePatientRequest;
import proto.PatientServiceOuterClass.UpdatePatientResponse;
import proto.PatientServiceOuterClass.DeletePatientRequest;
import proto.PatientServiceOuterClass.DeletePatientResponse;
import proto.PatientServiceOuterClass.GetAllHospitalRequest;
import proto.PatientServiceOuterClass.GetAllHospitalResponse;

import java.util.Optional;

@GrpcService
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void addPatient(AddPatientRequest patientRequest, StreamObserver<AddPatientResponse> responseObserver) {
        if (patientRepository.existsById(patientRequest.getPatient().getId())) {
            AddPatientResponse patientResponse = AddPatientResponse.newBuilder().build();

            responseObserver.onNext(patientResponse);
            responseObserver.onCompleted();
            return;
        }

        Patient patient = PatientConverter.convertPatientCommonToEntity(patientRequest.getPatient());
        Patient tmp = patientRepository.save(patient);
        AddPatientResponse patientResponse = AddPatientResponse.newBuilder().setPatient(PatientConverter.convertPatientEntityToCommon(tmp)).build();
        responseObserver.onNext(patientResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void updatePatient(UpdatePatientRequest updatePatientRequest, StreamObserver<UpdatePatientResponse> responseObserver) {
        if (!patientRepository.existsById(updatePatientRequest.getPatient().getId())) {
            UpdatePatientResponse patientResponse = UpdatePatientResponse.newBuilder().build();
            responseObserver.onNext(patientResponse);
            responseObserver.onCompleted();
            return;
        }

        Patient patient = PatientConverter.convertPatientCommonToEntity(updatePatientRequest.getPatient());
        Patient tmp = patientRepository.save(patient);

        UpdatePatientResponse patientResponse = UpdatePatientResponse.newBuilder().setPatient(PatientConverter.convertPatientEntityToCommon(tmp)).build();
        responseObserver.onNext(patientResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePatient(DeletePatientRequest deletePatientRequest, StreamObserver<DeletePatientResponse> responseObserver) {
        if (!patientRepository.existsById(deletePatientRequest.getId())) {
            DeletePatientResponse response = DeletePatientResponse.newBuilder().setSuccess(false)
                    .setMessage("Patient does not exist").build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        patientRepository.deleteById(deletePatientRequest.getId());
        DeletePatientResponse response = DeletePatientResponse.newBuilder().setSuccess(true).setMessage("Deleted!").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllHospitals(GetAllHospitalRequest request, StreamObserver<GetAllHospitalResponse> responseObserver) {

        Optional<Patient> patient = patientRepository.findById(request.getPatient().getId());

        if (patient.isEmpty()) {
            GetAllHospitalResponse response = GetAllHospitalResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        var list = patient.get().getRegisteredHospitals();
        GetAllHospitalResponse response = GetAllHospitalResponse.newBuilder().addAllHospitals(list.stream().map(HospitalConverter::convertHospitalEntityToCommon).toList()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
