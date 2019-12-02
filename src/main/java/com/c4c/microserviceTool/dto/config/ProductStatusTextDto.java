package com.c4c.microserviceTool.dto.config;

import com.c4c.microserviceTool.dto.AuditDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductStatusTextDto extends AuditDto {
    private String lang;
    private String text;

    public ProductStatusTextDto() {
    }


}
