package com.example.storemanagement.validate;

import com.example.storemanagement.exceptions.NotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public final class Validate {

    private Validate() {}

    public static void notNull(Object obj){
        if(obj == null){
            throw new NotFoundException("Object is null");
        }
    }

    public static void notEmpty(String... str){
        for(String string: str){
            notEmpty(string);
        }
    }

    public static void notEmpty(String str){
        if(StringUtils.isEmpty(str)){
            throw new NotFoundException("Object is empty");
        }
    }

    public static boolean hasIntegersAndSpecialCharacters(String str){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*., ?]).+$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }

    public static boolean hasCorrectEmailFormat(String str){
        String emailFormat = "johndoe@example.com";
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(emailFormat).matches();
    }
}
