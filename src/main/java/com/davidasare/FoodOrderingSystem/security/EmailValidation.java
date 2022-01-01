package com.davidasare.FoodOrderingSystem.security;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidation implements Predicate<String> {

    @Override
    public boolean test(String s) {
        String ePattern = "^(.+)@(\\S+) $.";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(s);
        return m.matches();
    }
}
