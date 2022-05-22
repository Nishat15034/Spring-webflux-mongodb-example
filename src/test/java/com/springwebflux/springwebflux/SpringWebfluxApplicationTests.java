package com.springwebflux.springwebflux;

import com.springwebflux.springwebflux.controller.ProductController;
import com.springwebflux.springwebflux.dto.ProductDto;
import com.springwebflux.springwebflux.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringWebfluxApplicationTests {


	@Autowired
	private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
	public void addProductTest() {

		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("01","moblie",2,10000));

		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		webTestClient.post().uri("/products/save")
				.body(Mono.just(productDtoMono),ProductDto.class)
				.exchange()
				.expectStatus().isOk();
    }

	@Test
	public void getProductsTest()
	{
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("01","mobile",2,10000),
				new ProductDto("02","tv",1,10000));

		when(productService.getAllProducts()).thenReturn(productDtoFlux);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/list")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("01","mobile",2,10000))
				.expectNext(new ProductDto("02","tv",1,10000))
				.verifyComplete();
	}

	@Test
	public void getProductTest()
	{
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("01","mobile",2,10000));

		when(productService.getProductById(any())).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/01")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> p.getName().equals("mobile"))
				.verifyComplete();
	}

	@Test
	public void updateProductTest()
	{
         Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("01","mobile",2,10000));
		 when(productService.updateProduct(productDtoMono,"01")).thenReturn(productDtoMono);

		 webTestClient.put().uri("/products/update/01")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void deleteProductTest()
	{

		given(productService.deleteProduct(any())).willReturn(Mono.empty());

		webTestClient.delete().uri("/products/delete/01")
				.exchange()
				.expectStatus().isOk();

	}


}
