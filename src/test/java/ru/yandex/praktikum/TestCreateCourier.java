package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.clients.CourierClient;
import ru.yandex.praktikum.models.Courier;

import static org.hamcrest.CoreMatchers.is;


public class TestCreateCourier {
    private CourierClient courierClient;

    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp(){
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Courier creation with valid data")
    @Description("create courier with valid data and check if it can login")
    public void courierIsCreatedWithValidData() {
        Courier courier = new Courier("validLogin","validPassword","name");

        courierClient.create(courier)
                .then()
                .assertThat().statusCode(201)
                .and()
                .assertThat()
                .body("ok", is(true));

        //login to check courier creation
        Response loginCourierResp = courierClient.login(courier);
        loginCourierResp.then()
                .assertThat().statusCode(200);

        //delete created courier
        courierClient.delete(loginCourierResp.then().extract().path("id"));

    }

    @Test
    @DisplayName("Courier creation with invalid password")
    @Description("Try to create courier with empty password")
    public void createCourierWithEmptyPassword(){
        Courier courier = new Courier("loginWithEmptyPassword",null,"name");

        courierClient.create(courier)
                .then()
                .assertThat().statusCode(400)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Courier creation with invalid login")
    @Description("Try to create courier with empty login")
    public void createCourierWithEmptyLogin(){
        Courier courier = new Courier(null,"passwordWithEmptyLogin","name");

        courierClient.create(courier)
                .then()
                .assertThat().statusCode(400)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Doudbe Courier creation")
    @Description("Try to create two couriers with the same data")
    public void createCouriersWithSameData(){
        Courier courier = new Courier("doubleLogin","doublePassword","doubleName");

        //delete if courier is exist
        courierClient.delete(courierClient.login(courier).then().extract().path("id"));

        CourierClient doubleCourierClient = new CourierClient();

        courierClient.create(courier);

        doubleCourierClient.create(courier)
                .then()
                .assertThat().statusCode(409)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется"));
    }

}
