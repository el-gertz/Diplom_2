import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.UserAPI;
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
    @DisplayName("Изменение имени пользователя")
    public void changeName() {
        User expectedChangedUser = new User(user.getEmail(), user.getPassword(), "New name");
        ValidatableResponse response = api.change(expectedChangedUser, accessToken).statusCode(HttpURLConnection.HTTP_OK);
        String changedName = response.extract().path("user.name");

        Assert.assertEquals("Unexpected name in response", expectedChangedUser.getName(), changedName);
    }

    @Test
    @DisplayName("Изменение email пользователя")
    public void changeEmail() {
        User expectedChangedUser = new User("new_email_for_elgertz@yandex.ru", user.getPassword(), user.getName());
        ValidatableResponse response = api.change(expectedChangedUser, accessToken).statusCode(HttpURLConnection.HTTP_OK);
        String changedEmail = response.extract().path("user.email");

        Assert.assertEquals("Unexpected email in response", expectedChangedUser.getEmail(), changedEmail);
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    public void changePassword() {
        User expectedChangedUser = new User(user.getEmail(), "new password", user.getName());
        api.change(expectedChangedUser, accessToken).statusCode(HttpURLConnection.HTTP_OK);
        boolean loginResponse = api.login(Credentials.fromUser(expectedChangedUser)).statusCode(HttpURLConnection.HTTP_OK).extract().path("success");

        Assert.assertTrue("Unexpected result login with new password", loginResponse);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void invalidAuth() {
        String message = api.change(User.random(), "123").statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .extract().path("message");
        Assert.assertEquals("You should be authorised", message);
    }
}
