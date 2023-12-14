package exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import exercise.model.Error;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
@Log4j2
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();
    private Error error;
    //private List<Page> page;
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts") // Список страниц
    public ResponseEntity index(@RequestParam(defaultValue = "10") @Min(value = 1,message = "limit>=1") @Max(value = 100,message = "limit<100") Integer limit, @RequestParam(defaultValue = "1") @Min(1) Integer page) {
        List<Post> filtredPosts = filterPosts(limit, page, posts);
        List<String> errors = new ArrayList<String>();
        Set<ConstraintViolation<Integer>> violations = validator.validate(limit);
        for (ConstraintViolation<Integer> violation : violations) {
            errors.add(violation.getMessage());
        }
        if(violations.isEmpty()){
            return new ResponseEntity<Object>(filtredPosts.stream().limit(limit).toList(),new HttpHeaders(), HttpStatus.OK);
        }
        else {
            error.setErrors(errors);
            error.setMessage("Errors in parameters");
            error.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<Object>(error,new HttpHeaders(), error.getStatus());
        }


    }

    private List<Post> filterPosts(Integer pageLimit, Integer pageNumber, List<Post> posts){
        List<Post> filtredPosts = new ArrayList<Post>();
        int postIndex=1;
        int startIndex = pageLimit*pageNumber-pageLimit+1;
        int lastIndex = pageLimit*pageNumber;
        for (Post post: posts) {
            if(postIndex>=startIndex && postIndex<=lastIndex){
                filtredPosts.add(post);
            }
            postIndex++;
        }
        return filtredPosts;
    }

    @PostMapping("/posts") // Создание страницы
    public Post create(@RequestBody Post page) {
        posts.add(page);
        return page;
    }

    @GetMapping("/posts/{id}") // Вывод страницы
    public Optional<Post> show(@PathVariable String id) {
        var page = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        return page;
    }

    @PutMapping("/posts/{id}") // Обновление страницы
    public Post update(@PathVariable String id, @RequestBody Post data) {
        var maybePage = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (maybePage.isPresent()) {
            var page = maybePage.get();
            page.setId(data.getId());
            page.setTitle(data.getTitle());
            page.setBody(data.getBody());
        }
        return data;
    }

    @DeleteMapping("/posts/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
    // END
}
