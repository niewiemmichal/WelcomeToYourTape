package pl.niewiemmichal.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class GenericRepository<T, ID extends Serializable> {

    @PersistenceContext(unitName="niewiemmichal")
    protected EntityManager entityManager;

    private Class<T> persistentClass;

    GenericRepository(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public Optional<T> findById(ID id) {
        return Optional.of(entityManager.find(persistentClass, id));
    }

    public List<T> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        return  entityManager.createQuery(query.select(query.from(persistentClass))).getResultList();
    }

    @Transactional
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Transactional
    public void delete(T entity) {
        entityManager.remove(entity);
    }
}
