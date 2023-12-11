package socialnet.socialnetwork.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import socialnet.socialnetwork.mapper.CommentMapper;
import socialnet.socialnetwork.service.CommentService;
import socialnet.socialnetwork.web.model.CommentListResponse;
import socialnet.socialnetwork.web.model.SimpleResponse;

@RestController
@RequestMapping("/api/v1/app/task")
@RequiredArgsConstructor
public class CommentController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @PostMapping("/{taskId}/comment")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SimpleResponse> addComment(@PathVariable Long taskId,
                                                     @RequestBody String comment,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        commentService.addComment(taskId, comment, userDetails);
        return ResponseEntity.ok(new SimpleResponse("Комментарий пользователя добавлен!"));
    }


    @GetMapping("/comment/byTaskId/{taskId}")
    public ResponseEntity<CommentListResponse> findAllCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(
                commentMapper.commentListToCommentResponseList(
                        commentService.findAllCommentsByTaskId(taskId)
                )
        );
    }

    @GetMapping("/comment/byCommentAuthorId/{commentAuthorId}")
    public ResponseEntity<CommentListResponse> findAllCommentsByCommentAuthorId(@PathVariable Long commentAuthorId) {
        return ResponseEntity.ok(
                commentMapper.commentListToCommentResponseList(
                        commentService.findAllCommentsByCommentAuthorId(commentAuthorId)
                )
        );
    }

}
