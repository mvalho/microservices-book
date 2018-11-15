package microservices.book.multiplication.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Marcelo Carvalho on 10/25/2018.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class MultiplicationResultAttempt {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private final User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn( name = "MULTIPLICATION_ID")
    private final Multiplication multiplication;

    private final int resultAttempt;

    @Setter
    private boolean correct;

    public MultiplicationResultAttempt() {
        user = null;
        multiplication = null;
        resultAttempt = -1;
        correct = false;
    }
}
