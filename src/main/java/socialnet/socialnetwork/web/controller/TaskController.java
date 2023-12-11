package socialnet.socialnetwork.web.controller;

import com.sun.security.auth.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import socialnet.socialnetwork.mapper.CommentMapper;
import socialnet.socialnetwork.mapper.UserMapper;
import socialnet.socialnetwork.model.TaskStatus;
import socialnet.socialnetwork.service.CommentService;
import socialnet.socialnetwork.web.model.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialnet.socialnetwork.mapper.TaskMapper;
import socialnet.socialnetwork.model.Task;
import socialnet.socialnetwork.service.TaskService;

@RestController
@RequestMapping("/api/v1/app/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    private final CommentService commentService;

    @GetMapping("/{taskId}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> findByTaskId(@PathVariable Long taskId){
        return ResponseEntity.ok(taskMapper.taskToResponse(taskService.findById(taskId)));
    }

    @GetMapping("/all")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskListResponse> findAllTasks(){
        return ResponseEntity.ok(
                taskMapper.taskListToTaskResponseList(
                        taskService.findAll()
                )
        );
    }

    @GetMapping("/all/byAuthorId/{authorId}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskListResponse> findAllByAuthorId(@PathVariable Long authorId){
        return ResponseEntity.ok(
                taskMapper.taskListToTaskResponseList(
                        taskService.findAllByAuthorId(authorId)
                )
        );
    }

    @GetMapping("/all/byExecutorId/{executorId}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskListResponse> findAllByExecutorId(@PathVariable Long executorId){
        return ResponseEntity.ok(
                taskMapper.taskListToTaskResponseList(
                        taskService.findAllByExecutorId(executorId)
                )
        );
    }

    @GetMapping("/all/own")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskListResponse> findAllOwn(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(
                taskMapper.taskListToTaskResponseList(
                        taskService.findAllOwnTasks(userDetails)
                )
        );
    }

    @GetMapping("/all/own/asAuthor")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskListResponse> findAllOwnAsAuthor(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(
                taskMapper.taskListToTaskResponseList(
                        taskService.findAllOwnTasksAsAuthor(userDetails)
                )
        );
    }

    @GetMapping("/all/own/asExecutor")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskListResponse> findAllOwnAsExecutor(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(
                taskMapper.taskListToTaskResponseList(
                        taskService.findAllOwnTasksAsExecutor(userDetails)
                )
        );
    }



    @PostMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid CreateTaskRequest request,
                                                   @AuthenticationPrincipal UserDetails userDetails){
        Task newTask = taskService.create(request, userDetails);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskMapper.taskToResponse(newTask));
    }

    @PostMapping("{taskId}/changeStatus/{newStatus}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SimpleResponse> changeTaskStatus(@PathVariable Long taskId,
                                                          @PathVariable @Valid TaskStatus newStatus,
                                                          @AuthenticationPrincipal UserDetails userDetails){
        taskService.changeTaskStatus(taskId, newStatus, userDetails);
        return ResponseEntity.ok(new SimpleResponse("Статус задачи изменён!"));
    }

    @PostMapping("{taskId}/appointExecutor/{executorId}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SimpleResponse> appointExecutor(@PathVariable Long taskId,
                                                          @PathVariable Long executorId,
                                                          @AuthenticationPrincipal UserDetails userDetails){
        taskService.appointExecutor(taskId, executorId, userDetails);
        return ResponseEntity.ok(new SimpleResponse("Исполнитель задачи назначен!"));
    }

    @DeleteMapping("/{taskId}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SimpleResponse> deleteOwnTask(@PathVariable Long taskId,
                                                        @AuthenticationPrincipal UserDetails userDetails){
        if(!taskService.existsByTaskId(taskId)){
            throw new EntityNotFoundException("Задачи с таким ID нет!");
        }
        taskService.deleteByTaskId(taskId, userDetails);
        return ResponseEntity.ok(new SimpleResponse("Задача удалена!"));
    }

    @PostMapping("/all/own")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SimpleResponse> deleteAllOwnTasks(@AuthenticationPrincipal UserDetails userDetails){
        taskService.deleteAllOwn(userDetails);
        return ResponseEntity.ok(new SimpleResponse("Все задачи пользователя удалены!"));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<SimpleResponse> updateTask(@PathVariable Long taskId,
                                                     @RequestBody CreateTaskRequest createTaskRequest){
        taskService.updateTask(taskId, createTaskRequest);
        return ResponseEntity.ok(new SimpleResponse("Задача обновлена!"));
    }

}
