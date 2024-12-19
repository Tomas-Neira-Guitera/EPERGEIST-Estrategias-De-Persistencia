package ar.edu.unq.epersgeist.servicios.impl;


import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.modelo.TipoDeCondicion;
import ar.edu.unq.epersgeist.modelo.condicion.Condicion;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseNeoCleaner;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.exceptions.EntidadConNombreYaExistenteException;
import ar.edu.unq.epersgeist.servicios.exceptions.EspirituSinHabilidadesExeption;
import ar.edu.unq.epersgeist.servicios.exceptions.HabilidadesNoConectadasException;
import ar.edu.unq.epersgeist.servicios.exceptions.MutacionImposibleException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.HabilidadService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Set;

import static ar.edu.unq.epersgeist.modelo.TipoDeCondicion.CANTIDAD_DE_ENERGIA;
import static ar.edu.unq.epersgeist.modelo.TipoDeCondicion.EXORCISMOS_EVITADOS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HabilidadServiceTest {

    @Autowired
    private HabilidadService habilidadService;
    @Autowired
    private DatabaseNeoCleaner databaseNeoCleaner;

    @Autowired
    private DatabaseCleanerService databaseCleanerService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private UbicacionService ubicacionService;

    private Condicion condicionNivelDeConexion;
    private Condicion condicionEnegia;
    private TipoDeCondicion tipoCondicionNivelDeConexion;
    private TipoDeCondicion tipoCondicionEnegia;

    private HabilidadNode habilidadOrigen;
    private HabilidadNode habilidadConectadaEnergia;
    private HabilidadNode habilidadConectadaEnergia2;
    private HabilidadNode habilidadConectadaNivelDeConexion;
    private HabilidadNode habilidadDestino;
    private HabilidadNode habilidadOrigenIgualNombre;

    private Ubicacion ubicacion;
    private Angel guido;
    private Demonio joaco;
    private Angel nahue;

    private HabilidadNode ilusiones;
    private HabilidadNode absorcion;
    private HabilidadNode telepatia;
    private Habilidad ilusionesSQL;

    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;


    @BeforeEach
    void setUp(){
        tipoCondicionNivelDeConexion = TipoDeCondicion.NIVEL_DE_CONEXION;
        condicionNivelDeConexion = new Condicion(tipoCondicionNivelDeConexion, 1);

        tipoCondicionEnegia = TipoDeCondicion.CANTIDAD_DE_ENERGIA;
        condicionEnegia = new Condicion(tipoCondicionEnegia, 1);

        habilidadOrigen  = new HabilidadNode("HabilidadOrigen");
        habilidadDestino = new HabilidadNode("HabilidadDestino");
        habilidadConectadaEnergia = new HabilidadNode("HabilidadConectadaEnergia");
        habilidadConectadaEnergia2 = new HabilidadNode("HabilidadConectadaEnergia2");
        habilidadConectadaNivelDeConexion = new HabilidadNode("HabilidadConectadaNivelDeConexion");
        habilidadOrigenIgualNombre = new HabilidadNode("HabilidadOrigen");

        ilusiones = new HabilidadNode("Ilusiones");
        absorcion = new HabilidadNode("Absorcion");
        telepatia = new HabilidadNode("Telepatia");

        habilidadService.crear(ilusiones);
        ilusionesSQL = new Habilidad("Ilusiones");
        ilusionesSQL.setId(ilusiones.getIdSQL());


        ubicacion = new Santuario("Casa de Guido", 22);
        Area area00San= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        ubicacionService.crear(ubicacion, area00San);

        nahue = new Angel("Nahue",100);
        nahue.setNivelDeConexion(10);

        guido = new Angel("Guido",100, ilusionesSQL);
        guido.setNivelDeConexion(10);

        joaco = new Demonio("Joaco",100, ilusionesSQL);
        joaco.setNivelDeConexion(10);

    }

    @Test
    public void testCreacionDeHabilidad(){
        HabilidadNode habilidadNode = new HabilidadNode("Habilidad");
        var recu = habilidadService.crear(habilidadNode);
        assertEquals(habilidadNode.getNombre(), recu.getNombre());
        assertNotNull(habilidadNode.getId());
    }

    @Test
    public void NoSePuedeCrearUnaHabilidadConNombreExistente(){
        HabilidadNode habilidadNode = new HabilidadNode("Habilidad");
        habilidadService.crear(habilidadNode);
        assertThrows(EntidadConNombreYaExistenteException.class, () -> habilidadService.crear(habilidadNode));
    }
    @Test
    public void testNoSePuedeDescubirHabilidadesQueNoExisten(){

       assertThrows(NoExisteLaEntidadException.class, ()
               -> habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionNivelDeConexion));
    }

    @Test
    public void testNoSePuedeDescubirUnaHabilidadSiOrigenExisteYDestinoNo(){
        habilidadService.crear(habilidadOrigen);
        assertThrows(NoExisteLaEntidadException.class, ()
                -> habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionNivelDeConexion));
    }

    @Test
    public void testNoSePuedeDescubirUnaHabilidadSiOrigenNoExisteYDestinoSi(){
        habilidadService.crear(habilidadDestino);
        assertThrows(NoExisteLaEntidadException.class, ()
                -> habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionNivelDeConexion));
    }

    @Test
    public void testSePuedeDescubrirUnaHabilidadCorrectamente(){
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionNivelDeConexion);
        var habilidades = habilidadService.habilidadesConectadas("HabilidadOrigen");

        assertTrue(habilidades.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadDestino")));
        assertEquals(1, habilidades.size());
    }

    @Test
    public void testNoSePuedenDescubrirDosHabilidadesConElMismoNombre(){
        habilidadService.crear(habilidadOrigen);
        assertThrows(EntidadConNombreYaExistenteException.class, ()
                -> habilidadService.crear(habilidadOrigenIgualNombre));
    }

    @Test
    public void testHabilidadesConectadas(){
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionNivelDeConexion);

        var habilidadesConectadas = habilidadService.habilidadesConectadas("HabilidadOrigen");

        assertEquals( 1, habilidadesConectadas.size());
        assertTrue(habilidadesConectadas.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadDestino")));
    }

    @Test
    public void testSePuedeDescubrirUnaHabilidadCorrectamenteConMasDeUna(){
        HabilidadNode habilidadTercera = new HabilidadNode("HabilidadTercera");
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.crear(habilidadTercera);

        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionEnegia);
        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadTercera", condicionEnegia);
        var habilidades = habilidadService.habilidadesConectadas("HabilidadOrigen");

        assertTrue(habilidades.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadDestino")));
        assertTrue(habilidades.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadTercera")));
        assertEquals(2, habilidades.size());
    }


    @Test
    public void testSePuedeDescubrirUnaHabilidadCorrectamenteConMasDeUnaYDiferentes(){
        HabilidadNode habilidadTercera = new HabilidadNode("HabilidadTercera");
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.crear(habilidadTercera);

        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionEnegia);
        habilidadService.descubrirHabilidad("HabilidadDestino", "HabilidadTercera", condicionEnegia);

        var habilidadesOrigen  = habilidadService.habilidadesConectadas("HabilidadOrigen");
        var habilidadesDestino = habilidadService.habilidadesConectadas("HabilidadDestino");


        assertEquals(1, habilidadesOrigen.size());
        assertEquals(1, habilidadesDestino.size());
    }

    @Test
    public void testSePuedeDescubrirUnaHabilidadCorrectamenteConMasDeUnaYDiferentesYOtras(){
        HabilidadNode habilidadTercera = new HabilidadNode("HabilidadTercera");
        HabilidadNode habilidadCuarta = new HabilidadNode("HabilidadCuarta");
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.crear(habilidadTercera);
        habilidadService.crear(habilidadCuarta);

        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionEnegia);
        habilidadService.descubrirHabilidad("HabilidadDestino", "HabilidadTercera", condicionEnegia);
        habilidadService.descubrirHabilidad("HabilidadTercera", "HabilidadCuarta", condicionEnegia);

        var habilidadesOrigen  = habilidadService.habilidadesConectadas("HabilidadOrigen");
        var habilidadesDestino = habilidadService.habilidadesConectadas("HabilidadDestino");
        var habilidadesTercera = habilidadService.habilidadesConectadas("HabilidadTercera");

        assertTrue(habilidadesOrigen.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadDestino")));
        assertTrue(habilidadesDestino.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadTercera")));
        assertTrue(habilidadesTercera.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadCuarta")));
        assertEquals(1, habilidadesOrigen.size());
        assertEquals(1, habilidadesDestino.size());
        assertEquals(1, habilidadesTercera.size());
    }

    @Test
    public void testSePuedeDescubrirUnaHabilidadCorrectamenteConMasDeUnaYDiferentesYLaPrimeraAOtra(){
        HabilidadNode habilidadTercera = new HabilidadNode("HabilidadTercera");
        HabilidadNode habilidadCuarta = new HabilidadNode("HabilidadCuarta");
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.crear(habilidadTercera);
        habilidadService.crear(habilidadCuarta);

        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadDestino", condicionEnegia);
        habilidadService.descubrirHabilidad("HabilidadDestino", "HabilidadTercera", condicionEnegia);
        habilidadService.descubrirHabilidad("HabilidadOrigen", "HabilidadCuarta", condicionEnegia);

        var habilidadesDestino = habilidadService.habilidadesConectadas("HabilidadDestino");
        var habilidadesTercera = habilidadService.habilidadesConectadas("HabilidadTercera");
        var habilidadesOrigen  = habilidadService.habilidadesConectadas("HabilidadOrigen");

        assertTrue(habilidadesOrigen.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadDestino")));
        assertTrue(habilidadesOrigen.stream().anyMatch(habilidad -> habilidad.getNombre().equals("HabilidadCuarta")));
        assertEquals(2, habilidadesOrigen.size());
        assertEquals(1, habilidadesDestino.size());
        assertEquals(0, habilidadesTercera.size());
    }

    @Test
    public void testHabilidadesConectadasDeUnaHabilidadSinConexiones(){
        habilidadService.crear(habilidadOrigen);

        var habilidadesConectadas = habilidadService.habilidadesConectadas("HabilidadOrigen");


        assertEquals( 0, habilidadesConectadas.size());
    }

    @Test
    public void testHabilidadesConectadasDeUnaHabilidadQueNoExiste(){
        assertThrows(NoExisteLaEntidadException.class, () -> habilidadService.habilidadesConectadas("wadawd"));
    }

    @Test
    public void dosEspiritusPuedenTenerLaMismaHabilidad(){
        Espiritu guidoDB = espirituService.crear(guido);
        Espiritu joacoDB = espirituService.crear(joaco);

        assertEquals(1, guidoDB.getHabilidades().size());
        assertEquals(1, joacoDB.getHabilidades().size());
        assertTrue(guidoDB.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Ilusiones")));
        assertTrue(joacoDB.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Ilusiones")));

    }

    @Test
    public void testDosEspiritusTienenLaMismaHabilidadYObtienenLasMismasMutacionesPosibles() {
        habilidadService.crear(absorcion);
        habilidadService.crear(telepatia);

        Espiritu guidoDB = espirituService.crear(guido);
        Espiritu joacoDB = espirituService.crear(joaco);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionNivelDeConexion);
        habilidadService.descubrirHabilidad("Absorcion", "Telepatia", condicionNivelDeConexion);

        assertEquals(1, guidoDB.getHabilidades().size());
        assertTrue(guidoDB.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Ilusiones")));
        assertEquals(2, habilidadService.habilidadesPosibles(guidoDB.getId()).size());
        assertTrue(habilidadService.habilidadesPosibles(guidoDB.getId()).stream().anyMatch(habilidad -> habilidad.getNombre().equals("Absorcion")));
        assertTrue(habilidadService.habilidadesPosibles(guidoDB.getId()).stream().anyMatch(habilidad -> habilidad.getNombre().equals("Telepatia")));

        assertEquals(1, joacoDB.getHabilidades().size());
        assertTrue(joacoDB.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Ilusiones")));
        assertEquals(2, habilidadService.habilidadesPosibles(joacoDB.getId()).size());
        assertTrue(habilidadService.habilidadesPosibles(joacoDB.getId()).stream().anyMatch(habilidad -> habilidad.getNombre().equals("Absorcion")));
        assertTrue(habilidadService.habilidadesPosibles(joacoDB.getId()).stream().anyMatch(habilidad -> habilidad.getNombre().equals("Telepatia")));
    }


    @Test
    public void testNoHayUnCaminoPosibleEntreDosHabilidadesDiferentes(){
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        Set<TipoDeCondicion> setDeCondiciones = Set.of(tipoCondicionNivelDeConexion);

        assertThrows(HabilidadesNoConectadasException.class, () -> habilidadService.caminoMasRentable(habilidadOrigen.getNombre(), habilidadDestino.getNombre(), setDeCondiciones));
    }

    @Test
    public void testNoHayUnCaminoPosibleEntreDosHabilidadesIguales(){
        habilidadService.crear(habilidadOrigen);
        Set<TipoDeCondicion> setDeCondiciones = Set.of(tipoCondicionNivelDeConexion);

        assertThrows(MutacionImposibleException.class, () -> habilidadService.caminoMasRentable(habilidadOrigen.getNombre(), habilidadOrigen.getNombre(), setDeCondiciones));
    }

    @Test
    public void testNoHayUnaMutacionPosibleEntreDosHabilidades() {
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        Set<TipoDeCondicion> setDeCondiciones = Set.of(tipoCondicionNivelDeConexion);

        habilidadService.descubrirHabilidad(habilidadOrigen.getNombre(), habilidadDestino.getNombre(), condicionEnegia);

        assertThrows(MutacionImposibleException.class, () -> habilidadService.caminoMasRentable(habilidadOrigen.getNombre(), habilidadDestino.getNombre(), setDeCondiciones));
    }

    @Test
    public void testSeEncuentraUnCaminoPosibleEntreDosHabilidades() {
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.crear(habilidadConectadaEnergia);
        Set<TipoDeCondicion> setDeCondiciones = Set.of(tipoCondicionNivelDeConexion, tipoCondicionEnegia);

        habilidadService.descubrirHabilidad(habilidadOrigen.getNombre(), habilidadConectadaEnergia.getNombre(), condicionEnegia);
        habilidadService.descubrirHabilidad(habilidadConectadaEnergia.getNombre(), habilidadDestino.getNombre(), condicionNivelDeConexion);

        List<HabilidadNode> caminoEncontrado = habilidadService.caminoMasRentable(habilidadOrigen.getNombre(), habilidadDestino.getNombre(), setDeCondiciones);

        assertTrue(caminoEncontrado.stream().anyMatch(habilidad -> habilidad.getNombre().equals(habilidadOrigen.getNombre())));
        assertTrue(caminoEncontrado.stream().anyMatch(habilidad -> habilidad.getNombre().equals(habilidadConectadaEnergia.getNombre())));
        assertTrue(caminoEncontrado.stream().anyMatch(habilidad -> habilidad.getNombre().equals(habilidadDestino.getNombre())));
        assertEquals(3, caminoEncontrado.size());
    }

    @Test
    public void testSeEncuentraElCaminoMasRentableEntreDosHabilidades() {
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(habilidadDestino);
        habilidadService.crear(habilidadConectadaEnergia);
        habilidadService.crear(habilidadConectadaEnergia2);
        habilidadService.crear(habilidadConectadaNivelDeConexion);

        Set<TipoDeCondicion> setDeCondiciones = Set.of(tipoCondicionNivelDeConexion, tipoCondicionEnegia);

        habilidadService.descubrirHabilidad(habilidadOrigen.getNombre(), habilidadConectadaEnergia.getNombre(), condicionEnegia);
        habilidadService.descubrirHabilidad(habilidadOrigen.getNombre(), habilidadConectadaEnergia2.getNombre(), condicionEnegia);
        habilidadService.descubrirHabilidad(habilidadConectadaEnergia.getNombre(), habilidadConectadaNivelDeConexion.getNombre(), condicionNivelDeConexion);
        habilidadService.descubrirHabilidad(habilidadConectadaNivelDeConexion.getNombre(), habilidadDestino.getNombre(), condicionNivelDeConexion);
        habilidadService.descubrirHabilidad(habilidadConectadaEnergia2.getNombre(), habilidadDestino.getNombre(), condicionNivelDeConexion);

        List<HabilidadNode> caminoEncontrado = habilidadService.caminoMasRentable(habilidadOrigen.getNombre(), habilidadDestino.getNombre(), setDeCondiciones);

        assertEquals(3, caminoEncontrado.size());
    }

    @Test
    void testCaminoMasMutable() {
        habilidadService.crear(absorcion);
        habilidadService.crear(telepatia);
        var habilidad = new HabilidadNode("Habilidad");
        var habilidad2 = new HabilidadNode("Habilidad2");
        habilidadService.crear(habilidad);
        habilidadService.crear(habilidad2);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", new Condicion(CANTIDAD_DE_ENERGIA, 1));
        habilidadService.descubrirHabilidad("Ilusiones", "Habilidad2", new Condicion(CANTIDAD_DE_ENERGIA, 1));
        habilidadService.descubrirHabilidad("Absorcion", "Telepatia", new Condicion(CANTIDAD_DE_ENERGIA, 5));
        habilidadService.descubrirHabilidad("Ilusiones", "Habilidad", new Condicion(EXORCISMOS_EVITADOS, 10));

        Espiritu guidoBD = espirituService.crear(guido);

        var habilidades = habilidadService.caminoMasMutable(guidoBD.getId(), "Ilusiones");

        assertEquals(3, habilidades.size());
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Telepatia")));
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Absorcion")));

    }

    @Test
    void testCaminoMenosMutable() {
        habilidadService.crear(absorcion);
        habilidadService.crear(telepatia);
        var habilidad = new HabilidadNode("Habilidad");
        var habilidad2 = new HabilidadNode("Habilidad2");
        habilidadService.crear(habilidad);
        habilidadService.crear(habilidad2);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", new Condicion(CANTIDAD_DE_ENERGIA, 1));
        habilidadService.descubrirHabilidad("Ilusiones", "Habilidad2", new Condicion(CANTIDAD_DE_ENERGIA, 1));
        habilidadService.descubrirHabilidad("Absorcion", "Telepatia", new Condicion(CANTIDAD_DE_ENERGIA, 5));
        habilidadService.descubrirHabilidad("Ilusiones", "Habilidad", new Condicion(EXORCISMOS_EVITADOS, 10));

        Espiritu guidoBD = espirituService.crear(guido);

        var habilidades = habilidadService.caminoMenosMutable("Ilusiones", guidoBD.getId());

        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Ilusiones")));
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Habilidad2")));
        assertEquals(2, habilidades.size());
    }

    @Test
    void testEvolucionar() {
        habilidadService.crear(absorcion);
        habilidadService.crear(telepatia);
        var habilidad = new HabilidadNode("Habilidad");
        var habilidad2 = new HabilidadNode("Habilidad2");
        habilidadService.crear(habilidad);
        habilidadService.crear(habilidad2);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", new Condicion(CANTIDAD_DE_ENERGIA, 1));
        habilidadService.descubrirHabilidad("Ilusiones", "Habilidad2", new Condicion(CANTIDAD_DE_ENERGIA, 1));
        habilidadService.descubrirHabilidad("Absorcion", "Telepatia", new Condicion(CANTIDAD_DE_ENERGIA, 5));
        habilidadService.descubrirHabilidad("Ilusiones", "Habilidad", new Condicion(EXORCISMOS_EVITADOS, 10));

        Espiritu guidoBD = espirituService.crear(guido);

        habilidadService.evolucionar(guidoBD.getId());

        var habilidades = espirituService.recuperar(guidoBD.getId()).getHabilidades();

        assertEquals(3, habilidades.size());
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Ilusiones")));
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Habilidad2")));
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Absorcion")));
    }

    @Test
    void testEvolucionarUnEspirituNoExistente() {
        assertThrows(NoExisteLaEntidadException.class, () -> habilidadService.evolucionar(100L));
    }

    @Test
    void testEvolucionarNoMutaAHabilidadesQueYaPosee() {
        habilidadService.crear(absorcion);
        var absorcionSQL = new Habilidad(absorcion.getIdSQL(),"Absorcion");

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", new Condicion(CANTIDAD_DE_ENERGIA, 1));
        habilidadService.descubrirHabilidad("Absorcion", "Ilusiones", new Condicion(CANTIDAD_DE_ENERGIA, 1));

        guido.addHabilidad(absorcionSQL);

        Espiritu guidoBD = espirituService.crear(guido);

        habilidadService.evolucionar(guidoBD.getId());

        var habilidades = espirituService.recuperar(guidoBD.getId()).getHabilidades();

        assertEquals(2, habilidades.size());
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Ilusiones")));
        assertTrue(habilidades.stream().anyMatch(h -> h.getNombre().equals("Absorcion")));
    }

    @Test
    void testEvolucionarNoTienePosiblesEvoluciones() {

        Espiritu nahueDB = espirituService.crear(nahue);

        habilidadService.evolucionar(nahueDB.getId());

        var habilidades = espirituService.recuperar(nahueDB.getId()).getHabilidades();

        assertEquals(0, habilidades.size());
    }


    @Test
    public void seObtienenLasPosiblesMutacionesDeUnEspirituSinHabilidad(){
        Espiritu nahueDB = espirituService.crear(nahue);

        assertThrows(EspirituSinHabilidadesExeption.class, () -> habilidadService.habilidadesPosibles(nahueDB.getId()));
    }

    @Test
    public void seObtienenLasPosiblesMutacionesDeUnEspirituConUnaHabilidadSinMutaciones(){
        Espiritu guidoDB = espirituService.crear(guido);

        assertEquals(0, habilidadService.habilidadesPosibles(guidoDB.getId()).size());
    }

    @Test
    public void seObtienenLasPosiblesMutacionesDeUnEspiritu(){
        habilidadService.crear(absorcion);
        habilidadService.crear(telepatia);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionEnegia);
        habilidadService.descubrirHabilidad("Absorcion", "Telepatia", condicionEnegia);

        Espiritu guidoDB = espirituService.crear(guido);

        assertEquals(2, habilidadService.habilidadesPosibles(guidoDB.getId()).size());
    }

    @Test
    public void siSePasaUnIdDeEspirituQueNoExisteSeLanzaUnaExcepcion(){
        assertThrows(NoExisteLaEntidadException.class, () -> habilidadService.habilidadesPosibles(1L));
    }

    @Test
    public void siElEspirituNoTieneHabilidadesMeTiraExceptionDeQueNotieneHabilidades(){
        Espiritu nahueDB = espirituService.crear(nahue);
        assertThrows(EspirituSinHabilidadesExeption.class, () -> habilidadService.habilidadesPosibles(nahueDB.getId()));
    }
    @Test
    public void siElEspirituTieneUnaHabilidadSolaElSetEsVacio(){
        guido.setNivelDeConexion(100);
        espirituService.crear(guido);
        var guidoDB = espirituService.recuperar(guido.getId());
        var habilidadesPosiblesGuido = habilidadService.habilidadesPosibles(guidoDB.getId());

        assertTrue(habilidadesPosiblesGuido.isEmpty());
    }
    @Test
    public void siElEspiritiTienePosiblesHabilidadesYCumpleLasDevuelve(){
        habilidadService.crear(absorcion);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionEnegia);

        guido.setNivelDeConexion(100);
        espirituService.crear(guido);
        var guidoDB = espirituService.recuperar(guido.getId());
        var habilidadesPosiblesGuido = habilidadService.habilidadesPosibles(guidoDB.getId());

        assertTrue(habilidadesPosiblesGuido.stream().anyMatch(habilidad -> habilidad.getNombre().equals("Absorcion")));
        assertEquals(1, habilidadesPosiblesGuido.size());
    }
    @Test
    public void siElEspiritiTienePosiblesHabilidadesYCumpleLasDevuelveComplejoRamificado(){
        habilidadService.crear(absorcion);
        habilidadService.crear(telepatia);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionEnegia);
        habilidadService.descubrirHabilidad("Absorcion", "Telepatia", condicionNivelDeConexion);

        guido.setNivelDeConexion(100);
        espirituService.crear(guido);
        var guidoDB = espirituService.recuperar(guido.getId());
        var habilidadesPosiblesGuido = habilidadService.habilidadesPosibles(guidoDB.getId());

        var setToNamesHabilidades = habilidadesPosiblesGuido.stream().map(HabilidadNode::getNombre).toList();

        assertFalse(habilidadesPosiblesGuido.isEmpty());
        assertEquals(2, habilidadesPosiblesGuido.size());
        assertTrue(setToNamesHabilidades.contains("Absorcion"));
        assertTrue(setToNamesHabilidades.contains("Telepatia"));
    }

    @Test
    public void siElEspiritiTienePosiblesHabilidadesYCumpleLasDevuelvePeroSiNoLlegaATodasSoloLasQuePuede(){
        habilidadService.crear(absorcion);
        habilidadService.crear(telepatia);
        habilidadService.crear(habilidadDestino);
        Condicion condicionNivel1000 = new Condicion(TipoDeCondicion.NIVEL_DE_CONEXION, 1000);

        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionEnegia);
        habilidadService.descubrirHabilidad("Absorcion", "Telepatia", condicionNivelDeConexion);
        habilidadService.descubrirHabilidad("Telepatia", "HabilidadDestino", condicionNivel1000);

        espirituService.crear(guido);
        var guidoDB = espirituService.recuperar(guido.getId());
        var habilidadesPosiblesGuido = habilidadService.habilidadesPosibles(guidoDB.getId());

        assertTrue(habilidadesPosiblesGuido.stream().anyMatch(habilidad -> habilidad.getNombre().equals("Absorcion")));
        assertTrue(habilidadesPosiblesGuido.stream().anyMatch(habilidad -> habilidad.getNombre().equals("Telepatia")));
        assertEquals(2, habilidadesPosiblesGuido.size());
    }

    @Test
    public void siHayDosCaminosMenosMutablesParaElegirDevuelveUnoSolo(){
        habilidadService.crear(habilidadOrigen);
        habilidadService.crear(absorcion);
        habilidadService.crear(habilidadDestino);
        habilidadService.crear(telepatia);


        habilidadService.descubrirHabilidad("Ilusiones", "HabilidadOrigen", condicionEnegia);
        habilidadService.descubrirHabilidad("HabilidadOrigen", "Absorcion", condicionEnegia);

        habilidadService.descubrirHabilidad("Ilusiones", "HabilidadDestino", condicionNivelDeConexion);
        habilidadService.descubrirHabilidad("HabilidadDestino", "Telepatia", condicionNivelDeConexion);

        espirituService.crear(guido);
        var guidoDB = espirituService.recuperar(guido.getId());
        var caminoMenosMutable = habilidadService.caminoMenosMutable("Ilusiones", guidoDB.getId());

        assertEquals(3, caminoMenosMutable.size());
    }

    @AfterEach
    public void cleanUp() {
        databaseCleanerService.deleteAll();
        databaseNeoCleaner.deleteAll();
        databaseMongoCleaner.deleteAll();
    }

}
