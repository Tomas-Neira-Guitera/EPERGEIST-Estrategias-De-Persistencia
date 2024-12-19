package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Mutacion;
import ar.edu.unq.epersgeist.modelo.TipoDeCondicion;

import ar.edu.unq.epersgeist.modelo.condicion.Condicion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import ar.edu.unq.epersgeist.persistencia.jpa.HabilidadDAO;
import ar.edu.unq.epersgeist.persistencia.neo.HabilidadNeoDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.EntidadConNombreYaExistenteException;
import ar.edu.unq.epersgeist.servicios.exceptions.HabilidadesNoConectadasException;
import ar.edu.unq.epersgeist.servicios.exceptions.MutacionImposibleException;
import ar.edu.unq.epersgeist.servicios.exceptions.EspirituSinHabilidadesExeption;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.HabilidadService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class HabilidadServiceImpl implements HabilidadService {

    private final HabilidadDAO habilidadDAO;
    private final HabilidadNeoDAO habilidadNeoDAO;
    private final EspirituService espirituService;


    public HabilidadServiceImpl(HabilidadDAO habilidadDAO, HabilidadNeoDAO habilidadNeoDAO, EspirituService espirituService) {
        this.habilidadDAO = habilidadDAO;
        this.habilidadNeoDAO = habilidadNeoDAO;
        this.espirituService = espirituService;

    }

    @Override
    public HabilidadNode crear(HabilidadNode habilidadNode) {
        try {
            Habilidad habilidad = new Habilidad(habilidadNode.getNombre());
            var habilidadSQL = habilidadDAO.save(habilidad);
            habilidadNode.setIdSQL(habilidadSQL.getId());
            return habilidadNeoDAO.save(habilidadNode);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadConNombreYaExistenteException("Habilidad");
        }
    }

    @Override
    public void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, Condicion condicion) {
        HabilidadNode habilidadOrigen = habilidadNeoDAO.findByName(nombreHabilidadOrigen).orElseThrow(() -> new NoExisteLaEntidadException(nombreHabilidadOrigen));
        HabilidadNode habilidadDestino = habilidadNeoDAO.findByName(nombreHabilidadDestino).orElseThrow(() -> new NoExisteLaEntidadException(nombreHabilidadDestino));

        TipoDeCondicion tipoCondicion = condicion.getTipoDeCondicion();
        int cantidad = condicion.getCantidad();

        Mutacion mutacion = new Mutacion(tipoCondicion, cantidad, habilidadDestino);
        habilidadOrigen.agregarMutacion(mutacion);
        habilidadNeoDAO.save(habilidadOrigen);
    }

    @Override
    public  HabilidadNode recuperar(String nombreHabilidad) {
        return habilidadNeoDAO.findByName(nombreHabilidad).orElseThrow(() -> new NoExisteLaEntidadException(nombreHabilidad));
    }

    @Override
    public List<HabilidadNode> recuperarTodos() {
        return habilidadNeoDAO.findAll();
    }

    @Override
    public void evolucionar(Long espirituId) {

        Espiritu espiritu = espirituService.recuperar(espirituId);
        List<String> nombres = espiritu.getNombreHabilidades();

        if(!nombres.isEmpty()){
            List <Long> idsPosibleMutaciones = habilidadNeoDAO.findEvolutions(nombres,
                    espiritu.getExorcismosResueltos(),
                    espiritu.getExorcismosEvitados(),
                    espiritu.getEnergia(),
                    espiritu.getNivelDeConexion());

            List<Habilidad> posiblesMutaciones = habilidadDAO.recuperarPorIds(idsPosibleMutaciones);
            espiritu.addHabilidades(posiblesMutaciones);

            espirituService.actualizar(espiritu);
        };
    }




    @Override
    public Set<HabilidadNode> habilidadesConectadas(String nombreHabilidad) {
        habilidadDAO.findByName(nombreHabilidad).orElseThrow(() -> new NoExisteLaEntidadException(nombreHabilidad));
        return habilidadNeoDAO.getHabilidadesConectadas(nombreHabilidad);
    }

    @Override
    public Set<HabilidadNode> habilidadesPosibles(Long espirituId) {
        Espiritu espirituRecuperado = espirituService.recuperar(espirituId);
        List<String> habilidadesDelEspiritu = espirituRecuperado.getHabilidades().stream().map(Habilidad::getNombre).toList();

        if (habilidadesDelEspiritu.isEmpty()) {
            throw new EspirituSinHabilidadesExeption("El espiritu no tiene habilidades");
        }
        List<HabilidadNode> habilidadesPosibles = habilidadNeoDAO.posiblesMutacionesDelEspiritu(habilidadesDelEspiritu, espirituRecuperado.getExorcismosResueltos(), espirituRecuperado.getExorcismosEvitados(), espirituRecuperado.getEnergia(), espirituRecuperado.getNivelDeConexion());
        return new HashSet<>(habilidadesPosibles);
    }

    @Override
    public List<HabilidadNode> caminoMasRentable(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<TipoDeCondicion> condiciones) {

        HabilidadNode habilidadOrigen = habilidadNeoDAO.findByName(nombreHabilidadOrigen).orElseThrow(() -> new NoExisteLaEntidadException(nombreHabilidadOrigen));
        HabilidadNode habilidadDestino = habilidadNeoDAO.findByName(nombreHabilidadDestino).orElseThrow(() -> new NoExisteLaEntidadException(nombreHabilidadDestino));

        if(Objects.equals(nombreHabilidadOrigen, nombreHabilidadDestino)) {
            throw new MutacionImposibleException("No se puede mutar a la misma habilidad");
        }

        List<HabilidadNode> caminoPosible = habilidadNeoDAO.caminoPosible(habilidadOrigen.getNombre(), habilidadDestino.getNombre());

        if (caminoPosible.isEmpty()) {
            throw new HabilidadesNoConectadasException();
        }

        List<HabilidadNode> caminoEncontrado = habilidadNeoDAO.caminoMasRentable(habilidadOrigen.getNombre(), habilidadDestino.getNombre(), condiciones);

        if(caminoEncontrado.isEmpty()) {
            throw new MutacionImposibleException("No existe un camino rentable entre las habilidades");
        }

        return caminoEncontrado;

    }

    @Override
    public List<HabilidadNode> caminoMasMutable(Long espirituId, String nombreHabilidad) {
        Espiritu espiritu = espirituService.recuperar(espirituId);

        return habilidadNeoDAO.caminoMasMutable(nombreHabilidad,
                                                  espiritu.getExorcismosResueltos(),
                                                  espiritu.getExorcismosEvitados(),
                                                  espiritu.getEnergia(),
                                                  espiritu.getNivelDeConexion());
    }

    @Override
    public List<HabilidadNode> caminoMenosMutable(String nombreHabilidad, Long espirituId) {

        Espiritu espiritu = espirituService.recuperar(espirituId);


        return habilidadNeoDAO.caminoMenosMutable(nombreHabilidad,
                espiritu.getExorcismosResueltos(),
                espiritu.getExorcismosEvitados(),
                espiritu.getEnergia(),
                espiritu.getNivelDeConexion());
    }
}
