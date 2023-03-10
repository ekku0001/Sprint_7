package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.clients.CourierClient;
import ru.yandex.praktikum.models.Courier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestCourierLogin {
    private CourierClient courierClient;
    //courier with valid data
    private Courier courier;

    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp(){
        courier = new Courier("courierLogin","courierPassword",null);
        courierClient = new CourierClient();
        courierClient.create(courier);
    }

    @Test
    @DisplayName("Courier login with valid data")
    @Description("Login with existed pair login and password")
    public void courierCanLoginWithValidData() {
        //login
        courierClient.login(courier)
                .then()
                .assertThat().statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Courier login with invalid data")
    @Description("Try to login without login")
    public void loginWithEmptyLogin(){
        courierClient.login(new Courier(null, "courierPassword"))
                .then()
                .assertThat().statusCode(400)
                .and()
                .assertThat().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier login with invalid data")
    @Description("Try to login without password")
    public void loginWithEmptyPassword(){
        courierClient.login(new Courier("courierLogin", null))
                .then()
                .assertThat().statusCode(400)
                .and()
                .assertThat().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier login with invalid data")
    @Description("Try to login with incorrect login")
    public void loginWithIncorrectLogin(){
        courierClient.login(new Courier("incorrectLogin", "courierPassword"))
                .then()
                .assertThat().statusCode(404)
                .and()
                .assertThat().body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier login with invalid data")
    @Description("Try to login with incorrect password")
    public void loginWithIncorrectPassword(){
        courierClient.login(new Courier("courierLogin", "incorrectPassword"))
                .then()
                .assertThat().statusCode(404)
                .and()
                .assertThat().body("message", is("Учетная запись не найдена"));
    }

    @After
    public void cleanUp(){
        courierClient.delete(courierClient.login(courier).then().extract().path("id"));
    }
}
