package edu.til.jpastartshop.repository;

import edu.til.jpastartshop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
