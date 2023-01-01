package ua.stepanchuk.cryptocurrencyStatistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.stepanchuk.cryptocurrencyStatistics.model.CryptoItem;
import ua.stepanchuk.cryptocurrencyStatistics.repository.CryptoItemRepository;
import ua.stepanchuk.cryptocurrencyStatistics.service.CryptoItemService;
import ua.stepanchuk.cryptocurrencyStatistics.util.JsonUtil;
import ua.stepanchuk.cryptocurrencyStatistics.util.RequestSender;

import javax.annotation.PostConstruct;
import java.util.List;

import static ua.stepanchuk.cryptocurrencyStatistics.util.constants.CurrencyName.*;

/**
 * CryptoItemServiceImpl is a service class that interacts with the repository
 *
 * @author Andrii Stepanchuk
 */

@Service
public class CryptoItemServiceImpl implements CryptoItemService {

    public CryptoItemRepository cryptoItemRepository;

    @Autowired
    public CryptoItemServiceImpl(CryptoItemRepository cryptoItemRepository) {
        this.cryptoItemRepository = cryptoItemRepository;
    }

    /**
     * getCryptoItemWithMinPrice method returns record with the lowest price of selected cryptocurrency
     *
     * @param currency the name of the cryptocurrency
     * @return CryptoItem if records were found
     * in the opposite case throw NoSuchElementException
     */

    @Override
    public CryptoItem getCryptoItemWithMinPrice(String currency) {
        return cryptoItemRepository
                .findTopByCurr1OrderByPriceAsc(currency)
                .orElseThrow();
    }

    /**
     * getCryptoItemWithMaxPrice method returns record with the highest price of selected cryptocurrency
     *
     * @param currency the name of the cryptocurrency
     * @return CryptoItem if records were found
     * in the opposite case throw NoSuchElementException
     */

    @Override
    public CryptoItem getCryptoItemWithMaxPrice(String currency) {
        return cryptoItemRepository
                .findTopByCurr1OrderByPriceDesc(currency)
                .orElseThrow();
    }

    /**
     * getCryptoItemsByPagination method returns a selected page with selected number of elements
     * and sorted by price from lowest to highest
     *
     * @param currency the name of the cryptocurrency
     * @param page page number of records
     * @param size page size of records
     * @return List<CryptoItem> filled with data if records were found
     * in the opposite case an empty list
     */

    @Override
    public List<CryptoItem> getCryptoItemsByPagination(String currency, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cryptoItemRepository.findCryptoItemsByCurr1OrderByPriceAsc(currency, pageable);
    }

    /**
     * special method what fetch cryptocurrency data prices from CEX.IO.
     * It pull last prices for the following pairs: BTC/USD, ETH/USD and XRP/USD
     * and stores all data in the mongodb database
     *
     * If you do not want new data to be added to the database, comment this method
     */

    @PostConstruct
    private void dataBaseInit() {
        String jsonBTC = RequestSender.sendHttpPostRequestPriceStatsOnCexIO(BTC, USD);
        List<CryptoItem> cryptoBTCItems = JsonUtil.JsonToCryptoItems(BTC, USD, jsonBTC);
        cryptoItemRepository.saveAll(cryptoBTCItems);

        String jsonETH = RequestSender.sendHttpPostRequestPriceStatsOnCexIO(ETH, USD);
        List<CryptoItem> cryptoETHItems = JsonUtil.JsonToCryptoItems(ETH, USD, jsonETH);
        cryptoItemRepository.saveAll(cryptoETHItems);

        String jsonXRP = RequestSender.sendHttpPostRequestPriceStatsOnCexIO(XRP, USD);
        List<CryptoItem> cryptoXRPItems = JsonUtil.JsonToCryptoItems(XRP, USD, jsonXRP);
        cryptoItemRepository.saveAll(cryptoXRPItems);
    }
}
