package socialnet.socialnetwork.web.model;

import lombok.Data;
import socialnet.socialnetwork.model.Comment;
import socialnet.socialnetwork.model.TaskPriority;
import socialnet.socialnetwork.model.TaskStatus;
import socialnet.socialnetwork.model.User;

import java.util.Collection;

@Data
public class TaskResponse {

    private Long id;

    private String header;

    private String description;

    private TaskStatus taskStatus;

    private TaskPriority taskPriority;

    private User author;

    private User executor;

    private Collection<Comment> comments;

}
