package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Survey;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Stateless
public class SurveyJpaRepository extends GenericJpaRepository<Survey, Long> implements SurveyRepository {
    SurveyJpaRepository() { super(Survey.class); }

    @Override
    public Optional<Survey> findByTeacherIdAndSubjectId(Long teacherId, Long subjectId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Survey> query = builder.createQuery(Survey.class);
        Root<Survey> root = query.from(Survey.class);
        query.select(root)
                .where(builder.and(
                        builder.equal(root.get("subject_id"), subjectId),
                        builder.equal(root.get("teacher_id"), teacherId)
                ));

        return entityManager.createQuery(query).getResultStream().findAny();
    }
}
