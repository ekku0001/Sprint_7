package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.clients.OrderClient;
import static org.hamcrest.CoreMatchers.notNullValue;


public class TestOrdersList {

    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Get list of orders")
    @Description("Get orders list and check it is not empty")
    public void getOrdersList() {
        OrderClient orderClient = new OrderClient();
        orderClient.getOrders()
                .then()
                .assertThat().statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}
