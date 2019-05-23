package pl.niewiemmichal.web.endpoints;

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
    public Subject getSubject(@PathParam("id") Long id){
        return subjectRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Subject", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Subject addSubject(@Valid Subject subject) {
        subject.setId(null);
        return subjectRepository.save(subject);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Subject updateSubject(@PathParam("id") Long id, @Valid Subject subject) {
        return subjectRepository.findById(id)
                .map(s -> updateSubject(s, subject))
                .orElseGet(() -> addSubject(subject));
    }

    @DELETE
    @Path("/{id}")
    public void deleteSubject(@PathParam("id") Long id) {
        subjectRepository.findById(id)
                .ifPresent(this::deleteSubjectAndSurveys);
    }

    private void throwIfHasAnswers(Subject subject, String message) {
        answerRepository.findBySurvey_SubjectId(subject.getId())
                .findAny()
                .ifPresent(a -> {
                    throw new BadRequestException(message);
                });
    }

    private void deleteSubjectAndSurveys(Subject subject) {
        throwIfHasAnswers(subject, "Cannot delete subject");
        subjectRepository.delete(subject);
        surveyRepository.deleteByTeacherId(subject.getId());
    }

    private Subject updateSubject(Subject subject, Subject newSubject) {
        throwIfHasAnswers(subject, "Cannot update subject");
        subject.setName(newSubject.getName());
        subject.setSemester(newSubject.getSemester());
        subject.setYear(newSubject.getYear());
        return subjectRepository.update(subject);
    }
}
