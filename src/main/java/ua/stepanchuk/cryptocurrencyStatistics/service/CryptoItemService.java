package ua.stepanchuk.cryptocurrencyStatistics.service;

import ua.stepanchuk.cryptocurrencyStatistics.model.CryptoItem;

import java.util.List;

/**
 * CryptoItemService interface
 *
 * @author Andrii Stepanchuk
 */

public interface CryptoItemService {
    CryptoItem getCryptoItemWithMinPrice(String currency);
    CryptoItem getCryptoItemWithMaxPrice(String currency);
    List<CryptoItem> getCryptoItemsByPagination(String currency, int page, int size);
}
