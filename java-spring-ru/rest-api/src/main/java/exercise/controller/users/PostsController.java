package exercise.controller.users;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@Log4j2
@RequestMapping("/api")
public class PostsController {
    private List<Post> posts = Data.getPosts();
    @GetMapping("/users/{id}/posts")
    public ResponseEntity getPosts(@PathVariable int id){
        List<Post> result = posts.stream().filter(p -> p.getUserId() == id).collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/users/{id}/posts") // Создание страницы
    public ResponseEntity create(@PathVariable int id, @RequestBody Post page) {
        page.setUserId(id);
        posts.add(page);
        return new ResponseEntity<Object>(page,new HttpHeaders(), HttpStatus.CREATED);
    }
}
// END
