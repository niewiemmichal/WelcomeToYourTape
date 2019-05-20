package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Survey;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.Optional;

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
}
