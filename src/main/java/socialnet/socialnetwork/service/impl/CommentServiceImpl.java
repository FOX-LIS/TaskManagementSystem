package socialnet.socialnetwork.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import socialnet.socialnetwork.model.Comment;
import socialnet.socialnetwork.model.Task;
import socialnet.socialnetwork.model.User;
import socialnet.socialnetwork.repository.CommentRepository;
import socialnet.socialnetwork.repository.TaskRepository;
import socialnet.socialnetwork.repository.UserRepository;
import socialnet.socialnetwork.service.CommentService;
import socialnet.socialnetwork.service.TaskService;
import socialnet.socialnetwork.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    @Override
    public void addComment(Long taskId, String commentText, UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        Task commentedTask = taskRepository.findById(taskId)
                .orElseThrow(()-> new EntityNotFoundException("Задача с ID=" + taskId + " не найдена!"));
        Comment comment = Comment.builder()
                .value(commentText)
                .commentAuthor(currentUser)
                .task(commentedTask)
                .build();
        commentRepository.save(comment);
    }
    @Override
    public List<Comment> findAllCommentsByTaskId(Long taskId){
        Task commentedTask = taskRepository.findById(taskId)
                .orElseThrow(()-> new EntityNotFoundException("Задача с ID=" + taskId + " не найдена!"));
        return commentRepository.findAllByTask(commentedTask);
    }
    @Override
    public List<Comment> findAllCommentsByCommentAuthorId(Long commentAuthorId){
        User currentUser = userRepository.findById(commentAuthorId)
                .orElseThrow(()-> new EntityNotFoundException("Пользователя с таким ID не найдено!"));
        return commentRepository.findAllByCommentAuthor(currentUser);
    }

}
