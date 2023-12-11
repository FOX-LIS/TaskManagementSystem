package socialnet.socialnetwork.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import socialnet.socialnetwork.model.Comment;
import socialnet.socialnetwork.model.Task;
import socialnet.socialnetwork.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @NonNull Comment save(@NonNull Comment comment);

    List<Comment> findAllByTask(Task task);

    List<Comment> findAllByCommentAuthor(User commentAuthor);

}

