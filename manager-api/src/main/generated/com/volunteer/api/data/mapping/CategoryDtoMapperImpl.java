package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.CategoryDtoV1;
import com.volunteer.api.data.model.api.CategoryDtoV1.CategoryDtoV1Builder;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Category.CategoryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-15T17:27:08+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class CategoryDtoMapperImpl implements CategoryDtoMapper {

    @Override
    public Collection<CategoryDtoV1> map(Collection<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        Collection<CategoryDtoV1> collection = new ArrayList<CategoryDtoV1>( categories.size() );
        for ( Category category : categories ) {
            collection.add( map( category ) );
        }

        return collection;
    }

    @Override
    public CategoryDtoV1 map(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDtoV1Builder categoryDtoV1 = CategoryDtoV1.builder();

        categoryDtoV1.id( category.getId() );
        categoryDtoV1.parent( map( category.getParent() ) );
        categoryDtoV1.name( category.getName() );
        categoryDtoV1.note( category.getNote() );

        return categoryDtoV1.build();
    }

    @Override
    public Category map(CategoryDtoV1 categoryDto) {
        if ( categoryDto == null ) {
            return null;
        }

        CategoryBuilder category = Category.builder();

        category.id( categoryDto.getId() );
        category.parent( map( categoryDto.getParent() ) );
        category.name( categoryDto.getName() );
        category.note( categoryDto.getNote() );

        return category.build();
    }
}
