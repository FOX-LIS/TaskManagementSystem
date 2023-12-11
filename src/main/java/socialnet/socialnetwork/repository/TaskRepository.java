package socialnet.socialnetwork.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import socialnet.socialnetwork.model.Task;
import socialnet.socialnetwork.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @NonNull Optional<Task> findById(@NonNull Long id);

    @NonNull Task save(@NonNull Task task);

    void deleteByIdIn(List<Long>ids);

    List<Task> findAllByAuthor(User author);
    List<Task> findAllByExecutor(User executor);
    Boolean existsByIdAndAuthor(Long id, User author);
    Boolean existsByIdIn(List<Long> ids);

}

