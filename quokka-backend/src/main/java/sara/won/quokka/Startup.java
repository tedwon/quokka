package sara.won.quokka;

import io.quarkus.runtime.StartupEvent;
import sara.won.quokka.models.User;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class Startup {
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
//        User.deleteAll();
//        User.add("admin", "admin123!", "admin");
//        User.add("user", "user123!", "user");
    }
}
