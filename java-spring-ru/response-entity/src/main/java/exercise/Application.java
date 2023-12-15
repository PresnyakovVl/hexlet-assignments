package exercise;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;

@SpringBootApplication
@RestController
@Log4j2
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts") // Список страниц
    public ResponseEntity index(@RequestParam(defaultValue = "10") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        var result = posts.stream().skip((page-1)*limit).limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }
    @PostMapping("/posts") // Создание страницы
    public ResponseEntity create(@RequestBody Post page) {
        posts.add(page);
        return new ResponseEntity<Object>(page,new HttpHeaders(), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{id}") // Вывод страницы
    public ResponseEntity show(@PathVariable String id) {
        HttpStatus status = HttpStatus.OK;
        var page = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (page.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<Object>(page,new HttpHeaders(), status);
    }

    @PutMapping("/posts/{id}") // Обновление страницы
    public ResponseEntity update(@PathVariable String id, @RequestBody Post data) {
        HttpStatus status = HttpStatus.OK;
        var maybePage = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (maybePage.isPresent()) {
            var page = maybePage.get();
            page.setId(data.getId());
            page.setTitle(data.getTitle());
            page.setBody(data.getBody());
        }
        else{
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<Object>(data,new HttpHeaders(), status);
    }

    @DeleteMapping("/posts/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
    // END

}
