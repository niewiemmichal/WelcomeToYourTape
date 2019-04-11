package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Survey;

public class SurveyRepository extends GenericRepository<Survey, Long> implements Repository<Survey, Long> {
    SurveyRepository() { super(Survey.class); }
}
