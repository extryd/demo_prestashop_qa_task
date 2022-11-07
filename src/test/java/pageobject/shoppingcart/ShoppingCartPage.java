package pageobject.shoppingcart;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobject.pageheader.PageHeader;

import java.util.List;

public class ShoppingCartPage extends PageHeader {

    private final By cartItems = By.cssSelector("ul.cart-items li");
    private final By cartItemTitle = By.cssSelector("div.product-line-info a");
    private final By enabledProceedToCheckOutButton = By.cssSelector("div.cart-summary a");
    private final By disabledProceedToCheckOutButton = By.cssSelector("div.cart-summary button");
    private final By cartSummaryTotalItemsAmount = By.cssSelector("#cart-subtotal-products span:first-child");
    private final By cartSummaryTotalItemsPrice = By.cssSelector("#cart-subtotal-products span:last-child");
    private final By removeProductButton = By.cssSelector("a.remove-from-cart");
    private final By productQuantityInputField = By.xpath("//input[@name='product-quantity-spin']");
    private final By continueShoppingButton = By.xpath("//div[@class='card cart-container']/following-sibling::a");

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
        waitVisibilityOf(driver.findElement(cartSummaryTotalItemsAmount));
    }

    @Step("Getting cart items list")
    public List<WebElement> getCartItemsList() {
        return driver.findElements(cartItems);
    }

    @Step("Getting cart item title")
    public String getCartItemTitle(WebElement carItem) {
        return carItem.findElement(cartItemTitle).getText();
    }

    @Step("Verifying 'Proceed to checkout' button is clickable")
    public ShoppingCartPage verifyProceedToCheckOutButtonIsClickable() {
        wait.until(ExpectedConditions.elementToBeClickable(enabledProceedToCheckOutButton));
        return this;
    }

    @Step("Verifying 'Proceed to checkout' button is not clickable")
    public void verifyProceedToCheckOutButtonIsNotClickable() {
        waitVisibilityOf(driver.findElement(disabledProceedToCheckOutButton));
    }

    @Step("Getting cart product summary quantity")
    public int getCartProductSummaryQuantity() {
        String rawText = driver.findElement(cartSummaryTotalItemsAmount).getText();
        return Integer.parseInt(rawText.replaceAll("[^0-9]", ""));
    }

    @Step("Getting cart product summary price")
    public double getCartProductSummaryPrice() {
        String rawText = driver.findElement(cartSummaryTotalItemsPrice).getText();
        return Double.parseDouble(rawText.replaceAll("[^\\d.]", ""));
    }

    @Step("Removing product item")
    public ShoppingCartPage removeProductItem() {
        WebElement element = driver.findElement(removeProductButton);
        element.click();
        wait.until(ExpectedConditions.invisibilityOf(element));
        return this;
    }

    @Step("Setting item quantity item")
    public ShoppingCartPage setProductItemQuantity(int quantity) {
        WebElement elementToBeChanged = driver.findElement(cartSummaryTotalItemsPrice);
        WebElement productQuantityElement = driver.findElement(productQuantityInputField);
        productQuantityElement.sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(quantity));
        productQuantityElement.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.stalenessOf(elementToBeChanged));
        return this;
    }

    @Step("Clicking on continue shopping link")
    public void clickContinueShoppingLink() {
        waitClickabilityAndClick(driver.findElement(continueShoppingButton));
    }

}
