package com.volunteer.api.data.model.api.search.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(
    use = Id.NAME,
    property = "type"
)
@JsonSubTypes({
    @Type(name = "bool", value = ValueBoolFilterDto.class),
    @Type(name = "number", value = ValueNumberFilterDto.class),
    @Type(name = "text", value = ValueTextFilterDto.class),
    @Type(name = "operator", value = OperatorFilterDto.class)
})
public interface FilterDto {

}
