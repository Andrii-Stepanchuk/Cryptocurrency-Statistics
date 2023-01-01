package ua.stepanchuk.cryptocurrencyStatistics.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ua.stepanchuk.cryptocurrencyStatistics.util.constants.CurrencyName.*;
import static ua.stepanchuk.cryptocurrencyStatistics.util.constants.CurrencyName.USD;

class RequestSenderTest {

    @ParameterizedTest
    @MethodSource("currencyPairs")
    void sendHttpPostRequest(String cryptocurrency, String currency) {
        assertNotNull(RequestSender.sendHttpPostRequestPriceStatsOnCexIO(cryptocurrency, currency));
    }

    public static Stream<Arguments> currencyPairs() {
        return Stream.of(
                Arguments.of(BTC, USD),
                Arguments.of(ETH, USD),
                Arguments.of(XRP, USD)
        );
    }
}