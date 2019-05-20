package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Teacher;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Stateless
public class TeacherJpaRepository
        extends GenericJpaRepository<Teacher, Long> implements TeacherRepository {
    TeacherJpaRepository() {super(Teacher.class);}
}