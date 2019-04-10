package pl.niewiemmichal.model;

import lombok.*;

import javax.persistence.*;

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
}
