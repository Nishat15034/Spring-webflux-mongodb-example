package com.springwebflux.springwebflux.service;

import com.springwebflux.springwebflux.dto.ProductDto;
import com.springwebflux.springwebflux.repository.ProductRepository;
import com.springwebflux.springwebflux.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<ProductDto> getAllProducts()
    {
        return productRepository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id)
    {
        return productRepository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductInPriceRange(Double min,Double mx)
    {
        return productRepository.findByPriceBetween(Range.closed(min,mx));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono)
    {
        return productDtoMono.map(AppUtils::DtoToEntity)
                .flatMap(productRepository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto>productDtoMono, String id)
    {
      return  productRepository.findById(id)
                .flatMap(p -> productDtoMono.map(AppUtils::DtoToEntity)
                .doOnNext(e -> e.setId(id)))
                .flatMap(productRepository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id)
    {
        return productRepository.deleteById(id);
    }

}
