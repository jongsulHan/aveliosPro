package service;

import converter.HospitalConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.HospitalRepository;
import persistence.entity.Hospital;
import proto.HospitalServiceGrpc;

import proto.HospitalServiceOuterClass.AddHospitalRequest;
import proto.HospitalServiceOuterClass.AddHospitalResponse;
import proto.HospitalServiceOuterClass.DeleteHospitalRequest;
import proto.HospitalServiceOuterClass.DeleteHospitalResponse;
import proto.HospitalServiceOuterClass.UpdateHospitalRequest;
import proto.HospitalServiceOuterClass.UpdateHospitalResponse;

@GrpcService
public class HospitalService extends HospitalServiceGrpc.HospitalServiceImplBase {
    @Autowired
    private HospitalRepository hospitalRepository;


    @Override
    public void addHospital(AddHospitalRequest request, StreamObserver<AddHospitalResponse> responseObserver) {
        if (hospitalRepository.existsById(request.getHospital().getId())) {
            AddHospitalResponse response = AddHospitalResponse.newBuilder()
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Hospital hospital = HospitalConverter.convertHospitalToHospital(request.getHospital());

        Hospital tmp = hospitalRepository.save(hospital);
        AddHospitalResponse response = AddHospitalResponse.newBuilder()
                .setHospital(HospitalConverter.convertHospitalToGeneratedHospital(tmp))
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

        Hospital hospital = HospitalConverter.convertHospitalToHospital(request.getHospital());

        Hospital tmp = hospitalRepository.save(hospital);
        UpdateHospitalResponse response = UpdateHospitalResponse.newBuilder()
                .setUpdatedHospital(HospitalConverter.convertHospitalToGeneratedHospital(tmp))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
