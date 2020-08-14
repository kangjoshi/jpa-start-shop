package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Item;
import edu.til.jpastartshop.domain.Member;
import edu.til.jpastartshop.domain.Order;
import edu.til.jpastartshop.domain.OrderItem;
import edu.til.jpastartshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    OrderRepository orderRepository;
    MemberService memberService;

    public OrderService(OrderRepository orderRepository, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
    }

    public long order(long memberId, List<OrderItem> orderItems) {
        Member member = memberService.findById(memberId);
        Order order = new Order(member);
        order.setOrderItems(orderItems);

        try {
            order.validate();
            return orderRepository.save(order).getId();
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public Order findById(long orderId) {
        if (orderId == 0) {
            throw new IllegalArgumentException("잘못된 형식의 주문 아이디입니다.");
        }

        return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
    }

    public Order cancel(long orderId) {
        Order order = findById(orderId);

        try {
            order.cancel();
            return order;
        } catch (Exception e) {
            throw e;
        }
    }
}
