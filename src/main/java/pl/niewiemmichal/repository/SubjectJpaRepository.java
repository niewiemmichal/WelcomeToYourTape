package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Subject;

import javax.ejb.Stateless;

@Stateless
public class SubjectJpaRepository extends GenericJpaRepository<Subject, Long> implements SubjectRepository {
    SubjectJpaRepository() { super(Subject.class); }
}
