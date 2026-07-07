package in.sp.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entity.Users;
import in.sp.main.exceptione.OwnException;
import in.sp.main.repo.UserRepository;
import in.sp.main.validation.JwtServcie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private JwtServcie jwtService;

    @Autowired
    private UserRepository userRepository;

    public Users getCurrentUser(HttpServletRequest request) {

        String token = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }

            }

        }

        if (token == null) {
            throw new OwnException("Token Not Found");
        }

        String email = jwtService.extractEmail(token);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new OwnException("User Not Found"));

    }

}