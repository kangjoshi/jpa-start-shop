package edu.til.jpastartshop.repository;

import edu.til.jpastartshop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
