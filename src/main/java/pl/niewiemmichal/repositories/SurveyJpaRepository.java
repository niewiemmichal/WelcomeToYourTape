package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Survey;

import javax.ejb.Stateless;

@Stateless
public class SurveyJpaRepository extends GenericJpaRepository<Survey, Long> implements SurveyRepository {
    SurveyJpaRepository() { super(Survey.class); }
}
