package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Answer;

import javax.ejb.Stateless;

@Stateless
public class AnswerRepository extends GenericRepository<Answer, Long> implements Repository<Answer, Long>{
    AnswerRepository() { super(Answer.class); }
}
