package in.sp.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.entity.Response;
import in.sp.main.entity.Users;
import in.sp.main.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	
	
	@Autowired
	private UserService service;
	
	
	 @PostMapping("/register")
	    public ResponseEntity<Response> register(@Valid @RequestBody Users user) {

	        Response response = service.register(user);

	        return ResponseEntity.ok(response);
	    }

	
	 @PostMapping("/login")
	    public ResponseEntity<Response> login(@RequestBody Users user,
	                                          HttpServletResponse response) {

	        Response res = service.login(user);

	        Cookie cookie = new Cookie("token", res.getMessage());

	        cookie.setHttpOnly(true);
	        cookie.setPath("/");
	        cookie.setMaxAge(24 * 60 * 60);

	        response.addCookie(cookie);

	        return ResponseEntity.ok(
	                Response.builder()
	                        .statuscode(200)
	                        .message("Login Successful")
	                        .build()
	        );
	    }

}
