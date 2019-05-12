package pl.niewiemmichal.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"name", "year", "semester"}))
public class Subject {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private Integer year;

    @NonNull
    @Column(nullable = false)
    private Integer semester;
}
