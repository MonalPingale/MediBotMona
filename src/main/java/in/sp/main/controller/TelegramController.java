package in.sp.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.sp.main.entity.Users;
import in.sp.main.service.AuthService;
import in.sp.main.service.TelegramService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/telegram")
public class TelegramController {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private AuthService authService;

    @PostMapping("/test")
    public ResponseEntity<?> test(HttpServletRequest request) {

        Users user = authService.getCurrentUser(request);

        String response = telegramService.sendMessage(
                user.getTelegramChatId(),
                "💊 Hello " + user.getFullName() +
                "\n\nTime to take your medicine."
        );
        

        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/connect")
    public ResponseEntity<?> connect(HttpServletRequest request){

        return ResponseEntity.ok(
                telegramService.generateLinkCode(request)
        );

    }
    
    @GetMapping("/status")
    public ResponseEntity<?> status(HttpServletRequest request) {

        Users user = authService.getCurrentUser(request);

        boolean connected = user.getTelegramChatId() != null;

        return ResponseEntity.ok(connected);

    }
    
    
}