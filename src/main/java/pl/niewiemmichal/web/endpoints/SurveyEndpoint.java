package pl.niewiemmichal.web.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.model.Survey;
import pl.niewiemmichal.model.Teacher;
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

    @Inject
    public SurveyEndpoint(SurveyRepository surveyRepository,
                          TeacherRepository teacherRepository,
                          SubjectRepository subjectRepository) {
        this.surveyRepository = surveyRepository;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
    }

    public SurveyEndpoint() {}

    @GET
    @Path ("/{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Survey getSurvey(@PathParam ("subjectId") Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Survey with id=" + id + " does not exist"));
    }

    @GET
    @Path ("/{subjectId}/{teacherId}")
    @Produces (MediaType.APPLICATION_JSON)
    public Survey getSurvey(@PathParam ("subjectId") Long subjectId, @PathParam ("teacherId") Long teacherId) {
        return surveyRepository.findByTeacherIdAndSubjectId(teacherId, subjectId)
                .orElseThrow(() -> new NotFoundException("Survey for teacher with id=" + teacherId +
                        " and subject with id=" + subjectId + " does not exist"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Survey addSurvey(@Valid Survey survey) {
        return surveyRepository.save(survey);
    }

    @POST
    @Path("/burst/teacher")
    @Consumes(MediaType.APPLICATION_JSON)
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
    public void addSurveysAndSubject( @Valid Survey[] surveys ){
        if(surveys.length > 0) {
            final Subject subject = subjectRepository.save(surveys[0].getSubject());
            Arrays.stream(surveys).forEach(survey -> {
                survey.setSubject(subject);
                surveyRepository.save(survey);
            });
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Survey updateSurvey(@PathParam("id") Long id, @Valid Survey survey) {
        if(!surveyRepository.findById(id).isPresent())
            throw new NotFoundException("Survey with id=" + id + " does not exist");
        else if(survey.getId() != null && !(id.equals(survey.getId())))
            throw new BadRequestException("Survey id does not equal id provided in uri");
        else {
            survey.setId(id);
            return surveyRepository.update(survey);
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteSurvey(@PathParam("id") Long id) {
        surveyRepository.findById(id)
                .ifPresent(q -> surveyRepository.delete(q));
    }
}
