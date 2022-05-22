package com.springwebflux.springwebflux.controller;

import com.springwebflux.springwebflux.dto.ProductDto;
import com.springwebflux.springwebflux.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public Flux<ProductDto> getProducts()
    {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProduct(@PathVariable("id") String id)
    {
        return  productService.getProductById(id);
    }


    @GetMapping("/product-range")
    public Flux<ProductDto> getProductBetweenRange(@RequestParam("min") Double min, @RequestParam("max") Double max)
    {
        return productService.getProductInPriceRange(min,max);
    }

    @PostMapping("/save")
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono)
    {
        return productService.saveProduct(productDtoMono);
    }

    @PutMapping("/update/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable("id") String id)
    {
        return productService.updateProduct(productDtoMono,id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable("id") String id)
    {
        return productService.deleteProduct(id);
    }



}
