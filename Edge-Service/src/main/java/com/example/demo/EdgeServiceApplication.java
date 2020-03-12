package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.Data;

@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class EdgeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeServiceApplication.class, args);
	}

}

@Data
class Item {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

@FeignClient("item-catalog-service")
interface ItemClient {

	@GetMapping("/items")
	CollectionModel<Item> readItems();
	// was Resources<Item> see
	// https://stackoverflow.com/questions/25352764/hateoas-methods-not-found

}

@RestController
class GoodItemApiAdapterRestController {

	private final ItemClient itemClient;
	private static final Logger logger = LoggerFactory.getLogger(GoodItemApiAdapterRestController.class);

	public GoodItemApiAdapterRestController(ItemClient itemClient) {
		this.itemClient = itemClient;
	}

	@HystrixCommand(fallbackMethod = "fallback")
	@GetMapping("/top-brands")
	public Collection<Item> goodItems() {
		return itemClient.readItems().getContent().stream().filter(i -> i.getName().startsWith("A")).collect(Collectors.toList());
		//return itemClient.readItems().getContent().stream().filter(this::isGreat).collect(Collectors.toList());
	}

	private boolean isGreat(Item item) {
		return !item.getName().equals("Nike") && !item.getName().equals("Adidas") && !item.getName().equals("Reebok");
	}
	
	public Collection<Item> fallback() {
	    return new ArrayList<>();
	}

}
