package ru.netology.sender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MessageSenderImplTest {
    @Mock
    GeoServiceImpl geoService = Mockito.spy(GeoServiceImpl.class);
    LocalizationServiceImpl localizationService = Mockito.spy(LocalizationServiceImpl.class);

    @ParameterizedTest
    @CsvSource({
            "172.000.00.00, Добро пожаловать",
            "96.000.00.00, Welcome"
    })
    void test_messageSenderImpl_RU_or_EN(String ipAddress, String expected) {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);
        String actual;

        if (ipAddress.startsWith("172.")) {

            Mockito.when(geoService.byIp(ipAddress))
                    .thenReturn(new Location(null, Country.RUSSIA, null, 0));

            Mockito.when(localizationService.locale(geoService.byIp(ipAddress).getCountry()))
                    .thenReturn("Добро пожаловать");

        } else {
            Mockito.when(geoService.byIp(ipAddress))
                    .thenReturn(new Location(null, Country.USA, null, 0));

            Mockito.when(localizationService.locale(geoService.byIp(ipAddress).getCountry()))
                    .thenReturn("Welcome");
        }
        actual = new MessageSenderImpl(geoService, localizationService).send(headers);
        assertEquals(expected, actual);
    }


    @Test
    void MessageSenderImplTest_Exception() {
        Map<String, String> headers = new HashMap<String, String>();

        assertThrows(NullPointerException.class, () -> {
            new MessageSenderImpl(geoService, localizationService).send(headers);
        });
    }
}