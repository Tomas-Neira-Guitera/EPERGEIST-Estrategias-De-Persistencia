package ar.edu.unq.epersgeist.helper;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataDao {

    @Autowired
    private EntityManager entityManager;

    public void deleteALL(){
        List<?> nombreDeTablas = entityManager.createNativeQuery("SHOW TABLES").getResultList();


        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();


        for (Object result : nombreDeTablas) {
            String tabla;
            if (result instanceof String) {
                tabla = (String) result;
            } else if (result instanceof Object[]) {
                tabla = ((Object[]) result)[0].toString();
            } else {
                continue;
            }
            entityManager.createNativeQuery("TRUNCATE TABLE " + tabla).executeUpdate();
        }


        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
    }


}

