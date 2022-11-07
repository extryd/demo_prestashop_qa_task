package tests;

import framework.BaseTest;
import framework.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageobject.home.HomePage;
import pageobject.home.constants.Language;
import pageobject.quickviewpopup.QuickViewPopUpPage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Owner("vasily_kush")
@Test
public class HomePageTests extends BaseTest {

    @Description("Verify that header cart area is present at the page")
    public void verifyProductsCartIsPresentOnThePageTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.verifyCartAreaIsVisible();
    }

    @Description("Verify that header cart area is not clickable when the cart is empty")
    public void verifyEmptyProductCartIsNotClickableTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.verifyCartAreaIsNotClickable();
    }

    @Description("Verify that header cart area is clickable when the cart is not empty")
    public void verifyNotEmptyProductCartIsClickableTest() {
        HomePage homePage = new HomePage(driver.get());
        // Select random product and add it into cart
        List<WebElement> productItemList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemList);
        homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickContinueShoppingButton();
        // Verify cart became clickable
        homePage.verifyCartAreaIsClickable();
    }

    @Description("Verify that clicking at not empty cart icon opens cart page")
    public void verifyOpeningCartPageByClickingAtNotEmptyCartIconTest() {
        HomePage homePage = new HomePage(driver.get());
        // Select random product and add it into cart
        List<WebElement> productItemList = homePage.getProductItemsList();
        WebElement randomProduct = Utils.getRandomElementFromList(productItemList);
        homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickContinueShoppingButton();
        // Click at cart icon and verify that cart page is opened
        homePage.clickOnEnabledCart();
    }

    @Description("Verify that adding products into increases cart header counter")
    public void verifyAddingProductsIntoCartIncreasesCartItemCounterTest() {
        HomePage homePage = new HomePage(driver.get());

        // Select two random products and add it into cart with specified quantities
        int expectedCartItemQuantity = 0;
        List<WebElement> productItemList = homePage.getProductItemsList();
        List<WebElement> randomProductList = Utils.getRandomElementsFromList(productItemList, 2);
        for (WebElement product : randomProductList) {
            int randomPreparedValidQuantity = Utils.getRandomValidProductQuantityValue();
            homePage.hoverOnProductItemQuickView(product)
                    .clickOnProductItemQuickView(product)
                    .setProductQuantity(randomPreparedValidQuantity)
                    .clickAddToCartButton()
                    .clickContinueShoppingButton();
            expectedCartItemQuantity += randomPreparedValidQuantity;
        }

        // Verify cart icon has correct quantity
        int actualCartQuantity = homePage.getCartAreaProductsCount();
        Assertions.assertThat(actualCartQuantity).as("Actual cart product count")
                .isEqualTo(expectedCartItemQuantity);
    }

    @Issue("Wrong price counting with special product/quantity pair (e.g. Hummingbird printed sweater x17)")
    @Description("Verify that quick view subtotal products quantity and price are correct")
    public void verifyDisplayingQuickViewSubtotalCartDataTest() {
        HomePage homePage = new HomePage(driver.get());
        int expectedCartItemQuantity = 0;
        double expectedCartItemPrice = 0;

        // Select two random products and add it into cart with specified quantities
        List<WebElement> allProductItemList = homePage.getProductItemsList();
        List<WebElement> selectedProductList = Utils.getRandomElementsFromList(allProductItemList, 2);
        for (WebElement product : selectedProductList) {
            double productPrice = homePage.getProductPrice(product);
            int randomPreparedValidQuantity = Utils.getRandomValidProductQuantityValue();
            expectedCartItemPrice += Utils.roundToTwoDigitsAfterComma(productPrice * randomPreparedValidQuantity);
            expectedCartItemQuantity += randomPreparedValidQuantity;

            // Add product and get actual quantity and price
            QuickViewPopUpPage quickViewPopUpPage = homePage
                    .hoverOnProductItemQuickView(product)
                    .clickOnProductItemQuickView(product)
                    .setProductQuantity(randomPreparedValidQuantity)
                    .clickAddToCartButton();
            int actualProductQuantity = quickViewPopUpPage.getCartSubtotalProductsAmountValue();
            double actualProductPrice = quickViewPopUpPage.getCartSubtotalProductsPriceValue();
            // Close quick view pop-up after 1st product adding
            if (randomPreparedValidQuantity == expectedCartItemQuantity)
                quickViewPopUpPage.clickContinueShoppingButton();

            // Verify cart subtotal modal has correct quantity and price
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(actualProductQuantity).as("Actual cart product quantity")
                    .isEqualTo(expectedCartItemQuantity);
            Assertions.assertThat(actualProductPrice).as("Actual cart product price")
                    .isEqualTo(Utils.roundToTwoDigitsAfterComma(expectedCartItemPrice));
            softly.assertAll();
        }
    }

    @Description("Verify that languages dropdown contains all required options")
    public void verifyCartNamingInDifferentLanguagesTest() {
        HomePage homePage = new HomePage(driver.get());
        // Open language dropdown and collect available languages
        List<String> availableLanguageList = homePage.getAvailableLanguagesList();
        // Verify all required languages are available
        List<String> expectedLanguageList = Arrays.stream(Language.values())
                .map(Language::getTitle)
                .collect(Collectors.toList());
        Assertions.assertThat(availableLanguageList).as("Actual available language list")
                .containsAll(expectedLanguageList);
    }

    @Test(dataProvider = "getLanguageList")
    @Description("Verify that all cart related areas has correct naming in different languages")
    public void verifyDisplayingCorrectNamingOfCartRelatedMenusInDifferentLanguagesTest(Language language) {
        HomePage homePage = new HomePage(driver.get());

        // Select certain language and collect cart related elements namings
        String actualSelectedLanguage = homePage.selectLanguage(language)
                .getSelectedApplicationLanguage();
        WebElement randomElement = Utils.getRandomElementFromList(homePage.getProductItemsList());
        QuickViewPopUpPage quickViewPopUpPage = homePage.hoverOnProductItemQuickView(randomElement)
                .clickOnProductItemQuickView(randomElement);
        String actualAddToCartButtonTitle = quickViewPopUpPage.getAddToCartButtonTitle();
        quickViewPopUpPage.clickAddToCartButton();
        String actualContinueShoppingButtonTitle = quickViewPopUpPage.getContinueShoppingButtonTitle();
        String actualProceedToCheckOutButtonTitle = quickViewPopUpPage.getProceedToCheckOutButtonTitle();

        // Verify buttons' titles are valid
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualSelectedLanguage).as("Actual selected language title")
                .isEqualTo(language.getTitle());
        softly.assertThat(actualAddToCartButtonTitle).as("Actual add to cart button title")
                .isEqualTo(language.getAddToCartButton());
        softly.assertThat(actualContinueShoppingButtonTitle).as("Actual continue shopping button title")
                .isEqualTo(language.getContinueShoppingButton());
        softly.assertThat(actualProceedToCheckOutButtonTitle).as("Actual proceed to checkout button title")
                .isEqualTo(language.getProceedToCheckoutButton());
        softly.assertAll();
    }

    @Description("Verify that clicking at quick menu pop-up close icon closes the quick view modal")
    public void verifyClosingQuickMenuPopUpByCloseIconTest() {
        HomePage homePage = new HomePage(driver.get());
        // Open some product quick menu and click close icon
        WebElement randomProduct = Utils.getRandomElementFromList(homePage.getProductItemsList());
        homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAtQuickMenuCloseIcon();
        // Verify quick view modal is closed
        homePage.verifyCartAreaIsVisible();
    }

    @Description("Verify that clicking at quick menu summary pop-up close icon closes the quick view modal")
    public void verifyClosingQuickMenuSummaryPopUpByCloseIconTest() {
        HomePage homePage = new HomePage(driver.get());
        // Open some product quick menu summary and click close icon
        WebElement randomProduct = Utils.getRandomElementFromList(homePage.getProductItemsList());
        homePage.hoverOnProductItemQuickView(randomProduct)
                .clickOnProductItemQuickView(randomProduct)
                .clickAddToCartButton()
                .clickAtQuickMenuSummaryCloseIcon();
        // Verify quick view modal is closed
        homePage.verifyCartAreaIsVisible();
    }

    @DataProvider(parallel = true)
    private Object[][] getLanguageList() {
        return Arrays.stream(Language.values()).map(value -> new Object[]{value}).toArray(Object[][]::new);
    }

}
