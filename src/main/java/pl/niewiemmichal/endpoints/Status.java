package pl.niewiemmichal.endpoints;

import pl.niewiemmichal.model.Survey;
import pl.niewiemmichal.repository.SurveyRepository;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/status")
public class Status {

    @EJB
    private SurveyRepository surveyRepository;

    @GET
    @Produces("text/html")
    public String status() {
        surveyRepository.save(new Survey("Ankietka"));
        surveyRepository.save(new Survey("Paletka"));
        surveyRepository.save(new Survey("Kupka"));
        return "saved";
    }

    @GET
    @Path("/retrieve")
    @Produces("text/html")
    public String retrieve() {
        return surveyRepository.findAll().toString();
    }

}
