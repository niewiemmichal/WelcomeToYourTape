package pl.niewiemmichal.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data @RequiredArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(uniqueConstraints = {@UniqueConstraint(columnNames={"name", "surname", "degree"})})
public class Teacher {

    @EqualsAndHashCode.Exclude
    @Id @GeneratedValue
    private Long id;

    @NonNull @NotBlank @Size(max = 64)
    @Column(nullable = false, length = 64)
    private String name;

    @NonNull @NotBlank @Size(max = 64)
    @Column(nullable = false, length = 64)
    private String surname;

    @NonNull
    @Column(nullable = false)
    private AcademicDegree degree;
}
