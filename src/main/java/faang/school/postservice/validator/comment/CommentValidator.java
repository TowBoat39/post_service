package faang.school.postservice.validator.comment;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.comment.CommentDto;
import faang.school.postservice.exception.DataValidationException;
import faang.school.postservice.model.Comment;
import faang.school.postservice.service.comment.CommentService;
import faang.school.postservice.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import faang.school.postservice.model.Post;

@Component
@RequiredArgsConstructor
public class CommentValidator {
    private CommentService commentService;
    private PostService postService;
    private UserServiceClient userServiceClient;

    public void validateId(long id) {
        if (id < 1) {
            throw new DataValidationException("Id mast be more 1, your isn't");
        }
    }

    public void validateCommentDto(CommentDto commentDto) {
        validateId(commentDto.getId());
        validatePostExist(commentDto.getPostId());
        validateAuthorExist(commentDto.getAuthorId());

        int lenComment = commentDto.getContent().length();
        if (lenComment > 4096 || lenComment == 0) {
            throw new DataValidationException("Length of comment is not correct");
        }
    }

    public void validatePostExist(long postId) {
        validateId(postId);
        postService.getPostById(postId);
    }

    public void validateAuthorExist(long authorId) {
        validateId(authorId);
        if (userServiceClient.getUser(authorId) == null) {
            throw new DataValidationException("User is not exist");
        }
    }

    public void validateUpdateComment(Post post, Comment comment) {
        if (!post.getComments().contains(comment)) {
            throw new DataValidationException("Comment from another post");
        }
    }

    public void validateDeleteComment(long commentId, long authorId) {
        validateId(commentId);
        validateId(authorId);
        Comment comment = commentService.getCommentById(commentId);
        if (comment.getAuthorId() != authorId) {
            throw new DataValidationException("It's comment another author");
        }
    }
}
