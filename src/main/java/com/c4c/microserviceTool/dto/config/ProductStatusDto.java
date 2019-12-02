package com.c4c.microserviceTool.dto.config;

import com.c4c.microserviceTool.dto.AuditDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@ApiModel("ProductStatus")
@Getter
@Setter
public class ProductStatusDto extends AuditDto {

     private String code;

     @JsonProperty("texts")
     private List<ProductStatusTextDto> texts = new ArrayList<>();

     public ProductStatusDto() {
     }

}
