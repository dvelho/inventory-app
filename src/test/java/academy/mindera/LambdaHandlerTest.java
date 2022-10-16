package academy.mindera;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static academy.mindera.ProcessingService.CAN_ONLY_GREET_NICKNAMES;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class LambdaHandlerTest {

    @Test
    public void testSimpleLambdaSuccess() throws Exception {
        // you test your lambdas by invoking on http://localhost:8081
        // this works in dev mode too

        InputObject in = new InputObject();
        in.setName("Joaq");
        in.setGreeting("Hello");
        given()
                .contentType("application/json")
                .accept("application/json")
                .body(in)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body(containsString("Hello Joaq"));
    }

    @Test
    public void testSimpleLambdaNoSuccess() throws Exception {
        // you test your lambdas by invoking on http://localhost:8081
        // this works in dev mode too

        InputObject in = new InputObject();
        in.setName("Stuart");
        in.setGreeting("Hello");
        given()
                .contentType("application/json")
                .accept("application/json")
                .body(in)
                .when()
                .post()
                .then()
                .statusCode(500)
                .body(containsString(CAN_ONLY_GREET_NICKNAMES));
    }

}
