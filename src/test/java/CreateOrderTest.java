import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.API.OrderAPI;
import praktikum.API.UserAPI;
import praktikum.dto.Order;
import praktikum.dto.User;

import java.net.HttpURLConnection;
import java.util.List;


public class CreateOrderTest {

    private final OrderAPI orderApi = new OrderAPI();
    private final UserAPI userApi = new UserAPI();
    private final User user = User.random();
    private String accessToken;

    @Before
    public void prepareTestData() {
        accessToken = userApi.create(user).statusCode(HttpURLConnection.HTTP_OK)
                .extract().path("accessToken");
    }

    @After
    public void clearTestData() {
        if (accessToken != null) userApi.delete(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderAuth() {
        boolean response = orderApi.createOrder(new Order(List.of("61c0c5a71d1f82001bdaaa6d")), accessToken)
                .statusCode(HttpURLConnection.HTTP_OK).extract().path("success");
        Assert.assertTrue(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        boolean response = orderApi.createOrder(new Order(List.of("61c0c5a71d1f82001bdaaa6d")))
                .statusCode(HttpURLConnection.HTTP_OK).extract().path("success");
        Assert.assertTrue(response);
    }

    @Test
    @DisplayName("Создание заказа с несколькими ингредиентами")
    public void createOrderWithManyIngredients() {
        boolean response = orderApi.createOrder(new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f")))
                .statusCode(HttpURLConnection.HTTP_OK).extract().path("success");
        Assert.assertTrue(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        String response = orderApi.createOrder(new Order(null))
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST).extract().path("message");

        Assert.assertEquals("Ingredient ids must be provided", response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void orderWithInvalidHash() {
        orderApi.createOrder(new Order(List.of("12345")))
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

}
