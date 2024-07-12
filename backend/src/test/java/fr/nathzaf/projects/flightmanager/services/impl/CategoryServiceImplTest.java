package fr.nathzaf.projects.flightmanager.services.impl;

import fr.nathzaf.projects.flightmanager.exceptions.alreadyexists.CategoryNameAlreadyExistsException;
import fr.nathzaf.projects.flightmanager.exceptions.notfound.CategoryNotFoundException;
import fr.nathzaf.projects.flightmanager.models.Category;
import fr.nathzaf.projects.flightmanager.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;


    @Test
    public void testGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Test Category"));
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Category", result.getFirst().getName());
    }

    @Test
    public void testGetCategoryById() throws CategoryNotFoundException {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category("Test Category");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.getById(categoryId);

        assertEquals("Test Category", result.getName());
    }

    @Test
    public void testCreateCategory_Success() throws CategoryNameAlreadyExistsException {
        String categoryName = "Test Category";
        when(categoryRepository.findCategoriesByName(categoryName)).thenReturn(new ArrayList<>());
        when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.create(categoryName);

        assertNotNull(result);
        assertEquals(categoryName, result.getName());
    }

    @Test
    public void testCreateCategory_AlreadyExists() {
        String categoryName = "Test Category";
        List<Category> existingCategories = new ArrayList<>();
        existingCategories.add(new Category(categoryName));
        when(categoryRepository.findCategoriesByName(categoryName)).thenReturn(existingCategories);

        assertThrows(CategoryNameAlreadyExistsException.class, () -> categoryService.create(categoryName));
    }

    @Test
    public void testUpdateCategory_Success() throws CategoryNotFoundException, CategoryNameAlreadyExistsException {
        UUID categoryId = UUID.randomUUID();
        String newName = "Updated Category";
        Category category = new Category("Test Category");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.findCategoriesByName(newName)).thenReturn(new ArrayList<>());
        when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.update(categoryId, newName);

        assertEquals(newName, result.getName());
    }

    @Test
    public void testUpdateCategory_NotFound() {
        UUID categoryId = UUID.randomUUID();
        String newName = "Updated Category";
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.update(categoryId, newName));
    }

    @Test
    public void testUpdateCategory_NameAlreadyExists() {
        UUID categoryId = UUID.randomUUID();
        String newName = "Updated Category";
        List<Category> existingCategories = new ArrayList<>();
        existingCategories.add(new Category(newName));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category("Test Category")));
        when(categoryRepository.findCategoriesByName(newName)).thenReturn(existingCategories);

        assertThrows(CategoryNameAlreadyExistsException.class, () -> categoryService.update(categoryId, newName));
    }

    @Test
    public void testDeleteCategory_Success() throws CategoryNotFoundException {
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        categoryService.delete(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    public void testDeleteCategory_NotFound() {
        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.delete(categoryId));
    }
}