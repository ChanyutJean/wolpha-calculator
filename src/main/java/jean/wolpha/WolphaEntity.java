package jean.wolpha;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "entity", schema = "wolpha")
@Setter
@NoArgsConstructor
public class WolphaEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private Timestamp at;
    @Column(nullable = false)
    private String record;
    @Column(nullable = false)
    private String result;
    private int error;
}
