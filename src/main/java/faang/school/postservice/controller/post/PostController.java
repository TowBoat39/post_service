package faang.school.postservice.controller.post;

import faang.school.postservice.dto.post.ResponsePostDto;
import faang.school.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/")
public class PostController {
    private final PostService postService;

    @GetMapping("{id}")
    public ResponsePostDto getById(@PathVariable Long id){
        return postService.getById(id);
    }

    @DeleteMapping("{id}")
    public ResponsePostDto softDelete(@PathVariable Long id){
        return postService.softDelete(id);
    }
}
