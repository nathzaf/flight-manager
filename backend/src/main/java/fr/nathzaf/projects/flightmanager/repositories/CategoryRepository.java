package fr.nathzaf.projects.flightmanager.repositories;

import fr.nathzaf.projects.flightmanager.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findCategoriesByName(String name);
}

