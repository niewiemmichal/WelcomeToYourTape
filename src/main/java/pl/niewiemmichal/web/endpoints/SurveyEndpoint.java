package pl.niewiemmichal.web.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Survey;
import pl.niewiemmichal.repository.Repository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/surveys")
public class SurveyEndpoint {

    private Repository<Survey, Long> surveyRepository;

    @Inject
    public SurveyEndpoint(Repository<Survey, Long> surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public SurveyEndpoint() {}

    @GET
    @Path ("/{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Survey getSurvey(@PathParam ("id") Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Survey", "id", id.toString()));
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