package edu.til.jpastartshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Date orderDate;

    private Date cancelDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
    }

    public Order(Member member) {
        this(member, new Date());
    }

    public Order(Member member, Date orderDate) {
        this.member = member;
        this.orderDate = orderDate;
        this.status = OrderStatus.READY;
    }

    public void validate() throws NoSuchElementException {
        try {
            for (OrderItem orderItem : orderItems) {
                Item item = orderItem.getItem();
                item.decreaseStock(orderItem.getQuantity());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public void cancel() {
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            item.increaseStock(orderItem.getQuantity());
        }
        this.status = OrderStatus.CANCEL;
        this.cancelDate = new Date();
    }
}
