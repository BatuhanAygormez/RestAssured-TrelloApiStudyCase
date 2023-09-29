import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TrelloAPITest {
    private String baseURI = "https://api.trello.com/1";
    private String token = "ATTAd04a7f319cee96c756c571f259972f5063e1a3ec97543ac33a8b7e6ebfca34971F096F26";
    private String key = "3aaaa8cac989379753e12580bc1a262e";
    private String boardId;
    private String listId;
    private String card1Id;
    private String card2Id;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = baseURI;
    }

    @Test(priority = 1)
    public void testCreateBoard() {
        String boardName = "MyNewBoard";
        boardId = RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .queryParam("name", boardName)
                .header("Content-Type", "application/json")
                .when()
                .post("/boards")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test(priority = 2)
    public void testCreateListFromBoard() {
        String listName = "MyNewList";
        listId = RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .queryParam("idBoard", boardId)
                .queryParam("name", listName)
                .header("Content-Type", "application/json")
                .when()
                .post("/lists")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }
    @Test(priority = 3)
    public void createCards() {
        Response response1 = RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .queryParam("idList", listId)
                .queryParam("name", "Card1")
                .header("Content-Type", "application/json")
                .when()
                .post("/cards");

        card1Id = response1.then()
                .statusCode(200)
                .extract()
                .path("id");

        Response response2 = RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .queryParam("idList", listId)
                .queryParam("name", "Card2")
                .header("Content-Type", "application/json")
                .when()
                .post("/cards");

        card2Id = response2.then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test(priority = 4)
    public void updateRandomCard() {
        String randomCardId = Math.random() < 0.5 ? card1Id : card2Id;

        RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .queryParam("id", randomCardId)
                .queryParam("name", "UpdatedCardName")
                .header("Content-Type", "application/json")
                .when()
                .put("/cards/{id}", randomCardId)
                .then()
                .statusCode(200);
    }

    @Test(priority = 5)
    public void deleteCards() {
        RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .header("Content-Type", "application/json")
                .when()
                .delete("/cards/{id}", card1Id)
                .then()
                .statusCode(200);

        RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .header("Content-Type", "application/json")
                .when()
                .delete("/cards/{id}", card2Id)
                .then()
                .statusCode(200);
    }


    @Test(priority = 6)
    public void testDeleteBoard() {
        RestAssured
                .given()
                .queryParam("token", token)
                .queryParam("key", key)
                .header("Content-Type", "application/json")
                .when()
                .delete("/boards/{id}", boardId)
                .then()
                .statusCode(200);
    }
}
