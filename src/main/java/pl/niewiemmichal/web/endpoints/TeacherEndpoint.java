package pl.niewiemmichal.web.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
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
    public Teacher getTeacher(@PathParam("id") Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Teacher", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Path("/subject/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teacher> getAllTeachersBySubject(@PathParam("id") Long id) {
        return surveyRepository.findAll().stream()
                .filter(s -> s.getSubject().getId().equals(id))
                .map(Survey::getTeacher)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Teacher addTeacher(@Valid Teacher teacher) {
        teacher.setId(null);
        return teacherRepository.save(teacher);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Teacher updateTeacher(@PathParam("id") Long id, @Valid Teacher teacher) {
        return teacherRepository.findById(id)
                .map(t -> updateTeacher(t, teacher))
                .orElseGet(() -> addTeacher(teacher));
    }

    @DELETE
    @Path("/{id}")
    public void deleteTeacher(@PathParam("id") Long id) {
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
