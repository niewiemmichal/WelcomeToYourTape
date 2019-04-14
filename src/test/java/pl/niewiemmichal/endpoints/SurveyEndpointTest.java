package pl.niewiemmichal.endpoints;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.model.*;
import pl.niewiemmichal.repositories.SurveyRepository;
import pl.niewiemmichal.web.endpoints.SurveyEndpoint;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SurveyEndpointTest {

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private SurveyEndpoint surveyEndpoint;

    private Teacher teacher = new Teacher("New", "Teacher", AcademicDegree.DOCTORAL);
    private Subject subject = new Subject("subject", 2019, 2);
    private Survey survey = new Survey(teacher, subject);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void shouldReturnSurvey() {
//        //given
//        given(surveyRepository.findById(1L))
//                .willReturn(Optional.of(survey));
//        //when
//        Survey actual = surveyEndpoint.getSurvey(1L);
//
//        //then
//        assertThat(actual).isEqualTo(survey);
//    }
//
//    @Test(expected = ResourceDoesNotExistException.class)
//    public void shouldThrowResourceDoesNotExistException() {
//        //given
//        given(surveyRepository.findById(2L))
//                .willReturn(Optional.empty());
//
//        //when
//        surveyEndpoint.getSurvey(2L);
//
//        //then
//        //expect exception
//    }

    @Test
    public void shouldReturnAllSurveys() {
        //given
        List<Survey> surveys = Lists.newArrayList(survey, survey, survey);
        given(surveyRepository.findAll()).willReturn(surveys);

        //when
        List<Survey> actual = surveyEndpoint.getAllSurveys();

        //then
        assertThat(actual).containsExactlyElementsOf(surveys);
    }

    @Test
    public void shouldSaveSurvey() {
        //given
        given(surveyRepository.save(survey)).willReturn(survey);

        //when
        Survey actual = surveyEndpoint.addSurvey(survey);

        //then
        verify(surveyRepository).save(survey);
        assertThat(actual).isEqualTo(survey);
    }

    @Test
    public void shouldUpdateSurvey() {
        //given
        given(surveyRepository.findById(3L)).willReturn(Optional.of(survey));
        given(surveyRepository.update(survey)).willReturn(survey);

        //when
        Survey actual = surveyEndpoint.updateSurvey(3L, survey);

        //then
        verify(surveyRepository).update(survey);
        assertThat(actual).isEqualTo(survey);
    }

    @Test(expected = ResourceConflictException.class)
    public void shouldReturnResourceConflictException() {
        //given
        given(surveyRepository.findById(6L)).willReturn(Optional.of(survey));

        //when
        Teacher teacher = new Teacher("New", "Teacher", AcademicDegree.DOCTORAL);
        Subject subject = new Subject("subject", 2019, 2);
        Survey diffSurvey = new Survey(teacher, subject);

        diffSurvey.setId(10L);
        surveyEndpoint.updateSurvey(6L, diffSurvey);

        //then
        //expect exception
    }

    @Test
    public void shouldDeleteSurvey() {
        //given
        given(surveyRepository.findById(5L)).willReturn(Optional.of(survey));

        //when
        surveyEndpoint.deleteSurvey(5L);

        //then
        verify(surveyRepository).delete(survey);
    }

    @Test
    public void shouldNotFailIfDeletingNotExistingSurvey() {
        //given
        given(surveyRepository.findById(5L)).willReturn(Optional.empty());

        //when
        surveyEndpoint.deleteSurvey(5L);

        //then
        verify(surveyRepository, times(0)).delete(survey);
    }

}
