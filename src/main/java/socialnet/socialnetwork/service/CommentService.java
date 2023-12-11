package socialnet.socialnetwork.service;

import org.springframework.security.core.userdetails.UserDetails;
import socialnet.socialnetwork.model.Comment;

import java.util.List;

public interface CommentService {

    void addComment(Long taskId, String comment, UserDetails userDetails);

    List<Comment> findAllCommentsByTaskId(Long taskId);

    List<Comment> findAllCommentsByCommentAuthorId(Long commentAuthorId);



}
