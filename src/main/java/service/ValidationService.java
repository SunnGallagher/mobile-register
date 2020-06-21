package service;

import model.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    public void validatePhoneNumber(String phoneNumber) throws BadRequestException {
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if(!matcher.matches()) {
            throw new BadRequestException("Phone number should be number and 10 digits");
        }
    }
}
