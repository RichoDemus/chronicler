package asd

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.examples.routeguide.*
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.stub.StreamObserver

fun main() {
//   io.grpc.ex
    val asd = HelloRequest.newBuilder()


}

class HelloWorldServer {

    init {
        start()
    }
    private var server: Server? = null

    fun start() {
        /* The port on which the server should run */
        val port = 50051
        server = ServerBuilder.forPort(port)
                .addService(GreeterImpl())
                .addService(ProtoReflectionService.newInstance())
                .build()
                .start()
//        logger.log(Level.INFO, "Server started, listening on {0}", port)
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down")
                this@HelloWorldServer.stop()
                System.err.println("*** server shut down")
            }
        })
    }

    fun stop() {
        server?.shutdown()
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    @Throws(InterruptedException::class)
    private fun blockUntilShutdown() {
        server?.awaitTermination()
    }

    internal class GreeterImpl : GreeterGrpc.GreeterImplBase() {

        override fun sayHello(req: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
            val reply = HelloReply.newBuilder().setMessage("Hello ${req.name}").build()
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }

    companion object {
//        private val logger = Logger.getLogger(HelloWorldServer::class.java.name)

        /**
         * Main launches the server from the command line.
         */
//        @Throws(IOException::class, InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val server = HelloWorldServer()
            server.start()
            server.blockUntilShutdown()
        }
    }
}
