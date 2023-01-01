package ua.stepanchuk.cryptocurrencyStatistics.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.stepanchuk.cryptocurrencyStatistics.model.CryptoItem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ua.stepanchuk.cryptocurrencyStatistics.util.constants.CurrencyName.*;
import static ua.stepanchuk.cryptocurrencyStatistics.util.constants.CurrencyName.USD;

class JsonUtilTest {

    @ParameterizedTest
    @MethodSource("cryptoItems")
    void jsonToCryptoItems(String cryptocurrency, String currency, double price, String tmsp) {

        List<CryptoItem> expected = List.of(
                new CryptoItem(
                        cryptocurrency,
                        currency,
                        price,
                        timestampToLocalDateTime(Long.parseLong(tmsp))
                )
        );

        String json =
                "[\n" +
                "    {\n" +
                "        \"tmsp\": " + tmsp + ",\n" +
                "        \"price\": \"" + price + "\"\n" +
                "    }\n" +
                "]";

        List<CryptoItem> actual = JsonUtil.JsonToCryptoItems(cryptocurrency, currency, json);
        assertEquals(expected, actual);
    }

    private static LocalDateTime timestampToLocalDateTime(long tmsp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(tmsp), ZoneId.systemDefault());
    }

    public static Stream<Arguments> cryptoItems() {
        return Stream.of(
                Arguments.of(BTC, USD, 1192.62, "1672418700"),
                Arguments.of(ETH, USD, 47788,   "1672416900"),
                Arguments.of(ETH, USD, 1195.0,  "1672416000"),
                Arguments.of(BTC, USD, 7575.2,  "1672414200"),
                Arguments.of(XRP, USD, 12,      "1672413300")
        );
    }
}