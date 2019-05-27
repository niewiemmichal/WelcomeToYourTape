package pl.niewiemmichal.web.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.repositories.AnswerRepository;
import pl.niewiemmichal.repositories.SubjectRepository;
import pl.niewiemmichal.repositories.SurveyRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/subjects")
public class SubjectEndpoint {

    private SubjectRepository subjectRepository;
    private AnswerRepository answerRepository;
    private SurveyRepository surveyRepository;

    @Inject
    public SubjectEndpoint(SubjectRepository subjectRepository, AnswerRepository answerRepository,
                           SurveyRepository surveyRepository) {
        this.subjectRepository = subjectRepository;
        this.answerRepository = answerRepository;
        this.surveyRepository = surveyRepository;
    }

    public SubjectEndpoint() {}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get subject by id",
            tags = "subjects",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Subject.class)),
                            description = "Subject with given id"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied"),
                    @ApiResponse(responseCode = "404", description = "Subject not found") }
    )
    public Subject getSubject(@Parameter(description = "Id of existing subject", required = true) @PathParam("id") Long id){
        return subjectRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Subject", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all subjects",
            tags = "subjects",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Subject.class))),
                            description = "All subjects") }
    )
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add subject",
            tags = "subjects",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public Subject addSubject(@Valid Subject subject) {
        subject.setId(null);
        return subjectRepository.save(subject);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update subject by id",
            description = "Updates subject if it has no surveys answered",
            tags = "subjects",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Subject.class)),
                            description = "Updated subject"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "409", description = "Subject has surveys answered"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public Subject updateSubject(@Parameter(description = "Id of existing subject", required = true) @PathParam("id") Long id, @Valid Subject subject) {
        return subjectRepository.findById(id)
                .map(s -> updateSubject(s, subject))
                .orElseGet(() -> addSubject(subject));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete subject by id",
            description = "Deletes subject if it has no surveys answered",
            tags = "subjects",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subject deleted or does not exist"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "409", description = "Subject has surveys answered")
            }
    )
    public void deleteSubject(@Parameter(description = "Id of subject", required = true) @PathParam("id") Long id) {
        subjectRepository.findById(id)
                .ifPresent(this::deleteSubjectAndSurveys);
    }

    private void throwIfHasAnswers(Subject subject, String message) {
        answerRepository.findBySurvey_SubjectId(subject.getId())
                .findAny()
                .ifPresent(a -> {
                    throw new ResourceConflictException(message);
                });
    }

    private void deleteSubjectAndSurveys(Subject subject) {
        throwIfHasAnswers(subject, "Cannot delete subject");
        surveyRepository.deleteBySubjectId(subject.getId());
        subjectRepository.delete(subject);
    }

    private Subject updateSubject(Subject subject, Subject newSubject) {
        throwIfHasAnswers(subject, "Cannot update subject");
        subject.setName(newSubject.getName());
        subject.setSemester(newSubject.getSemester());
        subject.setYear(newSubject.getYear());
        return subjectRepository.update(subject);
    }
}
