package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Item;
import edu.til.jpastartshop.domain.Member;
import edu.til.jpastartshop.domain.Order;
import edu.til.jpastartshop.domain.OrderItem;
import edu.til.jpastartshop.domain.OrderStatus;
import edu.til.jpastartshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceTest {
    /*
     * 상품 주문
     * 주문 조회
     * 주문 취소
     * */

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    MemberService memberService;

    OrderService orderService;

    Member member = new Member(1L, "kangjoshi");

    List<Item> items = Arrays.asList(
            new Item(1L, "서큘레이터", 134000, 99),
            new Item(2L, "선풍기", 56000, 1590),
            new Item(3L, "에어컨", 1560000, 20)
    );

    private Item getItem(long id) {
        return items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Item getItem(String name) {
        return items.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @BeforeAll
    public void init() {
        orderService = new OrderService(orderRepository, memberService);
    }

    @Test
    public void givenOrderThenReturnCreatedOrder() {
        Order order = new Order(member);
        order.setId(1L);

        List<OrderItem> orderItems = Arrays.asList(
            new OrderItem(1, getItem(1), order),
            new OrderItem(2, getItem(2), order)
        );

        when(memberService.findById(member.getId())).thenReturn(member);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        long orderId = orderService.order(member.getId(), orderItems);

        assertEquals(order.getId(), orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void givenHugeSizeQuantityItemOrderThenReturnCreatedOrder() {
        Order order = new Order(member);
        order.setId(1L);

        List<OrderItem> orderItems = Arrays.asList(
            new OrderItem(Integer.MAX_VALUE, getItem(1), order)
        );

        when(memberService.findById(member.getId())).thenReturn(member);

        assertThrows(NoSuchElementException.class, () -> orderService.order(member.getId(), orderItems));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void givenOrderIdThenReturnOrder() {
        long orderId = 1L;
        Order newOrder = new Order(member);
        newOrder.setId(1L);
        newOrder.setOrderItems(Arrays.asList(
            new OrderItem(1, getItem(1), newOrder),
            new OrderItem(2, getItem(2), newOrder)
        ));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(newOrder));

        Order order = orderService.findById(orderId);

        assertNotNull(order);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void givenWrongOrderIdThenThrowsException() {
        long orderId = 0;

        assertThrows(IllegalArgumentException.class, () -> orderService.findById(orderId));
        verify(orderRepository, never()).findById(orderId);
    }

    @Test
    public void givenNotExistOrderIdThenThrowsException() {
        long orderId = 99L;

        assertThrows(NoSuchElementException.class, () -> orderService.findById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void givenOrderIdThenCancelOrder() {
        long orderId = 1L;

        Item item1 = getItem(1);
        Item item2 = getItem(2);
        int item1Stock = item1.getStock();
        int item2Stock = item2.getStock();
        int item1Quantity = 1;
        int item2Quantity = 2;

        Order newOrder = new Order(member);
        newOrder.setId(1L);
        newOrder.setOrderItems(Arrays.asList(
                new OrderItem(item1Quantity, item1, newOrder),
                new OrderItem(item2Quantity, item2, newOrder)
        ));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(newOrder));

        Order order = orderService.cancel(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertNotNull(order.getCancelDate());
        assertEquals(item1Stock+item1Quantity, getItem(1).getStock());
        assertEquals(item2Stock+item2Quantity, getItem(2).getStock());
    }

}