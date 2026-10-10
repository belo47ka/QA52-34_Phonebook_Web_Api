package api_tests;

import dto.ContactDto;
import dto.ContactsDto;
import dto.TokenDto;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.BaseApi;
import utils.ICreateContact;
import utils.ILogin;

import java.io.IOException;

import static utils.ContactFactory.*;

public class UpdateContactApiTests implements BaseApi, ICreateContact, ILogin {
    ContactDto contactDto;
    TokenDto tokenDto;
    @BeforeClass
    public void login(){
        tokenDto = loginGetToken();
        contactDto = createContact();
    }
    @Test
    public void updateContactPositiveApiTests(){
        ContactDto contact = positiveContact();
        contact.setId(createContact().getId());
        RequestBody requestBody = RequestBody.create(GSON.toJson(contact),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + PUT_CONTACT)
                .addHeader(AUTH,tokenDto.getToken())
                .put(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(),200);
    }
}
