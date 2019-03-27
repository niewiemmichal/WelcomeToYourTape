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
import javax.validation.constraints.Size;

@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Respondent {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @NotBlank
    @Size(min = 16, max = 16)
    @Column(nullable = false)
    private String token;

    @NonNull
    @Column(nullable = false)
    private Integer semester;
}
