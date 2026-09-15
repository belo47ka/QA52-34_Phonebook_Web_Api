package manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
//import java.util.logging.Logger;

public class AppManager {
    private WebDriver driver;
    public WebDriver getDriver(){
        return driver;
    }

    public Logger logger = LoggerFactory.getLogger(AppManager.class);
    @BeforeMethod(alwaysRun = true)
    public void setup(Method method) {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        logger.info("Start testing with method->");
    }

    @AfterMethod(enabled = true)
    public void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }
}
