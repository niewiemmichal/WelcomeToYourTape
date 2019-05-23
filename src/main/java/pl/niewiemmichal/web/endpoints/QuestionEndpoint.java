package pl.niewiemmichal.web.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Question;
import pl.niewiemmichal.repositories.AnswerRepository;
import pl.niewiemmichal.repositories.QuestionRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/questions")
public class QuestionEndpoint {

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    @Inject
    public QuestionEndpoint(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
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
        question.setId(null);
        return questionRepository.save(question);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Question updateQuestion(@PathParam("id") Long id, @Valid Question question) {
        return questionRepository.findById(id)
                .map(q -> updateQuestion(q, question))
                .orElseGet(() -> addQuestion(question));
    }

    @DELETE
    @Path("/{id}")
    public void deleteQuestion(@PathParam("id") Long id) {
        questionRepository.findById(id)
                .ifPresent(this::deleteQuestion);
    }

    private void throwIfHasAnswers(Question question, String message) {
        answerRepository.findByQuestionId(question.getId())
                .findAny()
                .ifPresent(a -> {
                    throw new BadRequestException(message);
                });
    }

    private void deleteQuestion(Question question) {
        throwIfHasAnswers(question, "Cannot delete question");
        questionRepository.delete(question);
    }

    private Question updateQuestion(Question question, Question newQuestion) {
        throwIfHasAnswers(question, "Cannot update question");
        question.setContents(newQuestion.getContents());
        question.setIsOpen(newQuestion.getIsOpen());
        return questionRepository.update(question);
    }
}
