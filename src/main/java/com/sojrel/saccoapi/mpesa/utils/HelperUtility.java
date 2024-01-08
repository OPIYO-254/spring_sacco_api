package com.sojrel.saccoapi.mpesa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojrel.saccoapi.mpesa.dto.RegisterUrlRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CharacterPredicate;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


@Slf4j
public class HelperUtility {

    public static String toBase64String(String value) {
        byte[] data = value.getBytes(StandardCharsets.ISO_8859_1);
        return Base64.getEncoder().encodeToString(data);
    }

    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String generateTransactionUniqueNumber(){
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
        log.info(randomStringGenerator.generate(12).toUpperCase());
        return randomStringGenerator.generate(12).toUpperCase();
    }

    public static String getStkPushPassword(String shortCode, String passKey, String timestamp){
        String concatenatedString = String.format("%s%s%s", shortCode, passKey, timestamp);
        return toBase64String(concatenatedString);
    }

    public static String getTransactionTimestamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

}
