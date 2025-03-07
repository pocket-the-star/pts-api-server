package com.pts.api.lib.internal.shared.util.random;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomUtil implements IRandomUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 6;
    private static final Random RANDOM = new Random();

    @Override
    public String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
