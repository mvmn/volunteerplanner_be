package com.volunteer.api.data.user.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "region", "city", "address"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDtoV1 {
    private Integer id;
    private String name;
    private String note;
    private String path;
    private CategoryDtoV1 parentCategory;
}
