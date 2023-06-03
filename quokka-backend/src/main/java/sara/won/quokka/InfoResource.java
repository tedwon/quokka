package sara.won.quokka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import sara.won.quokka.utiles.date.Date;

@Path("/quokka/api/v1/info")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfoResource {

    private static final Logger LOGGER = Logger.getLogger(InfoResource.class);

    @ConfigProperty(name = "quokka.myage.year", defaultValue = "1910")
    String YEAR;

    @ConfigProperty(name = "quokka.myage.month", defaultValue = "8")
    String MONTH;

    @ConfigProperty(name = "quokka.myage.dayOfMonth", defaultValue = "29")
    String DAY_OF_MONTH;

    @GET
    @Path("myage")
    public String myage() {
        return Date.calcMyAge(Integer.parseInt(YEAR), Integer.parseInt(MONTH), Integer.parseInt(DAY_OF_MONTH));
    }
}
