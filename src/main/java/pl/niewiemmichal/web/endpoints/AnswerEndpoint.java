package pl.niewiemmichal.web.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Answer;
import pl.niewiemmichal.repositories.AnswerRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("/answers")
public class AnswerEndpoint {

    private AnswerRepository answerRepository;

    @Inject
    public AnswerEndpoint(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public AnswerEndpoint() {}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Answer getAnswer(@PathParam("id") Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Answer", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    @GET
    @Path("/s/{surveyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Answer> getAllSurveyAnswers(@PathParam("surveyId") Long surveyId) {
        return answerRepository.findBySurveyId(surveyId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addAnswer(@Valid Answer[] answers) {
        Arrays.stream(answers).forEach(answerRepository::save);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Answer updateAnswer(@PathParam("id") Long id, @Valid Answer answer) {
        if(!answerRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Answer", "id", id.toString());
        else if(answer.getId() != null && !(id.equals(answer.getId())))
            throw new ResourceConflictException("Answer", "id", id.toString(), answer.getId().toString());
        else {
            answer.setId(id);
            return answerRepository.update(answer);
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteAnswer(@PathParam("id") Long id) {
        answerRepository.findById(id)
                .ifPresent(q -> answerRepository.delete(q));
    }
}
