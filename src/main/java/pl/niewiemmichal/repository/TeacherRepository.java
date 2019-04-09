package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Teacher;

import javax.ejb.Stateless;

@Stateless
public class TeacherRepository
        extends GenericRepository<Teacher, Long> implements Repository<Teacher, Long> {
    TeacherRepository() {super(Teacher.class);}
}