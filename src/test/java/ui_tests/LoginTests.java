package ui_tests;

import data_providers.UserDataProvider;
import dto.UserLombok;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;
import static utils.PropertiesReader.*;

public class LoginTests extends AppManager {
    LoginPage loginPage;
    SoftAssert softAssert = new SoftAssert();
    @BeforeMethod
    public void goToRegesrationLoginPage(){
        new HomePage(getDriver()).clickLinkLogin();
        loginPage=new LoginPage(getDriver());
    }

    @Test(dataProvider = "dataProviderWrongPasswordOrEmail",
            dataProviderClass = UserDataProvider.class)
    public void loginNegativeWrongPasswordFieldsTest(UserLombok user){

        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        Assert.assertTrue(loginPage.closeAlert()
                .contains("Wrong email or password"));
    }
    @Test
    public void loginPositiveTest(){
        UserLombok user = UserLombok.builder()
                .username(getProperty("base.properties","email"))
                .password(getProperty("base.properties","login"))
                .build();
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnLogin();
        ContactsPage contactsPage=new ContactsPage(getDriver());
        softAssert.assertTrue(contactsPage.isLinkContactsDisplayed(),"validate isLinkContactsDisplayed");
        softAssert.assertTrue(contactsPage.isUrlContainsText("contacts"),"validate Url");
        softAssert.assertAll();

    }
    @Test
    public void loginNegativeAllFieldsEmptyTest(){
        loginPage.clickBtnLogin();
        //Assert.assertTrue(loginPage.closeAlert().contains("Wrong email or password"));
        Assert.assertEquals(loginPage.closeAlert(),"Wrong email or password");

    }

}
