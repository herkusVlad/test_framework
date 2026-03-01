package base;

import EndPointData.FakeRESTApi.FakeRestAPIData;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class BaseTest {

    //Need for manual local run
    @BeforeSuite
    public void cleanAllureReport() {
        File[] files = Path.of(System.getProperty("user.dir") + "/target/allure-results").toAbsolutePath().toFile().listFiles();
        if (!Objects.isNull(files)) {
            Arrays.stream(files).forEach(File::delete);
        }
    }

    @BeforeClass
    public void setup() {
        if (Objects.isNull(RestAssured.requestSpecification)) {
            RestAssured.requestSpecification = new RequestSpecBuilder()
                    .setBaseUri(FakeRestAPIData.URL)
                    .setBasePath(FakeRestAPIData.API_VERSION)
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .build();
        }
    }

    public void createStep(String step) {
        System.out.println(step);
        Allure.step(step);
    }

    public void addTestInfo(TestInfo testInfo) {
        String nameOfTest = "";

        //Check test id
        if (!Objects.isNull(testInfo.getId()) && !testInfo.getId().isEmpty()) {
            nameOfTest = testInfo.getId();
        }

        //Check test name
        if (!Objects.isNull(testInfo.getName()) && !testInfo.getName().isEmpty()) {
            nameOfTest += " ".concat(testInfo.getName());
            nameOfTest = nameOfTest.trim();
        }

        //set name
        if (!nameOfTest.isEmpty()) {
            //need for lambda
            String finalNameOfTest = nameOfTest;
            Allure.getLifecycle().updateTestCase(e -> e.setName(finalNameOfTest));
        }

        //Add Issue to TestCase
        if (!Objects.isNull(testInfo.getIssue()) && !testInfo.getIssue().isEmpty()) {
            Allure.issue(testInfo.getIssue(), FakeRestAPIData.JIRA_URL.concat(testInfo.getIssue()));
        }

        //Add description to TestCase
        if (!Objects.isNull(testInfo.getDescription()) && !testInfo.getDescription().isEmpty()) {
            Allure.description(testInfo.getDescription());
        }

        //Add epic to TestCase
        if (!Objects.isNull(testInfo.getEpic()) && !testInfo.getEpic().isEmpty()) {
            Allure.epic(testInfo.getEpic());
        }

        //Add suite to TestCase
        if (!Objects.isNull(testInfo.getSuite()) && !testInfo.getSuite().isEmpty()) {
            Allure.suite(testInfo.getSuite());
        }
    }
    //TODO uncomment when Allure fix bug with testNG Before annotation
//    @BeforeMethod
//    public void setupReport(Method method){
//        TestData testDataAnnotation = method.getAnnotation(TestData.class);
//        Test testNgAnnotation = method.getAnnotation(Test.class);
//
//        boolean hasTestNgAnnotation = !Objects.isNull(testNgAnnotation);
//        String nameOfTest;
//        if(hasTestNgAnnotation && !testNgAnnotation.testName().isEmpty() ){
//            nameOfTest = testNgAnnotation.testName();
//        } else {
//            nameOfTest = "";
//        }
//
//        if(testDataAnnotation != null){
//            //Add ID to TC` name
//            if(!testDataAnnotation.id().isEmpty()){
//                Allure.getLifecycle().updateTestCase(e->e.setName(testDataAnnotation.id().concat(" " + nameOfTest)));
//            }else if(!nameOfTest.isEmpty()){
//                Allure.getLifecycle().updateTestCase(e->e.setName(nameOfTest));
//            }
//
//            //Add Issue to TestCase
//            if(!testDataAnnotation.issue().isEmpty()){
//                Allure.issue(testDataAnnotation.issue(),FakeRestAPIData.JIRA_URL);
//            }
//
//            //Add description to TestCase
//            if(!testDataAnnotation.description().isEmpty()){
//                Allure.description(testDataAnnotation.description());
//            }
//
//            //Add epic to TestCase
//            if(!testDataAnnotation.epic().isEmpty()){
//                Allure.epic(testDataAnnotation.epic());
//            }
//
//            //Add suite to TestCase
//            if(!testDataAnnotation.suite().isEmpty()){
//                Allure.suite(testDataAnnotation.epic());
//            }else if(hasTestNgAnnotation && testNgAnnotation.groups().length > 0){
//                Allure.suite(String.join(",", testNgAnnotation.groups()));
//            }
//        }
//    }

}
