package exercise.model;

import lombok.*;


@RequiredArgsConstructor
@Setter
@Getter
public class Post {
    @NonNull
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String body;
    //private int page;
}


