package io.github.tbo007.testchamber.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.function.Function;

public class TimingAttack {




    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 200;
    private static final Random RANDOM = new Random();

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }





    @Test
    public void firstTry() throws Exception{
        String string = generateRandomString();
        String substr = string.substring(0, 122);
        String padded = String.format("%-"+LENGTH+"s", substr).replace(' ', '*');
        System.out.println(string);
        System.out.println(padded);
        String replaced = string.charAt(0) +"X" + string.substring(2);
        System.out.println(string);
        System.out.println(replaced);
        System.out.println(hash(string));
        System.out.println(hash(replaced));
        System.out.println(hash(replaced));



        assertEquals(string.length(),padded.length());
        timeFunction(string,padded);


    }

    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Umwandlung in Hex-String
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void blackhole(Object object) {

    }


    public void timeFunction(String str1, String str2) {
        long min = Long.MAX_VALUE;
        long avg = 0;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            str1.equals(str2);
            long stop = System.nanoTime();
            min = Math.min(min,(stop-start));
            avg = avg + (stop-start);
        }
        avg = avg/10;
        System.out.printf("Min: %d AVG: %d%n", min,avg);
    }




}
