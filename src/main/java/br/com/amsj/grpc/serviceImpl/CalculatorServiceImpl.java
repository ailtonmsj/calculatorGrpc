package br.com.amsj.grpc.serviceImpl;

import br.com.amsj.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    private static final Logger logger = LogManager.getLogger(CalculatorServiceImpl.class);

    @Override
    public StreamObserver<CalculatorAverageRequest> calculatorAverage(StreamObserver<CalculatorAverageResponse> responseObserver) {

        logger.info("Calling calculatorAverage");

        StreamObserver<CalculatorAverageRequest> averageRequestStreamObserver = new StreamObserver<CalculatorAverageRequest>() {

            int total = 0;
            double count = 0;

            @Override
            public void onNext(CalculatorAverageRequest value) {
                // when client sends a message
                total += value.getNumber();
                count++;
            }

            @Override
            public void onError(Throwable t) {
                // when client send an error
            }

            // when we want tyo send the response ( responseObserver )
            @Override
            public void onCompleted() {
                // client is done
                double average = total / count;

                responseObserver.onNext(
                        CalculatorAverageResponse.newBuilder()
                                .setAverage(average)
                                .build()
                );
                responseObserver.onCompleted();


            }
        };

        logger.info("Ending calculatorAverage");

        return averageRequestStreamObserver;
    }

    @Override
    public void calculatorSquareRoot(CalculatorSquareRootRequest request, StreamObserver<CalculatorSquareRootResponse> responseObserver) {

        logger.info("Calling calculatorSquareRoot");

        Integer number = request.getNumber();

        if(number >= 0){
            double squareRoot = Math.sqrt(number);
            responseObserver.onNext(CalculatorSquareRootResponse.newBuilder().
                    setSquare(squareRoot)
                    .build());
            responseObserver.onCompleted();
        }else{
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The number must be positive ( >= 0 )")
                            .augmentDescription("Number sent: " + number)
                            .asRuntimeException());
        }

        logger.info("Ending calculatorSquareRoot");
    }

    @Override
    public void calculatorSum(CalculatorSumRequest request, StreamObserver<CalculatorSumResponse> responseObserver) {

        logger.info("Calling calculatorSum");

        List<Integer> numbers = request.getNumbersList();

        int result = 0;

        for(Integer number : numbers){
            result += number;
        }

        responseObserver.onNext(CalculatorSumResponse.newBuilder().
                setResult(result).
                build());
        responseObserver.onCompleted();

        logger.info("Ending calculatorSum");
    }
}
