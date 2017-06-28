package com.grinnotech.patients;

import com.grinnotech.patients.dao.authorities.RequireAnyAuthority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequireAnyAuthority
public class WelcomeController {

    static final String REPLY_WELCOME = "Reply: Welcome";

    @GetMapping(value = "/welcome")
    public String welcome() throws Exception {
        return REPLY_WELCOME;
    }

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.build.artifact}")
    private String appArtifact;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.build.time}")
    private String buildTime;

    @GetMapping(value = "/version", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ModelMap version() {
        ModelMap model = new ModelMap();

        model.put("name", appName);
        model.put("description", appDescription);
        model.put("artifact", appArtifact);
        model.put("version", appVersion);
        model.put("buildTime", buildTime);

        return model;
    }
}
