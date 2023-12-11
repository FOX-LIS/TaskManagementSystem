package socialnet.socialnetwork.web.model;

import jakarta.persistence.*;
import lombok.*;
import socialnet.socialnetwork.model.TaskPriority;
import socialnet.socialnetwork.model.TaskStatus;
import socialnet.socialnetwork.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTaskRequest {

    private String header;

    private String description;

    private TaskStatus taskStatus;

    private TaskPriority taskPriority;

    private User author;

    private User executor;

}
