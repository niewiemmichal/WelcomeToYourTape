package pl.niewiemmichal.endpoints;

import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Teacher;
import pl.niewiemmichal.repository.Repository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path ("/teachers")
public class TeacherEndpoint {

    private Repository<Teacher, Long> teacherRepository;

    @Inject
    public TeacherEndpoint(Repository<Teacher, Long> teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public TeacherEndpoint() {}

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Teacher getTeacher(@PathParam("id") Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceDoesNotExistException("Question", "id", id.toString()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Teacher addTeacher(@Valid Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Teacher updateTeacher(@PathParam("id") Long id, @Valid Teacher teacher) {
        if(teacherRepository.findById(id).isPresent()) return teacherRepository.update(teacher);
        else return teacherRepository.save(teacher);
    }

    @DELETE
    @Path("/{id}")
    public void deleteTeacher(@PathParam("id") Long id) {
        teacherRepository.findById(id)
                .ifPresent(q -> teacherRepository.delete(q));
    }
}
