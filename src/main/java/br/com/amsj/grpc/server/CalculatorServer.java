package br.com.amsj.grpc.server;

import br.com.amsj.grpc.serviceImpl.CalculatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;

public class CalculatorServer {

    final static Logger logger = LogManager.getLogger(CalculatorServer.class);

    final static int PORT = 50081;

    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("CalculatorServer Initializing");

        Server server = ServerBuilder.forPort(PORT)
                .addService(new CalculatorServiceImpl())
                .build();

        server.start();

        logger.info("CalculatorServer Started on port " + PORT);

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("Received Shutdown Request");
            server.shutdown();
            logger.info("Successfully stopped the CalculatorServer");
        } ));

        server.awaitTermination();
    }
}
