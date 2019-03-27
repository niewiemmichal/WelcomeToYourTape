package pl.niewiemmichal.repository;

import pl.niewiemmichal.model.Question;

import javax.ejb.Stateless;

@Stateless
public class QuestionRepository extends GenericRepository<Question, Long> {
    QuestionRepository() { super(Question.class); }
}
