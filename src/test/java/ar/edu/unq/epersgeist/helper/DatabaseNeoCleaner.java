package ar.edu.unq.epersgeist.helper;

import ar.edu.unq.epersgeist.persistencia.neo.HabilidadNeoDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DatabaseNeoCleaner {

    @Autowired
    private HabilidadNeoDAO habilidadNeoDAO;

    public void deleteAll(){
        habilidadNeoDAO.deleteAll();
    }

}
