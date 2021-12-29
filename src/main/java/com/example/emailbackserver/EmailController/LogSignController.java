package com.example.emailbackserver.EmailController;


import com.example.emailbackserver.EmailModel.User;
import com.example.emailbackserver.EmailService.LoggerService;
import com.example.emailbackserver.EmailService.SignUpService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/emailLogger")
public class LogSignController {
    private SignUpService signUpService;
    private LoggerService loggerService;

    @Autowired
    public LogSignController(SignUpService signUpService, LoggerService loggerService) {
        this.signUpService = signUpService;
        this.loggerService = loggerService;
    }

    @PostMapping("/signUp")
    public boolean signUp(@RequestBody String user) throws IOException, ParseException, CloneNotSupportedException {
        System.out.println("2"+user);
        return signUpService.isRegistered(user);
    }

    @PostMapping("/logIn")
    public String login(@RequestBody String user){
        GsonBuilder gsonBldr = new GsonBuilder();
        gsonBldr.registerTypeAdapter(User.class, new userLoggedAdapter() );
        User parsedUser = gsonBldr.create().fromJson(user, User.class);

        return loggerService.logINCheck(parsedUser.getEmailAddress(), parsedUser.getPassword());
    }

    @PostMapping("/logOut")
    public String logOut(@RequestBody String user){
        GsonBuilder gsonBldr = new GsonBuilder();
        gsonBldr.registerTypeAdapter(User.class, new userLoggedAdapter() );
        User parsedUser = gsonBldr.create().fromJson(user, User.class);

        return loggerService.logOutCheck(parsedUser.getEmailAddress(), parsedUser.getPassword());
    }

}



