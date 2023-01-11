package sara.won.quokka;

import org.jboss.logging.Logger;
import sara.won.quokka.models.Memo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Refer to https://quarkus.io/guides/cdi
 */
@ApplicationScoped
public class MemoService {

    private static final Logger LOGGER = Logger.getLogger(MemoService.class);

    @Inject
    EntityManager entityManager;

    public List<Memo> getAllMemos() {
        return entityManager.createNamedQuery("Memos.findAll", Memo.class)
                .getResultList();
    }
}
