package socialnet.socialnetwork.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import socialnet.socialnetwork.model.Comment;
import socialnet.socialnetwork.web.model.CommentListResponse;
import socialnet.socialnetwork.web.model.CommentResponse;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentResponse commentToResponse(Comment comment);
    
    default CommentListResponse commentListToCommentResponseList(List<Comment> comments){
        CommentListResponse response = new CommentListResponse();
        response.setComments(comments.stream()
                .map(this::commentToResponse).collect(Collectors.toList()));
        return response;
    }
    
}
