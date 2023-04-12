package com.SystemDesign.URLShortener.Util;

import org.springframework.stereotype.Component;

import java.util.zip.CRC32;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class ShortenURL {

    private static final String PREDEFINED_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 7;
    private static Map<String, String> shortToLongUrlMap;

    public ShortenURL() {
        shortToLongUrlMap = new HashMap<String, String>();
    }

    public String shorten(String longUrl) {
        String shortUrl = "";

        // Hash longUrl using CRC32
        CRC32 crc = new CRC32();
        crc.update(longUrl.getBytes());
        long hashValue = crc.getValue();

        // Convert hashValue to a string of hexadecimal digits
        String hexString = Long.toHexString(hashValue);

        // Hash longUrl using MD5
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(longUrl.getBytes());
            byte[] md5Bytes = md5.digest();

            // Convert md5Bytes to a string of hexadecimal digits
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                sb.append(Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hexString += sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Hash longUrl using SHA-1
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(longUrl.getBytes());
            byte[] sha1Bytes = sha1.digest();

            // Convert sha1Bytes to a string of hexadecimal digits
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sha1Bytes.length; i++) {
                sb.append(Integer.toString((sha1Bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hexString += sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Take the first SHORT_URL_LENGTH characters from hexString
        hexString = hexString.substring(0, SHORT_URL_LENGTH);
        shortUrl = hexString;

        // Check if the shortUrl already exists in the map
        while (shortToLongUrlMap.containsKey(shortUrl)) {
            // If it does, append a character from the predefined string and try again
            shortUrl += PREDEFINED_STRING.charAt(new Random().nextInt(PREDEFINED_STRING.length()));
        }

        // Add the mapping to the map
        shortToLongUrlMap.put(shortUrl, longUrl);

        return shortUrl;
    }

    public String getLongUrl(String shortUrl) {
        return shortToLongUrlMap.get(shortUrl);
    }
}

