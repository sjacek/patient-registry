package com.grinnotech.patients.api;

import static org.springframework.http.HttpStatus.OK;

import com.grinnotech.patients.api.model.Patient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javax.validation.Valid;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-29T18:01:46.990+02:00[Europe/Warsaw]")

@Controller
@RequestMapping("${openapi.patientRegistry.base-path:/v1}")
public class PatientApiController implements PatientApi {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public PatientApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

	@Override
	public ResponseEntity<Void> addPatient(@Valid Patient patient) {
    	logger.info("addPatient 1");
		return new ResponseEntity<Void>(OK);
	}

	@Override
	public ResponseEntity<Void> updatePatient(@Valid Patient patient) {
		logger.info("updatePatient 1");
		return new ResponseEntity<Void>(OK);
	}

}
