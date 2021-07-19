package com.davidasare.FoodOrderingSystem.security;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidation implements Predicate<String> {

    @Override
    public boolean test(String s) {
        // TODO: Regex to validate email
        return true;
    }
}
