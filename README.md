# gRPC Java boilerplate application ( build in gradle )

## To Generate Proto
```bash
./gradlew generateProto
```

#Structure:

## /src/main/proto
Proto definitions

## /src/main/java
Java implementation for gRPC based on gRPC generated code

## Run the server
    br.com.amsj.grpc.calculator.server.CalculatorServer
### gRPC client streaming
    br.com.amsj.grpc.calculator.client.AverageServerStreamClient
### gRPC unary call with error handling
    br.com.amsj.grpc.calculator.client.SquareRootUnaryClient

### gRPC Reflections 
#### Reference: https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md

#### configuration
    build.gradle

# Test With evans (Install evans is needed)

#### 1. Install evans
#### https://github.com/ktr0731/evans#installation
#### 2. Start the server
#### 3.1 (WITHOUT DISCOVERY) Run Evans looking for proto file:
    evans --proto <PATH-TO-PROTO-FILE> --port <PORT> --host <HOST>
    evans --proto calculator.proto --port 50081 --host localhost

#### 3.2 (USING DISCOVERY) Run Evans looking for proto file:
    evans --host <LOCALHOST> -p <PORT> -r
    evans --host localhost -p 50081 -r

#### 4. Discovery the services
    show services
#### 5. Call some service
    call <SERVICE-NAME>
    call CalculatorSquareRoot
    call CalculatorAverage

#### Tips:
#### To stop input streaming on Evans use "CTRL+D"
