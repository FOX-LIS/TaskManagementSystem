package socialnet.socialnetwork.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socialnet.socialnetwork.event.exception.MismatchException;
import socialnet.socialnetwork.model.Task;
import socialnet.socialnetwork.model.TaskPriority;
import socialnet.socialnetwork.model.TaskStatus;
import socialnet.socialnetwork.model.User;
import socialnet.socialnetwork.repository.TaskRepository;
import socialnet.socialnetwork.repository.UserRepository;
import socialnet.socialnetwork.service.TaskService;
import socialnet.socialnetwork.service.UserService;
import socialnet.socialnetwork.web.model.CreateTaskRequest;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Задача с ID= {0} не найдена!", id)));
    }

    @Override
    public List<Task> findAllByAuthorId(Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(()-> new EntityNotFoundException("Пользователя с таким ID не найдено!"));
        return taskRepository.findAllByAuthor(author);
    }

    @Override
    public List<Task> findAllByExecutorId(Long executorId) {
        User executor = userRepository.findById(executorId)
                .orElseThrow(()-> new EntityNotFoundException("Пользователя с таким ID не найдено!"));
        return taskRepository.findAllByExecutor(executor);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Boolean existsByTaskId(Long taskId) {
        return taskRepository.existsById(taskId);
    }

    @Override
    public List<Task> findAllOwnTasksAsAuthor(UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return taskRepository.findAllByAuthor(currentUser);
    }

    @Override
    public List<Task> findAllOwnTasksAsExecutor(UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        return taskRepository.findAllByExecutor(currentUser);
    }

    @Override
    public List<Task> findAllOwnTasks(UserDetails userDetails) {
        List<Task> authorTaskList = findAllOwnTasksAsAuthor(userDetails);
        List<Task> executorTaskList = findAllOwnTasksAsExecutor(userDetails);
        Set<Task> taskSet = new HashSet<>(authorTaskList);
        taskSet.addAll(executorTaskList);
        return taskSet.stream().toList();
    }

    @Override
    public Task create(CreateTaskRequest request, UserDetails userDetails) {
        User author = userService.getCurrentUser(userDetails);
        var task = Task.builder()
                .header(request.getHeader())
                .description(request.getDescription())
                .status(request.getTaskStatus())
                .priority(request.getTaskPriority())
                .author(author)
                .executor(request.getExecutor())
                .build();
        return save(task);
    }

    @Override
    public void changeTaskStatus(Long taskId, TaskStatus newTaskStatus, UserDetails userDetails) {
        Task task = findById(taskId);
        User author = task.getAuthor();
        User executor = task.getExecutor();
        User currentUser = userService.getCurrentUser(userDetails);
        if(!currentUser.equals(author) && !currentUser.equals(executor)){
            throw new MismatchException("Текущий пользователь не является ни автором задачи, ни её исполнителем!");
        }
        task.setStatus(newTaskStatus);
    }

    @Override
    public void appointExecutor(Long taskId, Long executorId, UserDetails userDetails) {
        Task task = findById(taskId);
        User author = task.getAuthor();
        User currentUser = userService.getCurrentUser(userDetails);
        if(!currentUser.equals(author)){
            throw new MismatchException("Текущий пользователь не является автором задачи!");
        }
        User newExecutor = userRepository.findById(executorId)
                .orElseThrow(()-> new EntityNotFoundException("Пользователя с таким ID не найдено!"));
        task.setExecutor(newExecutor);
    }
    @Modifying
    @Transactional
    @Override
    public void deleteByTaskId(Long taskId, UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        boolean existFlag = taskRepository.existsByIdAndAuthor(taskId, currentUser);
        if(!existFlag){
            throw new MismatchException("Задача с ID=" + taskId + " не удалена! " +
                    "Она принадлежит другому пользователю, либо такой не существует!");
        }
        taskRepository.deleteById(taskId);
    }

    @Modifying
    @Transactional
    @Override
    public void deleteAllOwn(UserDetails userDetails) {
        User currentUser = userService.getCurrentUser(userDetails);
        List<Task> tasks = taskRepository.findAllByAuthor(currentUser);
        taskRepository.deleteAllInBatch(tasks);
    }
    @Transactional
    @Override
    public void updateTask(Long taskId, CreateTaskRequest createTaskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new EntityNotFoundException("Задачи с таким ID не найдено!"));
        String newHeader = createTaskRequest.getHeader();
        if (newHeader!= null){
            task.setHeader(newHeader);
        }
        String description = createTaskRequest.getDescription();
        if (description!= null){
            task.setDescription(description);
        }
        TaskStatus taskStatus = createTaskRequest.getTaskStatus();
        if (taskStatus!= null){
            task.setStatus(taskStatus);
        }
        TaskPriority taskPriority = createTaskRequest.getTaskPriority();
        if (taskPriority!= null){
            task.setPriority(taskPriority);
        }
        User executor = createTaskRequest.getExecutor();
        if (executor!= null){
            task.setExecutor(executor);
        }
        taskRepository.save(task);
    }

}
