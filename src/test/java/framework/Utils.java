package framework;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utils {

    public static <T> T getRandomElementFromList(List<T> list) {
        Assertions.assertThat(list).as("Data list").isNotEmpty();
        return list.get(new Random().nextInt(list.size()));
    }

    @SneakyThrows
    public static <T> List<T> getRandomElementsFromList(List<T> list, int elementsNumber) {
        ArrayList<T> tempListCopy = new ArrayList<>(list);
        Collections.shuffle(tempListCopy);
        if (tempListCopy.size() < elementsNumber)
            throw new Exception("Attempt to get more elements than provided list contains");
        ArrayList<T> dataToReturn = new ArrayList<>();
        for (int i = 0; i < elementsNumber; i++) {
            T randomElement = tempListCopy.get(new Random().nextInt(tempListCopy.size()));
            dataToReturn.add(randomElement);
            tempListCopy.remove(randomElement);
        }
        return dataToReturn;
    }

    public static double roundToTwoDigitsAfterComma(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // Let's consider valid values are from 1 to 100
    public static int getRandomValidProductQuantityValue() {
        return new Random().nextInt(100 - 1) + 1;
    }
}
