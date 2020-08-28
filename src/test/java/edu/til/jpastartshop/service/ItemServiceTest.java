package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Item;
import edu.til.jpastartshop.repository.ItemRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceTest {
    /*
     * 상품 등록
     * 상품 조회
     * 상품 목록 조회
     * 상품 재고 수량 증가
     * 상품 재고 수량 차감
     * */

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    CategoryService categoryService;

    ItemService itemService;

    @BeforeAll
    public void init() {
        itemService = new ItemService(itemRepository, categoryService);
    }

    @Test
    public void givenItemThenReturnCreatedItem() {
        Item item = new Item(1L, "서큘레이터", 134000, 99);

        when(itemRepository.save(item)).thenReturn(item);

        Item created = itemService.create(item);

        assertNotNull(created);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void givenItemAndCategoryThenReturnCreatedItem() {
        Item item = new Item(1L, "서큘레이터", 134000, 99);
        long categoryId = 1L;

        when(itemRepository.save(item)).thenReturn(item);

        Item created = itemService.create(item, categoryId);

        assertNotNull(created);
        verify(itemRepository, times(1)).save(item);
    }



    @Test
    public void givenItemWithExistedIdThenThrowsException() {
        Item item = new Item(1L, "서큘레이터", 134000, 99);

        when(itemService.create(item)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> itemService.create(item));
    }

    @Test
    public void givenItemIdThenReturnItem() {
        long itemId = 1L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(new Item(1L, "서큘레이터", 134000, 99)));

        Item item = itemService.findById(itemId);

        assertNotNull(item);
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    public void givenWrongItemIdThenThrowsException() {
        long itemId = 0;

        assertThrows(IllegalArgumentException.class, () -> itemService.findById(itemId));
        verify(itemRepository, never()).findById(itemId);
    }

    @Test
    public void givenNotExistItemIdThenThrowsException() {
        long itemId = 99L;

        assertThrows(NoSuchElementException.class, () -> itemService.findById(itemId));
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    public void givenNotingThenReturnAllItems() {
        List<Item> newItems = Arrays.asList(
                new Item(1L, "서큘레이터", 134000, 99),
                new Item(2L, "선풍기", 56000, 1590),
                new Item(3L, "에어컨", 1560000, 20)
        );

        when(itemRepository.findAll()).thenReturn(newItems);

        List<Item> items = itemService.findAll();

        assertNotNull(items);
        assertEquals(newItems.size(), items.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    public void givenItemIdAndStockThenIncreaseStock() {
        Item newItem = new Item(1L, "서큘레이터", 134000, 99);
        int increase = 2;

        when(itemRepository.findById(newItem.getId())).thenReturn(Optional.of(newItem));

        Item item  = itemService.increaseStock(newItem.getId(), increase);

        assertNotNull(item);
        assertEquals(newItem.getStock(), item.getStock());
        verify(itemRepository, times(1)).findById(newItem.getId());
    }

    @Test
    public void givenItemIdAndStockThenDecreaseStock() {
        Item newItem = new Item(1L, "서큘레이터", 134000, 99);
        int decrease = 2;

        when(itemRepository.findById(newItem.getId())).thenReturn(Optional.of(newItem));

        Item item  = itemService.decreaseStock(newItem.getId(), decrease);

        assertNotNull(item);
        assertEquals(newItem.getStock(), item.getStock());
        verify(itemRepository, times(1)).findById(newItem.getId());
    }

    @Test
    public void givenItemIdAndHugeSizeStockDecreaseThenTrowsException() {
        Item newItem = new Item(1L, "서큘레이터", 134000, 99);
        int decrease = Integer.MAX_VALUE;

        when(itemRepository.findById(newItem.getId())).thenReturn(Optional.of(newItem));

        assertThrows(NoSuchElementException.class, () -> itemService.decreaseStock(newItem.getId(), decrease));
        verify(itemRepository, times(1)).findById(newItem.getId());
    }
}