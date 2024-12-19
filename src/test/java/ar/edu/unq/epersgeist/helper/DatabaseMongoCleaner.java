package ar.edu.unq.epersgeist.helper;

import ar.edu.unq.epersgeist.persistencia.mongodb.AreaDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.CoordenadaDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.ReporteEspirituDominioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DatabaseMongoCleaner {

    @Autowired
    private AreaDAO areaDAO;

    @Autowired
    private CoordenadaDAO coordenadaDAO;

    @Autowired
    private ReporteEspirituDominioDAO reporteEspirituDominioDAO;

    public void deleteAll(){
        areaDAO.deleteAll();
        coordenadaDAO.deleteAll();
        reporteEspirituDominioDAO.deleteAll();
    }

}
