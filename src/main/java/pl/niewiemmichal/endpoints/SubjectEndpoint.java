package pl.niewiemmichal.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.repository.Repository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/subjects")
public class SubjectEndpoint {

    private Repository<Subject, Long> subjectRepository;

    @Inject
    public SubjectEndpoint(Repository<Subject, Long> subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public SubjectEndpoint() {}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Subject getSubject(@PathParam("id") Long id){
        return subjectRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Subject", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Subject addSubject(@Valid Subject subject) {
        return subjectRepository.save(subject);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Subject updateSubject(@PathParam("id") Long id, @Valid Subject subject) {
        if(!subjectRepository.findById(id).isPresent())
            throw new ResourceDoesNotExistException("Subject", "id", id.toString());
        else if(subject.getId() != null && !(id.equals(subject.getId())))
            throw new ResourceConflictException("Subject", "id", id.toString(), subject.getId().toString());
        else {
            subject.setId(id);
            return subjectRepository.update(subject);
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteSubject(@PathParam("id") Long id) {
        subjectRepository.findById(id)
                .ifPresent(q -> subjectRepository.delete(q));
    }
}
