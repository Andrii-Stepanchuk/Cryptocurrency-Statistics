package ua.stepanchuk.cryptocurrencyStatistics.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import ua.stepanchuk.cryptocurrencyStatistics.model.CryptoItem;
import ua.stepanchuk.cryptocurrencyStatistics.service.CryptoItemService;
import ua.stepanchuk.cryptocurrencyStatistics.service.CsvExportService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.stepanchuk.cryptocurrencyStatistics.util.constants.CurrencyName.*;


@WebMvcTest(CryptoItemController.class)
class CryptoItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoItemService cryptoItemService;

    @MockBean
    private CsvExportService csvExportService;

    @ParameterizedTest
    @MethodSource("cryptoItemAndPagination")
    void getCryptoItemsByPaginationSuccess(String cryptocurrency, String currency, int page, int size) throws Exception {
        int firstNumber = 0;
        List<CryptoItem> expected = IntStream
                .range(firstNumber, size)
                .mapToObj(i -> new CryptoItem(cryptocurrency, currency, i, LocalDateTime.now()))
                .collect(Collectors.toList());

        Mockito.when(cryptoItemService.getCryptoItemsByPagination(cryptocurrency, page, size)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/cryptocurrencies?name=%s&page=%s&size=%s", cryptocurrency, page, size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(size)))
                .andExpect(jsonPath("$[0].curr1", is(cryptocurrency)))
                .andExpect(jsonPath("$[0].curr2", is(currency)))
                .andExpect(jsonPath("$[0].price", is((double) firstNumber)));
    }

    @ParameterizedTest
    @MethodSource("cryptoItemAndPagination")
    void getCryptoItemsByPaginationSuccessWithDefaultValues(String cryptocurrency, String currency) throws Exception {
        int defaultPage = 0;
        int defaultSize = 10;
        int firstNumber = 0;

        List<CryptoItem> expected = IntStream
                .range(firstNumber, defaultSize)
                .mapToObj(i -> new CryptoItem(cryptocurrency, currency, i, LocalDateTime.now()))
                .collect(Collectors.toList());

        Mockito.when(cryptoItemService.getCryptoItemsByPagination(cryptocurrency, defaultPage, defaultSize)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/cryptocurrencies?name=%s", cryptocurrency))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(defaultSize)))
                .andExpect(jsonPath("$[0].curr1", is(cryptocurrency)))
                .andExpect(jsonPath("$[0].curr2", is(currency)))
                .andExpect(jsonPath("$[0].price", is((double) firstNumber)));
    }

    @ParameterizedTest
    @MethodSource("cryptoItemAndPagination")
    void getCryptoItemsByPaginationNotFound(String cryptocurrency, String currency, int page, int size) throws Exception {

        Mockito.when(cryptoItemService.getCryptoItemsByPagination(cryptocurrency, page, size)).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/cryptocurrencies?name=%s&page=%s&size=%s", cryptocurrency, page, size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCryptoItemsByPaginationInputData() throws Exception {
        String cryptocurrency = "   ";
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(String.format("/cryptocurrencies?name=%s", cryptocurrency)));
        } catch (NestedServletException e) {
            assertTrue(e.getMessage().contains("must not be blank"));
        }
    }

    @ParameterizedTest
    @MethodSource("cryptoItems")
    void getCryptoItemWithMinPriceSuccess(String cryptocurrency, String currency, double price) throws Exception {
        CryptoItem expected = new CryptoItem(cryptocurrency, currency, price, LocalDateTime.now());

        Mockito.when(cryptoItemService.getCryptoItemWithMinPrice(cryptocurrency)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/cryptocurrencies/minprice?name=%s", cryptocurrency))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.curr1", is(expected.getCurr1())))
                .andExpect(jsonPath("$.curr2", is(expected.getCurr2())))
                .andExpect(jsonPath("$.price", is(expected.getPrice())));
    }

    @Test
    void getCryptoItemWithMinPriceNotFound() throws Exception {
        String cryptocurrency = "BTCC";

        Mockito.when(cryptoItemService.getCryptoItemWithMinPrice(cryptocurrency)).thenThrow(new NoSuchElementException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/cryptocurrencies/minprice?name=%s", cryptocurrency))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCryptoItemWithMinPriceWithIncorrectInputData() throws Exception {
        String cryptocurrency = "   ";
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(String.format("/cryptocurrencies/minprice?name=%s", cryptocurrency)));
        } catch (NestedServletException e) {
            assertTrue(e.getMessage().contains("must not be blank"));
        }
    }

    @ParameterizedTest
    @MethodSource("cryptoItems")
    void getCryptoItemWithMaxPriceSuccess(String cryptocurrency, String currency, double price) throws Exception {
        CryptoItem expected = new CryptoItem(cryptocurrency, currency, price, LocalDateTime.now());

        Mockito.when(cryptoItemService.getCryptoItemWithMaxPrice(cryptocurrency)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/cryptocurrencies/maxprice?name=%s", cryptocurrency))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.curr1", is(expected.getCurr1())))
                .andExpect(jsonPath("$.curr2", is(expected.getCurr2())))
                .andExpect(jsonPath("$.price", is(expected.getPrice())));
    }

    @Test
    void getCryptoItemWithMaxnPriceNotFound() throws Exception {
        String cryptocurrency = "BTCC";

        Mockito.when(cryptoItemService.getCryptoItemWithMaxPrice(cryptocurrency)).thenThrow(new NoSuchElementException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/cryptocurrencies/maxprice?name=%s", cryptocurrency))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCryptoItemWithMaxPriceWithIncorrectInputData() throws Exception {
        String cryptocurrency = "   ";
        try {
            mockMvc.perform(MockMvcRequestBuilders.get(String.format("/cryptocurrencies/maxprice?name=%s", cryptocurrency)));
        } catch (NestedServletException e) {
            assertTrue(e.getMessage().contains("must not be blank"));
        }
    }

    @Test
    void exportIntoCSV() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cryptocurrencies/csv")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static Stream<Arguments> cryptoItems() {
        return Stream.of(
                Arguments.of(BTC, USD, 1658),
                Arguments.of(ETH, USD, 1126.63),
                Arguments.of(XRP, USD, 0.365),
                Arguments.of(XRP, USD, 9365.0),
                Arguments.of(BTC, USD, 255.1)
        );
    }

    public static Stream<Arguments> cryptoItemAndPagination() {
        return Stream.of(
                Arguments.of(BTC, USD, 0, 3),
                Arguments.of(ETH, USD, 2, 3),
                Arguments.of(ETH, USD, 3, 1),
                Arguments.of(BTC, USD, 0, 1),
                Arguments.of(XRP, USD, 1, 20)
        );
    }
}