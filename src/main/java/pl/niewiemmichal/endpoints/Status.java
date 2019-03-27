package pl.niewiemmichal.endpoints;

import pl.niewiemmichal.model.Question;
import pl.niewiemmichal.repository.QuestionRepository;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.stream.Collectors;

@Path("/status")
public class Status {

    @EJB
    private QuestionRepository questionRepository;

    @GET
    @Produces("text/html")
    public String status() {
        return questionRepository.save(new Question("Czy jestes frajerem?", Boolean.TRUE)).toString();
    }

    @GET
    @Path("/retrieve")
    @Produces("text/html")
    public String retrieve() {
        return questionRepository.findAll().toString();
    }

}
