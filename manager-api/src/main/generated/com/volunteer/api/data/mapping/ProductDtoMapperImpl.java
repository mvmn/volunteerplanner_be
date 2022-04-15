package com.volunteer.api.data.mapping;

import com.volunteer.api.data.model.api.CategoryDtoV1;
import com.volunteer.api.data.model.api.CategoryDtoV1.CategoryDtoV1Builder;
import com.volunteer.api.data.model.api.ProductDtoV1;
import com.volunteer.api.data.model.api.ProductDtoV1.ProductDtoV1Builder;
import com.volunteer.api.data.model.persistence.Category;
import com.volunteer.api.data.model.persistence.Category.CategoryBuilder;
import com.volunteer.api.data.model.persistence.Product;
import com.volunteer.api.data.model.persistence.Product.ProductBuilder;
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
public class ProductDtoMapperImpl implements ProductDtoMapper {

    @Override
    public Product map(ProductDtoV1 dto) {
        if ( dto == null ) {
            return null;
        }

        ProductBuilder product = Product.builder();

        product.id( dto.getId() );
        product.category( categoryDtoV1ToCategory( dto.getCategory() ) );
        product.name( dto.getName() );
        product.note( dto.getNote() );

        return product.build();
    }

    @Override
    public ProductDtoV1 map(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDtoV1Builder productDtoV1 = ProductDtoV1.builder();

        productDtoV1.id( product.getId() );
        productDtoV1.category( categoryToCategoryDtoV1( product.getCategory() ) );
        productDtoV1.name( product.getName() );
        productDtoV1.note( product.getNote() );

        return productDtoV1.build();
    }

    @Override
    public Collection<ProductDtoV1> map(Collection<Product> products) {
        if ( products == null ) {
            return null;
        }

        Collection<ProductDtoV1> collection = new ArrayList<ProductDtoV1>( products.size() );
        for ( Product product : products ) {
            collection.add( map( product ) );
        }

        return collection;
    }

    protected Category categoryDtoV1ToCategory(CategoryDtoV1 categoryDtoV1) {
        if ( categoryDtoV1 == null ) {
            return null;
        }

        CategoryBuilder category = Category.builder();

        category.id( categoryDtoV1.getId() );
        category.parent( categoryDtoV1ToCategory( categoryDtoV1.getParent() ) );
        category.name( categoryDtoV1.getName() );
        category.note( categoryDtoV1.getNote() );

        return category.build();
    }

    protected CategoryDtoV1 categoryToCategoryDtoV1(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDtoV1Builder categoryDtoV1 = CategoryDtoV1.builder();

        categoryDtoV1.id( category.getId() );
        categoryDtoV1.parent( categoryToCategoryDtoV1( category.getParent() ) );
        categoryDtoV1.name( category.getName() );
        categoryDtoV1.note( category.getNote() );

        return categoryDtoV1.build();
    }
}
