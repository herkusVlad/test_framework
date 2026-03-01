package tests;

import EndPointData.FakeRESTApi.POJO.BookEndPoint;
import base.BaseTest;
import base.TestInfo;
import io.restassured.response.Response;
import model.book.Book;
import model.book.Book400Error;
import model.book.Book404Error;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.Random;

import static base.Priority.*;
import static io.restassured.RestAssured.given;

public class BookTests extends BaseTest {

    @Test(testName = "Get all books", groups = {P0})
    public void getAllBooks() {
        addTestInfo(new TestInfo()
                .setId("TK-1")
                .setName("Get All books")
                .setDescription("Testing GET request for books API")
                .setSuite("Book")
        );

        createStep("Create GET request");
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

    @Test(testName = "Get all books performance test", groups = {P2})
    public void getAllBooksPerformanceTest() {
        addTestInfo(new TestInfo()
                .setId("TK-2")
                .setName("Get all books performance test")
                .setDescription("Testing performance for GET request books API")
                .setSuite("Book")
        );

        createStep("Create GET request");
        Response response = given()
                .when()
                .get(BookEndPoint.GET)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check performance < 700ms");
        Assert.assertTrue(response.getTime() < 700, "Performance time is expected : " +response.getTime());
    }

    @Test(testName = "Get book by id", groups = {P0})
    public void getBookById() {
        addTestInfo(new TestInfo()
                .setId("TK-3")
                .setName("Get book by id")
                .setSuite("Book")
        );

        createStep("Create GET by id request");
        int bookId = 1;

        Book book = given()
                .pathParam("id",bookId)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Book.class);

        createStep("Check the response");
        Assert.assertEquals(book.getId(), bookId,"Book id is correct");
        Assert.assertTrue(book.isAllFieldsNotNull(),"Fields of book is not empty");
    }

    @Test(testName = "Get book by incorrect data", groups = {P1})
    public void getBookByIncorrectId() {
        addTestInfo(new TestInfo()
                .setId("TK-4")
                .setName("Get book by incorrect data")
                .setSuite("Book")
        );

        createStep("Create GET by id request with using negative ID");
        int bookId = -1;

        Book400Error error400 = given()
                .pathParam("id",bookId)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for 400 error");
        Assert.assertTrue(!Objects.isNull(error400),"User can`t get book with negative id");

        createStep("Create GET by id request with incorrect ID");
        error400 = given()
                .pathParam("id","asas")
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for 400 error");
        Assert.assertTrue(!Objects.isNull(error400),"User can`t get book with incorrect id");
    }

    @Test(testName = "Get book by id performance", groups = {P2})
    public void getBookByIdPerformance() {
        addTestInfo(new TestInfo()
                .setId("TK-5")
                .setName("Get book by id performance")
                .setSuite("Book")
        );

        createStep("Create GET by id request");
        int bookId = 1;

        Response response = given()
                .pathParam("id",bookId)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check the response performance < 700");
        Assert.assertTrue(response.getTime() < 700, "Performance time is expected : " +response.getTime());
    }

    @Test(testName = "Add a new book", groups = {P0})
    public void addNewBook() {
        addTestInfo(new TestInfo()
                .setId("TK-6")
                .setName("Add a new book")
                .setSuite("Book")
        );

        createStep("Create POST request");
        Long id = new Random().nextLong(333_333,999_999);
        Book newBook = new Book(id, "Test name", "API testing", 350, "Great read", "2026-02-28T00:00:00Z");

        Book createdBook = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Book.class);

        createStep("Check the response");
        Assert.assertTrue(createdBook.equals(newBook),"Created book is created and contains all expected fields");

        createStep("Check created book is appear - Get request");
        Book getBook = given()
                .pathParam("id",id)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Book.class);

        createStep("Check the response");
        Assert.assertTrue(createdBook.equals(getBook),"Created book is appear and contains all expected fields");
    }

    @Test(testName = "Add a new book with empty fields", groups = {P1})
    public void addNewBookWithEmptyFields() {
        addTestInfo(new TestInfo()
                .setId("TK-7")
                .setName("Add a new book with empty fields")
                .setSuite("Book")
        );

        createStep("Create POST request with empty Title");
        Long id = new Random().nextLong(333_333, 999_999);
        Book newBook = new Book(id, "", "API testing", 350, "Great read", "2026-02-28T00:00:00Z");

        Book400Error error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for empty Title field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with empty Title");

        createStep("Create POST request with empty Description");
        id = new Random().nextLong(333_333, 999_999);
        newBook = new Book(id, "Test", "", 350, "Great read", "2026-02-28T00:00:00Z");

        error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for empty Description field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with empty Title");

        createStep("Create POST request with null pageCounter");
        id = new Random().nextLong(333_333, 999_999);
        newBook = new Book(id, "Test", "test", null, "Great read", "2026-02-28T00:00:00Z");

        error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for null pageCount field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with null pageCount");

        createStep("Create POST request with empty excerpt");
        id = new Random().nextLong(333_333, 999_999);
        newBook = new Book(id, "Test", "test", 350, "", "2026-02-28T00:00:00Z");

        error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for empty excerpt field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with empty excerpt");

        createStep("Create POST request with empty publishDate");
        id = new Random().nextLong(333_333, 999_999);
        newBook = new Book(id, "Test", "test", 350, "Great", "");

        error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for empty publishDate field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with empty publishDate");
    }

    @Test(testName = "Add a new book boundary fields check", groups = {P1})
    public void addNewBookBoundaryFieldsCheck() {
        addTestInfo(new TestInfo()
                .setId("TK-8")
                .setName("Add a new book boundary fields check")
                .setSuite("Book")
        );

        createStep("Create POST request with Title contains 1001 symbols");
        String longString1000 = "a".repeat(1001);
        Book newBook = new Book(new Random().nextLong(333_333, 999_999), longString1000, "API testing", 350, "Great read", "2026-02-28T00:00:00Z");

        Book400Error error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for long Title field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with Title contains 1001 symbols");

        createStep("Create POST request with description contains 1001 symbols");
        newBook = new Book(new Random().nextLong(333_333, 999_999), "Book", longString1000, 350, "Great read", "2026-02-28T00:00:00Z");

        error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for long description field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with description contains 1001 symbols");

        createStep("Create POST request with excerpt contains 1001 symbols");
        newBook = new Book(new Random().nextLong(333_333, 999_999), "Book", "test", 350, longString1000, "2026-02-28T00:00:00Z");

        error400 = given()
                .body(newBook)
                .when()
                .post(BookEndPoint.POST)
                .then()
                .statusCode(400)
                .extract().as(Book400Error.class);

        createStep("Check the response for long excerpt field");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t create book with excerpt contains 1001 symbols");
    }

    @Test(testName = "Change a book - PUT", groups = {P0})
    public void changeBook() {
        addTestInfo(new TestInfo()
                .setId("TK-9")
                .setName("Change book")
                .setSuite("Book")
        );

        createStep("Create PUT request");
        Long updatedBookId = 3L;
        Long id = new Random().nextLong(333_333,999_999);
        Book newBook = new Book(id, "Test name", "API testing", 350, "Great read", "2026-02-28T00:00:00Z");

        Book updatedBook = given()
                .body(newBook)
                .pathParam("id",updatedBookId)
                .when()
                .put(BookEndPoint.PUT_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Book.class);

        createStep("Check the response");
        Assert.assertTrue(updatedBook.equals(newBook),"Created book is created and contains all expected fields");

        createStep("Check created book is appear - Get request");
        Book getBook = given()
                .pathParam("id",updatedBookId)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Book.class);

        createStep("Check the response");
        Assert.assertTrue(updatedBook.equals(getBook),"Created book is appear and contains all expected fields");
    }

    @Test(testName = "Idempotency checking for book PUT request", groups = {P1})
    public void idempotencyCheckingForBookPUTRequest() {
        addTestInfo(new TestInfo()
                .setId("TK-10")
                .setName("Idempotency checking for book PUT request")
                .setSuite("Book")
        );

        createStep("Create PUT request - 3 times");
        Long updatedBookId = 3L;
        Long id = new Random().nextLong(333_333, 999_999);
        Book newBook = new Book(id, "Test name", "API testing", 350, "Great read", "2026-02-28T00:00:00Z");

        for(int i=0;i<3;i++){
            given()
                    .body(newBook)
                    .pathParam("id", updatedBookId)
                    .when()
                    .put(BookEndPoint.PUT_BY_ID)
                    .then()
                    .statusCode(200);
        }

        createStep("Make GET request");
        Response response = given()
                .pathParam("id",updatedBookId)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check the response");
        try{
            response.as(Book[].class);
        }catch (RuntimeException e){
            //if i have DB i will add checking in the DB
            Assert.assertTrue(!Objects.isNull(response.as(Book.class)), "Put request doesn`t create a new the same entity");
        }
    }

    @Test(testName = "Partial update book - PUT", groups = {P0})
    public void partialUpdateBook() {
        addTestInfo(new TestInfo()
                .setId("TK-11")
                .setName("Partial update book")
                .setSuite("Book")
        );

        createStep("Get book that before updates");
        Long updatedBookId = 3L;
        Book bookBefore = given()
                .pathParam("id",updatedBookId)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Book.class);

        createStep("Update PUT request with empty fields");
        Book bookAfter = given()
                .body("{}")
                .pathParam("id",updatedBookId)
                .when()
                .put(BookEndPoint.PUT_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Book.class);


        createStep("Check the response");
        Assert.assertTrue(bookBefore.equals(bookAfter),"Book` fields wasn`t changed by empty fields");
    }

    @Test(testName = "Delete a book - DELETE", groups = {P0})
    public void deleteBook() {
        addTestInfo(new TestInfo()
                .setId("TK-12")
                .setName("Delete book")
                .setSuite("Book")
        );

        createStep("Create DELETE request");
        Long deletedBookId = 4L;

        Response response = given()
                .pathParam("id",deletedBookId)
                .when()
                .delete(BookEndPoint.DELETE_BY_ID)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check the response");
        Assert.assertTrue(response.statusCode() == 200,"Book was deleted with 200 code");

        createStep("Check deleted book is disappear - Get request");
        response = given()
                .pathParam("id",deletedBookId)
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(404)
                .extract().response();

        createStep("Check the response");
        Assert.assertTrue(Objects.isNull(response.as(Book404Error.class)),"Deleted book was removed");
    }

    @Test(testName = "Idempotency checking for deleting a book", groups = {P1})
    public void idempotencyCheckingForDeleteBook() {
        addTestInfo(new TestInfo()
                .setId("TK-13")
                .setName("Idempotency checking for deleting a book")
                .setSuite("Book")
        );

        createStep("Create DELETE request");
        Long deletedBookId = 5L;
        given()
                .pathParam("id",deletedBookId)
                .when()
                .delete(BookEndPoint.DELETE_BY_ID)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Make DELETE request for the same id");
        Book404Error response = given()
                .pathParam("id",deletedBookId)
                .when()
                .delete(BookEndPoint.DELETE_BY_ID)
                .then()
                .statusCode(404)//or can be 202 depending on requirements
                .extract().as(Book404Error.class);

        createStep("Check the response");
        Assert.assertTrue(!Objects.isNull(response),"Delete is a idempotency method");
    }
}
