package in.sp.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entity.Response;
import in.sp.main.entity.Role;
import in.sp.main.entity.Users;
import in.sp.main.exceptione.OwnException;
import in.sp.main.repo.UserRepository;
import in.sp.main.validation.JwtServcie;
import jakarta.validation.Valid;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private JwtServcie jwtService;


	public Response register(@Valid Users user) {

	    if (user.getEmail() == null || user.getEmail().isBlank()
	            || user.getPassword() == null || user.getPassword().isBlank()) {
	        throw new OwnException("Email and Password are required");
	    }

	    if (repo.existsByEmail(user.getEmail())) {
	        throw new OwnException("Email already exists");
	    }

	    if (user.getRole() == null) {
	        user.setRole(Role.USER);
	    }

	    repo.save(user);

	    return Response.builder()
	            .statuscode(200)
	            .message("User registered successfully")
	            .build();
	}

	
	public Response login(Users user) {

	    Users dbUser = repo.findByEmail(user.getEmail())
	            .orElseThrow(() -> new OwnException("User Not Found"));

	    if (!dbUser.getPassword().equals(user.getPassword())) {
	        throw new OwnException("Invalid Password");
	    }

	    String token = jwtService.generateToken(dbUser.getEmail());

	    return Response.builder()
	            .statuscode(200)
	            .message(token)
	            .build();
	}
}
