package sara.won.quokka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.security.RolesAllowed;
import org.jboss.logging.Logger;
import sara.won.quokka.models.Memo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * See https://quarkus.io/guides/hibernate-orm
 */
@Path("/quokka/api/v1/memo")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MemoResource {
    private static final Logger LOGGER = Logger.getLogger(MemoResource.class);

    @Inject
    MemoService memoService;

    @Inject
    EntityManager entityManager;

    @GET
//    @RolesAllowed("admin")
    public List<Memo> get() {
        return memoService.getAllMemos();
    }

    @GET
    @Path("backup")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String backup() {
        List<Memo> allMemos = memoService.getAllMemos();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String allMemosToJson = gson.toJson(allMemos);

        // Write search results to a file
        final String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        final String outputPath = "quokka.backup." + formattedDateTime;
        java.nio.file.Path outputFilePath = java.nio.file.Path.of(outputPath);
        try {
            Files.write(outputFilePath, allMemosToJson.getBytes(StandardCharsets.UTF_8));
            System.out.println("File written successfully in " + Paths.get("").toAbsolutePath().toString() + File.separator + outputFilePath);
        } catch (IOException e) {
            System.out.println("Error writing file");
            e.printStackTrace();
        }
        return "SUCCESS";
    }

    @GET
    @Path("export/{a:text|txt}")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAsText() {
        StringBuilder result = new StringBuilder();
        List<Memo> resultList = entityManager.createNamedQuery("Memos.findAll", Memo.class)
                .getResultList();
        resultList.forEach(memo -> {
            result.append(memo.exportToString());
        });
        return result.toString();
    }

    /**
     * Filter memos for ebook generation
     * @return ebook memos
     */
    @GET
    @Path("ebook")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAsTextForEBookGeneration() {
        StringBuilder result = new StringBuilder();
        List<Memo> resultList = entityManager.createNamedQuery("Memos.findByTag")
                .setParameter("tagSearch", "%ebook%")
                .getResultList();
        resultList.forEach(memo -> {
            result.append(memo.exportToStringForEBook());
        });
        return result.toString();
    }

    @GET
    @Path("search/{keyword}")
    public List<Memo> getByKeyword(String keyword) {
        return entityManager.createNamedQuery("Memos.findByKeyword")
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    @GET
    @Path("pin/{pin}")
    public List<Memo> getByPinFlag(Boolean pin) {
        String namedQuery = "Memos.findAll";
        if (pin) {
            namedQuery = "Memos.findPinned";
        }
        return entityManager.createNamedQuery(namedQuery).getResultList();
    }

    @GET
    @Path("pin/{pin}/{keyword}")
    public List<Memo> getByPingFlagAndKeyword(String pin, String keyword) {
        if (keyword.startsWith("tag:")) {
            String tagSearch = keyword.substring(4);
            System.out.println(tagSearch);
            return entityManager.createNamedQuery("Memos.findByTag")
                    .setParameter("tagSearch", "%" + tagSearch + "%")
                    .getResultList();
        }

        String namedQuery = "Memos.findByKeyword";
        if (Boolean.parseBoolean(pin)) {
            namedQuery = "Memos.findPinnedByKeyword";
        }
        return entityManager.createNamedQuery(namedQuery)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    @GET
    @Path("id/{id}")
    public Memo getSingle(Integer id) {
        Memo entity = entityManager.find(Memo.class, id);
        if (entity == null) {
            throw new WebApplicationException("Memo with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Memo memo) {
        int code = 201;
        if (memo.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        memo.setDate(new Date());
        entityManager.persist(memo);
        return Response.ok(memo).status(code).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Memo update(Integer id, Memo memo) {
        if (memo.getBody() == null) {
            throw new WebApplicationException("Memo was not set on request.", 422);
        }

        Memo entity = entityManager.find(Memo.class, id);

        if (entity == null) {
            throw new WebApplicationException("Memo with id of " + id + " does not exist.", 404);
        }

//        entity.setTitle(memo.getTitle());
//        entity.setBody(memo.getBody());
//        entity.setTags(memo.getTags());
//        entity.setPin(memo.getPin());
//        entity.setDate(new Date());

        /**
         * Update entity.
         *
         * See https://jakarta.ee/specifications/persistence/3.1/apidocs/jakarta.persistence/jakarta/persistence/entitymanager#merge(T)
         *
         * Hibernate:
         *     update
         *         Memo
         *     set
         *         body=?,
         *         date=?,
         *         pin=?,
         *         tags=?,
         *         title=?
         *     where
         *         id=?
         */
        // set updated time
        memo.setDate(new Date());

        // update entity
        entityManager.merge(memo);

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Integer id) {
        Memo entity = entityManager.getReference(Memo.class, id);
        if (entity == null) {
            throw new WebApplicationException("Memo with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {
        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            // Catch duplicate key value violates
            Throwable throwable = exception.getCause();
            while (throwable != null && !(throwable instanceof SQLException)) {
                throwable = throwable.getCause();
            }
            if (throwable instanceof SQLException) {
                SQLException se = (SQLException) throwable;
                if ("23505".equals(se.getSQLState())) {
                    code = 409;
                }
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }
    }
}