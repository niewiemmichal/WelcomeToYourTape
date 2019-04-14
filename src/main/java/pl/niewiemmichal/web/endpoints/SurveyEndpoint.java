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
    @Path ("/{subjectId}/{teacherId}")
    @Produces (MediaType.APPLICATION_JSON)
    public Survey getSurvey(@PathParam ("subjectId") Long subjectId, @PathParam ("teacherId") Long teacherId) {
        return surveyRepository.findAll().stream()
                .filter(s -> s.getSubject().getId().equals(subjectId))
                .filter(s -> s.getTeacher().getId().equals(teacherId))
                .findFirst().orElseThrow(() -> new ResourceDoesNotExistException("Survey", "subjectId and teacherId",
                        subjectId.toString() + " and " + teacherId.toString())
                );
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
    @Path("/burst")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSurvey( @Valid Survey[] surveys ){
        Arrays.stream(surveys).forEach(survey -> {
            Optional<Long> teacherId = teacherRepository.getId(survey.getTeacher());
            if(teacherId.isPresent()) survey.getTeacher().setId(teacherId.get());
            else survey.getTeacher().setId(teacherRepository.save(survey.getTeacher()).getId());

            Optional<Long> subjectId = subjectRepository.getId(survey.getSubject());
            if(subjectId.isPresent()) survey.getSubject().setId(subjectId.get());
            else survey.getSubject().setId(subjectRepository.save(survey.getSubject()).getId());

            surveyRepository.save(survey);
        });
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Survey updateSurvey(@PathParam("id") Long id, @Valid Survey survey) {
        if(!surveyRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Survey", "id", id.toString());
        else if(survey.getId() != null && !(id.equals(survey.getId())))
            throw new ResourceConflictException("Survey", "id", id.toString(), survey.getId().toString());
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
