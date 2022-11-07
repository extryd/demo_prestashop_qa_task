package framework;

import framework.configuration.ConfigManager;
import framework.listener.TestListener;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.time.Duration;

@Listeners(TestListener.class)
public abstract class BaseTest {

    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @SneakyThrows
    @BeforeMethod
    public void setUp() {
        switch (ConfigManager.config.browser()) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
                break;
            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver.set(new EdgeDriver());
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver());
                break;
            default:
                throw new Exception("Wrong browser setup is passed to the configuration file");
        }
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.config.defaultTimeOut()));
    }

    @AfterMethod
    public void cleanUp() {
        if (driver.get() != null)
            driver.get().quit();
    }

    public WebDriver getDriver() {
        return driver.get();
    }

}
