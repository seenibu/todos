package sn.ept.git.seminaire.cicd.cucumber.definitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import sn.ept.git.seminaire.cicd.models.TodoDTO;
import sn.ept.git.seminaire.cicd.entities.Todo;
import sn.ept.git.seminaire.cicd.repositories.TodoRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@Slf4j
public class CucumberStepIT {


    public static final String COMPLETED = "completed";
    private final static String BASE_URI = "http://localhost";
    public static final String API_PATH = "/cicd/api/todos";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    @LocalServerPort
    private int port;
    //private String id;
    private String title;
    private String description;
    private Response response;
    private static final ObjectMapper objectMapper = new ObjectMapper();;
    @Autowired
    private TodoRepository todoRepository;


    @BeforeAll
     public static void beforeAll(){
        objectMapper.findAndRegisterModules();
    }

    @Before
    public void init(){
        todoRepository.deleteAll();
    }
    protected RequestSpecification request() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        return given()
                .contentType(ContentType.JSON)
                .log()
                .all();

    }


    @Given("table todo contains data:")
    public void tableTodoContainsData(DataTable dataTable) {
        List<Map<String, String >> data =dataTable.asMaps(String.class,String.class);
        List<Todo> todosList =  data
                .stream()
                .map(line->Todo
                        .builder()
                        .id(line.get(ID))
                        .title(line.get(TITLE))
                        .description(line.get(DESCRIPTION))
                        .completed(line.get(COMPLETED).equals("true"))
                        .completed(false)
                        .version(0)
                        .createdDate(Instant.now(Clock.systemUTC()))
                        .lastModifiedDate(Instant.now(Clock.systemUTC()))
                        .build()
                ).collect(Collectors.toList());

        todoRepository.saveAllAndFlush(todosList);

    }


    @When("call find by id with id={string}")
    public void callFindByIdWithId(String id) {
        response = request()
                .when().get(API_PATH+"/" +id);
    }


    @Then("the http status is {int}")
    public void theHttpStatusIs(int status) {
        response.then()
                .assertThat()
                .statusCode(status);
    }

    @And("the returned todo has properties title={string},description={string} and completed={string}")
    public void theRouturnedTodoHasPropertiesTitleDescriptionAndCompleted(String title, String description, String completed) throws JsonProcessingException {
        response.then()
                .assertThat()
                .body(TITLE, CoreMatchers.equalTo(title))
                .body(DESCRIPTION, CoreMatchers.equalTo(description))
                .body(COMPLETED, CoreMatchers.equalTo(Boolean.valueOf(completed)));
    }

    @When("call find all")
    public void callFindAll() {
            response = request().contentType(ContentType.JSON)
                    .log()
                    .all()
                    .when().get(API_PATH);

    }

    @And("the returned list has {int} elements")
    public void theReturnedListHasElements(int size)  {
        Assertions.assertThat(response.jsonPath().getList("content"))
                .hasSize(size);
    }



    @And("that list contains values: title={string}, description={string} and completed={string}:")
    public void thatListContainsValuesTitleAndDescriptionAndCompleted(String title, String description, String completed) {
        response.then().assertThat()
                .body("content*.title", Matchers.hasItem(title))
                .body("content*.description", Matchers.hasItem(description))
                .body("content*.completed", Matchers.hasItem(Boolean.valueOf(completed)));

    }

    @When("call delete with id={string}")
    public void callDeleteWithId(String id) {
        response = request()
                .when().delete(API_PATH+"/"+id);
    }

    @When("call complete with id={string}")
    public void callCompleteWithId(String id) {
        response = request()
                .when().get(API_PATH+"/"+id+"/complete");
    }

    @And("the completed todo has property completed={string}")
    public void theCompletedTodoHasPropertyCompleted(String completed) {
        response.then()
                .assertThat()
                .body(COMPLETED, CoreMatchers.equalTo(Boolean.valueOf(completed)));
    }


    @When("call add todo")
    public void callAddTodo() {
        TodoDTO requestBody =TodoDTO.builder().title(this.title).description(this.description).build();
        response = request()
                .body(requestBody)
                .when().post(API_PATH);
    }



    @And("the created todo has properties title={string} and description={string} and completed={string} ")
    public void theCreatedTodoHasPropertiesTitleAndDescriptionAndCompletedAndCreated_dateAndLast_modified_date(String title, String description, String completed) {
        response.then()
                .assertThat()
                .body(TITLE, CoreMatchers.equalTo(title))
                .body(DESCRIPTION, CoreMatchers.equalTo(description))
                .body(COMPLETED, CoreMatchers.equalTo(Boolean.valueOf(completed)));
    }

    @When("call update todo with id={string}")
    public void callUpdateTodoWithIdAndTitleAndDescription(String id) {
        TodoDTO requestBody =TodoDTO.builder().title(this.title).description(this.description).build();
        response = request()
                .body(requestBody)
                .when().put(API_PATH+"/"+id);
    }


    @And("the created todo has properties title={string}, description={string}, completed={string}")
    public void theCreatedTodoHasPropertiesTitleAndDescriptionAndCompleted(String title, String description, String completed) {
        response.then()
                .assertThat()
                .body(TITLE, CoreMatchers.equalTo(title))
                .body(DESCRIPTION, CoreMatchers.equalTo(description))
                .body(COMPLETED, CoreMatchers.equalTo(Boolean.valueOf(completed)));
    }

    @And("the updated todo has properties title={string}, description={string}, completed={string}")
    public void theUpdatedTodoHasPropertiesTitleAndDescriptionAndCompleted(String title, String description, String completed) {
        response.then()
                .assertThat()
                .body(TITLE, CoreMatchers.equalTo(title))
                .body(DESCRIPTION, CoreMatchers.equalTo(description))
                .body(COMPLETED, CoreMatchers.equalTo(Boolean.valueOf(completed)));
    }

    @And("title contains {int} characters")
    public void titleContainsCharacters(int size) {
        this.title= RandomStringUtils.randomAlphanumeric( size);
    }

    @And("description contains {int} characters")
    public void descriptionContainsCharacters(int size) {
        this.description= RandomStringUtils.randomAlphanumeric( size);
    }


    @And("title = {string}")
    public void title(String  title) {
        this.title=  title;
    }

    @And("description = {string}")
    public void description(String description) {
        this.description=description;
    }

}