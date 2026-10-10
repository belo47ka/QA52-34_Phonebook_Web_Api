package api_tests;

import data_providers.UserDataProvider;
import dto.ResponseMessageDto;
import dto.UserLombok;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.UserFactory.*;
import static utils.PropertiesReader.*;

public class RegistrationLoginApiTests implements BaseApi {
    @Test
    public void registrationApiPositive() {
        UserLombok user = positiveUser();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void registrationApiWrongDuplication() {
        UserLombok user = positiveUser();
        user.setPassword("Qwerty123!");
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            OK_HTTP_CLIENT.newCall(request).execute();
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 409);
    }

    @Test
    public void registrationApiWrongFormatNegativeTest() {
        UserLombok user = positiveUser();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), TEXT);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 500);
    }

    @Test
    public void loginApiPositive() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "email"))
                .password(getProperty("base.properties", "password"))
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void loginApiWrongNegativeTEst() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "email"))
                .password("qwerfm12!")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 401);
    }

    @Test
    public void registrationApiWrongEmailNegativeTest() {
        UserLombok user = positiveUser();
        user.setUsername("wrongemail.com");
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 400, "validate status code");
        softAssert.assertTrue(body.contains("must be a well-formed email address"), "validate message");
        softAssert.assertAll();
    }

    @Test
    public void registrationApiWrongPasswordNegativeTest() {
        UserLombok user = positiveUser();
        user.setPassword("qwerty");
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 400, "validate status code");
        softAssert.assertTrue(body.contains("At least 8 characters"), "validate message");
        softAssert.assertAll();
    }

    @Test
    public void registrationApiEmptyEmailNegativeTest() {
        UserLombok user = positiveUser();
        user.setUsername("");
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 400, "validate status code");
        softAssert.assertTrue(body.contains("must not be blank"), "validate message");
        softAssert.assertAll();
    }

    @Test
    public void loginApiUnregisteredUserNegativeTest() {
        UserLombok user = positiveUser();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        ResponseMessageDto responseMessageDto;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            responseMessageDto = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(responseMessageDto);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 401, "validate status code");
        softAssert.assertEquals(responseMessageDto.getMessage(), "Login or Password incorrect", "validate message");
        softAssert.assertAll();
    }

    @Test
    public void loginApiEmptyFieldsNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username("")
                .password("")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        ResponseMessageDto responseMessageDto;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            responseMessageDto = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(responseMessageDto);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 401, "validate status code");
        softAssert.assertEquals(responseMessageDto.getMessage(), "Login or Password incorrect", "validate message");
        softAssert.assertAll();
    }

    @Test
    public void registrationApiEmptyPasswordFieldNegativeTest() {
        UserLombok user = positiveUser();
        user.setPassword("");
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 400, "validate status code");
        softAssert.assertTrue(body.contains("Bad Request"), "validate message");
        softAssert.assertAll();
    }

    @Test
    public void registrationApiWrongFormatEmailNegativeTest() {
        UserLombok user = positiveUser();
        user.setPassword("AnnaKudr@gmai");
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 400, "validate status code");
        softAssert.assertTrue(body.contains("Bad Request"), "validate message");
        softAssert.assertAll();
    }
    @Test
    public void registrationApiWrongMethodGetNegativeTest() {
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .get()                                   // ← GET вместо POST, без тела
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("code: " + response.code() + ", body: '" + body + "'");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 403, "validate status code");
        softAssert.assertTrue(body.isEmpty(), "body must be empty");
        softAssert.assertAll();
    }
    @Test
    public void loginApiWrongEmailFormatNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username("AnnaKudr$")
                .password("Qwert56%")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        ResponseMessageDto responseMessageDto;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            responseMessageDto = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(responseMessageDto);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 401, "validate status code");
        softAssert.assertEquals(responseMessageDto.getMessage(), "Login or Password incorrect", "validate message");
        softAssert.assertAll();
    }

    @Test
    public void loginApiWrongMethodDeleteNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "email"))
                .password(getProperty("base.properties", "password"))
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .delete()
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("code: " + response.code() + ", body: '" + body + "'");
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 403, "validate status code");
        softAssert.assertTrue(body.isEmpty(), "body must be empty");
        softAssert.assertAll();
    }

    @Test
    public void registrationApiTextInsteadOfJsonNegativeTest() {
        RequestBody requestBody = RequestBody.create("hello", JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 500);
    }
    @Test
    public void registrationApiWrongJsonNegativeTest() {
        RequestBody requestBody = RequestBody.create("{\"username\":", JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 500);
        System.out.println(response.code());
    }
    @Test(dataProvider = "dataProviderWrongPasswordOrEmail",dataProviderClass = UserDataProvider.class)
    public void registrationApiWrongPasswordWithDataProviderNegativeTest(UserLombok user) {
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        String body;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            body = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(body);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 400, "validate status code");
        softAssert.assertTrue(body.contains("At least 8 characters"), "validate message");
        softAssert.assertAll();
    }
    @Test
    public void registrationApiWrongTypeOfRequestNegativeTest() {
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .get()
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);
        Assert.assertEquals(response.code(), 403);
    }
    @Test
    public void loginApiWrongKeyEmailNegativeTest() {
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties", "email"))
                .password(getProperty("base.properties", "password"))
                .build();
        Map<String,String> invalidJson = new HashMap<>();
        invalidJson.put("email",user.getUsername());
        invalidJson.put("password",user.getPassword());

        RequestBody requestBody = RequestBody.create(GSON.toJson(invalidJson), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        ResponseMessageDto responseMessageDto;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
            responseMessageDto = GSON.fromJson(response.body().string(), ResponseMessageDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(responseMessageDto);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.code(), 500, "validate status code");
        System.out.println(response.code());
        softAssert.assertEquals(responseMessageDto.getMessage(), "Login or Password incorrect", "validate message");
        softAssert.assertAll();
    }
}


