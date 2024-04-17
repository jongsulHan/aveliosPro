package service;


import converter.PatientConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import persistence.PatientRepository;

import persistence.entity.Patient;
import proto.PatientServiceGrpc;
import proto.PatientServiceOuterClass;
import proto.PatientServiceOuterClass.AddPatientRequest;
import proto.PatientServiceOuterClass.AddPatientResponse;

@GrpcService
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void addPatient(AddPatientRequest patientRequest,
                           StreamObserver<AddPatientResponse> responseObserver) {
        if (patientRepository.existsById(patientRequest.getPatient().getId())) {
            AddPatientResponse patientResponse = AddPatientResponse.newBuilder().build();

            responseObserver.onNext(patientResponse);
            responseObserver.onCompleted();
            return;
        }


    }
}
