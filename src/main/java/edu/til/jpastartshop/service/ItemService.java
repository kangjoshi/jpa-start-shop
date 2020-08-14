package edu.til.jpastartshop.service;

import edu.til.jpastartshop.domain.Item;
import edu.til.jpastartshop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemService {

    ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(Item item) {
        if (itemRepository.findById(item.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        return itemRepository.save(item);
    }

    public Item findById(long itemId) {
        if (itemId == 0) {
            throw new IllegalArgumentException("잘못된 형식의 상품 아이디입니다.");
        }

        return itemRepository.findById(itemId).orElseThrow(NoSuchElementException::new);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item increaseStock(long id, int increase) {
        Item item = findById(id);
        item.increaseStock(increase);
        return item;
    }

    public Item decreaseStock(Long id, int decrease) {
        Item item = findById(id);
        item.decreaseStock(decrease);
        return item;
    }


}
