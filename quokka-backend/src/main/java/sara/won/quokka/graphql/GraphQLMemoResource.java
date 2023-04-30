package sara.won.quokka.graphql;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.jboss.logging.Logger;
import sara.won.quokka.MemoService;
import sara.won.quokka.models.Memo;

import jakarta.inject.Inject;
import java.util.List;

/**
 * See https://quarkus.io/guides/smallrye-graphql
 */
@GraphQLApi
public class GraphQLMemoResource {

    private static final Logger LOGGER = Logger.getLogger(GraphQLMemoResource.class);

    @Inject
    MemoService memoService;

    @Query("allMemos")
    @Description("Get all memos from the datasource DB")
    public List<Memo> getAllMemos() {
        return memoService.getAllMemos();
    }
}
