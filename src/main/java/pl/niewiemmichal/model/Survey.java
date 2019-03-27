package pl.niewiemmichal.model;

import lombok.*;

import javax.persistence.*;

@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Survey {
    @Id @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    private Teacher teacher;

    @NonNull
    @ManyToOne
    private Subject subject;
}

