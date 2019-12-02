package com.c4c.microserviceTool.mashup;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.c4c.microserviceTool.dto.ProductDto;


@Service
public class ProductMashupService {
	
	public ProductDto createProduct(@Validated @RequestBody ProductDto requestDto) {
//		ProductDto productDto = new ProductDto();
//		productDto.setId(requestDto.getId());
//		productDto.setName(requestDto.getName());
        return new ProductDto();
    }

}
