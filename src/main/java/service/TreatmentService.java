package service;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.TreatmentRepository;

@GrpcService
public class TreatmentService {

    private TreatmentRepository treatmentRepository;
}
