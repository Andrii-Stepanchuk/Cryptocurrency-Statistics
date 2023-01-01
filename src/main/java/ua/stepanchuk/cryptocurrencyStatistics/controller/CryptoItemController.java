package ua.stepanchuk.cryptocurrencyStatistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.stepanchuk.cryptocurrencyStatistics.exception.CreateCSVException;
import ua.stepanchuk.cryptocurrencyStatistics.model.CryptoItem;
import ua.stepanchuk.cryptocurrencyStatistics.service.CryptoItemService;
import ua.stepanchuk.cryptocurrencyStatistics.service.CsvExportService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static ua.stepanchuk.cryptocurrencyStatistics.util.constants.CurrencyName.*;

/**
 * Controller for endpoints according to the task
 *
 * @author Andrii Stepanchuk
 */

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

    /**
     * getCryptoItemWithMinPrice method returns record with the lowest price of selected cryptocurrency
     *
     * @param name the name of the cryptocurrency
     * @return CryptoItem and code 200 if records were found
     * in the opposite case code 404
     */

    @GetMapping("/minprice")
    public ResponseEntity<?> getCryptoItemWithMinPrice(@RequestParam @NotBlank String name) {
        try {
            CryptoItem cryptoItem = cryptoItemService.getCryptoItemWithMinPrice(name);
            return ResponseEntity.ok().body(cryptoItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * getCryptoItemWithMaxPrice method returns record with the highest price of selected cryptocurrency
     *
     * @param name the name of the cryptocurrency
     * @return CryptoItem and code 200 if records were found
     * in the opposite case code 404
     */

    @GetMapping("/maxprice")
    public ResponseEntity<?> getCryptoItemWithMaxPrice(@RequestParam @NotBlank String name) {
        try {
            CryptoItem cryptoItem = cryptoItemService.getCryptoItemWithMaxPrice(name);
            return ResponseEntity.ok().body(cryptoItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * getCryptoItemsByPagination method returns a selected page with selected number of elements
     * and sorted by price from lowest to highest
     *
     * @param name the name of the cryptocurrency
     * @param page page number of records (optional - default = 0)
     * @param size page size of records (optional - default = 10)
     * @return List<CryptoItem> and code 200 if records were found
     * in the opposite case code 404
     */

    @GetMapping()
    public ResponseEntity<?> getCryptoItemsByPagination(@RequestParam @NotBlank String name,
                                                        @RequestParam(defaultValue = "0")  @Min(0) Integer page,
                                                        @RequestParam(defaultValue = "10") @Min(1) Integer size) {

        List<CryptoItem> cryptoItems = cryptoItemService.getCryptoItemsByPagination(name, page, size);
        return !cryptoItems.isEmpty() ?
                ResponseEntity.ok().body(cryptoItems) :
                ResponseEntity.notFound().build();
    }

    /**
     * exportIntoCSV method creates a csv report with the following fields: Cryptocurrency Name, Min Price, Max Price.
     * the report has only three records according to three different cryptocurrencies
     *
     * @param response provide HTTP-specific functionality in sending a response. For example, it has methods to access
     * HTTP headers and cookies.
     *
     * @return CryptoItem and code 200 if records were found
     * in the opposite case code 404
     */

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
