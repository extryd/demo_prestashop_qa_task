package tests;

import framework.BaseTest;
import framework.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pageobject.home.HomePage;
import pageobject.quickviewpopup.QuickViewPopUpPage;
import pageobject.shoppingcart.ShoppingCartPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Owner("vasily_kush")
@Test
public class CartPageTests extends BaseTest {

    @Description("Verify that header cart area is present at the page")
    public void verifyProductsCartIsPresentOnThePageTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random product into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemsList);
        ShoppingCartPage shoppingCartPage = homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickProceedToCheckOutButton();
        // Verify page contains header cart area
        shoppingCartPage.verifyCartAreaIsVisible();
    }

    @Description("Verify that page contains 'Continue shopping' button")
    public void verifyCartPageHasContinueShoppingButtonTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random product into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemsList);
        ShoppingCartPage shoppingCartPage = homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickProceedToCheckOutButton();
        // Verify 'continue shopping button' opens home page
        shoppingCartPage.clickContinueShoppingLink();
    }

    @Description("Verify that 'Proceed to checkout' button has clickable state when cart is not empty")
    public void verifyProceedToCheckoutButtonIsClickableInNotEmptyCartTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random product into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemsList);
        ShoppingCartPage shoppingCartPage = homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickProceedToCheckOutButton();
        // Verify 'proceed to checkout' button is clickable
        shoppingCartPage.verifyProceedToCheckOutButtonIsClickable();
    }

    @Description("Verify that 'Proceed to checkout' button hasn't clickable state when cart is empty")
    public void verifyProceedToCheckoutButtonIsNotClickableInEmptyCartTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random product into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemsList);
        ShoppingCartPage shoppingCartPage = homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickProceedToCheckOutButton();
        // Empty cart and verify 'proceed to checkout' button is not clickable
        shoppingCartPage.removeProductItem()
                .verifyProceedToCheckOutButtonIsNotClickable();
    }

    @Issue("Wrong price counting with special product/quantity pair (e.g. Hummingbird printed sweater x17)")
    @Description("Verify that total summary area has correct products calculation for quantity and price values")
    public void verifyCartHasCorrectTotalSummaryDataCalculationTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random two products with random quantity into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        List<WebElement> randomProductList = Utils.getRandomElementsFromList(productItemsList, 2);
        double expectedTotalPrice = 0;
        int expectedTotalProductQuantity = 0;
        for (WebElement product : randomProductList) {
            int randomValidQuantity = Utils.getRandomValidProductQuantityValue();
            expectedTotalProductQuantity += randomValidQuantity;
            expectedTotalPrice += randomValidQuantity * homePage.getProductPrice(product);
            QuickViewPopUpPage quickViewPopUpPage = homePage.hoverOnProductItemQuickView(product)
                    .clickOnProductItemQuickView(product)
                    .setProductQuantity(randomValidQuantity)
                    .clickAddToCartButton();
            // Opens cart page only when 2nd product gets added
            if (expectedTotalProductQuantity != randomValidQuantity)
                quickViewPopUpPage.clickProceedToCheckOutButton();
            else quickViewPopUpPage.clickContinueShoppingButton();
        }
        // Verify cart total quantity and price are correct
        ShoppingCartPage cartPage = new ShoppingCartPage(driver.get());
        double actualTotalPrice = cartPage.getCartProductSummaryPrice();
        int actualTotalProductQuantity = cartPage.getCartProductSummaryQuantity();
        int actualCartAreaProductsCount = cartPage.getCartAreaProductsCount();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualTotalPrice).as("Actual cart total price")
                .isEqualTo(Utils.roundTwoDigitsAfterComma(expectedTotalPrice));
        softly.assertThat(actualTotalProductQuantity).as("Actual cart total product quantity")
                .isEqualTo(expectedTotalProductQuantity);
        softly.assertThat(actualCartAreaProductsCount).as("Actual cart area product count")
                .isEqualTo(expectedTotalProductQuantity);
        softly.assertAll();
    }

    @Description("Verify that cart page has all added products")
    public void verifyCartContainsAllAddedProductsInsideTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random two products into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        List<WebElement> randomProductList = Utils.getRandomElementsFromList(productItemsList, 2);
        ArrayList<String> expectedProductTitleList = new ArrayList<>();
        for (WebElement product : randomProductList) {
            QuickViewPopUpPage quickViewPopUpPage = homePage
                    .hoverOnProductItemQuickView(product)
                    .clickOnProductItemQuickView(product);
            expectedProductTitleList.add(quickViewPopUpPage.getProductTitle());
            quickViewPopUpPage.
                    clickAddToCartButton()
                    .clickContinueShoppingButton();
        }
        // Open cart page and check that correct products are added
        ShoppingCartPage shoppingCartPage = homePage.clickOnEnabledCart();
        List<String> actualProductTitleList = shoppingCartPage.getCartItemsList().stream()
                .map(shoppingCartPage::getCartItemTitle)
                .collect(Collectors.toList());
        Assertions.assertThat(actualProductTitleList).as("Actual car product titles")
                .containsExactlyInAnyOrderElementsOf(expectedProductTitleList);
    }

    @Description("Verify that deleting product recalculates total summary area")
    public void verifyDeletingCartProductRecalculatesTotalSummaryDataTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random product into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemsList);
        ShoppingCartPage shoppingCartPage = homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickProceedToCheckOutButton();
        // Remove added product from cart
        shoppingCartPage.removeProductItem();
        // Verify cart total items quantity and price is 0
        int actualTotalProductQuantity = shoppingCartPage.getCartProductSummaryQuantity();
        double actualTotalProductPrice = shoppingCartPage.getCartProductSummaryPrice();
        int actualCartHeaderProductsCount = shoppingCartPage.getCartAreaProductsCount();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualTotalProductPrice).as("Actual total price").isEqualTo(0);
        softly.assertThat(actualTotalProductQuantity).as("Actual total quantity").isEqualTo(0);
        softly.assertThat(actualCartHeaderProductsCount).as("Actual cart products count").isEqualTo(0);
        softly.assertAll();
    }

    @Issue("Wrong price counting with special product/quantity pair (e.g. Hummingbird printed sweater x17)")
    @Description("Verify that changing product quantity recalculates total summary area")
    public void verifyChangingCartProductQuantityRecalculatesTotalSummaryDataTest() {
        HomePage homePage = new HomePage(driver.get());
        // Add random product into cart and open cart page
        List<WebElement> productItemsList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemsList);
        double productPrice = homePage.getProductPrice(randomProduct);
        ShoppingCartPage shoppingCartPage = homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickProceedToCheckOutButton();
        // Change cart product quantity
        int randomProductQuantity = Utils.getRandomValidProductQuantityValue();
        shoppingCartPage.setProductItemQuantity(randomProductQuantity);
        // Verify cart total items quantity and price were recalculated according to changed quantity
        double expectedExpectedTotalProductPrice = Utils.roundTwoDigitsAfterComma(productPrice * randomProductQuantity);
        double actualTotalProductPrice = shoppingCartPage.getCartProductSummaryPrice();
        int actualTotalProductQuantity = shoppingCartPage.getCartProductSummaryQuantity();
        int actualCartHeaderProductsCount = shoppingCartPage.getCartAreaProductsCount();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualTotalProductPrice).as("Actual total price")
                .isEqualTo(expectedExpectedTotalProductPrice);
        softly.assertThat(actualTotalProductQuantity).as("Actual total quantity")
                .isEqualTo(randomProductQuantity);
        softly.assertThat(actualCartHeaderProductsCount).as("Actual cart products count")
                .isEqualTo(randomProductQuantity);
        softly.assertAll();
    }

}
