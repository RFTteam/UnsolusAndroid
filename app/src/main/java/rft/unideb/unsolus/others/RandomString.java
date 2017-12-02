package rft.unideb.unsolus.others;

import java.util.Random;

public class RandomString {

    private String finalString;

    public RandomString() {
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        this.finalString = sb.toString();
    }

    public String getFinalString() {
        return finalString;
    }
}