package com.volunteer.api.data.user.service.impl;

import com.volunteer.api.data.user.model.persistence.Category;
import com.volunteer.api.data.user.repository.CategoryRepository;
import com.volunteer.api.data.user.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Collection<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> get(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        if (category.getPath() == null) {
            category.setPath("<empty>");
        }
        category = categoryRepository.save(category);
        updatePath(category);
        return categoryRepository.save(category);
    }

    @Override
    public Collection<Category> getRootCategories() {
        return categoryRepository.findAllByParentIdNull();
    }

    @Override
    public Collection<Category> getByParentId(Integer parentId) {
        return categoryRepository.findAllByParentId(parentId);
    }

    @Override
    public Collection<Category> getByNameStartedWith(String name) {
        return categoryRepository.findAllByNameLike(StringUtils.defaultString(name) + "%");
    }

    private void updatePath(Category category) {
        StringBuilder path = new StringBuilder();
        path.append("/").append(category.getId());
        Category cat = category.getParentCategory();
        while(cat != null) {
            path.insert(0, "/" + cat.getId());
            cat = cat.getParentCategory();
        }

        category.setPath(path.toString());
    }
}
