package cn.edu.scu.util.impl;

import cn.edu.scu.util.CreateSalt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;

@Repository
public class CreateSaltImpl implements CreateSalt {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreateMd5Impl createMd5;

    @Override
    public String getSalt() {
        Random random = new SecureRandom();
        byte[] saltBT = new byte[32];
        random.nextBytes(saltBT);
        String salt = null;
        try {
            salt = new String(saltBT, "UTF-8");
            salt = createMd5.getMd5ByOneParameter(salt);
        } catch (UnsupportedEncodingException e) {
            logger.error("无该编码方式");
        }
        return salt;
    }
}
