package pl.niewiemmichal.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.niewiemmichal.model.Answer;
import pl.niewiemmichal.model.Survey;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
public class AnswerJpaRepository extends GenericJpaRepository<Answer, Long> implements AnswerRepository{
    AnswerJpaRepository() { super(Answer.class); }

    @Override
    @Transactional
    public List<Answer> findBySurveyId(Long surveyId) {
        return findAll().stream()
                .filter(a -> a.getSurvey().getId().equals(surveyId))
                .collect(Collectors.toList());
    }

}
