package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.ActualizarUbicacionDTO;
import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.mongodb.AreaDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.EntidadConNombreYaExistenteException;
import ar.edu.unq.epersgeist.servicios.exceptions.ExisteUnaUbicacionEnEseAreaException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UbicacionServiceTest {

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private DatabaseCleanerService databaseCleanerService;

    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;

    private Ubicacion playa;
    private Area playaArea;

    private Ubicacion selva;
    private Area selvaArea;

    private Ubicacion cocodrilo;
    private Area cocodriloArea;

    private Medium joacor;

    private Espiritu melli;
    private Espiritu naguet;
    private Espiritu augusto;

    private GeoJsonPoint melliPunto;
    private GeoJsonPoint naguetPunto;
    private GeoJsonPoint augustoPunto;
    @Autowired
    private AreaDAO areaDAO;

    @BeforeEach
    void setUp() {
        playaArea = new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 1), new GeoJsonPoint(1, 1), new GeoJsonPoint(1, 0), new GeoJsonPoint(0, 0));
        playa = new Santuario("Playa", 20);

        selvaArea = new Area(new GeoJsonPoint(2, 0), new GeoJsonPoint(2, 1), new GeoJsonPoint(3, 1), new GeoJsonPoint(3, 0), new GeoJsonPoint(2, 0));
        selva = new Santuario("Selva", 20);

        cocodriloArea = new Area(new GeoJsonPoint(6, 0), new GeoJsonPoint(6, 1), new GeoJsonPoint(7, 1), new GeoJsonPoint(7, 0), new GeoJsonPoint(6.5, 0), new GeoJsonPoint(6, 0));
        cocodrilo = new Santuario("Cocodrilo", 20);

        melliPunto = new GeoJsonPoint(0.5, 0.5);
        naguetPunto = new GeoJsonPoint(2.2, 0.8);
        augustoPunto = new GeoJsonPoint(0.3, 0.2);

        joacor = new Medium("Joacor", 100);
        melli = new Demonio("Melli", 100);
        naguet = new Angel("Naguet", 100);
        augusto = new Demonio("August", 100);
    }

    @Test
    public void testCrearUbicacion() {

        ubicacionService.crear(playa, playaArea);

        assertNotNull(playa.getId());
        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(playa.getId());
        assertEquals(ubicacionRecuperada.getNombre(), playa.getNombre());
        assertEquals(ubicacionRecuperada.getEnergia(), playa.getEnergia());
        assertEquals(ubicacionRecuperada.getId(), playaArea.getIdUbicacion());
    }

    @Test
    public void testCrearUnaUbicacionConMasDe5Puntos() {
        ubicacionService.crear(cocodrilo, cocodriloArea);

        Ubicacion cocdriloRecu = ubicacionService.recuperar(cocodrilo.getId());

        assertEquals(cocdriloRecu.getNombre(), cocodrilo.getNombre());
        assertEquals(cocdriloRecu.getEnergia(), cocodrilo.getEnergia());
    }

    @Test
    public void testCrearUbicacionEnUnAreaYaOcupada() {
        var playa2Area = new Area(new GeoJsonPoint(0.5, 0.5), new GeoJsonPoint(1, 1.5), new GeoJsonPoint(1.5, 0.5), new GeoJsonPoint(0.5, 0.5));
        var playa2 = new Santuario("Playa2", 20);

        ubicacionService.crear(playa, playaArea);

        assertThrows(ExisteUnaUbicacionEnEseAreaException.class, () -> ubicacionService.crear(playa2, playa2Area));
    }

    @Test
    public void testCrearUbicacionEnUnAreaConBordeOcupado() {
        var playa2Area = new Area(new GeoJsonPoint(1, 0), new GeoJsonPoint(1, 1), new GeoJsonPoint(2, 1), new GeoJsonPoint(0, 2), new GeoJsonPoint(1, 0));
        var playa2 = new Santuario("Playa2", 20);

        ubicacionService.crear(playa, playaArea);

        assertThrows(ExisteUnaUbicacionEnEseAreaException.class, () -> ubicacionService.crear(playa2, playa2Area));
    }

    @Test
    public void testCrearUbicacionMayorYPorEncimaDeUnAreaExistente() {
        var playa2Area = new Area(new GeoJsonPoint(-1, -1), new GeoJsonPoint(-1, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, -1), new GeoJsonPoint(-1, -1));
        var playa2 = new Santuario("Playa2", 20);

        ubicacionService.crear(playa, playaArea);

        assertThrows(ExisteUnaUbicacionEnEseAreaException.class, () -> ubicacionService.crear(playa2, playa2Area));
    }

    @Test
    public void noSePuedeCrearUnaUbicacionConUnNombreYaExistente() {
        ubicacionService.crear(playa, playaArea);
        Area playa2Area = new Area(new GeoJsonPoint(-2, 0), new GeoJsonPoint(-2, -1), new GeoJsonPoint(-3, -1), new GeoJsonPoint(-3, 0), new GeoJsonPoint(-2, 0));
        Ubicacion playa2 = new Santuario("Playa", 20);

        assertThrows(EntidadConNombreYaExistenteException.class, () -> ubicacionService.crear(playa2, playa2Area));
    }

    @Test
    public void testRecuperarUbicacion() {
        ubicacionService.crear(playa, playaArea);

        Ubicacion ubicacionRecuperada = ubicacionService.recuperar(playa.getId());
        assertEquals(ubicacionRecuperada.getNombre(), playa.getNombre());
        assertEquals(ubicacionRecuperada.getId(), playa.getId());
        assertEquals(ubicacionRecuperada.getEnergia(), playa.getEnergia());
        assertEquals(ubicacionRecuperada.getId(), playaArea.getIdUbicacion());
    }

    @Test
    public void testRecuperarUbicacionInexistente() {
        assertThrows(NoExisteLaEntidadException.class, () -> ubicacionService.recuperar(1L));
    }

    @Test
    public void testRecuperarTodasLasUbicaciones() {
        ubicacionService.crear(playa, playaArea);
        ubicacionService.crear(selva, selvaArea);

        List<Ubicacion> ubicacionesRecuperadas = ubicacionService.recuperarTodos();
        assertEquals(2, ubicacionesRecuperadas.size());
        assertTrue(ubicacionesRecuperadas.stream().anyMatch(u -> u.getNombre().equals("Playa")));
        assertTrue(ubicacionesRecuperadas.stream().anyMatch(u -> u.getId().equals(playa.getId())));

        assertTrue(ubicacionesRecuperadas.stream().anyMatch(u -> u.getNombre().equals("Selva")));
        assertTrue(ubicacionesRecuperadas.stream().anyMatch(u -> u.getId().equals(selva.getId())));
    }

    @Test
    public void testActualizarUbicacion() {
        ubicacionService.crear(playa, playaArea);

        playa.setNombre("Templo");
        ubicacionService.actualizar(playa);

        Ubicacion ubicacionActualizada = ubicacionService.recuperar(playa.getId());
        assertEquals("Templo", ubicacionActualizada.getNombre());
        assertEquals(ubicacionActualizada.getId(), playa.getId());
        assertEquals(ubicacionActualizada.getEnergia(), playa.getEnergia());
        assertEquals(ubicacionActualizada.getId(), playaArea.getIdUbicacion());
    }

    @Test
    public void testActualizarUbicacionInexistente() {
        Ubicacion ubicacion = new Santuario("Bosque", 20);
        ubicacion.setId(1L);
        assertThrows(NoExisteLaEntidadException.class, () -> ubicacionService.actualizar(ubicacion));
    }

    @Test
    public void testActualizarUbicacionConDTO() {
        ubicacionService.crear(playa, playaArea);

        ActualizarUbicacionDTO dto = new ActualizarUbicacionDTO(playa.getId(), "Templo", 30);
        ubicacionService.actualizar(dto);

        Ubicacion ubicacionActualizada = ubicacionService.recuperar(playa.getId());

        assertEquals("Templo", ubicacionActualizada.getNombre());
        assertEquals(30, ubicacionActualizada.getEnergia());
        assertEquals(ubicacionActualizada.getId(), playa.getId());
        assertEquals(ubicacionActualizada.getId(), playaArea.getIdUbicacion());
    }

    @Test
    public void testActualizarUbicacionConDTOConIdInexistente() {
        ActualizarUbicacionDTO dto = new ActualizarUbicacionDTO(1240L, "Templo", 30);

        assertThrows(NoExisteLaEntidadException.class, () -> ubicacionService.actualizar(dto));
    }


    @Test
    public void testEliminarUbicacion() {

       ubicacionService.crear(playa, playaArea);
       ubicacionService.eliminar(playa.getId());

        Optional<Area> areaUbicacionEliminada = areaDAO.encontrarPorIdDeUbicacion(playa.getId());

       assertThrows(NoExisteLaEntidadException.class, () -> ubicacionService.recuperar(playa.getId()));
       assertEquals(Optional.empty(), areaUbicacionEliminada);
    }

    @Test
    public void testEliminarUbicacionInexistente() {
        assertThrows(NoExisteLaEntidadException.class, () -> ubicacionService.eliminar(1L));
    }

   @Test
   public void testEspiritusEnUbicacionVacia() {
       ubicacionService.crear(playa, playaArea);
       assertEquals(0, ubicacionService.espiritusEn(playa.getId()).size());
   }


    @Test
    public void testEspiritusEnUbicacion() {
        ubicacionService.crear(playa, playaArea);
        ubicacionService.crear(selva, selvaArea);

        espirituService.crear(melli, melliPunto);
        espirituService.crear(augusto, augustoPunto);
        espirituService.crear(naguet, naguetPunto);

        List<Espiritu> espiritusEnUbicacion = ubicacionService.espiritusEn(playa.getId());
        assertEquals(2, espiritusEnUbicacion.size());
        assertTrue(espiritusEnUbicacion.stream().anyMatch(e -> e.getId().equals(melli.getId())));
        assertTrue(espiritusEnUbicacion.stream().anyMatch(e -> e.getId().equals(augusto.getId())));
        assertFalse(espiritusEnUbicacion.stream().anyMatch(e -> e.getId().equals(naguet.getId())));
    }

    //TODO: Falta terminar medium para testear
//    @Test
//    public void testMediumsSinEspiritusEnUbicacion() {
//        ubicacionService.crear(playa, playaArea);
//
//        Medium medium1 = new Medium("Medium1", 10, ubicacion);
//        Medium medium2 = new Medium("Medium2", 50, ubicacion);
//        Medium mediumSinEspiritu = new Medium("Medium3", 100, ubicacion);
//
//        Espiritu espiritu1 = new Angel("Espiritu1", ubicacion, 70);
//        Espiritu espiritu2 = new Demonio("Espiritu2", ubicacion, 80);
//
//        espirituService.crear(espiritu1);
//        espirituService.crear(espiritu2);
//
//        medium1.crearConexion(espiritu1);
//        medium2.crearConexion(espiritu2);
//
//        mediumService.crear(medium1);
//        mediumService.crear(medium2);
//        mediumService.crear(mediumSinEspiritu);
//
//        espirituService.actualizar(espiritu1);
//        espirituService.actualizar(espiritu2);
//
//        List<Medium> mediumsSinEspiritus = ubicacionService.mediumsSinEspiritusEn(ubicacion.getId());
//
//        assertEquals(1, mediumsSinEspiritus.size());
//        assertTrue(mediumsSinEspiritus.stream().anyMatch(m -> m.getId().equals(mediumSinEspiritu.getId())));
//    }


    @Test
    public void testActualizarUbicacionConUnNombreYaExistente() {

        ubicacionService.crear(playa, playaArea);
        ubicacionService.crear(selva, selvaArea);

        playa.setNombre("Selva");

        assertThrows(EntidadConNombreYaExistenteException.class, () -> ubicacionService.actualizar(playa));
    }

    @AfterEach
    public void cleanUp() {
        databaseMongoCleaner.deleteAll();
        databaseCleanerService.deleteAll();
    }
}