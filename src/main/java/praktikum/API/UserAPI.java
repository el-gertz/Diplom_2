package praktikum.API;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.dto.Credentials;
import praktikum.dto.User;

public class UserAPI extends BaseAPI {

    public static final String AUTH = "auth";

    @Step("Регистрация пользователя")
    public ValidatableResponse create(User user) {
        return spec()
                .body(user)
                .when()
                .post(AUTH + "/register")
                .then()
                .log().all();
    }

    @Step("Логин")
    public ValidatableResponse login(Credentials credentials) {
        return spec()
                .body(credentials)
                .when()
                .post(AUTH + "/login")
                .then()
                .log().all();


    }

    @Step("Редактирование пользователя")
    public ValidatableResponse change(User user, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(AUTH + "/user")
                .then()
                .log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .delete(AUTH + "/user")
                .then()
                .log().all();
    }
}
