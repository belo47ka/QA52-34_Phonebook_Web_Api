package ui_tests;

import data_providers.UserDataProvider;
import dto.UserLombok;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;
import static utils.UserFactory.*;

import java.util.Random;

public class RegistrationTests extends AppManager {
    LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void goToRegistrationLoginPage() {
        logger.info("Start Registration");
        new HomePage(getDriver()).clickLinkLogin();
        loginPage = new LoginPage(getDriver());
    }

    @Test(groups = {"smoke","regress","user","positive"})
    public void registrationPositiveTest() {
        int i = new Random().nextInt(1000);
        UserLombok user = UserLombok.builder()
                .username("vbfgty" + i + "@fgrty.bh")
                .password("Adfert23!")
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(new ContactsPage(getDriver())
                .validateTextInMessageNoContacts("No Contacts here!"));
    }

    @Test
    public void registrationPositiveWithFakerTest() {
        UserLombok user = positiveUser();
        System.out.println(user);
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(new ContactsPage(getDriver())
                .validateTextInMessageNoContacts("No Contacts here!"));
    }

    @Test
    public void registrationNegativeEmptyAllFieldsTest(){
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

    @Test
    public void registrationNegativeEmptyEmailFieldsTest(){
        UserLombok user = positiveUser();
        user.setUsername("");
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

    @Test
    public void registrationNegativeEmptyPasswordFieldsTest(){
        UserLombok user = positiveUser();
        user.setPassword("");
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }

    @Test(dataProvider = "dataProviderWrongPasswordOrEmail",
            dataProviderClass = UserDataProvider.class)
    public void registrationNegativeWrongPasswordFieldsTest(UserLombok user){

        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password format"));
    }





//
//    @Test
//    public void testMethod() {
//        new HomePage(getDriver()).method();
//    }
//
//    @Test
//    public void testAjaxMethod() {
//        new HomePage(getDriver()).ajaxMethod();
//    }
}
