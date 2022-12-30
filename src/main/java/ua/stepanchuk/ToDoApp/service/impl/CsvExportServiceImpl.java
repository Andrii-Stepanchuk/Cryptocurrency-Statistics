package ua.stepanchuk.ToDoApp.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.stepanchuk.ToDoApp.exception.CreateCSVException;
import ua.stepanchuk.ToDoApp.model.CryptoItem;
import ua.stepanchuk.ToDoApp.repository.CryptoItemRepository;
import ua.stepanchuk.ToDoApp.service.CsvExportService;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CsvExportServiceImpl implements CsvExportService {

    private final CryptoItemRepository cryptoItemRepository;

    @Autowired
    public CsvExportServiceImpl(CryptoItemRepository cryptoItemRepository) {
        this.cryptoItemRepository = cryptoItemRepository;
    }

    public void writeCryptoItemsToCsv(List<String> cryptocurrencies, Writer writer) throws CreateCSVException {
        try {
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            printer.printRecord("Cryptocurrency Name", "Min Price", "Max Price");
            for (String currency : cryptocurrencies) {
                CryptoItem minPrice = cryptoItemRepository.findTopByCurr1OrderByPriceAsc(currency).orElseThrow();
                CryptoItem maxPrice = cryptoItemRepository.findTopByCurr1OrderByPriceDesc(currency).orElseThrow();
                printer.printRecord(currency, minPrice.getPrice(), maxPrice.getPrice());
            }
        } catch (IOException | NoSuchElementException e){
            throw new CreateCSVException();
        }
    }
}
