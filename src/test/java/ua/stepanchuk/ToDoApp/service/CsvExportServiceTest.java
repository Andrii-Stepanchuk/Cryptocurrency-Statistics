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
import ua.stepanchuk.ToDoApp.exception.CreateCSVException;
import ua.stepanchuk.ToDoApp.model.CryptoItem;
import ua.stepanchuk.ToDoApp.repository.CryptoItemRepository;
import ua.stepanchuk.ToDoApp.service.impl.CsvExportServiceImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ua.stepanchuk.ToDoApp.util.constants.CurrencyName.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CsvExportServiceTest {

    @Mock
    public CryptoItemRepository cryptoItemRepo;

    @InjectMocks
    private CsvExportServiceImpl csvExportService;

    private final String FILE_PATH = "src/test/resources/csv.txt";

    @ParameterizedTest
    @MethodSource("cryptocurrency")
    void writeCryptoItemsToCsv(String cryptocurrency, double minPrice, double maxPrice) throws IOException, CreateCSVException {
        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {

            CryptoItem expectedMin = new CryptoItem(cryptocurrency, USD, minPrice, LocalDateTime.now());
            CryptoItem expectedMax = new CryptoItem(cryptocurrency, USD, maxPrice, LocalDateTime.now());

            Mockito.when(cryptoItemRepo.findTopByCurr1OrderByPriceAsc(cryptocurrency))
                    .thenReturn(Optional.of(expectedMin));

            Mockito.when(cryptoItemRepo.findTopByCurr1OrderByPriceDesc(cryptocurrency))
                    .thenReturn(Optional.of(expectedMax));

            csvExportService.writeCryptoItemsToCsv(List.of(cryptocurrency), writer);
        }

        List<String> linesFromFile = Files.readAllLines(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
        String lines = String.join("\n", linesFromFile);

        assertTrue(lines.contains(String.format(Locale.ROOT,"%s,%.1f,%.1f", cryptocurrency, minPrice, maxPrice)));
    }

    @Test
    void writeCryptoItemsToCsvThrowCreateCSVException() throws FileNotFoundException {
        try(PrintWriter writer = new PrintWriter(FILE_PATH)) {
            csvExportService.writeCryptoItemsToCsv(List.of(BTC), writer);
        } catch (CreateCSVException e) {
            assertTrue(e.getMessage().contains("No value present"));
        }
    }

    public static Stream<Arguments> cryptocurrency() {
        return Stream.of(
                Arguments.of(BTC, 16955.3, 1836),
                Arguments.of(ETH, 1158, 1231.0),
                Arguments.of(XRP, 0.5, 1.2)
        );
    }
}