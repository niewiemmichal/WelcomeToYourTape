package pl.niewiemmichal.endpoints;

import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.repository.Repository;

import javax.inject.Inject;

public class SubjectEndpoint {
    private Repository<Subject, Long> subjectRepository;

    @Inject
    public SubjectEndpoint(Repository<Subject, Long> subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public SubjectEndpoint() {}
}
