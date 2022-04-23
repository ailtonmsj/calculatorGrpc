# gRPC Java boilerplate application ( build in gradle )

## To Generate Proto
```bash
./gradlew generateProto
```

# Structure:

### src/main/proto
Proto definitions

## src/main/java
Java implementation for gRPC based on gRPC generated code

### docker-build
File to build an image based on the application

### deploy
Files to deploy the application in a Kubernetes cluster with Istio

### googleapis
Files necessary to build .pb file, source "https://github.com/googleapis/googleapis"

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

# Test With evans ( Install evans is needed )

#### 1. Install evans
#### https://github.com/ktr0731/evans#installation
#### 2. Start the server
#### 3.1 ( WITHOUT DISCOVERY ) Run Evans looking for proto file:
    evans --proto <PATH-TO-PROTO-FILE> --port <PORT> --host <HOST>
    evans --proto calculator.proto --port 50081 --host localhost

#### 3.2 ( USING DISCOVERY ) Run Evans looking for proto file:
    evans --host <LOCALHOST> -p <PORT> -r
    evans --host localhost -p 50081 -r

#### 4. Discovery the services
    show services
#### 5. Call some service
    call <SERVICE-NAME>
    call CalculatorSquareRoot
    call CalculatorAverage

#### Tip:
#### To stop input streaming on Evans use "CTRL+D"

# Test with BloomRPC
#### Add .protofile and use "Unary Call" for "<SERVICE-DNS>>:<GRPC-NODE/LOAD-BALANCER-PORT>" example:
```bash
localhost:50081
```

# Deploy on Kubernetes ( Use for Istio - Service Mesh )

## To build a docker image ( linux/amd64 and linux/arm64 ):
```bash
docker buildx build -t <YOUR-DOCKERHUB-USER>/calculatorgrpc --platform linux/arm64,linux/amd64 --push -f docker-build/Dockerfile .
```

#### To consume use the Load Balancer for port 5000, or create Node port for Istio expose the application

## To configure the transcoder

### create a .pb file based on proto
```bash
protoc -Igoogleapis/ -I. --include_imports --include_source_info --descriptor_set_out=src/main/proto/calculator/proto.pb src/main/proto/calculator/calculator.proto
```

### To create the config map for transcoder from .pb file
```bash
kubectl -n calculatorgrpc create cm proto-descriptor --from-file=src/main/proto/calculator/proto.pb --dry-run=client -o yaml > deploy/06-calculatorgrpc-cm-proto.yaml
```

## To deploy on Kubernetes Cluster with Istio
#### ( It is Needed change the file deploy/05-calculatorgrpc-deploy.yaml to your image - Optional ):
```bash
kubectl apply -f deploy/
```

# Tests

## Test with BloomRPC
#### Add .protofile andSET THE URL for "<SERVICE-DNS>>:<GRPC-NODE/LOAD-BALANCER-PORT>" example:
```bash
"calculatorgrpc.teste.com.br:30119"
```

### Request for CalculatorSquareRoot ( unary )
```bash
{
"number": 81
}
```

### Request for CalculatorSum ( unary )
```bash
{
  "numbers": [
    10,
    20,
    30
  ]
}
```

### Request for CalculatorAverage ( streaming )
```bash
{
  "number": 10
}
```


## Transcoder Rest > gRPC -  ( Istio - Service Mesh ) - Using cURL

### CalculatorSquareRoot
```bash
curl --location --request GET 'https://<SERVICE-DNS>>:<HTTPS-PORT>>/v1/calculatorsquareroot/<SOME-INTEGER>', example "https://calculatorgrpc.teste.com.br:31767/v1/calculatorsquareroot/81" --header 'Content-Type: application/json'
```
example:
```bash
curl --location --request GET 'https://calculatorgrpc.teste.com.br:31767/v1/calculatorsquareroot/81' --header 'Content-Type: application/json'
```

### CalculatorSum
```bash
curl --location --request POST 'https://<SERVICE-DNS>:<HTTPS-PORT>/v1/calculatorsum' --header 'Content-Type: text/plain' \
--data-raw '[<ARRAY-INTEGER>]'
```
example:
```bash
curl --location --request POST 'https://calculatorgrpc.teste.com.br:31767/v1/calculatorsum' --header 'Content-Type: text/plain' \
--data-raw '  [
    10,
    20,
    30,
    30
  ]
'
```

### CalculatorAverage
```bash
curl --location --request POST 'https://<SERVICE-DNS>:<HTTPS-PORT>/v1/calculatorsquareroot/calculatoraverage' --header 'Content-Type: text/plain' \
--data-raw '[<ARRAY-INTEGER>]'
```
example:
```bash
curl --location --request POST 'https://calculatorgrpc.teste.com.br:31767/v1/calculatoraverage' --header 'Content-Type: text/plain' \
--data-raw '  [
    10,
    20,
    30,
    30,
    10
  ]
'
```

### Tip use curl -k to contour certificate problems


## Transcoder Rest > gRPC -  ( Istio - Service Mesh ) - Using cURL

### CalculatorSquareRoot
```bash
curl --location --request GET 'https://<SERVICE-DNS>>:<HTTPS-PORT>>/v1/calculatorsquareroot/<SOME-INTEGER>', example "https://calculatorgrpc.teste.com.br:31767/v1/calculatorsquareroot/81" --header 'Content-Type: application/json'
```
example:
```bash
curl --location --request GET 'https://calculatorgrpc.teste.com.br:31767/v1/calculatorsquareroot/81' --header 'Content-Type: application/json'
```

### CalculatorSum
```bash
curl --location --request POST 'https://<SERVICE-DNS>:<HTTPS-PORT>/v1/calculatorsum' --header 'Content-Type: text/plain' \
--data-raw '[<ARRAY-INTEGER>]'
```
example:
```bash
curl --location --request POST 'https://calculatorgrpc.teste.com.br:31767/v1/calculatorsum' --header 'Content-Type: text/plain' \
--data-raw '  [
    10,
    20,
    30,
    30
  ]
'
```

### CalculatorAverage
```bash
curl --location --request POST 'https://<SERVICE-DNS>:<HTTPS-PORT>/v1/calculatorsquareroot/calculatoraverage' --header 'Content-Type: text/plain' \
--data-raw '[<ARRAY-INTEGER>]'
```
example:
```bash
curl --location --request POST 'https://calculatorgrpc.teste.com.br:31767/v1/calculatoraverage' --header 'Content-Type: text/plain' \
--data-raw '  [
    10,
    20,
    30,
    30,
    10
  ]
'
```

### Tip use curl -k to contour certificate problems



