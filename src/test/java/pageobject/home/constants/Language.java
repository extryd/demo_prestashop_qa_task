package pageobject.home.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {

    // For example let's take only two of the actual list
    RUSSIAN("Русский", "В корзину", "Продолжить покупки",
            "Перейти к оформлению"),
    DEUTSCH("Deutsch", "In den Warenkorb", "Einkauf fortsetzen",
            "Zur Kasse");

    private final String title;
    private final String addToCartButton;
    private final String continueShoppingButton;
    private final String proceedToCheckoutButton;

    @Override
    public String toString() {
        return title;
    }
}
