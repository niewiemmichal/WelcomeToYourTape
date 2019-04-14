package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Question;

import javax.ejb.Stateless;

@Stateless
public class QuestionJpaRepository
        extends GenericJpaRepository<Question, Long> implements QuestionRepository {
    QuestionJpaRepository() { super(Question.class); }
}
