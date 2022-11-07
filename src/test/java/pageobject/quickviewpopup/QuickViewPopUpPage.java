package pageobject.quickviewpopup;

import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobject.BasePage;
import pageobject.shoppingcart.ShoppingCartPage;

public class QuickViewPopUpPage extends BasePage {

    private final By addToCartButton = By.xpath("//form[@id='add-to-cart-or-refresh']//button[@type='submit']");
    private final By productQuantityInputField = By.id("quantity_wanted");
    // Part 'not(@aria-hidden)' is added to avoid state StaleElementReferenceException for 2nd modal opening
    private final By proceedToCheckOutButton = By.xpath("//div[@id='blockcart-modal' and not(@aria-hidden)]//div[@class='cart-content-btn']//a");
    // Part 'not(@aria-hidden)' is added to avoid state StaleElementReferenceException for 2nd modal opening
    private final By continueShoppingButton = By.xpath("//div[@id='blockcart-modal' and not(@aria-hidden)]//div[@class='cart-content-btn']/button");
    private final By cartProductAmountLabel = By.cssSelector("div#blockcart-modal p.cart-products-count");
    private final By cartSubtotalPriceLabel = By.xpath("//div[@class='cart-content']//span[text()='Subtotal:']/following-sibling::span");
    private final By productQuickMenuCloseIcon = By.xpath("//div[contains(@id,'quickview-modal')]//button[@class='close']");
    private final By productQuickMenuSummaryCloseIcon = By.cssSelector("#blockcart-modal button.close");
    private final By productTitle = By.cssSelector(".modal-body .row h1");

    public QuickViewPopUpPage(WebDriver driver) {
        super(driver);
    }

    @Step("Clicking on 'Add to cart' button")
    public QuickViewPopUpPage clickAddToCartButton() {
        try {
            waitClickabilityAndClick(driver.findElement(addToCartButton));
            waitVisibilityOf(driver.findElement(proceedToCheckOutButton));
            return this;
        } catch (Exception e) {
            waitClickabilityAndClick(driver.findElement(addToCartButton));
            waitVisibilityOf(driver.findElement(proceedToCheckOutButton));
            return this;
        }
    }

    @Step("Clicking on 'Proceed to checkout' button")
    public ShoppingCartPage clickProceedToCheckOutButton() {
        waitClickabilityAndClick(driver.findElement(proceedToCheckOutButton));
        return new ShoppingCartPage(driver);
    }

    @Step("Clicking on 'Continue shopping' button")
    public void clickContinueShoppingButton() {
        waitClickabilityAndClick(driver.findElement(continueShoppingButton));
    }

    @Step("Setting product quantity")
    public QuickViewPopUpPage setProductQuantity(int productQuantity) {
        driver.findElement(productQuantityInputField).clear();
        driver.findElement(productQuantityInputField).sendKeys(String.valueOf(productQuantity));
        return this;
    }

    @Step("Getting cart subtotal product amount")
    public int getCartSubtotalProductsAmountValue() {
        waitVisibilityOf(driver.findElement(continueShoppingButton));
        String rawText = driver.findElement(cartProductAmountLabel).getText();
        return Integer.parseInt(rawText.replaceAll("[^0-9]", ""));
    }

    @Step("Getting cart subtotal product price")
    public double getCartSubtotalProductsPriceValue() {
        waitVisibilityOf(driver.findElement(continueShoppingButton));
        String rawText = driver.findElement(cartSubtotalPriceLabel).getText();
        return Double.parseDouble(rawText.replaceAll("[^\\d.]", ""));
    }

    @Step("Getting 'Add to cart' button title")
    public String getAddToCartButtonTitle() {
        WebElement cartButtonElement = driver.findElement(addToCartButton);
        String rawText = StringUtils.normalizeSpace(getTextOfElement(cartButtonElement));
        String textToBeExcluded = getTextOfElement(cartButtonElement.findElement(By.tagName("i")));
        return rawText.replace(textToBeExcluded, "").trim();
    }

    @Step("Getting 'Continue shopping' button title")
    public String getContinueShoppingButtonTitle() {
        return getTextOfElement(driver.findElement(continueShoppingButton));
    }

    @Step("Getting 'Proceed to checkout' button title")
    public String getProceedToCheckOutButtonTitle() {
        WebElement cartButtonElement = driver.findElement(proceedToCheckOutButton);
        String rawText = StringUtils.normalizeSpace(getTextOfElement(cartButtonElement));
        String textToBeExcluded = getTextOfElement(cartButtonElement.findElement(By.tagName("i")));
        return rawText.replace(textToBeExcluded, "").trim();
    }

    @Step("Clicking on quick view menu close icon")
    public void clickAtQuickMenuCloseIcon() {
        WebElement closeIcon = driver.findElement(productQuickMenuCloseIcon);
        try {
            waitClickabilityAndClick(closeIcon);
            wait.until(ExpectedConditions.invisibilityOf(closeIcon));
        } catch (Exception e) {
            waitClickabilityAndClick(closeIcon);
            wait.until(ExpectedConditions.invisibilityOf(closeIcon));
        }
    }

    @Step("Clicking on quick view summary menu close icon")
    public void clickAtQuickMenuSummaryCloseIcon() {
        waitClickabilityAndClick(driver.findElement(productQuickMenuSummaryCloseIcon));
    }

    @Step("Getting product title")
    public String getProductTitle() {
        return getTextOfElement(driver.findElement(productTitle));
    }

}
