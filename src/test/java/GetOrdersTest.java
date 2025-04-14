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
import java.util.ArrayList;
import java.util.List;

public class GetOrdersTest {
    private final List<Integer> orderIds = new ArrayList<>();
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
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    public void getOrdersWithoutAuth() {
        String response = orderApi.getOrders().assertThat().statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .extract().path("message");

        Assert.assertEquals("You should be authorised", response);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuth() {
        List<Integer> expectedOrderNumbers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            expectedOrderNumbers.add(orderApi.createOrder(new Order(List.of("61c0c5a71d1f82001bdaaa6d")), accessToken)
                    .extract().path("order.number"));
        }

        List<Integer> response = orderApi.getOrders(accessToken).statusCode(HttpURLConnection.HTTP_OK)
                .extract().path("orders.number");
        Assert.assertEquals(expectedOrderNumbers, response);
    }
}
