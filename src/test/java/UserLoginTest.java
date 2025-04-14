import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.API.UserAPI;
import praktikum.dto.Credentials;
import praktikum.dto.User;

import java.net.HttpURLConnection;

public class UserLoginTest {
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
    @DisplayName("Логин под существующим пользователем")
    public void login() {
        Boolean response = api.login(Credentials.fromUser(user)).statusCode(HttpURLConnection.HTTP_OK)
                .extract().path("success");
        Assert.assertTrue("Unexpected success field", response);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void invalidLogPass() {
        Credentials badCreds = new Credentials("sample", "Pass", user.getName());
        String message = api.login(badCreds).statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .extract().path("message");
        Assert.assertEquals("email or password are incorrect", message);
    }
}
