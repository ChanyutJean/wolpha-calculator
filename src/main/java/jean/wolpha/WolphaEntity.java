package jean.wolpha;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "entity", schema = "wolpha")
@Setter
@NoArgsConstructor
public class WolphaEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @NotNull
    private Timestamp at;
    @NotNull
    private String record;
    @NotNull
    private String result;
}
