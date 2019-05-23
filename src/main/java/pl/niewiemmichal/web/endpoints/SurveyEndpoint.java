package pl.niewiemmichal.web.endpoints;

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
    public Survey getSurvey(@PathParam("id") Long id) {
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
        survey.setId(null);
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

    @DELETE
    @Path("/{id}")
    public void deleteSurvey(@PathParam("id") Long id) {
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
