package exercise.controller;

import exercise.daytime.Day;
import exercise.daytime.Night;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

// BEGIN

@RestController
public class WelcomeController {

    @Autowired
    private Day day;

    @Autowired
    private Night night;
    @GetMapping("/welcome")
    public String index(){
        String result = "";
        int hour = LocalDateTime.now().getHour();

        if (hour >= 6 && hour < 22)
            return  "It is "+day.getName()+" now! Welcome to Spring!";
        else
            return "It is "+night.getName()+" now! Welcome to Spring!";

    }

}
// END
