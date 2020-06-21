package application;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;
import service.ValidationService;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class MobileRegisterController {

    @Autowired
    private UserService _userService;

    @Autowired
    private ValidationService _validationService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot Rest API";
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseDTO<User>> getUserInfoById(@PathVariable String id) throws ExecutionException, InterruptedException {
        try {
            ResponseDTO<User> response = new ResponseDTO<>();
            User user = _userService.getUserDetails(id);
            response.setResponse(user);
            return ResponseEntity.ok(response);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseDTO<String>> registerNewUser(@RequestBody User user) throws ExecutionException, InterruptedException, BadRequestException, UnprocessException {
        try {
            _validationService.validatePhoneNumber(user.getPhoneNumber());
            ResponseDTO<String> response = new ResponseDTO<>();
            String docId = _userService.registerNewUser(user);
            response.setResponse(docId);
            return ResponseEntity.ok(response);
        } catch(Exception exception) {
            ResponseErrorDTO<String> response = new ResponseErrorDTO<>();
            response.setMessage(exception.getMessage());
            if(exception.getClass().equals(BadRequestException.class)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if(exception.getClass().equals(UnprocessException.class)) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
