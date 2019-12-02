package com.c4c.microserviceTool.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTextDto extends AuditDto {
    private String lang;
    private String name;

    public ProductTextDto() {
    }



}
