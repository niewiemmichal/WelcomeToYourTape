package pl.niewiemmichal.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Subject {

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
