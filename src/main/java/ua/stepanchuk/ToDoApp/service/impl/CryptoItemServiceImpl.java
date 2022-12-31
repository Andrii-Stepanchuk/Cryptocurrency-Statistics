package ua.stepanchuk.ToDoApp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.stepanchuk.ToDoApp.model.CryptoItem;
import ua.stepanchuk.ToDoApp.repository.CryptoItemRepository;
import ua.stepanchuk.ToDoApp.service.CryptoItemService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class CryptoItemServiceImpl implements CryptoItemService {

    public CryptoItemRepository cryptoItemRepository;

    @Autowired
    public CryptoItemServiceImpl(CryptoItemRepository cryptoItemRepository) {
        this.cryptoItemRepository = cryptoItemRepository;
    }

    @Override
    public CryptoItem getCryptoItemWithMinPrice(String currency) {
        return cryptoItemRepository
                .findTopByCurr1OrderByPriceAsc(currency)
                .orElseThrow();
    }

    @Override
    public CryptoItem getCryptoItemWithMaxPrice(String currency) {
        return cryptoItemRepository
                .findTopByCurr1OrderByPriceDesc(currency)
                .orElseThrow();
    }

    @Override
    public List<CryptoItem> getCryptoItemsByPagination(String currency, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cryptoItemRepository.findCryptoItemsByCurr1OrderByPriceAsc(currency, pageable);
    }


    @PostConstruct
    private void dataBaseInit() {
//        String jsonBTC = RequestSender.sendHttpPostRequest(BTC, USD);
//        List<CryptoItem> cryptoBTCItems = JsonUtil.JsonToCryptoItems(BTC, USD, jsonBTC);
//        cryptoItemRepository.saveAll(cryptoBTCItems);
//
//        String jsonETH = RequestSender.sendHttpPostRequest(ETH, USD);
//        List<CryptoItem> cryptoETHItems = JsonUtil.JsonToCryptoItems(ETH, USD, jsonETH);
//        cryptoItemRepository.saveAll(cryptoETHItems);
//
//        String jsonXRP = RequestSender.sendHttpPostRequest(XRP, USD);
//        List<CryptoItem> cryptoXRPItems = JsonUtil.JsonToCryptoItems(XRP, USD, jsonXRP);
//        cryptoItemRepository.saveAll(cryptoXRPItems);
    }

}
