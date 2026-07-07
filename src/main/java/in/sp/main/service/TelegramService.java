package in.sp.main.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.sp.main.entity.Response;
import in.sp.main.entity.Users;
import in.sp.main.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepo;

    private final WebClient webClient = WebClient.create();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long lastUpdateId = 0L;

    // ================= SEND MESSAGE ===================

    public String sendMessage(String chatId, String message) {

        String url =
                "https://api.telegram.org/bot"
                        + botToken
                        + "/sendMessage";

        return webClient.post()
                .uri(url)
                .bodyValue(new SendMessageRequest(chatId, message))
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }

    static class SendMessageRequest {

        private String chat_id;
        private String text;

        public SendMessageRequest(String chatId, String text) {
            this.chat_id = chatId;
            this.text = text;
        }

        public String getChat_id() {
            return chat_id;
        }

        public String getText() {
            return text;
        }

    }

    // ================= GENERATE LINK ===================

    public Response generateLinkCode(HttpServletRequest request) {

        Users user = authService.getCurrentUser(request);

        String code = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        user.setTelegramLinkCode(code);

        userRepo.save(user);

        return Response.builder()
                .statuscode(200)
                .message("Open Telegram Bot and send:\n/start " + code)
                .build();

    }

    // ================= CHECK TELEGRAM ===================

    @Scheduled(fixedDelay = 5000)
    public void checkTelegramUpdates() {

        try {

            String url =
                    "https://api.telegram.org/bot"
                            + botToken
                            + "/getUpdates?offset="
                            + (lastUpdateId + 1);

            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);

            JsonNode result = root.get("result");

            if (result == null || !result.isArray()) {
                return;
            }

            for (JsonNode update : result) {

                lastUpdateId =
                        update.get("update_id").asLong();

                JsonNode message =
                        update.get("message");

                if (message == null)
                    continue;

                String text =
                        message.get("text").asText("");

                if (!text.startsWith("/start "))
                    continue;

                String code =
                        text.substring(7).trim();

                String chatId =
                        message.get("chat")
                                .get("id")
                                .asText();

                userRepo.findByTelegramLinkCode(code)
                        .ifPresent(user -> {

                            user.setTelegramChatId(chatId);

                            user.setTelegramLinkCode(null);

                            userRepo.save(user);

                            sendMessage(
                                    chatId,
                                    "✅ Telegram Connected Successfully!\n\nNow you will receive your medicine reminders here."
                            );

                            System.out.println(
                                    "Telegram Connected : "
                                            + user.getEmail()
                            );

                        });

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}