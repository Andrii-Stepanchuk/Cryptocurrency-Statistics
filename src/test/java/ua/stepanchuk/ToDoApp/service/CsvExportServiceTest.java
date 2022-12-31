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

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ua.stepanchuk.ToDoApp.util.constants.CurrencyName.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CsvExportServiceTest {

    @Mock
    public CryptoItemRepository cryptoItemRepo;

    @InjectMocks
    private CsvExportServiceImpl csvExportService;

//    @ParameterizedTest
//    @MethodSource("cryptocurrency")
    @Test
    void writeCryptoItemsToCsv() throws IOException, CreateCSVException {


       try (Writer writer = new FileWriter("csv.txt")){

           CryptoItem expected = new CryptoItem(BTC, USD, 1, LocalDateTime.now());

           Mockito.when(cryptoItemRepo.findTopByCurr1OrderByPriceAsc(BTC))
                   .thenReturn(Optional.of(expected));

           Mockito.when(cryptoItemRepo.findTopByCurr1OrderByPriceDesc(BTC))
                   .thenReturn(Optional.of(expected));

           csvExportService.writeCryptoItemsToCsv(List.of(BTC), writer);
           System.out.println(writer);
       }
    }

    public static Stream<Arguments> cryptocurrency() {
        return Stream.of(
                Arguments.of(BTC),
                Arguments.of(ETH),
                Arguments.of(XRP)
        );
    }
}