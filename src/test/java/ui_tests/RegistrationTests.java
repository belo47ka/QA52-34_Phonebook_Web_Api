package ui_tests;

import dto.UserLombok;
import manager.AppManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Homepage;
import pages.LoginPage;

import java.util.Random;

public class RegistrationTests extends AppManager {

   @BeforeMethod
   public void gotoRegistrationLoginPage(){
       new Homepage(getDriver()).clickBtnLogin();
   }
    @Test
    public void registrationPositiveTests(){
       int i = new Random().nextInt(1000);
        UserLombok user =  UserLombok.builder()
                .username("Kudryashov"+i+"47@gmail.com")
                .password("Belkaanna47$")
                .build();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationForm(user);
        loginPage.clickBtnRegistration();

    }
}
