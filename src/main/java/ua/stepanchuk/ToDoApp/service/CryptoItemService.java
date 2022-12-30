package ua.stepanchuk.ToDoApp.service;

import ua.stepanchuk.ToDoApp.model.CryptoItem;

import java.util.List;

public interface CryptoItemService {
    CryptoItem getCryptoItemWithMinPrice(String currency);
    CryptoItem getCryptoItemWithMaxPrice(String currency);
    List<CryptoItem> getCryptoItemsByPagination(String currency, int page, int size);
}
