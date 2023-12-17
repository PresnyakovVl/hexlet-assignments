package exercise;

import java.util.*;
import java.util.stream.Stream;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import  org.springframework.beans.factory.annotation.Autowired;

import exercise.model.User;
import exercise.component.UserProperties;

@SpringBootApplication
@RestController
@Log4j2
public class Application {

    // Все пользователи
    private List<User> users = Data.getUsers();
    @Autowired
    private UserProperties userProperties;

    // BEGIN
    @GetMapping("/admins")
    public List<String> admins() {
        List<String> adminNames = new ArrayList<>();
        List<String> adminEmails = userProperties.getAdmins();
        for (String adminEmail : adminEmails){
            log.info("adminEmail: "+adminEmail);
            List<User> adminList = users.stream().
                    filter(u -> Objects.equals(u.getEmail(), adminEmail)).toList();
            if(!adminList.isEmpty()){
                log.info("AddAdminName");
                for(User admin : adminList) {
                    if(!adminNames.contains(admin.getName())){
                        adminNames.add(admin.getName());
                    }
                }
            }
        }
        Collections.sort(adminNames);
        return adminNames;
    }
    // END

    @GetMapping("/users")
    public List<User> index() {
        return users;
    }

    @GetMapping("/users/{id}")
    public Optional<User> show(@PathVariable Long id) {
        var user = users.stream()
            .filter(u -> u.getId() == id)
            .findFirst();
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
