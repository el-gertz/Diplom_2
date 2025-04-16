import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.UserAPI;
import praktikum.dto.User;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;

public class UserCreateTest {

    private final UserAPI api = new UserAPI();
    public String duplicatedAccessToken;
    private final User duplicatedUser = User.random();
    private String userAccessToken;

    @Before
    public void prepareTestData() {
        duplicatedAccessToken = api.create(duplicatedUser)
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract().path("accessToken");
    }

    @After
    public void clearTestData() {
        if (duplicatedAccessToken != null) api.delete(duplicatedAccessToken);
        if (userAccessToken != null) api.delete(userAccessToken);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUser() {
        User user = User.random();
        ValidatableResponse createResponse = api.create(user).statusCode(HttpURLConnection.HTTP_OK);
        userAccessToken = createResponse.extract().path("accessToken");

        Assert.assertTrue("Unexpected success field in response", createResponse.extract().path("success"));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void duplicatedUser() {
        String message = api.create(duplicatedUser).statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .extract().path("message");

        assertEquals("User already exists", message);
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    public void oneFieldIsNullCreation() {
        User user = new User(null, "qwerty", "Steven");
        String message = api.create(user).statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .extract().path("message");

        assertEquals("Email, password and name are required fields", message);
    }

}
