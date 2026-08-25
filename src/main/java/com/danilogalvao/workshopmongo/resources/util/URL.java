package com.danilogalvao.workshopmongo.resources.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class URL {

    public static String decodeParam(String text) {
        return  URLDecoder.decode(text, StandardCharsets.UTF_8);
    }

    public static LocalDateTime parseDate(String date, LocalDateTime defaultDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            return LocalDate.parse(date, formatter).atStartOfDay();
        } catch (DateTimeParseException e){
            return defaultDate;
        }

    }
}
