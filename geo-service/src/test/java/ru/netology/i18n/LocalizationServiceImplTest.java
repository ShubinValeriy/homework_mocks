package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceImplTest {

    @ParameterizedTest
    @CsvSource({
            "RUSSIA, Добро пожаловать",
            "USA, Welcome",
            "GERMANY, Welcome",
            "BRAZIL, Welcome"
    })
    void localizationServiceImpl(Country country, String expected) {
        String actual = new LocalizationServiceImpl().locale(country);
        assertEquals(expected, actual);
    }

}