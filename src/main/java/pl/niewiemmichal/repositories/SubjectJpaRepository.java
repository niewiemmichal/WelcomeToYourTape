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
}
