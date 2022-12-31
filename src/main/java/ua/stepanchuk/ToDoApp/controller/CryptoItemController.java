package ua.stepanchuk.ToDoApp.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.stepanchuk.ToDoApp.exception.CreateCSVException;
import ua.stepanchuk.ToDoApp.model.CryptoItem;
import ua.stepanchuk.ToDoApp.service.CryptoItemService;
import ua.stepanchuk.ToDoApp.service.CsvExportService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static ua.stepanchuk.ToDoApp.util.constants.CurrencyName.*;

@Validated
@RestController
@RequestMapping("/cryptocurrencies")
public class CryptoItemController {

    private final CryptoItemService cryptoItemService;
    private final CsvExportService csvExportService;

    @Autowired
    public CryptoItemController(CryptoItemService cryptoItemService, CsvExportService csvExportService) {
        this.cryptoItemService = cryptoItemService;
        this.csvExportService = csvExportService;
    }

    @GetMapping()
    public ResponseEntity<?> getCryptoItemsByPagination(@RequestParam @NotBlank String name,
                                                        @RequestParam(defaultValue = "0")  @Min(0) Integer page,
                                                        @RequestParam(defaultValue = "10") @Min(1) Integer size) {

        List<CryptoItem> cryptoItems = cryptoItemService.getCryptoItemsByPagination(name, page, size);
        return !cryptoItems.isEmpty() ?
                ResponseEntity.ok().body(cryptoItems) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/minprice")
    public ResponseEntity<?> getCryptoItemWithMinPrice(@RequestParam @NotBlank String name) {
        try {
            CryptoItem cryptoItem = cryptoItemService.getCryptoItemWithMinPrice(name);
            return ResponseEntity.ok().body(cryptoItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/maxprice")
    public ResponseEntity<?> getCryptoItemWithMaxPrice(@RequestParam @NotBlank String name) {
        try {
            CryptoItem cryptoItem = cryptoItemService.getCryptoItemWithMaxPrice(name);
            return ResponseEntity.ok().body(cryptoItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/csv")
    public ResponseEntity<?> exportIntoCSV(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"Cryptocurrency_report.csv\"");

        try {
            csvExportService.writeCryptoItemsToCsv(List.of(BTC, ETH, XRP), response.getWriter());
            return ResponseEntity.ok().build();
        } catch (IOException | CreateCSVException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
