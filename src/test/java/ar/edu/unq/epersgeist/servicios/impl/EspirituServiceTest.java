package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.dto.espiritu.ActualizarEspirituDTO;
import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.servicios.exceptions.CoordenadaFueraDeLosLimitesException;
import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.mongodb.CoordenadaDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import ar.edu.unq.epersgeist.utils.Direccion;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.interfaces.HabilidadService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EspirituServiceTest {

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private HabilidadService habilidadService;

    @Autowired
    private CoordenadaDAO coordenadaDAO;

    @Autowired
    private DatabaseCleanerService databaseCleaner;

    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;


    private Medium joacor;
    private Espiritu melli;
    private Espiritu naguet;
    private Espiritu augusto;
    private Ubicacion bernal;
    private Ubicacion quilmes;

    private GeoJsonPoint melliPunto;
    private GeoJsonPoint joacorPunto;
    private GeoJsonPoint naguetPunto;
    private GeoJsonPoint augustoPunto;

    private Angel angelDebilitado;
    private Area bernalArea;

    private Ubicacion playa;
    private Area playaArea;

    @BeforeEach
    public void setUp() {

        melliPunto = new GeoJsonPoint(0.5, 0.5);
        joacorPunto = new GeoJsonPoint(0.5, 0.5);
        naguetPunto = new GeoJsonPoint(0.9, 0.7);
        augustoPunto = new GeoJsonPoint(0.3, 0.2);

        joacor = new Medium("Joacor", 100);
        melli = new Demonio("Melli", 100);
        naguet = new Angel("Naguet", 100);
        augusto = new Demonio("August", 100);

        playaArea = new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 1), new GeoJsonPoint(1, 1), new GeoJsonPoint(1, 0), new GeoJsonPoint(0, 0));
        playa = new Santuario("Playa", 20);
        ubicacionService.crear(playa, playaArea);

        angelDebilitado = new Angel("Debilitado", 23);

        bernalArea = new Area(new GeoJsonPoint(10, 10), new GeoJsonPoint(10, 11), new GeoJsonPoint(11, 11), new GeoJsonPoint(11, 10), new GeoJsonPoint(10, 10));
        bernal = new Santuario("Bernal", 20);
        ubicacionService.crear(bernal, bernalArea);
    }

    @Test
    public void testCrearUnEspiritu() {
        espirituService.crear(melli);
        Espiritu espirituRecuperado = espirituService.recuperar(melli.getId());

        assertEquals(melli.getNombre(), espirituRecuperado.getNombre());
        assertInstanceOf(Demonio.class, melli);
        assertEquals(melli.getEnergia(), espirituRecuperado.getEnergia());
        assertEquals(0, melli.getExorcismosResueltos());
        assertEquals(0, melli.getExorcismosEvitados());
    }

    @Test
    public void testCrearUnEspirituConCoordenadaValidas(){
        espirituService.crear(melli, melliPunto);

        Espiritu espirituRecuperado = espirituService.recuperar(melli.getId());

        Coordenada coordenadaEspiritu = coordenadaDAO.encontrarEnPunto(melliPunto, TipoDeEntidad.ESPIRITU).get();

        assertEquals(melli.getNombre(), espirituRecuperado.getNombre());
        assertInstanceOf(Demonio.class, melli);
        assertEquals(playa.getNombre(), espirituRecuperado.getUbicacion().getNombre());
        assertEquals(melli.getEnergia(), espirituRecuperado.getEnergia());
        assertEquals(0, melli.getExorcismosResueltos());
        assertEquals(0, melli.getExorcismosEvitados());
        assertEquals(coordenadaEspiritu.getPunto(), melliPunto);
    }

    @Test
    public void testNoSePuedeCrearUnEspirituConUnPuntoDeUnAreaNoExistente(){
        GeoJsonPoint puntoFueraDelArea = new GeoJsonPoint(1.5, 1.5);
        assertThrows(NoExisteUnaUbicacionEnEstePunto.class, () -> espirituService.crear(melli, puntoFueraDelArea));
    }

    @Test
    public void testNoSePuedeCrearUnEspirituConUnPuntoConCoordenadasInvalidas() {
        var puntoConXExcedida = new GeoJsonPoint(180.1, 0);
        var puntoConXInsuficiente = new GeoJsonPoint(-180.1, 0);

        var puntoConYExcedida = new GeoJsonPoint(0, 90.1);
        var puntoConYInsuficiente = new GeoJsonPoint(0, -90.1);

        assertThrows(CoordenadaFueraDeLosLimitesException.class, () -> espirituService.crear(melli, puntoConXExcedida));
        assertThrows(CoordenadaFueraDeLosLimitesException.class, () -> espirituService.crear(melli, puntoConXInsuficiente));
        assertThrows(CoordenadaFueraDeLosLimitesException.class, () -> espirituService.crear(melli, puntoConYExcedida));
        assertThrows(CoordenadaFueraDeLosLimitesException.class, () -> espirituService.crear(melli, puntoConYInsuficiente));
    }


    @Test
    public void testRecuperarUnEspiritu() {
        espirituService.crear(melli, melliPunto);
        Espiritu espirituRecuperado = espirituService.recuperar(melli.getId());

        assertEquals(melli.getNombre(), espirituRecuperado.getNombre());
        assertInstanceOf(Demonio.class, melli);
        assertEquals(playa.getNombre(), espirituRecuperado.getUbicacion().getNombre());
        assertEquals(melli.getEnergia(), espirituRecuperado.getEnergia());
    }

    @Test
    public void testNoSePuedeRecuperarUnEspirituQueNoEstaEnLaBaseDeDatos() {
        assertThrows(NoExisteLaEntidadException.class, () -> {
            espirituService.recuperar(1234L);
        });
    }

    @Test
    public void testRecuperarTodosLosEspiritus() {
        espirituService.crear(melli);
        espirituService.crear(naguet);

        List<Espiritu> espiritusRecuperados = espirituService.recuperarTodos();

        assertEquals(2, espiritusRecuperados.size());
    }

    @Test
    public void recuperarTodosLosEspiritusAunqueNoHayaNinguno() {
        List<Espiritu> espiritusRecuperados = espirituService.recuperarTodos();
        assertEquals(0, espiritusRecuperados.size());
    }

    @Test
    public void testActualizarUnEspiritu() {
        espirituService.crear(melli);
        melli.setNombre("Cocodrilo");
        espirituService.actualizar(melli);

        Espiritu espirituRecuperado = espirituService.recuperar(melli.getId());
        assertEquals("Cocodrilo", espirituRecuperado.getNombre());
    }


    @Test
    public void testNoSePuedeActualizarUnEspirituQueNoEstaEnLaBaseDeDatos() {
        Long idNoExistente = 1234L;
        melli.setId(idNoExistente);

        assertThrows(NoExisteLaEntidadException.class, () -> {
            espirituService.actualizar(melli);
        });
    }

    @Test
    void testActualizarPorDTO() {
        espirituService.crear(augusto);

        ActualizarEspirituDTO dto = new ActualizarEspirituDTO(augusto.getId(), "NombreNuevo");

        espirituService.actualizar(dto);
        Espiritu espirituRecuperado = espirituService.recuperar(dto.getId());


        assertEquals(espirituRecuperado.getNombre(), dto.getNombre());
    }

    @Test
    void testActualizarPorDTOConIdInexistente(){
        ActualizarEspirituDTO dto = new ActualizarEspirituDTO(1030L, "NombreNuevo");
        assertThrows(NoExisteLaEntidadException.class,()-> espirituService.actualizar(dto));
    }

    @Test
    public void testEliminarUnEspiritu() {
        espirituService.crear(naguet, naguetPunto);
        espirituService.eliminar(naguet.getId());

        List<Espiritu> espiritusRecuperados = espirituService.recuperarTodos();
        Optional<Coordenada> coordenadaEspirituEliminado = coordenadaDAO.encontrarEnPunto(naguetPunto, TipoDeEntidad.ESPIRITU);

        assertEquals(0, espiritusRecuperados.size());
        assertEquals(Optional.empty(), coordenadaEspirituEliminado);
    }


    @Test
    public void testNoSePuedeEliminarUnEspirituQueNoEstaEnLaBaseDeDatos() {
        assertThrows(NoExisteLaEntidadException.class, () -> {
            espirituService.eliminar(1234L);
        });
    }

    @Test
    public void testConseguirEspiritusDemoniacosConListaDescendente() {
        espirituService.crear(melli);
        espirituService.crear(augusto);

        List<Demonio> espiritusDemoniacos = espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 1, 10);

        assertEquals(2, espiritusDemoniacos.size());
        assertEquals(augusto.getNombre(), espiritusDemoniacos.getFirst().getNombre());
    }

    @Test
    public void testConseguirEspiritusDemoniacosConListaAscendente() {
        espirituService.crear(melli);
        espirituService.crear(augusto);

        List<Demonio> espiritusDemoniacos = espirituService.espiritusDemoniacos(Direccion.ASCENDENTE, 1, 10);

        assertEquals(2, espiritusDemoniacos.size());
        assertEquals(melli.getNombre(), espiritusDemoniacos.getFirst().getNombre());
    }

    @Test
    public void testSiCantidadDePaginaInvalidaSeSeteaDefault1() {
        espirituService.crear(melli);
        espirituService.crear(naguet);

        assertEquals(1,
                (espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 1, (-3))).size());
    }

    @Test
    public void testSiPaginaInvalidaSeSeteaDefault1() {
        espirituService.crear(melli);
        espirituService.crear(naguet);

        assertEquals(1,
                (espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 2)).size());
    }

    @Test
    public void testSiLePidoUnaPaginaMayorALaCantidadQuePoseeDevuelveListaVcia() {
        espirituService.crear(melli);
        espirituService.crear(naguet);

        assertEquals(0,
                (espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 10, 2)).size());
    }

    @Test
    public void testAlPedirUnaPaginaQueNoExisteDevuelveUnaListaVacia() {
        espirituService.crear(melli);
        espirituService.crear(naguet);

        List<Demonio> espiritusDemoniacos = espirituService.espiritusDemoniacos(Direccion.DESCENDENTE, 2, 10);

        assertEquals(0, espiritusDemoniacos.size());
    }

    @Test
    public void testConectarEspirituYMedium() {
        espirituService.crear(melli, melliPunto);
        mediumService.crear(joacor, joacorPunto);

        espirituService.conectar(melli.getId(), joacor.getId());

        Espiritu melliRecuperado = espirituService.recuperar(melli.getId());
        Medium joacorRecuperado = mediumService.recuperar(joacor.getId());

        assertEquals(melliRecuperado.getMedium().getNombre(), joacorRecuperado.getNombre());
    }

    @Test
    public void testUnEspirituYaConectadoNoSePuedeConectar() {
        espirituService.crear(melli , melliPunto);
        mediumService.crear(joacor, joacorPunto);
        espirituService.conectar(melli.getId(), joacor.getId());

        assertThrows(EspirituConectadoException.class, () -> {
            espirituService.conectar(melli.getId(), joacor.getId());
        });
    }

    @Test
    public void testUnEspirituYUnMediumDeDiferentesUbicacionesNoSePuedenConectar() {
        var puntoDiferente = new GeoJsonPoint(10.5, 10.5);
        espirituService.crear(naguet, naguetPunto);
        mediumService.crear(joacor, puntoDiferente);


        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> {
            espirituService.conectar(naguet.getId(), joacor.getId());
        });
    }

    @Test
    public void testUnEspirituAgregaUnaHabilidadCorrectamente() {
        Espiritu melliDB = espirituService.crear(melli);
        HabilidadNode habilidad = new HabilidadNode("Ilusiones");
        HabilidadNode habilidadDB = habilidadService.crear(habilidad);

        espirituService.agregarHabilidad(melli.getId(), habilidadDB.getIdSQL());

        Espiritu melliRecuperado = espirituService.recuperar(melli.getId());

        assertEquals(1, melliRecuperado.getHabilidades().size());
        assertEquals("Ilusiones", melliRecuperado.getHabilidades().getFirst().getNombre());
    }

    @Test
    public void unEspirituDominaAOtro(){
        GeoJsonPoint puntoDebilitado = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(angelDebilitado, puntoDebilitado);

        GeoJsonPoint puntoNahue = new GeoJsonPoint(0.5, 0.5 + 0.018);
        espirituService.crear(naguet, puntoNahue);

        espirituService.dominar(naguet.getId(), angelDebilitado.getId());

        var naguetDB = espirituService.recuperar(naguet.getId());
        var angelDebilitadoDB = espirituService.recuperar(angelDebilitado.getId());

        assertTrue(angelDebilitadoDB.estaDominado());
        assertEquals(naguetDB.getId(), angelDebilitadoDB.getDominador().getId());
        assertTrue(naguetDB.getEspiritusDominados().stream().anyMatch(e -> e.getId().equals(angelDebilitadoDB.getId())));
    }

    @Test
    public void unEspirituNoPuedeDominarAOtroSiEstaUbicadoAMenosDe2KMDeDistancia(){
        GeoJsonPoint puntoCercano = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(angelDebilitado,puntoCercano);

        GeoJsonPoint puntoDominante = new GeoJsonPoint(0.5, 0.501);
        espirituService.crear(naguet, puntoDominante);

        assertThrows(EspiritusDistanciaException.class, () -> espirituService.dominar(naguet.getId(), angelDebilitado.getId()));
    }

    @Test
    public void unEspirituNoPuedeDominarAOtroSiEstaubicadoAMasDe5KMDeDistancia() {
        GeoJsonPoint puntoLejano = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(angelDebilitado, puntoLejano);

        GeoJsonPoint puntoDominante = new GeoJsonPoint(0.5, 0.5 + 0.1);
        espirituService.crear(naguet, puntoDominante);

        assertThrows(EspiritusDistanciaException.class,
                () -> espirituService.dominar(naguet.getId(), angelDebilitado.getId()));
    }

    @AfterEach
    public void cleanUp() {
        databaseMongoCleaner.deleteAll();
        databaseCleaner.deleteAll();
    }

}