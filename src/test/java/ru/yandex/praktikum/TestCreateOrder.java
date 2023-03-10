package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.clients.OrderClient;
import ru.yandex.praktikum.models.Order;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class TestCreateOrder {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String color;

    public TestCreateOrder(String firstName, String lastName, String address, int metroStation, String phone,
                             int rentTime, String deliveryDate, String comment, String color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;

    }

    @BeforeClass
    public static void globalSetUp(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Parameterized.Parameters
    public static Object[] getInputData() {
        return new Object[][] {
                { "Сергей", "Иванов", "Невский проспект 106", 20, "89112345665",
                        5, "2023-06-31", "тестовый комментарий", "BLACK" },
                                { "Светлана", "Солнцева", "Московский проспект 6", 4, "89213456523",
                                       1, "2023-04-12", "", "GRAY"},
                              { "Юрий", "Демидов", "Колюжная 70", 2, "89012845065",
                                       3, "2024-01-31", "тестовый комментарий", "GRAY BLACK" },
                               { "Василий", "Иванов", "Невский проспект 106", 20, "89112345665",
                                       5, "2023-06-30", "тестовый комментарий", "" },
        };
    }

    @Test
    @DisplayName("Create order with valid data")
    public void createOrder(){
        Order order = new Order( firstName,  lastName, address,  metroStation, phone,
                rentTime,  deliveryDate,  comment, color.split(" "));
        OrderClient orderClient = new OrderClient();
        Response orderResp = orderClient.create(order);

        orderResp.then()
                .assertThat().statusCode(201)
                .and()
                .assertThat()
                .body("track", notNullValue());

        //make sure that order is added and can be canceled
        orderClient.cancel( orderResp.then().extract().path("track"))
                .then()
                .assertThat().statusCode(200);

    }
}
