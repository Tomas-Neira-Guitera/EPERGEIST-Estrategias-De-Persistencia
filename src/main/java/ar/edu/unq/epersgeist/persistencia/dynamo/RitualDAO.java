package ar.edu.unq.epersgeist.persistencia.dynamo;

import ar.edu.unq.epersgeist.modelo.ritual.Ritual;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RitualDAO {

    private final DynamoDBMapper dynamoDBMapper;


    public RitualDAO(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void save(Ritual ritual) {
        try {
            dynamoDBMapper.save(ritual);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el Ritual en DynamoDB", e);
        }
    }

    public Ritual get(String id) {
       Ritual ritual = dynamoDBMapper.load(Ritual.class, id);
         if (ritual == null) {
              throw new NoExisteLaEntidadException("Ritual", id);
         }
         return ritual;
    }

    public List<Ritual> getAll() {
        return dynamoDBMapper.scan(Ritual.class, new DynamoDBScanExpression());
    }

    public void delete(String id) {
        Ritual ritual = dynamoDBMapper.load(Ritual.class, id);
        if (ritual == null ){
            throw new NoExisteLaEntidadException("Ritual", id);
        }
        dynamoDBMapper.delete(ritual);
    }
}
