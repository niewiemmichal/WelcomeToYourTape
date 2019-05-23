package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Survey;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class SurveyJpaRepository extends GenericJpaRepository<Survey, Long> implements SurveyRepository {
    SurveyJpaRepository() { super(Survey.class); }

    @Override
    @Transactional
    public Optional<Survey> findByTeacherIdAndSubjectId(Long teacherId, Long subjectId) {
        return findAll().stream()
                .filter(s -> s.getTeacher().getId().equals(teacherId))
                .filter(s -> s.getSubject().getId().equals(subjectId)).findFirst();
    }

    @Override
    @Transactional
    public void deleteByTeacherId(Long teacherId) {
        findAll().stream()
                .filter(s -> s.getTeacher().getId().equals(teacherId))
                .forEach(this::delete);
    }

    @Override
    @Transactional
    public void deleteBySubjectId(Long subjectId) {
        findAll().stream()
                .filter(s -> s.getSubject().getId().equals(subjectId))
                .forEach(this::delete);
    }
}
