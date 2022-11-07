package tests;

import framework.BaseTest;
import framework.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pageobject.home.HomePage;
import pageobject.singleproduct.SingleProductPage;

import java.util.List;

@Owner("vasily_kush")
@Test
public class ProductPageTests extends BaseTest {

    @Description("Verify that header cart area is present at the page")
    public void verifyProductsCartIsPresentOnThePageTest() {
        HomePage homePage = new HomePage(driver.get());
        List<WebElement> productItemsList = homePage.getProductItemsList();
        homePage.openProduct(Utils.getRandomElementFromList(productItemsList))
                .verifyCartAreaIsVisible();
    }

    @Description("Verify that adding product into cart from single product page adds its to the cart")
    public void verifyAddingProductIntoCartTest() {
        HomePage homePage = new HomePage(driver.get());
        int preparedQuantity = Utils.getRandomValidProductQuantityValue();
        // Select random product and add it into cart
        List<WebElement> productItemList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemList);
        SingleProductPage singleProductPage = homePage.openProduct(randomProduct);
        singleProductPage.setProductQuantity(preparedQuantity)
                .clickAddToCartButton()
                .clickContinueShoppingButton();
        // Verify actual cart product quantity is matched to selected
        int actualCartProductQuantity = singleProductPage.getCartAreaProductsCount();
        Assertions.assertThat(actualCartProductQuantity).as("Actual cart area product quantity")
                .isEqualTo(preparedQuantity);
    }

}
