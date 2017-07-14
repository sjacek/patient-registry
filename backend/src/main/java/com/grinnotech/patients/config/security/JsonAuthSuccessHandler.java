package com.grinnotech.patients.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.dto.UserDetailDto;
import com.grinnotech.patients.model.User;
import com.grinnotech.patients.service.SecurityService;
import com.grinnotech.patients.web.CsrfController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonAuthSuccessHandler(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        Map<String, Object> result = new HashMap<String, Object>() {{
            put("success", true);
        }};

        MongoUserDetails userDetails = (MongoUserDetails) authentication.getPrincipal();
        if (userDetails != null) {
            User user = userRepository.findOne(userDetails.getUserDbId());
            if (userDetails.isPreAuth()) {
                user.setLastAccess(new Date());
                userRepository.save(user);
            }
            userRepository.loadOrganizationsData(user);
            result.put(SecurityService.AUTH_USER, new UserDetailDto(userDetails, user, CsrfController.getCsrfToken(request)));
        }

        response.getWriter().print(objectMapper.writeValueAsString(result));
        response.getWriter().flush();
    }

}
