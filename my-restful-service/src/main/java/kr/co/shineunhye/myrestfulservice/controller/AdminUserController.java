package kr.co.shineunhye.myrestfulservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import kr.co.shineunhye.myrestfulservice.bean.AdminUser;
import kr.co.shineunhye.myrestfulservice.bean.AdminUserV2;
import kr.co.shineunhye.myrestfulservice.bean.User;
import kr.co.shineunhye.myrestfulservice.dao.UserDaoService;
import kr.co.shineunhye.myrestfulservice.exception.UserNotFoundException;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
	private UserDaoService service;

	public AdminUserController(UserDaoService service) {
		this.service = service;
	}
	
//	@GetMapping("/v1/users/{id}")
	@GetMapping(value = "/users/{id}", params = "version=1")
	public MappingJacksonValue retrieveUser4Admin(@PathVariable int id) { //json filter 값을 반영해서 반환시켜야 하기 때문에 MappingJacksonValue로 반환
		User user = service.findOne(id);
		
		AdminUser adminUser = new AdminUser();
		
		if(user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}else {
			BeanUtils.copyProperties(user, adminUser);
		}
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
		mapping.setFilters(filters);
		
		return mapping;
	}
	
//	@GetMapping("/v2/users/{id}")
	@GetMapping(value = "/users/{id}", params = "version=2")
	public MappingJacksonValue retrieveUser4AdminV2(@PathVariable int id) { //json filter 값을 반영해서 반환시켜야 하기 때문에 MappingJacksonValue로 반환
		User user = service.findOne(id);
		
		AdminUserV2 adminUser = new AdminUserV2();
		
		if(user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}else {
			BeanUtils.copyProperties(user, adminUser);
			adminUser.setGrade("VIP");
		}
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
		mapping.setFilters(filters);
		
		return mapping;
	}
	
	@GetMapping("/users")
	public MappingJacksonValue retrieveAllUser4Admin() {
		List<User> users = service.findAll();
		
		List<AdminUser> adminUsers = new ArrayList<>();
		AdminUser adminUser = null;
		for(User user : users) {
			adminUser = new AdminUser();
			BeanUtils.copyProperties(user, adminUser);
			
			adminUsers.add(adminUser);
		}
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(adminUsers);
		mapping.setFilters(filters);
		
		return mapping;
	}
	
	
	
}
