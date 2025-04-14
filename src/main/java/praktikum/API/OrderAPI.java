package praktikum.API;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.dto.Order;

public class OrderAPI extends BaseAPI {

    private static final String ORDERS = "orders";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().all();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrder(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().all();
    }

    @Step("Получение списка заказов конкретного пользователя без авторизации")
    public ValidatableResponse getOrders() {
        return spec()
                .when()
                .get(ORDERS)
                .then().log().all();
    }

    @Step("Получение списка заказов конкретного пользователя c авторизацией")
    public ValidatableResponse getOrders(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS)
                .then().log().all();
    }

}
