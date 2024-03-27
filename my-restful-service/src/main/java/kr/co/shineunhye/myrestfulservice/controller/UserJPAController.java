package kr.co.shineunhye.myrestfulservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import kr.co.shineunhye.myrestfulservice.bean.Post;
import kr.co.shineunhye.myrestfulservice.bean.User;
import kr.co.shineunhye.myrestfulservice.exception.UserNotFoundException;
import kr.co.shineunhye.myrestfulservice.repository.PostRepository;
import kr.co.shineunhye.myrestfulservice.repository.UserRepository;

@RestController
@RequestMapping("/jpa")
public class UserJPAController {
	
	private UserRepository userRepository;
	private PostRepository postRepository;
	
	public UserJPAController(UserRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping(path ="/users/{id}")
	public ResponseEntity retrieveUserById(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent()) { //존재하는지 판단
			throw new UserNotFoundException("id-" + id);
		}
		
		EntityModel entityModel = EntityModel.of(user.get());
		
		WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(linTo.withRel("all users")); //all-users -> http://localhost:8088/users
		
		return ResponseEntity.ok(entityModel);
	}
	
	@DeleteMapping(path ="/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}
	
	@PostMapping(path ="/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() //어디에서 해당하는 정보를 가져올 것인지 = /users
				.path("/{id}") //경로지정
				.buildAndExpand(savedUser.getId()) //{id} 값에 어떤 값이 들어갈 것인지
				.toUri();
		
		//location은 사용자의 id 값을 가지고 상세보기 할 수 있도록 만들어져 있는 정보
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/users/{id}/posts")
	public List<Post> retrieveAllPostByUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(!user.isPresent()) { //존재하는지 판단
			throw new UserNotFoundException("id-" + id);
		}
		
		return user.get().getPosts();
	}
	
	@PostMapping(path ="/users/{id}/posts")
	public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
		Optional<User> userOptional = userRepository.findById(id);
		
		if(!userOptional.isPresent()) { //존재하는지 판단
			throw new UserNotFoundException("id-" + id);
		}
	
		User user = userOptional.get();
		
		post.setUser(user);
		
		postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() //어디에서 해당하는 정보를 가져올 것인지 = /users
				.path("/{id}") //경로지정
				.buildAndExpand(post.getId()) //{id} 값에 어떤 값이 들어갈 것인지
				.toUri();
		
		return ResponseEntity.created(location).build();
		
	}
}
