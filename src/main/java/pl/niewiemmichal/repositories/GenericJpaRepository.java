package pl.niewiemmichal.repositories;

import pl.niewiemmichal.commons.exceptions.ResourceConflictException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

abstract class GenericJpaRepository<T, ID extends Serializable> {

    @PersistenceContext(unitName="niewiemmichal")
    protected EntityManager entityManager;

    private Class<T> persistentClass;

    GenericJpaRepository(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(persistentClass, id));
    }

    public List<T> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        return  entityManager.createQuery(query.select(query.from(persistentClass))).getResultList();
    }

    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }

    public void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
}
