package dns.demo.jpa.repositories;

import dns.demo.jpa.entities.Bug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBugRepository {

    List<Bug> findAllWithMultipleOneToManyRelationsJpa(Pageable pageable);

    Page<Bug> findAllWithMultipleOneToManyRelationsBlazePersistence(Pageable pageable);

    Bug addTagWithPessimisticLock(Long id, String tag);

    Bug addTagWithOptimisticLock(Long id, String tag);
}
