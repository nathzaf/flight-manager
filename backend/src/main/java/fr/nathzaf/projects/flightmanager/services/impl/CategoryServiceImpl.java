package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.CategoryNameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.CategoryNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Category;
import fr.nathzaf.projects.flightmanager.repositories.CategoryRepository;
import fr.nathzaf.projects.flightmanager.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(UUID id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category create(String name) throws CategoryNameAlreadyExistsException {
        if (categoryRepository.findCategoriesByName(name).isEmpty()) {
            Category category = new Category(name);
            return categoryRepository.save(category);
        } else throw new CategoryNameAlreadyExistsException(name);
    }

    @Override
    public Category update(UUID id, String newName) throws CategoryNotFoundException, CategoryNameAlreadyExistsException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        if (categoryRepository.findCategoriesByName(newName).isEmpty()) {
            category.setName(newName);
            categoryRepository.save(category);
            return category;
        } else throw new CategoryNameAlreadyExistsException(newName);
    }

    @Override
    public void delete(UUID id) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(id))
            throw new CategoryNotFoundException(id);
        categoryRepository.deleteById(id);
    }
}
