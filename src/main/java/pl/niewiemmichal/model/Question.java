package pl.niewiemmichal.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Question {

    @Id @GeneratedValue
    private Long id;

    @NonNull
    @NotBlank
    @Lob
    @Column(nullable = false)
    private String contents;

    @NonNull
    @Column(nullable = false)
    private Boolean isOpen;
}
