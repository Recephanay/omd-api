import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertNotNull;

public class OmdbApiTest {


    @Test
    public void shouldSearchByTitleAndYear(){
        Response response = RestAssured.given()
           .queryParam("t","Harry Potter")
            .queryParam("y,","2011")
                .queryParam("apikey","3df7714d")
                .get("https://omdbapi.com/")
                .then()
                .statusCode(200)
                .extract().response();
        response.getBody().prettyPrint();
        assertThat(response.getBody().jsonPath().getString("Title"), Matchers.containsString("Harry Potter"));

    }
    @Test
    public void shouldSearchByImdbId(){
        Response byIdOrTitle = RestAssured.given()
                .queryParam("t","Harry Potter")
                .queryParam("y,","2011")
                .queryParam("apikey","3df7714d")
                .get("https://omdbapi.com/")
                .then()
                .statusCode(200)
                .extract().response();
        String imdbID = byIdOrTitle.getBody().jsonPath().getString("imdbID");
        assertNotNull(imdbID);

        Response bySearch = RestAssured.given()
                .queryParam("i", imdbID)
                .queryParam("apikey", "3df7714d")
                .get("https://omdbapi.com/")
                .then()
                .statusCode(200)
                .extract().response();
        assertThat(byIdOrTitle.getBody().jsonPath().getString("Title"),Matchers.is(bySearch.getBody().jsonPath().getString("Title")));
    }
    @Test
    public void shouldNotGetResponseWithoutApiKey(){
        Response response = RestAssured.given()
                .queryParam("t", "Harry Potter")
                .get("https://omdbapi.com/")
                .then()
                .statusCode(401)
                .extract().response();
        assertThat(response.getBody().jsonPath().getString("Error"),Matchers.is("No API key provided"));
    }

}
