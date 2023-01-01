package ua.stepanchuk.ToDoApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * CryptoItem is an entity that represents an object from the database
 *
 * @author Andrii Stepanchuk
 */

@Document("cryptocurrency")
public class CryptoItem {

    @Id
    private String id;
    private String curr1;
    private String curr2;
    private double price;
    private LocalDateTime createdAt;

    public CryptoItem() {}

    public CryptoItem(String curr1, String curr2, double price, LocalDateTime createdAt) {
        this.curr1 = curr1;
        this.curr2 = curr2;
        this.price = price;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurr1() {
        return curr1;
    }

    public void setCurr1(String curr1) {
        this.curr1 = curr1;
    }

    public String getCurr2() {
        return curr2;
    }

    public void setCurr2(String curr2) {
        this.curr2 = curr2;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoItem that = (CryptoItem) o;
        return Double.compare(that.price, price) == 0 && Objects.equals(id, that.id) && Objects.equals(curr1, that.curr1) && Objects.equals(curr2, that.curr2) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, curr1, curr2, price, createdAt);
    }

    @Override
    public String toString() {
        return "CryptoItem{" +
                "id='" + id + '\'' +
                ", curr1='" + curr1 + '\'' +
                ", curr2='" + curr2 + '\'' +
                ", lastPrice=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}
