package ua.stepanchuk.ToDoApp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.stepanchuk.ToDoApp.model.CryptoItem;

import java.util.List;
import java.util.Optional;

/**
 * CryptoItemRepository interface
 *
 * @author Andrii Stepanchuk
 */

public interface CryptoItemRepository extends MongoRepository<CryptoItem, String> {

    Optional<CryptoItem> findTopByCurr1OrderByPriceAsc(String curr1);

    Optional<CryptoItem> findTopByCurr1OrderByPriceDesc(String curr1);

    List<CryptoItem> findCryptoItemsByCurr1OrderByPriceAsc(String curr1, Pageable pageable);
}