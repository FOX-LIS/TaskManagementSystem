package socialnet.socialnetwork.web.model;

import lombok.Data;
import socialnet.socialnetwork.model.*;

import java.util.Collection;

@Data
public class CommentResponse {

    private Long id;

    private String value;

    private User commentAuthor;

    private Task task;

}
