package pl.niewiemmichal.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Answer {

    @Id @GeneratedValue
    private Long id;

    @Lob
    private String contents;

    private Integer rating;

    @NonNull
    @ManyToOne
    private Survey survey;

    @NonNull
    @ManyToOne
    private Question question;

    @NonNull
    @ManyToOne
    private Respondent respondent;
}
