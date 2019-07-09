package univ.study.recruitjogbo.security;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "spring.config.location=classpath:/google.yml")
public class OAuthConfigTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void google_로그인_시도하면_OAuth_인증창_나타난다() {
        given()
                .when()
                .redirects().follow(false)
                .get("/login")
                .then()
                .statusCode(302)
                .header("Location", containsString("https://accounts.google.com/o/oauth2/auth"));
    }

}
