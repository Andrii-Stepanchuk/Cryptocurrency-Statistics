package ua.stepanchuk.ToDoApp.util;

import org.json.JSONArray;
import org.json.JSONObject;
import ua.stepanchuk.ToDoApp.model.CryptoItem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class JsonUtil {

    private static final String PRICE = "price";
    private static final String TMSP = "tmsp";

    public static List<CryptoItem> JsonToCryptoItem(String curr1, String curr2, String json) {
        List<CryptoItem> cryptoItems = new ArrayList<>();

        JSONArray jsonarray = new JSONArray(json);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            cryptoItems.add(createItem(curr1, curr2, jsonobject));
        }

        return cryptoItems;
    }

    private static CryptoItem createItem(String curr1, String curr2, JSONObject jsonobject) {
        double price = Double.parseDouble(jsonobject.getString(PRICE));
        LocalDateTime createdAt = timestampToLocalDateTime(jsonobject.getLong(TMSP));

        return new CryptoItem(curr1, curr2, price, createdAt);
    }

    private static LocalDateTime timestampToLocalDateTime(long tmsp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(tmsp), ZoneId.systemDefault());
    }
}
