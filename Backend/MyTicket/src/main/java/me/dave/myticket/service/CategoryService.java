package me.dave.myticket.service;

import me.dave.myticket.dto.CategoryCreateDto;
import me.dave.myticket.dto.CategoryResponseDto;
import me.dave.myticket.dto.CategoryUpdateDto;
import me.dave.myticket.model.Category;
import me.dave.myticket.repository.CategoryRepository;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {
    private final CategoryRepository repository;
    
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }
    
    public static Category map(CategoryCreateDto in) {
        if (in == null) {
            return null;
        }

        Category category = new Category();
        category.setName(in.name());
        category.setPrice(in.price());
        category.setStock(in.stock());

        return category;
    }

    public static Category map(CategoryUpdateDto in) {
        if (in == null) {
            return null;
        }

        Category category = new Category();
        category.setId(in.id());
        category.setName(in.name());
        category.setPrice(in.price());
        category.setStock(in.stock());

        return category;
    }

    public static CategoryResponseDto map(Category in) {
        if (in == null) {
            return null;
        }
        
        return new CategoryResponseDto(
            in.getId(),
            in.getName(),
            in.getPrice(),
            in.getStock()
        );
    }
    
    public Category save(Category category) {
        return repository.save(category);
    }
}
