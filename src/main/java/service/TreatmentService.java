package service;

import converter.TreatmentConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.TreatmentRepository;
import persistence.entity.Treatment;
import proto.TreatmentServiceGrpc;
import proto.TreatmentServiceOuterClass.AddTreatmentRequest;
import proto.TreatmentServiceOuterClass.AddTreatmentResponse;
import proto.TreatmentServiceOuterClass.DeleteTreatmentRequest;
import proto.TreatmentServiceOuterClass.DeleteTreatmentResponse;
import proto.TreatmentServiceOuterClass.UpdateTreatmentRequest;
import proto.TreatmentServiceOuterClass.UpdateTreatmentResponse;

@GrpcService
public class TreatmentService extends TreatmentServiceGrpc.TreatmentServiceImplBase {

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Override
    public void addTreatment(AddTreatmentRequest request, StreamObserver<AddTreatmentResponse> responseObserver) {
        if (treatmentRepository.existsById(request.getTreatment().getId())) {
            AddTreatmentResponse resp = AddTreatmentResponse.newBuilder().build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
            return;
        }

        Treatment treatment = TreatmentConverter.convertTreatmentCommonToEntity(request.getTreatment());
        Treatment tmp = treatmentRepository.save(treatment);

        AddTreatmentResponse resp = AddTreatmentResponse.newBuilder().setTreatment(TreatmentConverter.convertTreatmentEntityToCommon(tmp)).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTreatment(DeleteTreatmentRequest request, StreamObserver<DeleteTreatmentResponse> responseObserver) {
        if (!treatmentRepository.existsById(request.getId())) {
            DeleteTreatmentResponse resp = DeleteTreatmentResponse.newBuilder().build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
            return;
        }

        treatmentRepository.deleteById(request.getId());
        DeleteTreatmentResponse resp = DeleteTreatmentResponse.newBuilder().setSuccess(true).setMessage("Successfully deleted!").build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTreatment(UpdateTreatmentRequest request, StreamObserver<UpdateTreatmentResponse> responseObserver) {
        if (!treatmentRepository.existsById(request.getTreatment().getId())) {
            UpdateTreatmentResponse resp = UpdateTreatmentResponse.newBuilder().build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
            return;
        }

        Treatment treatment = TreatmentConverter.convertTreatmentCommonToEntity(request.getTreatment());
        Treatment tmp = treatmentRepository.save(treatment);
        UpdateTreatmentResponse resp = UpdateTreatmentResponse.newBuilder().setTreatment(TreatmentConverter.convertTreatmentEntityToCommon(tmp)).build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
