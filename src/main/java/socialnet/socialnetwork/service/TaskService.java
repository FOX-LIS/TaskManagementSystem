package socialnet.socialnetwork.service;

import org.springframework.security.core.userdetails.UserDetails;
import socialnet.socialnetwork.model.Comment;
import socialnet.socialnetwork.model.Task;
import socialnet.socialnetwork.model.TaskStatus;
import socialnet.socialnetwork.model.User;
import socialnet.socialnetwork.web.model.CreateTaskRequest;

import java.util.List;

public interface TaskService {

    List<Task> findAll();

    Task findById(Long taskId);

    List<Task> findAllByAuthorId(Long authorId);
    List<Task> findAllByExecutorId(Long authorId);

    List<Task> findAllOwnTasksAsAuthor(UserDetails userDetails);

    List<Task> findAllOwnTasksAsExecutor(UserDetails userDetails);

    List<Task> findAllOwnTasks(UserDetails userDetails);

    Task save(Task Task);

    Boolean existsByTaskId(Long taskId);

    Task create(CreateTaskRequest request, UserDetails userDetails);

    void changeTaskStatus(Long taskId, TaskStatus newTaskStatus, UserDetails userDetails);

    void appointExecutor(Long taskId, Long executorId, UserDetails userDetails);

    void deleteByTaskId(Long userId, UserDetails userDetails);

    void deleteAllOwn(UserDetails userDetails);

    void updateTask(Long taskId, CreateTaskRequest createTaskRequest);

}
