package pl.niewiemmichal.web.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Answer;
import pl.niewiemmichal.repositories.AnswerRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return answerRepository.findBySurveyId(surveyId).collect(Collectors.toList());
}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addAnswers(@Valid Answer[] answers) {
        Arrays.stream(answers).forEach(this::addAnswer);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Answer updateAnswer(@PathParam("id") Long id, @Valid Answer answer) {
        return answerRepository.findById(id)
                .map(a -> updateAnswer(a, answer))
                .orElseGet(() -> addAnswer(answer));
    }

    @DELETE
    @Path("/{id}")
    public void deleteAnswer(@PathParam("id") Long id) {
        answerRepository.findById(id)
                .ifPresent(q -> answerRepository.delete(q));
    }

    private Answer addAnswer(Answer answer) {
        answer.setId(null);
        return answerRepository.save(answer);
    }

    private Answer updateAnswer(Answer answer, Answer newAnswer) {
        if(answer.getQuestion().getIsOpen()) {
            answer.setContents(newAnswer.getContents());
        } else {
            answer.setRating(newAnswer.getRating());
        }
        return answerRepository.update(answer);
    }
}
