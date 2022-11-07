package pageobject.home;

import framework.configuration.ConfigManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobject.pageheader.PageHeader;
import pageobject.quickviewpopup.QuickViewPopUpPage;
import pageobject.singleproduct.SingleProductPage;

import java.util.List;

public class HomePage extends PageHeader {

    private static final By iframe = By.xpath("//iframe[@id='framelive' and @src]");
    private final By productList = By.xpath("//div[@class='thumbnail-top']/parent::div");
    private final By quickView = By.xpath(".//a[@data-link-action='quickview']");
    private final By productPrice = By.xpath(".//span[@class='price']");

    public HomePage(WebDriver driver) {
        super(driver);
        driver.get(ConfigManager.config.baseUri());
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
        waitVisibilityOf(driver.findElement(productList));
    }

    @Step("Getting product item list")
    public List<WebElement> getProductItemsList() {
        return driver.findElements(productList);
    }

    @Step("Clicking on product item quick view button")
    public QuickViewPopUpPage clickOnProductItemQuickView(WebElement productItem) {
        try {
            waitClickabilityAndClick(productItem.findElement(quickView));
            return new QuickViewPopUpPage(driver);
        } catch (TimeoutException e) {
            waitClickabilityAndClick(productItem.findElement(quickView));
            return new QuickViewPopUpPage(driver);
        }
    }

    @Step("Hovering on product item quick view button")
    public HomePage hoverOnProductItemQuickView(WebElement productItem) {
        hoverOnElement(productItem);
        return this;
    }

    @Step("Getting product price")
    public double getProductPrice(WebElement productElement) {
        // Scroll is added to avoid case when product data is not loaded completely sometimes
        // till user hasn't scrolled to the element
        Actions actions = new Actions(driver);
        actions.moveToElement(productElement);
        actions.perform();
        String rawPrice = productElement.findElement(productPrice).getText();
        return Double.parseDouble(rawPrice.replaceAll("[^\\d.]", ""));
    }

    @Step("Opening single product page")
    public SingleProductPage openProduct(WebElement productElement) {
        productElement.click();
        return new SingleProductPage(driver);
    }

}
