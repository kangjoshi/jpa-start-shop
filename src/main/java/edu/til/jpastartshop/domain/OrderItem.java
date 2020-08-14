package edu.til.jpastartshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ITEM_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    public OrderItem() {
    }

    public OrderItem(int quantity, Item item, Order order) {
        this.quantity = quantity;
        this.item = item;
        // TODO NPE 위험
        this.price = this.quantity * this.item.getPrice();
        this.order = order;
    }


}
