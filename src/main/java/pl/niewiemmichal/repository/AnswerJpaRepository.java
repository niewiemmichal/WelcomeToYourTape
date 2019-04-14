package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Answer;

import javax.ejb.Stateless;

@Stateless
public class AnswerJpaRepository extends GenericJpaRepository<Answer, Long> implements AnswerRepository{
    AnswerJpaRepository() { super(Answer.class); }
}
