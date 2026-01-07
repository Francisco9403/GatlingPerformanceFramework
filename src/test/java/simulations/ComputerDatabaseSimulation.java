package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ComputerDatabaseSimulation extends Simulation {

    // 1. PROTOCOLO HTTP
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://jsonplaceholder.typicode.com")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling/PerformanceFramework");

    // 2. DATA FEEDER (Carga de datos dinámica)
    // "circular" significa que si se acaban los datos, vuelve a empezar desde el 1.
    FeederBuilder.FileBased<String> csvFeeder = csv("data/posts.csv").circular();

    // 3. DEFINICIÓN DEL ESCENARIO
    ScenarioBuilder myScenario = scenario("Advanced API Load Test")
            .feed(csvFeeder) // Inyectamos una línea del CSV a cada usuario virtual

            .exec(
                    http("01_Get_Specific_Post")
                            // Usamos #{postId} para leer el valor del CSV
                            .get("/posts/#{postId}")
                            .check(status().is(200))
                            // Guardamos el título de la respuesta para usarlo después (Correlación)
                            .check(jsonPath("$.title").saveAs("postTitle"))
            )
            .pause(1, 3) // Pausa aleatoria entre 1 y 3 segundos (Más realista)

            .exec(session -> {
                // Imprimimos en consola para debuguear (Opcional)
                System.out.println("Usuario consultó post: " + session.getString("postId"));
                return session;
            })

            .exec(
                    http("02_Get_Comments_For_Post")
                            .get("/posts/#{postId}/comments")
                            .check(status().is(200))
                            .check(bodyString().saveAs("responseBody"))
            )
            .exec(
                    http("03_Create_Post")
                            .post("/posts")
                            .body(StringBody("{ \"title\": \"#{postTitle}\", \"body\": \"Performance Test\", \"userId\": #{postId} }"))
                            .check(status().is(201))
            );

    // 4. INYECCIÓN Y ASERCIONES (SLAs)
    {
        setUp(
                myScenario.injectOpen(
                        rampUsers(20).during(10) // 20 usuarios en 10 segundos
                )
        ).protocols(httpProtocol)
                // ASERCIONES: Criterios para aprobar o reprobar el test
                .assertions(
                        // 1. El 99% de las peticiones deben tardar menos de 500ms
                        global().responseTime().percentile3().lt(500),

                        // 2. El 100% de las peticiones deben ser exitosas (0 fallos)
                        global().successfulRequests().percent().is(100.0),

                        // 3. La media de tiempo de respuesta debe ser menor a 300ms
                        global().responseTime().mean().lt(300)
                );
    }
}