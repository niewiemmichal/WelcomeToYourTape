package pl.niewiemmichal.endpoints;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.AcademicDegree;
import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.model.Survey;
import pl.niewiemmichal.model.Teacher;
import pl.niewiemmichal.repositories.SurveyRepository;
import pl.niewiemmichal.repositories.TeacherRepository;
import pl.niewiemmichal.web.endpoints.TeacherEndpoint;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TeacherEndpointTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private TeacherEndpoint teacherEndpoint;

    private Teacher teacher = new Teacher("New", "Teacher", AcademicDegree.DOCTORAL);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnTeacher() {
        //given
        given(teacherRepository.findById(1L))
                .willReturn(Optional.of(teacher));
        //when
        Teacher actual = teacherEndpoint.getTeacher(1L);

        //then
        assertThat(actual).isEqualTo(teacher);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowResourceDoesNotExistException() {
        //given
        given(teacherRepository.findById(2L))
                .willReturn(Optional.empty());

        //when
        teacherEndpoint.getTeacher(2L);

        //then
        //expect exception
    }

    @Test
    public void shouldReturnAllTeachers() {
        //given
        List<Teacher> teachers = Lists.newArrayList(teacher, teacher, teacher);
        given(teacherRepository.findAll()).willReturn(teachers);

        //when
        List<Teacher> actual = teacherEndpoint.getAllTeachers();

        //then
        assertThat(actual).containsExactlyElementsOf(teachers);
    }

    @Test
    public void shouldReturnAllTeachersBySubject() {
        //given
        Subject subject = new Subject("name", 1997, 6);
        subject.setId(100L);
        Subject anotherSubject = new Subject("another", 1997, 6);
        anotherSubject.setId(101L);
        Teacher teacher = new Teacher("name", "surname", AcademicDegree.DOCTORAL);
        Survey survey = new Survey(teacher, subject);
        Survey anotherSurvey = new Survey(teacher, anotherSubject);
        List<Survey> surveys = Lists.newArrayList(survey, survey, anotherSurvey);
        given(surveyRepository.findAll()).willReturn(surveys);

        //when
        List<Teacher> actual = teacherEndpoint.getAllTeachersBySubject(100L);

        //then
        List<Teacher> expected = Lists.newArrayList(teacher, teacher);
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    public void shouldSaveTeacher() {
        //given
        given(teacherRepository.save(teacher)).willReturn(teacher);

        //when
        Teacher actual = teacherEndpoint.addTeacher(teacher);

        //then
        verify(teacherRepository).save(teacher);
        assertThat(actual).isEqualTo(teacher);
    }

    @Test
    public void shouldUpdateTeacher() {
        //given
        given(teacherRepository.findById(3L)).willReturn(Optional.of(teacher));
        given(teacherRepository.update(teacher)).willReturn(teacher);

        //when
        Teacher actual = teacherEndpoint.updateTeacher(3L, teacher);

        //then
        verify(teacherRepository).update(teacher);
        assertThat(actual).isEqualTo(teacher);
    }

    @Test(expected = ResourceConflictException.class)
    public void shouldReturnResourceConflictException() {
        //given
        given(teacherRepository.findById(6L)).willReturn(Optional.of(teacher));

        //when
        Teacher diffTeacher = new Teacher("New", "Teacher", AcademicDegree.DOCTORAL);
        diffTeacher.setId(10L);
        teacherEndpoint.updateTeacher(6L, diffTeacher);

        //then
        //expect exception
    }

    @Test
    public void shouldDeleteTeacher() {
        //given
        given(teacherRepository.findById(5L)).willReturn(Optional.of(teacher));

        //when
        teacherEndpoint.deleteTeacher(5L);

        //then
        verify(teacherRepository).delete(teacher);
    }

    @Test
    public void shouldNotFailIfDeletingNotExistingTeacher() {
        //given
        given(teacherRepository.findById(5L)).willReturn(Optional.empty());

        //when
        teacherEndpoint.deleteTeacher(5L);

        //then
        verify(teacherRepository, times(0)).delete(teacher);
    }
}
