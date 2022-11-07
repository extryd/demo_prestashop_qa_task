package pageobject.singleproduct;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import pageobject.pageheader.PageHeader;
import pageobject.quickviewpopup.QuickViewPopUpPage;

public class SingleProductPage extends PageHeader {
    private final By quantityInput = By.cssSelector("input#quantity_wanted");

    public SingleProductPage(WebDriver driver) {
        super(driver);
    }

    @Step("Setting product quantity")
    public QuickViewPopUpPage setProductQuantity(int productQuantity) {
        driver.findElement(quantityInput)
                .sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(productQuantity));
        return new QuickViewPopUpPage(driver);
    }

}
