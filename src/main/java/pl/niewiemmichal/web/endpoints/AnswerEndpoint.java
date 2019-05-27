package pl.niewiemmichal.web.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get answer by id",
            tags = "answers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Answer.class)),
                            description = "Answer with given id"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied"),
                    @ApiResponse(responseCode = "404", description = "Answer not found") }
    )
    public Answer getAnswer(@Parameter(description = "Id of existing answer", required = true) @PathParam("id") Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Answer", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all answers",
            tags = "answers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Answer.class))),
                            description = "All answers") }
    )
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    @GET
    @Path("/s/{surveyId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all answers for survey",
            tags = "answers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Answer.class))),
                            description = "All answers for survey"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied") }
    )
    public List<Answer> getAllSurveyAnswers(@Parameter(description = "Id of existing survey", required = true) @PathParam("surveyId") Long surveyId) {
        return answerRepository.findBySurveyId(surveyId).collect(Collectors.toList());
}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add multiple answers",
            tags = "answers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public void addAnswers(@Valid Answer[] answers) {
        Arrays.stream(answers).forEach(this::addAnswer);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update answer by id",
            tags = "answers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Answer.class)),
                            description = "Updated answer"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public Answer updateAnswer(@Parameter(description = "Id of existing answer", required = true) @PathParam("id") Long id, @Valid Answer answer) {
        return answerRepository.findById(id)
                .map(a -> updateAnswer(a, answer))
                .orElseGet(() -> addAnswer(answer));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete answer by id",
            tags = "answers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Answer deleted or does not exist"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied")
            }
    )
    public void deleteAnswer(@Parameter(description = "Id of existing answer", required = true) @PathParam("id") Long id) {
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
