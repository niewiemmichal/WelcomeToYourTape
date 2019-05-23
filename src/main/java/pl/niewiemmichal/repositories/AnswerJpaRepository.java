package pl.niewiemmichal.repositories;

import java.util.stream.Stream;

import pl.niewiemmichal.model.Answer;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
public class AnswerJpaRepository extends GenericJpaRepository<Answer, Long> implements AnswerRepository{
    AnswerJpaRepository() { super(Answer.class); }

    @Override
    @Transactional
    public Stream<Answer> findBySurveyId(Long surveyId) {
        return findAll().stream()
                .filter(a -> a.getSurvey().getId().equals(surveyId));
    }

    @Override
    public Stream<Answer> findByQuestionId(Long questionId) {
        return findAll().stream()
                .filter(a -> a.getQuestion().getId().equals(questionId));
    }

    @Override
    public Stream<Answer> findBySurvey_TeacherId(Long teacherId) {
        return findAll().stream()
                .filter(s -> s.getSurvey().getTeacher().getId().equals(teacherId));
    }

    @Override
    public Stream<Answer> findBySurvey_SubjectId(Long subjectId) {
        return findAll().stream()
                .filter(s -> s.getSurvey().getSubject().getId().equals(subjectId));
    }
}
