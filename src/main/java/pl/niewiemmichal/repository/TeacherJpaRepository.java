package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Teacher;

import javax.ejb.Stateless;

@Stateless
public class TeacherJpaRepository
        extends GenericJpaRepository<Teacher, Long> implements TeacherRepository {
    TeacherJpaRepository() {super(Teacher.class);}
}