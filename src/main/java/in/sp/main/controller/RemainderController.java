package in.sp.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.entity.Reminder;
import in.sp.main.service.RemainderServcie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reminder")
public class RemainderController {
	
	@Autowired
	private RemainderServcie service;
	
	
	@PostMapping("/add")
	public ResponseEntity<?> createRem(@Valid @RequestBody Reminder rem,
	                                   HttpServletRequest req) {

	    return ResponseEntity.ok(service.remdercreta(rem, req));
	}
	
	@GetMapping("/allmyreminders")
	public ResponseEntity<?> getall(HttpServletRequest req){
		return ResponseEntity.ok(service.getAll(req));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id,
	                                 HttpServletRequest req){

	    return ResponseEntity.ok(service.getReminder(id,req));

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,
	                                @RequestBody Reminder reminder,
	                                HttpServletRequest req){

	    return ResponseEntity.ok(service.update(id,reminder,req));

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id,
	                                HttpServletRequest req){

	    return ResponseEntity.ok(service.delete(id,req));

	}

}
