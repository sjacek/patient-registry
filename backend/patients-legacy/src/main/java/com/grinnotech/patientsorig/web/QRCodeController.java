package com.grinnotech.patientsorig.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.grinnotech.patientsorig.config.security.MongoUserDetails;
import com.grinnotech.patientsorig.dao.authorities.RequireAnyAuthority;
import com.grinnotech.patients.mongodb.dao.UserRepository;
import com.grinnotech.patients.mongodb.model.User;
import com.grinnotech.patientsorig.util.ThrowingFunction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

@Controller
public class QRCodeController {

	private final UserRepository userRepository;

	private final String appName;

	QRCodeController(UserRepository userRepository, @Value("${info.app.name}") String appName) {
		this.userRepository = userRepository;
		this.appName = appName;
	}

	@RequireAnyAuthority
	@RequestMapping(value = "/qr", method = RequestMethod.GET)
	public void qrcode(HttpServletResponse response, @AuthenticationPrincipal MongoUserDetails userDetails)
			throws WriterException, IOException {

		Optional<User> oUser = userRepository.findById(userDetails.getUserDbId());
		oUser.ifPresent(user -> {
			if (!StringUtils.hasText(user.getSecret())) {
				return;
			}
			response.setContentType("image/png");
			String contents =
					"otpauth://totp/" + user.getEmail() + "?secret=" + user.getSecret() + "&issuer=" + this.appName;

			QRCodeWriter writer = new QRCodeWriter();
			try {
				BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, 200, 200);
				MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
				response.getOutputStream().flush();
			} catch (WriterException | IOException e) {
				ThrowingFunction.sneakyThrow(e);
			}
		});
	}

}
