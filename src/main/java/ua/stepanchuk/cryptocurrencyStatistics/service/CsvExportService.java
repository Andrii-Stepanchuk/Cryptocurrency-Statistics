package ua.stepanchuk.cryptocurrencyStatistics.service;

import ua.stepanchuk.cryptocurrencyStatistics.exception.CreateCSVException;

import java.io.Writer;
import java.util.List;

/**
 * CsvExportService interface
 *
 * @author Andrii Stepanchuk
 */

public interface CsvExportService {
    void writeCryptoItemsToCsv(List<String> cryptocurrencies, Writer writer) throws CreateCSVException;
}
