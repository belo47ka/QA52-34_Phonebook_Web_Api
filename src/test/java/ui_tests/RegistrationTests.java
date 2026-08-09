package ui_tests;

import dto.UserLombok;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ContactsPage;
import pages.Homepage;
import pages.LoginPage;
import utils.UserFactory;
import static utils.UserFactory.*;

import java.util.Random;

public class RegistrationTests extends AppManager {
    LoginPage loginPage;

    @BeforeMethod
    public void gotoRegistrationLoginPage() {
        new Homepage(getDriver()).clickBtnLogin();
        loginPage = new LoginPage(getDriver());
    }

    @Test
    public void registrationPositiveTests() {
        int i = new Random().nextInt(1000);
        UserLombok user = UserLombok.builder()
                .username("Kudryashov" + i + "47@gmail.com")
                .password("Belkaanna47$")
                .build();

        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(new ContactsPage(getDriver())
                .validateTextInMessageNoContacts("No Contacts here!"));

    }

    @Test
    public void registrationPositiveTestsWithFaker() {

        UserLombok user = positiveUser();
        System.out.println(user);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(new ContactsPage(getDriver())
                .validateTextInMessageNoContacts("No Contacts here!"));

    }
    @Test
    public void registrationNegativeEmptyAllFieldsTest(){
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format2"));
    }
}
