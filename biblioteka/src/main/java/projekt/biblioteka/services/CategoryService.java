package projekt.biblioteka.services;

import org.springframework.stereotype.Service;
import projekt.biblioteka.models.Category;
import projekt.biblioteka.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllWithoutParent(){
        return categoryRepository.findAllByParentCategoryEqualsOrderByNameAsc(null);
    }

    public Category getCategoryById(int id){
        return categoryRepository.findById(id).orElse(null);
    }
    public Category getCategoryByName(String name){
        return categoryRepository.findByName(name);
    }
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public boolean existsByName(String name){
        return categoryRepository.existsByName(name);
    }
}
