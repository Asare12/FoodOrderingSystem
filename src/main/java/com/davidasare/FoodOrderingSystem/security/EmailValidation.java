package com.davidasare.FoodOrderingSystem.security;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidation implements Predicate<String> {

    @Override
    public boolean test(String s) {
        String ePattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(s);
        return m.matches();
    }
}
//7c8ed143-9006-4301-ada7-e5b0b69d3f29