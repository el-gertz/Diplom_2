package praktikum.api;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import praktikum.dto.EnvConfig;

import static io.restassured.RestAssured.given;

public class BaseAPI {

    public static final String BASE_PATH = "api/";

    public RequestSpecification spec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(EnvConfig.BASE_URI)
                .basePath(BASE_PATH);

    }
}
