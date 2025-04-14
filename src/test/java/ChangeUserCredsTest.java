import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.API.UserAPI;
import praktikum.dto.Credentials;
import praktikum.dto.User;

import java.net.HttpURLConnection;

public class ChangeUserCredsTest {
    private final UserAPI api = new UserAPI();
    private final User user = User.random();
    private String accessToken;

    @Before
    public void prepareTestData() {
        accessToken = api.create(user).statusCode(HttpURLConnection.HTTP_OK)
                .extract().path("accessToken");
    }

    @After
    public void clearTestData() {
        if (accessToken != null) api.delete(accessToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void change() {
        User expectedChangedUser = User.random();
        ValidatableResponse response = api.change(expectedChangedUser, accessToken).statusCode(HttpURLConnection.HTTP_OK);
        String changedEmail = response.extract().path("user.email");
        String changedName = response.extract().path("user.name");
        boolean loginResponse = api.login(Credentials.fromUser(expectedChangedUser)).statusCode(HttpURLConnection.HTTP_OK).extract().path("success");
        Assert.assertEquals(expectedChangedUser.getEmail(), changedEmail);
        Assert.assertEquals(expectedChangedUser.getName(), changedName);
        Assert.assertTrue(loginResponse);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void invalidAuth() {
        String message = api.change(User.random(), "123").statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .extract().path("message");
        Assert.assertEquals("You should be authorised", message);
    }
}
