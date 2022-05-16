package jean.wolpha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;

@Repository
public interface WolphaRepository extends JpaRepository<WolphaEntity, Integer> {
    default void addEntity(String record, String result, Timestamp at) {
        WolphaEntity ent = new WolphaEntity();
        ent.setRecord(record);
        ent.setResult(result);
        ent.setAt(at);
        this.save(ent);
    }
}
