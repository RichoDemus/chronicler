package com.richodemus.chronicler.test




    import io.grpc.ManagedChannel
    import io.grpc.ManagedChannelBuilder
    import io.grpc.StatusRuntimeException
    import io.grpc.examples.routeguide.GreeterGrpc
    import io.grpc.examples.routeguide.HelloReply
    import io.grpc.examples.routeguide.HelloRequest
    import org.slf4j.LoggerFactory
    import java.util.concurrent.TimeUnit
    import java.util.logging.Level
    import java.util.logging.Logger

    /**
     * A simple client that requests a greeting from the [HelloWorldServer].
     */
    internal class TestClient
    /** Construct client for accessing RouteGuide server using the existing channel.  */
    internal constructor(private val channel: ManagedChannel) {
        private val logger = LoggerFactory.getLogger(javaClass)
        private val blockingStub: GreeterGrpc.GreeterBlockingStub
                = GreeterGrpc.newBlockingStub(channel)

        /** Construct client connecting to HelloWorld server at `host:port`.  */
        constructor(host: String, port: Int) : this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build()) {
        }


        @Throws(InterruptedException::class)
        fun shutdown() {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
        }

        /** Say hello to server.  */
        fun greet(name: String): String? {
            logger.info("Will try to greet {}...", name)
            val request = HelloRequest.newBuilder().setName(name).build()
            val response: HelloReply =  try {
                blockingStub.sayHello(request)
            } catch (e: StatusRuntimeException) {
                logger.warn("RPC failed: {}", e.status)
                return "faaailed: ${e.message}"
            }

            logger.info("Greeting: ${response.message}")
            return response.message
        }

//        companion object {
//            private val logger = Logger.getLogger(HelloWorldClient::class.java.name)
//
//            /**
//             * Greet server. If provided, the first element of `args` is the name to use in the
//             * greeting.
//             */
//            @Throws(Exception::class)
//            @JvmStatic
//            fun main(args: Array<String>) {
//                val client = HelloWorldClient("localhost", 50051)
//                try {
//                    /* Access a service running on the local machine on port 50051 */
//                    val user = if (args.size > 0) "${args[0]}" else "world"
//                    client.greet(user)
//                } finally {
//                    client.shutdown()
//                }
//            }
//        }
    }