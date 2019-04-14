package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Teacher;

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

    @Override
    public Optional<Long> getId(Teacher teacher) {
        TypedQuery<Teacher> query = entityManager.createQuery("select t from Teacher t " +
                "where t.name = :name and t.surname = :surname and t.degree = :degree", Teacher.class);
        query.setParameter("name", teacher.getName());
        query.setParameter("surname", teacher.getSurname());
        query.setParameter("degree", teacher.getDegree());
        return query.getResultStream().findAny().map(Teacher::getId);
    }
}