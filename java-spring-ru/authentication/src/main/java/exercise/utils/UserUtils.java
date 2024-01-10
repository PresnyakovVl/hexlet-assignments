package exercise.utils;

import exercise.model.User;
import exercise.repository.ArticleRepository;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    // BEGIN
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        var email = authentication.getName();
        return userRepository.findByEmail(email).get();
    }

    public boolean isAuthor(long postId) {
        var postAuthorEmail = articleRepository.findById(postId).get().getAuthor().getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return postAuthorEmail.equals(authentication.getName());
    }
    // END

    public User getTestUser() {
        return  userRepository.findByEmail("hexlet@example.com")
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
