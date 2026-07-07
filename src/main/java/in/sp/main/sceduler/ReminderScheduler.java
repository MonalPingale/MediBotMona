package in.sp.main.sceduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.sp.main.repo.ReminderRepository;
import in.sp.main.service.TelegramService;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import in.sp.main.entity.Reminder;

@Component
public class ReminderScheduler {
	

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private TelegramService telegramService;

    
    
    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void sendReminder() {

        LocalTime currentTime = LocalTime.now()
                .withSecond(0)
                .withNano(0);

        System.out.println("Checking Reminder : " + currentTime);

        List<Reminder> reminders =
                reminderRepository.findByReminderTimeAndStatus(currentTime, true);

        for (Reminder reminder : reminders) {

            if (reminder.getLastSentAt() != null &&
                    reminder.getLastSentAt().toLocalDate().equals(LocalDate.now())) {

                continue;
            }

            String message =
                    "💊 MediBot Reminder\n\n" +
                    "Medicine : " + reminder.getMedicineName() + "\n" +
                    "Dosage : " + reminder.getDosage() + "\n\n" +
                    "Time to take your medicine.";

            telegramService.sendMessage(
            	    reminder.getUser().getTelegramChatId(),
            	    message
            	);

            reminder.setLastSentAt(LocalDateTime.now());

            reminderRepository.save(reminder);

            System.out.println(reminder.getMedicineName() + " Sent");
        }

    }

}
