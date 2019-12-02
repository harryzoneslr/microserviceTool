package com.c4c.microserviceTool.controller;

import java.util.UUID;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.c4c.microserviceTool.dto.ProductDto;
import com.c4c.microserviceTool.mashup.ProductMashupService;

@Api(value = "Simple Product Management API", tags = { "Simple Product Management API" })
@RestController
@Validated
@RequestMapping(path = "/api/product")
public class ProductController {

	private final ProductMashupService productMashupService;

	@Autowired
	public ProductController(ProductMashupService productMashupService) {
		this.productMashupService = productMashupService;
	}

	@ApiOperation(value = "Create a product")
	@PostMapping
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProductDto createProduct(
			@Validated @RequestBody ProductDto requestDto) {
		return productMashupService.createProduct(requestDto);
	}

	@ApiOperation(value = "Get a product by id")
	@GetMapping(value = "/{product-id}")
	@ResponseBody
	public ProductDto getProduct(
			@PathVariable(name = "product-id") UUID productId,
			@RequestParam(required = false) String expand) {
		return new ProductDto();
	}

}
