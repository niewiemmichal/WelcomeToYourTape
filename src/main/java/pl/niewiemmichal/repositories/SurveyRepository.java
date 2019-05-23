package pl.niewiemmichal.repositories;

import pl.niewiemmichal.model.Survey;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends Repository<Survey, Long> {

    Optional<Survey> findByTeacherIdAndSubjectId(Long teacherId, Long subjectId);
    void deleteByTeacherId(Long teacherId);
    void deleteBySubjectId(Long subjectId);

}
