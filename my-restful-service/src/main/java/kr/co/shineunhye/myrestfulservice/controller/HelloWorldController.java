package kr.co.shineunhye.myrestfulservice.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import kr.co.shineunhye.myrestfulservice.bean.HelloWorldBean;

@RestController //화면단을 만들지 않을거기 때문에 일반 Controller가 아닌 RestController로 어노테이션을 줌
public class HelloWorldController {
	
	private MessageSource messageSource;
	
	public HelloWorldController(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@GetMapping(path = "/hello-world")
	public String helloworld() {
		return "Hello World";
	}
	
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloworldBean() {
		return new HelloWorldBean("Hello World!");
	}
	
	@GetMapping(path = "/hello-world-bean/path-variable/{name}")
	public HelloWorldBean helloworldBeanPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World, %s", name));
	}
	
	@GetMapping(path = "/hello-world-internationalized")
	public String helloworldInternationalized(@RequestHeader(name="Accept-Language", required = false) Locale locale) {
		return messageSource.getMessage("greeting.message", null, locale);
	}
}
