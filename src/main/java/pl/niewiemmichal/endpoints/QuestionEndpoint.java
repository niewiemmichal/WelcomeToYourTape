package pl.niewiemmichal.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Question;
import pl.niewiemmichal.repository.Repository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/questions")
public class QuestionEndpoint {

    private Repository<Question, Long> questionRepository;

    @Inject
    public QuestionEndpoint(Repository<Question, Long> questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionEndpoint() {}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Question getQuestion(@PathParam("id") Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Question", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Question addQuestion(@Valid Question question) {
        return questionRepository.save(question);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Question updateQuestion(@PathParam("id") Long id, @Valid Question question) {
        if(questionRepository.findById(id).isPresent()) return questionRepository.update(question);
        else return questionRepository.save(question);
    }

    @DELETE
    @Path("/{id}")
    public void deleteQuestion(@PathParam("id") Long id) {
        questionRepository.findById(id)
                .ifPresent(q -> questionRepository.delete(q));
    }
}
