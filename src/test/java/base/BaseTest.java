package base;

import EndPointData.FakeRESTApi.FakeRestAPIData;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;

import java.util.Objects;

public class BaseTest {

//    @BeforeSuite
//    public void cleanAllureReport(){
//        File[] files = Path.of(System.getProperty("user.dir")+"/target/allure-results").toAbsolutePath().toFile().listFiles();
//        if(!Objects.isNull(files)){
//            Arrays.stream(files).forEach(File::delete);
//        }
//    }

    @BeforeClass
    public void setup() {
        if(Objects.isNull(RestAssured.requestSpecification)){
            RestAssured.requestSpecification = new RequestSpecBuilder()
                    .setBaseUri(FakeRestAPIData.URI)
                    .setBasePath(FakeRestAPIData.API_VERSION)
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .build();
        }
    }

    public void createStep(String step){
        Allure.step(step);
    }

//    @BeforeMethod
//    public void setup(Method method){
//        TestData testDataAnnotation = method.getAnnotation(TestData.class);
//
//        if(testDataAnnotation != null){
//            String uri = testDataAnnotation.uri().isEmpty() ? "" : testDataAnnotation.uri();
//            String path = testDataAnnotation.path().isEmpty() ? "" : testDataAnnotation.path();
//
//            if(!testDataAnnotation.endpoint().isEmpty()){
//                path += testDataAnnotation.endpoint();
//            }
//
//            RequestSpecification requestSpecification = new RequestSpecBuilder()
//                    .setBaseUri(uri)
//                    .setBasePath(path)
//                    .setContentType(ContentType.JSON)
//                    .setAccept(ContentType.JSON)
//                    .build();
//        }else{
//            Assert.fail("Data is missed");
//        }
//    }

}
