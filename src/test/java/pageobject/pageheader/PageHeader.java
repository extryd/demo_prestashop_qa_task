package pageobject.pageheader;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageobject.BasePage;
import pageobject.home.constants.Language;
import pageobject.shoppingcart.ShoppingCartPage;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PageHeader extends BasePage {

    private final By enabledCartArea = By.cssSelector("div#_desktop_cart a");
    private final By disabledCartArea = By.xpath("//div[@id='_desktop_cart' and not(.//a)]");
    private final By cartAreaProductsCount = By.xpath("//span[@class='cart-products-count']");
    private final By languagesDropdown = By.cssSelector("div.language-selector-wrapper");
    private final By languagesDropdownList = By.cssSelector("div#_desktop_language_selector ul li");
    private final By selectedApplicationLanguage = By.cssSelector("#_desktop_language_selector span.expand-more");

    public PageHeader(WebDriver driver) {
        super(driver);
    }

    @Step("Verifying cart area is visible")
    public void verifyCartAreaIsVisible() {
        waitVisibilityOf(driver.findElement(cartAreaProductsCount));
    }

    @Step("Verifying cart area is clickable")
    public void verifyCartAreaIsClickable() {
        waitClickabilityOf(driver.findElement(enabledCartArea));
    }

    @Step("Verifying cart area is not clickable")
    public void verifyCartAreaIsNotClickable() {
        waitClickabilityOf(driver.findElement(disabledCartArea));
    }

    @Step("Clicking on header cart icon")
    public ShoppingCartPage clickOnEnabledCart() {
        waitClickabilityAndClick(driver.findElement(enabledCartArea));
        return new ShoppingCartPage(driver);
    }

    @Step("Getting cart area product count")
    public int getCartAreaProductsCount() {
        WebElement cardElement = driver.findElement(cartAreaProductsCount);
        waitVisibilityOf(cardElement);
        String cartCountRaw = cardElement.getText();
        String cartCount = cartCountRaw.replaceAll("[()]", "");
        return Integer.parseInt(cartCount);
    }

    @Step("Getting available language list")
    public List<String> getAvailableLanguagesList() {
        waitClickabilityAndClick(driver.findElement(languagesDropdown));
        List<WebElement> elements = driver.findElements(languagesDropdownList);
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Selecting application language")
    public PageHeader selectLanguage(Language language) {
        waitClickabilityAndClick(driver.findElement(languagesDropdown));
        driver.findElement(By.xpath(String.format("//a[contains(text(),'%s')]", language))).click();
        waitVisibilityOf(driver.findElement(selectedApplicationLanguage));
        return this;
    }

    @Step("Getting selected application language")
    public String getSelectedApplicationLanguage() {
        return driver.findElement(selectedApplicationLanguage).getText();
    }

}
