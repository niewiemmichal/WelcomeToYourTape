package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.model.Survey;
import pl.niewiemmichal.model.Teacher;

import javax.ejb.Stateless;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class SurveyRepository extends GenericRepository<Survey, Long> implements Repository<Survey, Long> {
    SurveyRepository() { super(Survey.class); }
}
