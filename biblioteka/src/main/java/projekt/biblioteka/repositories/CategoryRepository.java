package projekt.biblioteka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByParentCategoryEqualsOrderByNameAsc(Category parent);
//    List<Category> findAllB
    Category findByName(String name);
    boolean existsByName(String name);
}
