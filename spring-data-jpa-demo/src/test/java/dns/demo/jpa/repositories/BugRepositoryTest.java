package dns.demo.jpa.repositories;

import dns.demo.jpa.entities.Bug;
import dns.demo.jpa.entities.Tag;
import jakarta.persistence.EntityManager;
import org.hibernate.loader.MultipleBagFetchException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@DataJpaTest
@ContextConfiguration(classes = JpaTestConfig.class)
class BugRepositoryTest {
    public static final long BUG_ID = 1234L;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BugRepository bugRepository;

    @Test
    void findAllWithMultipleOneToManyRelationsJpa() {
        assertEquals(4, bugRepository.findAllWithMultipleOneToManyRelationsJpa(Pageable.unpaged()).size());
    }

    @Test
    void findAll() {
        InvalidDataAccessApiUsageException e = assertThrows(InvalidDataAccessApiUsageException.class, () -> bugRepository.findAll());
        assertThat(e.getCause().getCause()).isInstanceOf(MultipleBagFetchException.class);
    }

    @Test
    void addTagWithPessimisticLock() {
        Bug bug = bugRepository.findById(BUG_ID).orElseThrow();
        int initialVersion = bug.getVersion();
        bugRepository.addTagWithPessimisticLock(BUG_ID, "pessimistic-lock-tag");
        bug = bugRepository.findById(BUG_ID).orElseThrow();
        Optional<Tag> tagOptional = bug.getTags().stream().filter(t -> t.getId().getTag().equals("pessimistic-lock-tag")).findFirst();
        assertThat(tagOptional).isNotEmpty();
        assertThat(bug.getVersion()).isEqualTo(initialVersion + 1);
    }

    @Test
    void addTagWithOptimisticLock() {
        Bug bug = bugRepository.findById(BUG_ID).orElseThrow();
        int initialVersion = bug.getVersion();

        // A commit in a separate transaction is required to see the version increment
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(REQUIRES_NEW.value());
        transactionTemplate.execute(status ->
                bugRepository.addTagWithOptimisticLock(BUG_ID, "optimistic-lock-tag")
        );

        // Clear level 1 (persistence context) cache
        entityManager.clear();

        bug = bugRepository.findById(BUG_ID).orElseThrow();
        Optional<Tag> tagOptional = bug.getTags().stream().filter(t -> t.getId().getTag().equals("optimistic-lock-tag")).findFirst();
        assertThat(tagOptional).isNotEmpty();
        assertThat(bug.getVersion()).isEqualTo(initialVersion + 1);
    }
}