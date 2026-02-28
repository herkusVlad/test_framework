package tests;

import EndPointData.FakeRESTApi.BookEndPoint;
import base.BaseTest;
import io.restassured.response.Response;
import model.Book;
import org.testng.Assert;
import org.testng.annotations.Test;

import static base.Priority.P0;
import static io.restassured.RestAssured.given;

public class BookTests extends BaseTest {

    @Test(testName = "Get all books", groups = {P0})
    public void testGetAllBooks() {

        createStep("Send GET request");
        Response response = given()
                .when()
                .get(BookEndPoint.GET)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check the response");
        Book[] books = response.as(Book[].class);
        Assert.assertTrue(books.length > 0, "Books list should not be empty");
    }

}
