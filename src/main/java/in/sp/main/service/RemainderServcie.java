package in.sp.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entity.Reminder;
import in.sp.main.entity.Response;
import in.sp.main.entity.Users;
import in.sp.main.exceptione.OwnException;
import in.sp.main.repo.ReminderRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Service
public class RemainderServcie {

	@Autowired
	private ReminderRepository repo;
	
	@Autowired
	private AuthService authService;
	
	public Response remdercreta(Reminder rem,HttpServletRequest request){

	    Users currentUser = authService.getCurrentUser(request);

	    rem.setUser(currentUser);

	    repo.save(rem);

	    return Response.builder()
	            .statuscode(200)
	            .message("Reminder Created Successfully")
	            .build();
	}

	public List<Reminder> getAll(HttpServletRequest req) {

	    Users currentUser = authService.getCurrentUser(req);

	    return repo.findByUser(currentUser);

	}

	public Reminder getReminder(Long id,HttpServletRequest req){

	    Users currentUser = authService.getCurrentUser(req);

	    Reminder reminder = repo.findById(id)
	            .orElseThrow(() -> new OwnException("Reminder Not Found"));

	    if(!reminder.getUser().getId().equals(currentUser.getId())){
	        throw new OwnException("Unauthorized");
	    }

	    return reminder;
	}
	public Response update(Long id,
            Reminder reminder,
            HttpServletRequest req){
				
				Users currentUser = authService.getCurrentUser(req);
				
				Reminder db = repo.findById(id)
				 .orElseThrow(() -> new OwnException("Reminder Not Found"));
				
				if(!db.getUser().getId().equals(currentUser.getId())){
				throw new OwnException("Unauthorized");
				}
				
				db.setMedicineName(reminder.getMedicineName());
				db.setDosage(reminder.getDosage());
				db.setReminderTime(reminder.getReminderTime());
				db.setRepeatType(reminder.getRepeatType());
				
				repo.save(db);
				
				return Response.builder()
				 .statuscode(200)
				 .message("Reminder Updated Successfully")
				 .build();
				
				}

	public Response delete(Long id,
            HttpServletRequest req){
				
				Users currentUser = authService.getCurrentUser(req);
				
				Reminder reminder = repo.findById(id)
				 .orElseThrow(() -> new OwnException("Reminder Not Found"));
				
				if(!reminder.getUser().getId().equals(currentUser.getId())){
				throw new OwnException("Unauthorized");
				}
				
				repo.delete(reminder);
				
				return Response.builder()
				 .statuscode(200)
				 .message("Reminder Deleted Successfully")
				 .build();
				
				}
	
	

}
