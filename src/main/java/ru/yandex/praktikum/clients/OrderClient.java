package ru.yandex.praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.clients.base.ScooterClient;
import ru.yandex.praktikum.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterClient {
    private final String ORDER_URI = BASE_URI + "/api/v1/orders";

    @Step("Create order {order}")
    public Response create(Order order){
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(ORDER_URI);
    }

    @Step("Cancel order {order}")
    public Response cancel(int track){
        String requestURI= String.format("/cancel?track=%d", track);
        return given()
                .spec(getBaseReqSpec())
                .when()
                .put(ORDER_URI + requestURI);
    }

    @Step("Get orders list")
    public Response getOrders(){
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI);
    }
}
