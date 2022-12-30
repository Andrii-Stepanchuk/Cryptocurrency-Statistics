package ua.stepanchuk.ToDoApp.service;

import ua.stepanchuk.ToDoApp.exception.CreateCSVException;

import java.io.Writer;
import java.util.List;

public interface CsvExportService {
    void writeCryptoItemsToCsv(List<String> cryptocurrencies, Writer writer) throws CreateCSVException;
}
