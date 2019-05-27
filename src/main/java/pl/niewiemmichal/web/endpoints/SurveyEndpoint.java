package pl.niewiemmichal.web.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.model.Survey;
import pl.niewiemmichal.model.Teacher;
import pl.niewiemmichal.repositories.AnswerRepository;
import pl.niewiemmichal.repositories.SubjectRepository;
import pl.niewiemmichal.repositories.SurveyRepository;
import pl.niewiemmichal.repositories.TeacherRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/surveys")
public class SurveyEndpoint {

    private SurveyRepository surveyRepository;
    private TeacherRepository teacherRepository;
    private SubjectRepository subjectRepository;
    private AnswerRepository answerRepository;

    @Inject
    public SurveyEndpoint(SurveyRepository surveyRepository,
                          TeacherRepository teacherRepository,
                          SubjectRepository subjectRepository,
                          AnswerRepository answerRepository) {
        this.surveyRepository = surveyRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.answerRepository = answerRepository;
    }

    public SurveyEndpoint() {}

    @GET
    @Path ("/{id}")
    @Produces (MediaType.APPLICATION_JSON)
    @Operation(summary = "Get survey by id",
            tags = "surveys",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Survey.class)),
                            description = "Teacher with given id"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied"),
                    @ApiResponse(responseCode = "404", description = "Survey not found") }
    )
    public Survey getSurvey(@Parameter(description = "Id of existing survey", required = true) @PathParam("id") Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Survey", "id", id.toString()));
    }

    @GET
    @Path ("/{subjectId}/{teacherId}")
    @Produces (MediaType.APPLICATION_JSON)
    @Operation(summary = "Get survey by id",
            tags = "surveys",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Survey.class)),
                            description = "Survey with teacher and subject with given ids"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied"),
                    @ApiResponse(responseCode = "404", description = "Survey not found") }
    )
    public Survey getSurvey(@Parameter(description = "Id of existing subject", required = true)
                                @PathParam ("subjectId") Long subjectId,
                            @Parameter(description = "Id of existing teacher", required = true)
                                @PathParam ("teacherId") Long teacherId) {
        return surveyRepository.findByTeacherIdAndSubjectId(teacherId, subjectId)
                .orElseThrow(() -> new ResourceDoesNotExistException("Survey", "teacherId and subjectId",
                        teacherId.toString() + " " + subjectId.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all surveys",
            tags = "surveys",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Survey.class))),
                            description = "All teachers") }
    )
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    @POST
    @Path("/burst/teacher")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add surveys and teacher",
            description = "Creates teacher from first survey, then creates all surveys with this teacher",
            tags = "surveys",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public void addSurveysAndTeacher( @Valid Survey[] surveys ){
        if(surveys.length > 0) {
            final Teacher teacher = teacherRepository.save(surveys[0].getTeacher());
            Arrays.stream(surveys).forEach(survey -> {
                survey.setTeacher(teacher);
                surveyRepository.save(survey);
            });
        }
    }

    @POST
    @Path("/burst/subject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add surveys and subject",
            description = "Creates subject from first survey, then creates all surveys with this subject",
            tags = "surveys",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public void addSurveysAndSubject( @Valid Survey[] surveys ){
        if(surveys.length > 0) {
            final Subject subject = subjectRepository.save(surveys[0].getSubject());
            Arrays.stream(surveys).forEach(survey -> {
                survey.setSubject(subject);
                surveyRepository.save(survey);
            });
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete survey by id",
            description = "Deletes survey if it has no answers",
            tags = "surveys",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Survey deleted or does not exist"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "409", description = "Survey has answers")
            }
    )
    public void deleteSurvey(@Parameter(description = "Id of survey", required = true) @PathParam("id") Long id) {
        surveyRepository.findById(id)
                .ifPresent(this::deleteSurvey);
    }

    private void throwIfHasAnswers(Survey survey, String message) {
        answerRepository.findBySurveyId(survey.getId())
                .findAny()
                .ifPresent(a -> {
                    throw new BadRequestException(message);
                });
    }

    private void deleteSurvey(Survey survey) {
        throwIfHasAnswers(survey, "Cannot delete survey");
        surveyRepository.delete(survey);
    }
}
