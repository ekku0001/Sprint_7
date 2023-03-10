package ru.yandex.praktikum.clients.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.InputStream;

public class ScooterClient {
    protected final String BASE_URI = "http://qa-scooter.praktikum-services.ru";

    protected RequestSpecification getBaseReqSpec(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}
