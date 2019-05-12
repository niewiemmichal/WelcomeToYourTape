package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.model.Teacher;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless
public class SubjectJpaRepository extends GenericJpaRepository<Subject, Long> implements SubjectRepository {
    SubjectJpaRepository() { super(Subject.class); }

    @Override
    public Optional<Long> getId(Subject subject) {
        TypedQuery<Subject> query = entityManager.createQuery("select s from Subject s " +
                "where s.name = :name and s.year = :year and s.semester = :semester", Subject.class);
        query.setParameter("name", subject.getName());
        query.setParameter("year", subject.getYear());
        query.setParameter("semester", subject.getSemester());
        return query.getResultStream().findAny().map(Subject::getId);
    }
}
