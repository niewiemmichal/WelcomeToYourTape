package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Survey;
import javax.ejb.Stateless;

@Stateless
public class SurveyRepository extends GenericRepository<Survey, Long> {
    public SurveyRepository() {
        super(Survey.class);
    }
}