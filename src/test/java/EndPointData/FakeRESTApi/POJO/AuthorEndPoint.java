package EndPointData.FakeRESTApi.POJO;

public interface AuthorEndPoint {
    String GET = "/Authors";
    String POST = "/Authors";
    String GET_BY_BOOK_ID= "/Authors/authors/books/{idBook}";
    String GET_BY_ID="/Authors/{id}";
    String PUT_BY_ID="/Authors/{id}";
    String DELETE_BY_ID="/Authors/{id}";
}
