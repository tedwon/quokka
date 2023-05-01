// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;

public class RunBackupWithCamelCLI extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        String user = "admin";
        String pwd = "admin123!";
        String queryParameters = "authMethod=Basic&authUsername=" + user + "&authPassword=" + pwd;

        // Backup every 12 hours
        // 12 hours * 3600 seconds/hour = 43200 seconds
        from("timer:java?period={{time:43200000}}").routeId("Quokka Backup")
                .to("http://localhost:8080/quokka/api/v1/memo/backup?" + queryParameters)
                .setBody()
                .simple("${routeId} is done")
                .log("${body}");
    }
}
