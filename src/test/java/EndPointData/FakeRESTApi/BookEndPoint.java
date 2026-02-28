package EndPointData.FakeRESTApi;

public interface BookEndPoint {
    String GET = "/Books";
    String POST = "/Books";
    String GET_BY_ID= "/api/v1/Books/{id}";
    String PUT_BY_ID="/api/v1/Books/{id}";
    String DELETE_BY_ID="/api/v1/Books/{id}";
}
