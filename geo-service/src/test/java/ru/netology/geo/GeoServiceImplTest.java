package ru.netology.geo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.jupiter.api.Assertions.*;

class GeoServiceImplTest {

    @ParameterizedTest
    @CsvSource({
            "127.0.0.1, , , , 0",
            "172.0.32.11, Moscow, RUSSIA, Lenina, 15",
            "96.44.183.149, New York, USA, 10th Avenue, 32",
            "172.0.00.00, Moscow, RUSSIA, , 0",
            "96.00.000.000, New York, USA, , 0"
    })
    void geoServiceImplTest(String ip, String city, Country country, String street, int building) {
        GeoServiceImpl geoService = new GeoServiceImpl();

        Location actual = geoService.byIp(ip);
        Location expected = new Location(city, country, street, building);


        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
        assertEquals(expected.getStreet(), actual.getStreet());
        assertEquals(expected.getBuiling(), actual.getBuiling());
    }

}