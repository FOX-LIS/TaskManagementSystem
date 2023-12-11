package socialnet.socialnetwork.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import socialnet.socialnetwork.model.Task;
import socialnet.socialnetwork.web.model.TaskResponse;
import socialnet.socialnetwork.web.model.TaskListResponse;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
    TaskResponse taskToResponse(Task task);
    default TaskListResponse taskListToTaskResponseList(List<Task> tasks){
        TaskListResponse response = new TaskListResponse();
        response.setTasks(tasks.stream()
                .map(this::taskToResponse).collect(Collectors.toList()));
        return response;
    }
    
}
