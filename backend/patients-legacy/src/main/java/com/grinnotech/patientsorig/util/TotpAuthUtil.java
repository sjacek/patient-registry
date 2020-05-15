package com.grinnotech.patientsorig.util;

import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.joining;

public class TotpAuthUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static boolean verifyCode(String secret, int code, int variance) {
        long timeIndex = System.currentTimeMillis() / 1000 / 30;
        byte[] secretBytes = new Base32().decode(secret);
        for (int i = -variance; i <= variance; i++) {
            if (getCode(secretBytes, timeIndex + i) == code) {
                return true;
            }
        }
        return false;
    }

    private static long getCode(byte[] secret, long timeIndex) {
        try {
            SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1");
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.putLong(timeIndex);
            byte[] timeBytes = buffer.array();
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signKey);
            byte[] hash = mac.doFinal(timeBytes);
            int offset = hash[19] & 0xf;
            long truncatedHash = hash[offset] & 0x7f;
            for (int i = 1; i < 4; i++) {
                truncatedHash <<= 8;
                truncatedHash |= hash[offset + i] & 0xff;
            }
            return truncatedHash % 1000000;
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException e) {
            LOGGER.error("getCode", e);
            return 0;
        }
    }

    public static String randomSecret() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        return new Random().ints(16, 0, 32).mapToObj(i -> valueOf(chars.charAt(i))).collect(joining());
    }

}
