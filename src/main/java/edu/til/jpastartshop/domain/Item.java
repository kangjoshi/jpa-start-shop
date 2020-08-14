package edu.til.jpastartshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.NoSuchElementException;

@Entity
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stock;

    public Item() {
    }

    public Item(Long id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void increaseStock(int increase) {
        this.stock += increase;
    }

    public void decreaseStock(int decrease) {
        int remain = this.stock -= decrease;

        if (remain < 0) {
            throw new NoSuchElementException("재고가 없습니다.");
        }

        this.stock = remain;
    }

}
