package tests;

import EndPointData.FakeRESTApi.POJO.AuthorEndPoint;
import EndPointData.FakeRESTApi.POJO.BookEndPoint;
import base.BaseTest;
import base.TestInfo;
import io.restassured.response.Response;
import model.author.Author;
import model.author.Author400Error;
import model.author.Author404Error;
import model.book.Book;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.Random;

import static base.Priority.P0;
import static base.Priority.P1;
import static io.restassured.RestAssured.given;

public class AuthorTests extends BaseTest {

    @Test(testName = "Get all authors", groups = {P0})
    public void getAllAuthors() {
        addTestInfo(new TestInfo()
                .setId("TK-14")
                .setName("Get All authors")
                .setSuite("Author")
        );

        createStep("Create GET request");
        Response response = given()
                .when()
                .get(AuthorEndPoint.GET)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check the response");
        Author[] authors = response.as(Author[].class);
        Assert.assertTrue(authors.length > 0, "Authors list should not be empty");

        createStep("Create GET request for author` book id");
        Book book = given()
                .pathParam("id", authors[0].getIdBook())
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                //Use not array of books because fakerestapi send only 1 object
                .extract().as(Book.class);

        Assert.assertTrue(book.isAllFieldsNotNull(), "Author` book exists");
    }

    @Test(testName = "Get author by ID", groups = {P0})
    public void getAuthorByID() {
        addTestInfo(new TestInfo()
                .setId("TK-15")
                .setName("Get author by ID")
                .setSuite("Author")
        );

        createStep("Create GET request by id");
        Author author = given()
                .pathParam("id", 1)
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        String expectedFirstName = "First Name 1";
        String expectedLastName = "Last Name 1";
        Assert.assertTrue(author.getFirstName().equals(expectedFirstName) &&
                author.getLastName().equals(expectedLastName), "Got author by id contains expected fields");

    }

    @Test(testName = "Get author by incorrect ID data", groups = {P1})
    public void getAuthorByIncorrectID() {
        addTestInfo(new TestInfo()
                .setId("TK-16")
                .setName("Get author by incorrect ID data")
                .setSuite("Author")
        );

        createStep("Create GET request by incorrect id");
        Author400Error error400 = given()
                .pathParam("id", "asf")
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(400)
                .extract().as(Author400Error.class);

        createStep("Check the response");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t get author by incorrect id");

        createStep("Create GET request by negative id");
        error400 = given()
                .pathParam("id", -1)
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(400)
                .extract().as(Author400Error.class);

        createStep("Check the response");
        Assert.assertTrue(!Objects.isNull(error400), "User can`t get author by negative id");
    }

    @Test(testName = "Add a new author", groups = {P0})
    public void addNewAuthor() {
        addTestInfo(new TestInfo()
                .setId("TK-17")
                .setName("Add a new author")
                .setSuite("Author")
        );

        createStep("Create POST request");
        Long id = new Random().nextLong(333_333, 999_999);
        Author newAuthor = new Author(id, 6, "TestName", "LastTest");

        Author createdAuthor = given()
                .body(newAuthor)
                .when()
                .post(AuthorEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(newAuthor), "Author is created and contains all expected fields");

        createStep("Check created author exists - Get request");
        Author getAuthor = given()
                .pathParam("id", id)
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(getAuthor), "Created author exists and contains all expected fields");
    }

    @Test(testName = "Get author by book ID", groups = {P0})
    public void getAuthorByBookID() {
        addTestInfo(new TestInfo()
                .setId("TK-23")
                .setName("Get author by book ID")
                .setSuite("Author")
        );

        createStep("Create book GET request");
        Book book = given()
                .when()
                .get(BookEndPoint.GET)
                .then()
                .statusCode(200)
                .extract().as(Book[].class)[0];

        createStep("Create GET request by book id");
        Author[] authors = given()
                .pathParam("idBook", book.getId())
                .when()
                .get(AuthorEndPoint.GET_BY_BOOK_ID)
                .then()
                .statusCode(200)
                .extract().as(Author[].class);

        createStep("Check the response");
        //we can`t check any field from Book and Author, because they have got only ID like same field
        Assert.assertTrue(authors.length>0, "Got authors by book id");

    }

    @Test(testName = "Add a new author with spec symbols", groups = {P1})
    public void addNewAuthorWithSpecSymbols() {
        addTestInfo(new TestInfo()
                .setId("TK-18")
                .setName("Add a new author")
                .setSuite("Author")
        );

        createStep("Create POST request with ~!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/`");
        String specSymbol = "~!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/`";
        Long id = new Random().nextLong(333_333, 999_999);
        Author newAuthor = new Author(id, 6, specSymbol, specSymbol);

        Author createdAuthor = given()
                .body(newAuthor)
                .when()
                .post(AuthorEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(newAuthor), "Author is created and contains all expected fields with spec Symbols " + specSymbol);

        createStep("Create POST request with SELECT * FROM AUTHOR;");
        specSymbol = "SELECT * FROM AUTHOR;";
        id = new Random().nextLong(333_333, 999_999);
        newAuthor = new Author(id, 6, specSymbol, specSymbol);

        createdAuthor = given()
                .body(newAuthor)
                .when()
                .post(AuthorEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(newAuthor), "Author is created and contains all expected fields with spec Symbols " + specSymbol);

        createStep("Create POST request with <script>alert('xss')</script>");
        specSymbol = "<script>alert('xss')</script>";
        id = new Random().nextLong(333_333, 999_999);
        newAuthor = new Author(id, 6, specSymbol, specSymbol);

        createdAuthor = given()
                .body(newAuthor)
                .when()
                .post(AuthorEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(newAuthor), "Author is created and contains all expected fields with spec Symbols " + specSymbol);

        createStep("Create POST request with \\u200B \t\r\n");
        specSymbol = "\\u200B \t\n";
        id = new Random().nextLong(333_333, 999_999);
        newAuthor = new Author(id, 6, specSymbol, specSymbol);

        createdAuthor = given()
                .body(newAuthor)
                .when()
                .post(AuthorEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(newAuthor), "Author is created and contains all expected fields with spec Symbols " + specSymbol);

        createStep("Create POST request with ñüá");
        specSymbol = "ñüá";
        id = new Random().nextLong(333_333, 999_999);
        newAuthor = new Author(id, 6, specSymbol, specSymbol);

        createdAuthor = given()
                .body(newAuthor)
                .when()
                .post(AuthorEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(newAuthor), "Author is created and contains all expected fields with spec Symbols " + specSymbol);

        createStep("Create POST request with \uD83D\uDD25\uD83E\uDD16\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66");
        specSymbol = "\uD83D\uDD25\uD83E\uDD16\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66";
        id = new Random().nextLong(333_333, 999_999);
        newAuthor = new Author(id, 6, specSymbol, specSymbol);

        createdAuthor = given()
                .body(newAuthor)
                .when()
                .post(AuthorEndPoint.POST)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(createdAuthor.equals(newAuthor), "Author is created and contains all expected fields with spec Symbols " + specSymbol);
    }

    //we can add test for checking conflict for unique value(books) but the fakerestapi doesn`t validate it
    @Test(testName = "Change an author - PUT", groups = {P0})
    public void changeAuthor() {
        addTestInfo(new TestInfo()
                .setId("TK-19")
                .setName("Change an author")
                .setSuite("Author")
        );

        createStep("Create PUT request");
        Long updatedAuthorId = 3L;
        Long id = new Random().nextLong(333_333,999_999);
        Author newAuthor = new Author(id, 3, "Name first","Last name");

        Author updatedAuthor = given()
                .body(newAuthor)
                .pathParam("id",updatedAuthorId)
                .when()
                .put(AuthorEndPoint.PUT_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(updatedAuthor.equals(newAuthor),"Author is updated and contains all expected fields");

        createStep("Check updated author exists - Get request");
        Author getAuthor = given()
                .pathParam("id",updatedAuthorId)
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Check the response");
        Assert.assertTrue(updatedAuthor.equals(getAuthor),"Updated author exists and contains all expected fields");
    }

    @Test(testName = "Idempotency checking for author PUT request", groups = {P1})
    public void idempotencyCheckingForAuthorPUTRequest() {
        addTestInfo(new TestInfo()
                .setId("TK-20")
                .setName("Idempotency checking for author PUT request")
                .setSuite("Author")
        );

        createStep("Create PUT request - 3 times");
        Long updatedAuthorId = 3L;
        Long id = new Random().nextLong(333_333, 999_999);
        Author newAuthor = new Author(id, 5, "Name first","Last name");

        for(int i=0;i<3;i++){
            given()
                    .body(newAuthor)
                    .pathParam("id", updatedAuthorId)
                    .when()
                    .put(AuthorEndPoint.PUT_BY_ID)
                    .then()
                    .statusCode(200);
        }

        createStep("Make GET request");
        Response response = given()
                .pathParam("id",updatedAuthorId)
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check the response");
        try{
            response.as(Author[].class);
        }catch (RuntimeException e){
            //if i have DB i will add checking in the DB
            Assert.assertTrue(!Objects.isNull(response.as(Author.class)), "Put request doesn`t create a new the same entity");
        }
    }

    @Test(testName = "Partial update author - PUT", groups = {P0})
    public void partialUpdateAuthor() {
        addTestInfo(new TestInfo()
                .setId("TK-21")
                .setName("Partial update author")
                .setSuite("Author")
        );

        createStep("Get author that before updates");
        Long updatedAuthorId = 3L;
        Author authorBefore = given()
                .pathParam("id",updatedAuthorId)
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Author.class);

        createStep("Update PUT request with empty fields");
        Author authorAfter = given()
                .body("{}")
                .pathParam("id",updatedAuthorId)
                .when()
                .put(AuthorEndPoint.PUT_BY_ID)
                .then()
                .statusCode(200)
                .extract().as(Author.class);


        createStep("Check the response");
        Assert.assertTrue(authorBefore.equals(authorAfter),"Author` fields wasn`t changed by empty fields");
    }

    //we need to ask Analytics/PM what we need to do with author`s books
    //Cancel user to remove author with books while book is existing - 409 error
    //After deleting author, authorId of books have got null for deleted author
    //Books are deleted with author - I will do this check
    @Test(testName = "Delete an author - DELETE", groups = {P0})
    public void deleteBook() {
        addTestInfo(new TestInfo()
                .setId("TK-22")
                .setName("Delete an author")
                .setSuite("Author")
        );

        createStep("Get id author`s book");
        Author[] author = given()
                .when()
                .get(AuthorEndPoint.GET)
                .then()
                .statusCode(200)
                .extract().as(Author[].class);

        createStep("Create DELETE request");
        Long deletedAuthorId = author[0].getId();

        Response response = given()
                .pathParam("id",deletedAuthorId)
                .when()
                .delete(AuthorEndPoint.DELETE_BY_ID)
                .then()
                .statusCode(200)
                .extract().response();

        createStep("Check the response");
        Assert.assertTrue(response.statusCode() == 200,"Author was deleted with 200 code");

        createStep("Check deleted Author is disappear - Get request");
        response = given()
                .pathParam("id",deletedAuthorId)
                .when()
                .get(AuthorEndPoint.GET_BY_ID)
                .then()
                .statusCode(404)
                .extract().response();

        createStep("Check the response");
        Assert.assertTrue(!Objects.isNull(response.as(Author404Error.class)),"Deleted author was removed");

        createStep("Check Author`s books are disappear - Book Get request");
        response = given()
                .pathParam("id",author[0].getIdBook())
                .when()
                .get(BookEndPoint.GET_BY_ID)
                .then()
                .statusCode(404)
                .extract().response();

        createStep("Check the response");
        Assert.assertTrue(!Objects.isNull(response.as(Author404Error.class)),"Author`s books were removed");

    }

}
