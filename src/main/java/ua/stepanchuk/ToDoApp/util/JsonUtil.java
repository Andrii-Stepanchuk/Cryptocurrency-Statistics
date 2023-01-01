package ua.stepanchuk.ToDoApp.util;

import org.json.JSONArray;
import org.json.JSONObject;
import ua.stepanchuk.ToDoApp.model.CryptoItem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


/**
 * JsonUtil is an util class to work with json
 *
 * @author Andrii Stepanchuk
 */

public final class JsonUtil {

    private static final String PRICE = "price";
    private static final String TMSP = "tmsp";


    /**
     * JsonToCryptoItems method converts a string written in ion format into objects CryptoItem
     *
     * @param curr1 the first cryptocurrency
     * @param curr2 the second cryptocurrency
     * @return List<CryptoItem> a list of CryptoItem objects
     */

    public static List<CryptoItem> JsonToCryptoItems(String curr1, String curr2, String json) {
        List<CryptoItem> cryptoItems = new ArrayList<>();

        JSONArray jsonarray = new JSONArray(json);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            cryptoItems.add(createItem(curr1, curr2, jsonobject));
        }

        return cryptoItems;
    }

    /**
     * createItem method create an createItem object
     *
     * @param curr1 the first cryptocurrency
     * @param curr2 the second cryptocurrency
     * @return CryptoItem object
     */

    private static CryptoItem createItem(String curr1, String curr2, JSONObject jsonobject) {
        double price = Double.parseDouble(jsonobject.getString(PRICE));
        LocalDateTime createdAt = timestampToLocalDateTime(jsonobject.getLong(TMSP));

        return new CryptoItem(curr1, curr2, price, createdAt);
    }

    /**
     * timestampToLocalDateTime method converts timestamp to LocalDateTime
     *
     * @param tmsp timestamp in seconds
     * @return LocalDateTime
     */

    private static LocalDateTime timestampToLocalDateTime(long tmsp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(tmsp), ZoneId.systemDefault());
    }
}
