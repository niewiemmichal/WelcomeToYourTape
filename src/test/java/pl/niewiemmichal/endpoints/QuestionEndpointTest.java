package pl.niewiemmichal.endpoints;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Question;
import pl.niewiemmichal.repositories.QuestionRepository;
import pl.niewiemmichal.web.endpoints.QuestionEndpoint;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class QuestionEndpointTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionEndpoint questionEndpoint;

    private Question question = new Question("contents", false);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnQuestion() {
        //given
        given(questionRepository.findById(1L))
                .willReturn(Optional.of(question));
        //when
        Question actual = questionEndpoint.getQuestion(1L);

        //then
        assertThat(actual).isEqualTo(question);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowResourceDoesNotExistException() {
        //given
        given(questionRepository.findById(2L))
                .willReturn(Optional.empty());

        //when
        questionEndpoint.getQuestion(2L);

        //then
        //expect exception
    }

    @Test
    public void shouldReturnAllQuestions() {
        //given
        List<Question> questions = Lists.newArrayList(question, question, question);
        given(questionRepository.findAll()).willReturn(questions);

        //when
        List<Question> actual = questionEndpoint.getAllQuestions();

        //then
        assertThat(actual).containsExactlyElementsOf(questions);
    }

    @Test
    public void shouldSaveQuestion() {
        //given
        given(questionRepository.save(question)).willReturn(question);

        //when
        Question actual = questionEndpoint.addQuestion(question);

        //then
        verify(questionRepository).save(question);
        assertThat(actual).isEqualTo(question);
    }

    @Test
    public void shouldUpdateQuestion() {
        //given
        given(questionRepository.findById(3L)).willReturn(Optional.of(question));
        given(questionRepository.update(question)).willReturn(question);

        //when
        Question actual = questionEndpoint.updateQuestion(3L, question);

        //then
        verify(questionRepository).update(question);
        assertThat(actual).isEqualTo(question);
    }

    @Test(expected = ResourceConflictException.class)
    public void shouldReturnResourceConflictException() {
        //given
        given(questionRepository.findById(6L)).willReturn(Optional.of(question));

        //when
        Question diffQuestion= new Question("contents", false);
        diffQuestion.setId(10L);
        questionEndpoint.updateQuestion(6L, diffQuestion);

        //then
        //expect exception
    }

    @Test
    public void shouldDeleteQuestion() {
        //given
        given(questionRepository.findById(5L)).willReturn(Optional.of(question));

        //when
        questionEndpoint.deleteQuestion(5L);

        //then
        verify(questionRepository).delete(question);
    }

    @Test
    public void shouldNotFailIfDeletingNotExistingQuestion() {
        //given
        given(questionRepository.findById(5L)).willReturn(Optional.empty());

        //when
        questionEndpoint.deleteQuestion(5L);

        //then
        verify(questionRepository, times(0)).delete(question);
    }

}