package in.sp.main.repo;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.main.entity.Reminder;
import in.sp.main.entity.Users;

public interface ReminderRepository extends JpaRepository<Reminder, Long>{

    List<Reminder> findByUser(Users user);

    Optional<Reminder> findById(Long id);
    
    List<Reminder> findByReminderTimeAndStatus(LocalTime time, boolean status);
}
