package pl.niewiemmichal.model;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class Survey {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Student student;

    @NonNull
    private String name;

}
