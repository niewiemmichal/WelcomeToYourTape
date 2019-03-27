package pl.niewiemmichal.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Teacher {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotBlank
    @Size(max = 64)
    @Column(nullable = false)
    private String name;

    @NonNull
    @NotBlank
    @Size(max = 64)
    @Column(nullable = false)
    private String surname;

    @NonNull
    @NotBlank
    @Size(max = 64)
    @Column(nullable = false)
    private AcademicDegree degree;
}
