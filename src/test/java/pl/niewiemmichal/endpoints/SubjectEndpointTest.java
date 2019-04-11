package pl.niewiemmichal.endpoints;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;
import pl.niewiemmichal.model.Subject;
import pl.niewiemmichal.repository.Repository;
import pl.niewiemmichal.web.endpoints.SubjectEndpoint;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SubjectEndpointTest {

    @Mock
    private Repository<Subject, Long> subjectRepository;

    @InjectMocks
    private SubjectEndpoint subjectEndpoint;

    private Subject subject = new Subject("subject", 2019, 2);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnSubject() {
        //given
        given(subjectRepository.findById(1L))
                .willReturn(Optional.of(subject));
        //when
        Subject actual = subjectEndpoint.getSubject(1L);

        //then
        assertThat(actual).isEqualTo(subject);
    }

    @Test(expected = ResourceDoesNotExistException.class)
    public void shouldThrowResourceDoesNotExistException() {
        //given
        given(subjectRepository.findById(2L))
                .willReturn(Optional.empty());

        //when
        subjectEndpoint.getSubject(2L);

        //then
        //expect exception
    }

    @Test
    public void shouldReturnAllSubjects() {
        //given
        List<Subject> subjects = Lists.newArrayList(subject, subject, subject);
        given(subjectRepository.findAll()).willReturn(subjects);

        //when
        List<Subject> actual = subjectEndpoint.getAllSubjects();

        //then
        assertThat(actual).containsExactlyElementsOf(subjects);
    }

    @Test
    public void shouldSaveSubject() {
        //given
        given(subjectRepository.save(subject)).willReturn(subject);

        //when
        Subject actual = subjectEndpoint.addSubject(subject);

        //then
        verify(subjectRepository).save(subject);
        assertThat(actual).isEqualTo(subject);
    }

    @Test
    public void shouldUpdateSubject() {
        //given
        given(subjectRepository.findById(3L)).willReturn(Optional.of(subject));
        given(subjectRepository.update(subject)).willReturn(subject);

        //when
        Subject actual = subjectEndpoint.updateSubject(3L, subject);

        //then
        verify(subjectRepository).update(subject);
        assertThat(actual).isEqualTo(subject);
    }

    @Test(expected = ResourceConflictException.class)
    public void shouldReturnResourceConflictException() {
        //given
        given(subjectRepository.findById(6L)).willReturn(Optional.of(subject));

        //when
        Subject diffSubject = new Subject("name", 2019, 5);
        diffSubject.setId(10L);
        subjectEndpoint.updateSubject(6L, diffSubject);

        //then
        //expect exception
    }

    @Test
    public void shouldDeleteSubject() {
        //given
        given(subjectRepository.findById(5L)).willReturn(Optional.of(subject));

        //when
        subjectEndpoint.deleteSubject(5L);

        //then
        verify(subjectRepository).delete(subject);
    }

    @Test
    public void shouldNotFailIfDeletingNotExistingSubject() {
        //given
        given(subjectRepository.findById(5L)).willReturn(Optional.empty());

        //when
        subjectEndpoint.deleteSubject(5L);

        //then
        verify(subjectRepository, never()).delete(subject);
    }

}