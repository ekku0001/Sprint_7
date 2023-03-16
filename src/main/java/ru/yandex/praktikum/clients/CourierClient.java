package ru.yandex.praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.clients.base.ScooterClient;
import ru.yandex.praktikum.models.Courier;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterClient {

    private final String COURIER_URI = BASE_URI + "/api/v1/courier/";

    @Step("Create courier {courier}")
    public Response create(Courier courier){
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_URI);
    }

    @Step("Login courier {courier}")
    public Response login(Courier courier){
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_URI+ "login/");
    }

    @Step("Delete courier {id}")
    public Response delete(int id){
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI + id);
    }
}
