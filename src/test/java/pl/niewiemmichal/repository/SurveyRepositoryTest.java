package pl.niewiemmichal.repository;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import pl.niewiemmichal.services.SurveyService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SurveyRepositoryTest {

    private SurveyService surveyService;

    @Before
    public void setUp() {
        SurveyRepository surveyRepository = mock(SurveyRepository.class);
    }

    @Test
    public void shouldReturnTeachersSurveys() {
        //given
        given(surveyService.getTeacherSurveys(666L))
                .willReturn(ImmutableList.of(

                ));
    }

}