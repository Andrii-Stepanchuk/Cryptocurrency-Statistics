package ua.stepanchuk.cryptocurrencyStatistics.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.stepanchuk.cryptocurrencyStatistics.exception.CreateCSVException;
import ua.stepanchuk.cryptocurrencyStatistics.model.CryptoItem;
import ua.stepanchuk.cryptocurrencyStatistics.repository.CryptoItemRepository;
import ua.stepanchuk.cryptocurrencyStatistics.service.CsvExportService;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * CsvExportServiceImpl is a service class that interacts with the repository for сryptocurrency report
 *
 * @author Andrii Stepanchuk
 */

@Service
public class CsvExportServiceImpl implements CsvExportService {

    private CryptoItemRepository cryptoItemRepository;

    @Autowired
    public CsvExportServiceImpl(CryptoItemRepository cryptoItemRepository) {
        this.cryptoItemRepository = cryptoItemRepository;
    }

    /**
     * writeCryptoItemsToCsv method creates a csv report with the following fields:
     * Cryptocurrency Name, Min Price, Max Price.
     *
     * @param cryptocurrencies list of cryptocurrencies for which a report is required
     * @param writer any child class Writer that will generate a report to a file
     * @throws CreateCSVException if an error occurred while generating the report
     */

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
            throw new CreateCSVException(e);
        }
    }
}
