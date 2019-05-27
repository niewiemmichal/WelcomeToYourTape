package pl.niewiemmichal.web.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
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
    @Operation(summary = "Get question by id",
            tags = "questions",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Question.class)),
                            description = "Question with given id"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied"),
                    @ApiResponse(responseCode = "404", description = "Question not found") }
    )
    public Question getQuestion(@Parameter(description = "Id of existing question", required = true) @PathParam("id") Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Question", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all answers",
            tags = "questions",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Question.class))),
                            description = "All questions") }
    )
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add question",
            tags = "questions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public Question addQuestion(@Valid Question question) {
        question.setId(null);
        return questionRepository.save(question);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update question by id",
            description = "Updates question if it has no answers",
            tags = "questions",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Question.class)),
                            description = "Updated question"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "409", description = "Question has answers"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public Question updateQuestion(@Parameter(description = "Id of existing question", required = true) @PathParam("id") Long id, @Valid Question question) {
        return questionRepository.findById(id)
                .map(q -> updateQuestion(q, question))
                .orElseGet(() -> addQuestion(question));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete question by id",
            description = "Deletes question if it has no answers",
            tags = "questions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Question deleted or does not exist"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied"),
                    @ApiResponse(responseCode = "409", description = "Question has answers")
            }
    )
    public void deleteQuestion(@Parameter(description = "Id of existing question", required = true) @PathParam("id") Long id) {
        questionRepository.findById(id)
                .ifPresent(this::deleteQuestion);
    }

    private void throwIfHasAnswers(Question question, String message) {
        answerRepository.findByQuestionId(question.getId())
                .findAny()
                .ifPresent(a -> {
                    throw new ResourceConflictException(message);
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
