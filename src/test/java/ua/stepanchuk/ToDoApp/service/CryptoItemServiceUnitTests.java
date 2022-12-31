package ua.stepanchuk.ToDoApp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.stepanchuk.ToDoApp.model.CryptoItem;
import ua.stepanchuk.ToDoApp.repository.CryptoItemRepository;
import ua.stepanchuk.ToDoApp.service.impl.CryptoItemServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.stepanchuk.ToDoApp.util.constants.CurrencyName.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CryptoItemServiceUnitTest {

    @Mock
    public CryptoItemRepository cryptoItemRepo;

    @InjectMocks
    private CryptoItemServiceImpl cryptoItemService;

    @ParameterizedTest
    @MethodSource("prices")
    void getMinPriceTest(double price, String cryptocurrency) {
        CryptoItem expected = new CryptoItem();
        expected.setPrice(price);

        Mockito.when(cryptoItemRepo.findTopByCurr1OrderByPriceAsc(cryptocurrency))
                .thenReturn(Optional.of(expected));

        CryptoItem actual = cryptoItemService.getCryptoItemWithMinPrice(cryptocurrency);

        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void getNotFoundCryptoItemMinPriceTest() {
        try {
            cryptoItemService.getCryptoItemWithMinPrice(BTC);
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("prices")
    void getMaxPriceTest(double price, String cryptocurrency) {
        CryptoItem expected = new CryptoItem();
        expected.setPrice(price);

        Mockito.when(cryptoItemRepo.findTopByCurr1OrderByPriceDesc(cryptocurrency))
                .thenReturn(Optional.of(expected));

        CryptoItem actual = cryptoItemService.getCryptoItemWithMaxPrice(cryptocurrency);

        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void getNotFoundCryptoItemMaxPriceTest() {
        try {
            cryptoItemService.getCryptoItemWithMaxPrice(BTC);
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("cryptoItemAndPagination")
    void getCryptoItemsByPagination(String cryptocurrency, String currency, int page, int size) {

        List<CryptoItem> expected = IntStream
                .range(0, size)
                .mapToObj(i -> new CryptoItem(cryptocurrency, currency, i, LocalDateTime.now()))
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        Mockito.when(cryptoItemRepo.findCryptoItemsByCurr1OrderByPriceAsc(cryptocurrency, pageable))
                .thenReturn(expected);

        List<CryptoItem> actual = cryptoItemService.getCryptoItemsByPagination(cryptocurrency, page, size);

        assertEquals(expected, actual);
    }

    public static Stream<Arguments> prices() {
        return Stream.of(
                Arguments.of(11111.1, BTC),
                Arguments.of(9999999, ETH),
                Arguments.of(10,      XRP),
                Arguments.of(0,       XRP),
                Arguments.of(255.0,   BTC)
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