package pl.niewiemmichal;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("")
@OpenAPIDefinition(
        info = @Info(
            title = "Welcome To Your Tape API Documentation",
            version = "1.0"),
        servers = @Server(
                description = "Application running locally",
                url = "http://localhost:8080/app")
)
public class WebApp extends Application {
}
