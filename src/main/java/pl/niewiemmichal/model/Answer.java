package pl.niewiemmichal.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
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

    @NotNull
    @Size(max = 64)
    @Column(nullable = false, length = 64)
    private String respondent;
}
