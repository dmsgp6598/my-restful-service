package kr.co.shineunhye.myrestfulservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.shineunhye.myrestfulservice.bean.User;
import kr.co.shineunhye.myrestfulservice.dao.UserDaoService;
import kr.co.shineunhye.myrestfulservice.exception.UserNotFoundException;;

@RestController
@Tag(name = "user-controller", description = "일반 사용자 서비스를 위한 컨트롤러 입니다.") //클래스에 대한 설명을 작성
public class UserController {
	private UserDaoService service;

	public UserController(UserDaoService service) {
		this.service = service;
	}
	
	@GetMapping(path ="/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}
	
	@Operation(summary = "사용자 정보 조회 API", description = "사용자 ID를 이용해서 사용자 상세 정보 조회를 합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK !!"),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
		@ApiResponse(responseCode = "404", description = "USER NOT FOUND !!"),
		@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!"),
		}
	)
	@GetMapping(path ="/users/{id}")
	public EntityModel<User> retrieveUser(
			@Parameter(description = "사용자 ID", required = true, example = "1")@PathVariable int id) {
		User user = service.findOne(id);
		
		if(user == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}
		
		EntityModel entityModel = EntityModel.of(user);
		
		WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(linTo.withRel("all users")); //all-users -> http://localhost:8088/users
		
		return entityModel;
	}
	
	@PostMapping(path ="/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() //어디에서 해당하는 정보를 가져올 것인지 = /users
				.path("/{id}") //경로지정
				.buildAndExpand(savedUser.getId()) //{id} 값에 어떤 값이 들어갈 것인지
				.toUri();
		
		//location은 사용자의 id 값을 가지고 상세보기 할 수 있도록 만들어져 있는 정보
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path ="/users/{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {
		User deleteUser = service.deleteById(id);
		
		if(deleteUser == null) {
			throw new UserNotFoundException(String.format("ID[%s] not found", id));
		}
		
		return ResponseEntity.noContent().build();
	}
	

}
