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
import pl.niewiemmichal.repositories.SurveyRepository;
import pl.niewiemmichal.repositories.TeacherRepository;

import javax.inject.Inject;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path ("/teachers")
public class TeacherEndpoint {

    private TeacherRepository teacherRepository;
    private SurveyRepository surveyRepository;
    private AnswerRepository answerRepository;

    @Inject
    public TeacherEndpoint(TeacherRepository teacherRepository,
                           SurveyRepository surveyRepository,
                           AnswerRepository answerRepository) {
        this.teacherRepository = teacherRepository;
        this.surveyRepository = surveyRepository;
        this.answerRepository = answerRepository;
    }

    public TeacherEndpoint() {}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get teacher by id",
            tags = "teachers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Teacher.class)),
                            description = "Teacher with given id"),
                    @ApiResponse(responseCode = "400", description = "Invalid id format supplied"),
                    @ApiResponse(responseCode = "404", description = "Teacher not found") }
    )
    public Teacher getTeacher(@Parameter(description = "Id of existing teacher", required = true) @PathParam("id") Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Teacher", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all teachers",
            tags = "teachers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Teacher.class))),
                            description = "All teachers") }
    )
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Path("/subject/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all teachers by subject",
            tags = "teachers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Teacher.class))),
                            description = "All teachers by subject") }
    )
    public List<Teacher> getAllTeachersBySubject(@Parameter(description = "Id of subject", required = true) @PathParam("id") Long id) {
        return surveyRepository.findAll().stream()
                .filter(s -> s.getSubject().getId().equals(id))
                .map(Survey::getTeacher)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add teacher",
            tags = "teachers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public Teacher addTeacher(@Valid Teacher teacher) {
        teacher.setId(null);
        return teacherRepository.save(teacher);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update teacher by id",
            description = "Updates teacher if it has no surveys answered",
            tags = "teachers",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Teacher.class)),
                            description = "Updated teacher"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "409", description = "Teacher has surveys answered"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public Teacher updateTeacher(@Parameter(description = "Id of existing teacher", required = true) @PathParam("id") Long id, @Valid Teacher teacher) {
        return teacherRepository.findById(id)
                .map(t -> updateTeacher(t, teacher))
                .orElseGet(() -> addTeacher(teacher));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete teacher by id",
            description = "Deletes teacher if it has no surveys answered",
            tags = "teachers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Teacher deleted or does not exist"),
                    @ApiResponse(responseCode = "400", description = "Invalid body"),
                    @ApiResponse(responseCode = "409", description = "Teacher has surveys answered")
            }
    )
    public void deleteTeacher(@Parameter(description = "Id of teacher", required = true) @PathParam("id") Long id) {
        teacherRepository.findById(id)
                .ifPresent(this::deleteTeacherAndSurveys);
    }

    private void throwIfHasAnswers(Teacher teacher, String message) {
        answerRepository.findBySurvey_TeacherId(teacher.getId())
                .findAny()
                .ifPresent(a -> {
                    throw new BadRequestException(message);
                });
    }

    private void deleteTeacherAndSurveys(Teacher teacher) {
        throwIfHasAnswers(teacher, "Cannot delete teacher");
        teacherRepository.delete(teacher);
        surveyRepository.deleteByTeacherId(teacher.getId());
    }

    private Teacher updateTeacher(Teacher teacher, Teacher newTeacher) {
        throwIfHasAnswers(teacher, "Cannot update teacher");
        teacher.setDegree(newTeacher.getDegree());
        teacher.setName(newTeacher.getName());
        teacher.setSurname(newTeacher.getSurname());
        return teacherRepository.update(teacher);
    }
}
