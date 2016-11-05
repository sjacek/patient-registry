package com.grinno.patients.service;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LogService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

    @ExtDirectMethod
    @Async
    public void logClientCrash(@RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent, Map<String, Object> crashData) {

        StringBuilder sb = new StringBuilder();
        sb.append("JavaScript Error").append(LINE_SEPARATOR).append("User-Agent: ").append(userAgent);

        crashData.forEach((k, v) ->
                sb.append(LINE_SEPARATOR).append(k).append(": ").append(v));

        LOGGER.error(sb.toString());
    }

    @ExtDirectMethod
    @Async
    public void debug(String message) {
        LOGGER.debug(message);
    }
}
