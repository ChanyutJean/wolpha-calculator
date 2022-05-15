package jean.wolpha;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface WolphaRepository extends JpaRepository<WolphaEntity, Integer> {
    default void addEntity(String record, Date at) {
        WolphaEntity ent = new WolphaEntity();
        ent.setRecord(record);
        ent.setAt(at);
        this.save(ent);
    }
}
