package com.c4c.microserviceTool.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@ApiModel("ProductQuery")
@Getter
@Setter
public class ProductQueryModel extends AuditDto {
    private UUID uuid;
    private String productId;
    private String name;
    private String externalId;
    private String status;
    private String baseUOM;

    public ProductQueryModel() {
    }

}
