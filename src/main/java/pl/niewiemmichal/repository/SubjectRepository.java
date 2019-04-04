package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Subject;

import javax.ejb.Stateless;

@Stateless
public class SubjectRepository extends GenericRepository<Subject, Long> implements Repository<Subject, Long> {
    SubjectRepository() { super(Subject.class); }
}
