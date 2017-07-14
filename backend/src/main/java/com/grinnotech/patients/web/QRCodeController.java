package com.grinnotech.patients.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.dao.authorities.RequireAnyAuthority;
import com.grinnotech.patients.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class QRCodeController {

    private final UserRepository userRepository;
    
    private final String appName;

    @Autowired
    QRCodeController(UserRepository userRepository, @Value("${info.app.name}") String appName) {
        this.userRepository = userRepository;
        this.appName = appName;
    }

    @RequireAnyAuthority
    @RequestMapping(value = "/qr", method = RequestMethod.GET)
    public void qrcode(HttpServletResponse response, @AuthenticationPrincipal MongoUserDetails userDetails)
            throws WriterException, IOException {

        User user = userRepository.findOne(userDetails.getUserDbId());
        if (user != null && StringUtils.hasText(user.getSecret())) {
            response.setContentType("image/png");
            String contents = "otpauth://totp/" + user.getEmail() + "?secret=" + user.getSecret() + "&issuer=" + this.appName;

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

}
