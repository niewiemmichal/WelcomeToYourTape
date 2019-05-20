package pl.niewiemmichal.repositories;

import java.util.List;

import pl.niewiemmichal.model.Answer;

public interface AnswerRepository extends Repository<Answer, Long> {
    List<Answer> findBySurveyId(Long surveyId);
}
