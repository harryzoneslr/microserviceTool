package com.c4c.microserviceTool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApiModel("Product")
@Getter
@Setter
public class ProductDto extends AuditDto {
    private UUID uuid;
    private String productId;
    private String externalId;
    private String lifeCycleStatusCode;
    private String baseUOM;

    @JsonProperty("texts")
    private List<ProductTextDto> texts = new ArrayList<>();

    public ProductDto() {
    }


}
