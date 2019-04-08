package pl.niewiemmichal.endpoints;

import pl.niewiemmichal.model.Question;
import pl.niewiemmichal.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//status endpointg
@Path("/status")
public class Status {

    @Inject
    private Repository<Question, Long> questionRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Question status() {
        return questionRepository.save(new Question("Czy jestes frajerem?", Boolean.TRUE));
    }
}
