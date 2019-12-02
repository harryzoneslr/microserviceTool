package com.c4c.microserviceTool.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditDto extends BaseDto{
    private String createdBy;
    private Date createdAt;
    private String modifiedBy;
    private Date modifiedAt;

}
