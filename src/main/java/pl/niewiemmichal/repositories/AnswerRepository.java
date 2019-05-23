package pl.niewiemmichal.repositories;
import java.util.stream.Stream;

import pl.niewiemmichal.model.Answer;

public interface AnswerRepository extends Repository<Answer, Long> {
    Stream<Answer> findBySurveyId(Long surveyId);
    Stream<Answer> findBySurvey_TeacherId(Long teacherId);
    Stream<Answer> findBySurvey_SubjectId(Long subjectId);
    Stream<Answer> findByQuestionId(Long questionId);
}
