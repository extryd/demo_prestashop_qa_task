package pageobject;

import framework.configuration.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(ConfigManager.config.defaultTimeOut()));
    }

    protected void hoverOnElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.build().perform();
    }

    protected void waitClickabilityAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void waitVisibilityOf(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitClickabilityOf(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected String getTextOfElement(WebElement element) {
        return element.getAttribute("textContent");
    }

}
