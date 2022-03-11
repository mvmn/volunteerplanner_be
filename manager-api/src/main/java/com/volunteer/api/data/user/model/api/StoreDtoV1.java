package com.volunteer.api.data.user.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "name", "address", "contactPerson", "note"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDtoV1 {
    private Integer id;
    private String name;
    private AddressDtoV1 address;
    private AddressDtoV1 recipientAddress;
    private String contactPerson;
    private String note;
}
