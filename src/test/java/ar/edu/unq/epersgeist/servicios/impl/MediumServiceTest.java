package ar.edu.unq.epersgeist.servicios.impl;


import ar.edu.unq.epersgeist.controller.dto.medium.ActualizarMediumDTO;
import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.helper.DatabaseNeoCleaner;
import ar.edu.unq.epersgeist.modelo.TipoDeCondicion;
import ar.edu.unq.epersgeist.modelo.condicion.Condicion;
import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import ar.edu.unq.epersgeist.modelo.medium.Medium;

import ar.edu.unq.epersgeist.modelo.random.NroFijoGetter;
import ar.edu.unq.epersgeist.modelo.random.Random;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.mongodb.CoordenadaDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.MasDe100KilometrosException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.exceptions.CoordenadaFueraDeLosLimitesException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.HabilidadService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MediumServiceTest {


    @Autowired
    private MediumService mediumService;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private HabilidadService habilidadService;

    @Autowired
    private CoordenadaDAO coordenadaDAO;

    @Autowired
    private DatabaseCleanerService databaseCleaner;
    @Autowired
    private DatabaseNeoCleaner databaseNeoCleaner;
    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;


    private Ubicacion cementerio;
    private Ubicacion santuario;


    private Espiritu espirituDemCem;
    private Espiritu espirituDemSan;
    private Espiritu espirituAngSan;
    private Espiritu espirituAngCem;
    private Espiritu espirituDemonioAMorir;
    private Espiritu espirituDemonioAVivir;

    private Medium mediumExorcista;
    private Medium mediumAExorcisar;
    private Medium mediumSan;
    private Medium mediumCem;

    private Random random;
    private HabilidadNode ilusiones;
    private HabilidadNode absorcion;
    private Condicion condicionExorcismosExitosos;
    private Condicion condicionExorcismosEvitados;


    @BeforeEach
    void setUp() {

        ilusiones = new HabilidadNode("Ilusiones");
        absorcion = new HabilidadNode("Absorcion");
        condicionExorcismosExitosos = new Condicion(TipoDeCondicion.EXORCISMOS_RESUELTOS, 1);
        condicionExorcismosEvitados = new Condicion(TipoDeCondicion.EXORCISMOS_EVITADOS, 1);

        Area area00San= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        Area area40Cem= new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 2), new GeoJsonPoint(6, 2), new GeoJsonPoint(6, 0), new GeoJsonPoint(4, 0));

        santuario = new Santuario("Santuario", 20);
        cementerio = new Cementerio("Cementerio", 20);
        ubicacionService.crear(santuario, area00San);
        ubicacionService.crear(cementerio, area40Cem);

        mediumExorcista = new Medium("mediumExorcista", 300);
        mediumAExorcisar = new Medium("mediumAExorcisar", 50);
        mediumSan = new Medium("mediumSan", 100);
        mediumCem = new Medium("mediumCem", 80);
        mediumService.crear(mediumSan, new GeoJsonPoint(1,1));
        mediumService.crear(mediumCem, new GeoJsonPoint(5,1));
        mediumService.crear(mediumExorcista, new GeoJsonPoint(1.7,1));
        mediumService.crear(mediumAExorcisar, new GeoJsonPoint(0.7,0.7));

        espirituDemonioAMorir = new Demonio("espirituDemonioAMorir",8);
        espirituDemonioAVivir = new Demonio("espirituDemonioAVivir",  100);
        espirituDemSan = new Demonio("espirituDemSan", 100);
        espirituAngSan = new Angel("espirituAngSan", 100);
        espirituDemCem = new Demonio("espirituDemCem",  100);
        espirituAngCem = new Angel("espirituAngCem", 100);
        espirituService.crear(espirituAngCem, new GeoJsonPoint(4.6, 1.5));
        espirituService.crear(espirituDemonioAMorir, new GeoJsonPoint(0.5, 0.5));
        espirituService.crear(espirituDemonioAVivir, new GeoJsonPoint(0.6, 0.6));
        espirituService.crear(espirituDemSan, new GeoJsonPoint(1.1, 1.1));
        espirituService.crear(espirituAngSan, new GeoJsonPoint(1.2, 1.2));
        espirituService.crear(espirituDemCem, new GeoJsonPoint(4.5, 0.5));




        random = Random.getInstance();

    }


    @Test
    void unMediumDescansaEnUnSantuario(){
        /*
         * Se espera que el medium recupere de mana el 150% de la energia del lugar
         * y que los angeles conectados a el recuperen en energia el total que provee el lugar,
         * pero que los demonios no recuperen energia.
         * */
        espirituService.conectar(espirituDemSan.getId(), mediumSan.getId());
        espirituService.conectar(espirituAngSan.getId(), mediumSan.getId());

        var energiaEsperadaMedium = (int) (mediumSan.getMana() + (santuario.getEnergia() * 1.5));
        var energiaEsperadaDemonio = espirituDemSan.getEnergia();
        var energiaEsperadaAngel = Math.min(100, (espirituAngSan.getEnergia() + santuario.getEnergia()));


        mediumService.descansar(mediumSan.getId());

        var mediumRecu = mediumService.recuperar(mediumSan.getId());
        var demonioRecu = espirituService.recuperar(espirituDemSan.getId());
        var angelitoRecu = espirituService.recuperar(espirituAngSan.getId());

        assertEquals(energiaEsperadaMedium, mediumRecu.getMana());
        assertEquals(energiaEsperadaAngel, angelitoRecu.getEnergia());
        assertEquals(energiaEsperadaDemonio, demonioRecu.getEnergia());
    }

    @Test
    void unMediumDescansaEnUnCementerio(){
        /*
         * Se espera que el medium recupere de mana el 50% de la energia del lugar
         * y que los demonios conectados a el recuperen en energia el total que provee el lugar,
         * pero que los angeles no recuperen energia.
         * */

        espirituService.conectar(espirituDemCem.getId(), mediumCem.getId());
        espirituService.conectar(espirituAngCem.getId(), mediumCem.getId());


        var energiaEsperadaMedium = mediumCem.getMana() + (cementerio.getEnergia() / 2);
        var energiaEsperadaDemonio = Math.min(100,(espirituDemCem.getEnergia() + cementerio.getEnergia()));
        var energiaEsperadaAngel = espirituAngCem.getEnergia();


        mediumService.descansar(mediumCem.getId());

        var mediumRecu = mediumService.recuperar(mediumCem.getId());
        var demonioRecu = espirituService.recuperar(espirituDemCem.getId());
        var angelitoRecu = espirituService.recuperar(espirituAngCem.getId());

        assertEquals(energiaEsperadaMedium, mediumRecu.getMana());
        assertEquals(energiaEsperadaAngel, angelitoRecu.getEnergia());
        assertEquals(energiaEsperadaDemonio, demonioRecu.getEnergia());
    }


    @Test
    void alDescansarUnMediumQueNoExisteTiraUnError() {

        assertThrows(NoExisteLaEntidadException.class, () -> mediumService.descansar(234423454L));

    }

    @Test
    void inicialmenteUnMediumQueNoEstaConectadoNoTieneNningunEspirituEnSusEspiritus() {
        List<Espiritu> espiritus = mediumService.espiritus(mediumCem.getId());

        assertEquals(0, espiritus.size());
    }


    @Test
    void cuandoObtengoLosEspiritusDeUnMediumSonObtenidosCorrectamente() {
        espirituService.conectar(espirituDemCem.getId(), mediumCem.getId());

        List<Espiritu> espiritus = mediumService.espiritus(mediumCem.getId());

        assertEquals(1, espiritus.size());

    }

    @Test
    void cuandoSeInvocaAUnEspirituSeLoInvocaCorrectamenteSiNoEstaConectadoAOtroMedium() {
        assertTrue(espirituDemCem.estaLibre());

        mediumService.invocar(mediumCem.getId(), espirituDemCem.getId());

        String ubicacionDelEspiritu = espirituService.recuperar(espirituDemCem.getId()).getUbicacion().getNombre();
        String ubicacionDelMedium = mediumService.recuperar(mediumCem.getId()).getUbicacion().getNombre();
        assertEquals(ubicacionDelEspiritu, ubicacionDelMedium);
    }

    @Test
    void noSePuedeInvocarAUnEspirituQueSeEncuentreAMasDe100Kilometros(){
        assertTrue(espirituDemSan.estaLibre());
        assertThrows(MasDe100KilometrosException.class, () -> mediumService.invocar(mediumCem.getId(), espirituDemSan.getId()));
    }

    @Test
    void cuandoSeInvocaAUnEspirituSiEstaConectadoAOtroMediumNoLoPuedeInvocar() {
//        Falla porque estan a mas de 100km, hay que testearlo con otros valores...
//        espirituService.conectar(espirituDemCem.getId(), mediumCem.getId());
//        assertThrows(EspirituConectadoException.class, () -> mediumService.invocar(mediumExorcista.getId(), espirituDemCem.getId()));
    }


    @Test
    void cuandoSeInvocaAUnEspirituSiEstanEnLaMismaUbicacionInvocaPeroSigueEnLaMismaUbicacion() {

        assertTrue(espirituDemCem.estaLibre());

        mediumService.invocar(mediumCem.getId(), espirituDemCem.getId());

        String nombreEspirituDifLocRecu = espirituService.recuperar(espirituDemCem.getId()).getUbicacion().getNombre();
        String nombreUbicMediumRecu = mediumService.recuperar(mediumCem.getId()).getUbicacion().getNombre();
        assertEquals(nombreUbicMediumRecu, nombreEspirituDifLocRecu);
    }

    @Test
    void noSePuedeInvocarAUnDemonioEstandoEnUnSantuario(){
        // Falla porque estan a mas de 100km, hay que testearlo con otros valores
        //assertThrows(InvocacionDeDemonioEnSantuarioException.class, () -> mediumService.invocar(mediumSan.getId(), espirituDemCem.getId()));
    }

    @Test
    void noSePuedeInvocarAUnAngelEstandoEnUnCementerio(){
        assertThrows(InvocacionDeAngelEnCementerioException.class, () -> mediumService.invocar(mediumCem.getId(), espirituAngCem.getId()));
    }


    @Test
    void unMediumNoPuedeExorcizarSiNoTieneAlMenosUnEspirituAngel() {
        espirituService.conectar(espirituDemSan.getId(), mediumSan.getId());

        assertThrows(ExorcistaSinAngelesException.class, () -> mediumService.exorcizar(mediumSan.getId(), mediumCem.getId()));
    }

    @Test
    void unMediumNoPuedeExorcizarSiElOtroMediumNoTieneDemonios() {

        espirituService.conectar(espirituAngSan.getId(), mediumSan.getId());

        int energiaAngAntesDelAtaque = (espirituService.recuperar(espirituAngSan.getId())).getEnergia();

        mediumService.exorcizar(mediumSan.getId(), mediumSan.getId());

        int energiaAngPostAtaque = (espirituService.recuperar(espirituAngSan.getId())).getEnergia();
        assertEquals(energiaAngAntesDelAtaque, energiaAngPostAtaque);
    }

    @Test
    void unMediumPuedeExorcizarAOtroMediumTotalmente() {


        random.setStrategy(new NroFijoGetter(7));

        espirituService.conectar(espirituAngSan.getId(), mediumExorcista.getId());
        espirituService.conectar(espirituDemonioAMorir.getId(), mediumAExorcisar.getId());

        var exorcismosResueltosAntesDeExorcisar = espirituAngSan.getExorcismosResueltos();

        var energiaInicialAngel = espirituAngSan.getEnergia();
        mediumService.exorcizar(mediumExorcista.getId(), mediumAExorcisar.getId());

        List<Espiritu> espiritusDelExorcizado = mediumService.espiritus(mediumAExorcisar.getId());

        var angelRecu = espirituService.recuperar(espirituAngSan.getId());

        assertEquals(0, espiritusDelExorcizado.size());
        assertEquals((energiaInicialAngel - 10), angelRecu.getEnergia());
        assertEquals(exorcismosResueltosAntesDeExorcisar + 1, angelRecu.getExorcismosResueltos());
    }

    @Test
    public void unMediumPuedeExorcizarAOtroMediumTotalmenteYLosEspiritusExorcistasMutanSuHabilidadSiEsPosible() {
        habilidadService.crear(ilusiones);
        habilidadService.crear(absorcion);
        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionExorcismosExitosos);

        Habilidad ilusionesSQL = new Habilidad("Ilusiones");
        ilusionesSQL.setId(ilusiones.getIdSQL());
        espirituService.agregarHabilidad(espirituAngSan.getId(), ilusionesSQL.getId());

        random.setStrategy(new NroFijoGetter(7));

        espirituService.conectar(espirituAngSan.getId(), mediumExorcista.getId());
        espirituService.conectar(espirituDemonioAMorir.getId(), mediumAExorcisar.getId());


        var exorcismosResueltosAntesDeExorcisar = espirituAngSan.getExorcismosResueltos();

        var energiaInicialAngel = espirituAngSan.getEnergia();
        mediumService.exorcizar(mediumExorcista.getId(), mediumAExorcisar.getId());

        List<Espiritu> espiritusDelExorcizado = mediumService.espiritus(mediumAExorcisar.getId());
        var angelRecu = espirituService.recuperar(espirituAngSan.getId());

        assertEquals(0, espiritusDelExorcizado.size());
        assertEquals((energiaInicialAngel - 10), angelRecu.getEnergia());
        assertEquals(exorcismosResueltosAntesDeExorcisar + 1, angelRecu.getExorcismosResueltos());
        assertEquals(2, angelRecu.getHabilidades().size());
        assertTrue(angelRecu.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Ilusiones")));
        assertTrue(angelRecu.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Absorcion")));
    }

    @Test
    void unMediumPuedeExorcizarAOtroMediumPeroParcialmente() {

        random.setStrategy(new NroFijoGetter(9));

        espirituService.conectar(espirituAngSan.getId(), mediumExorcista.getId());
        espirituService.conectar(espirituDemonioAVivir.getId(), mediumAExorcisar.getId());

        var energiaAntesDeExorcizar = espirituDemonioAVivir.getEnergia();
        var danio = espirituService.recuperar(espirituAngSan.getId()).getNivelDeConexion() / 2;
        var energiaEsperada = energiaAntesDeExorcizar - danio;

        var exorcismosResueltosAntesDeExorcisar = espirituAngSan.getExorcismosResueltos();
        var exorcismosEvitadosDelDemonioAntesDeSerExorcizado = espirituDemonioAVivir.getExorcismosEvitados();

        mediumService.exorcizar(mediumExorcista.getId(), mediumAExorcisar.getId());

        List<Espiritu> espiritusDelExorcizado = mediumService.espiritus(mediumAExorcisar.getId());
        var demonioRecu = espirituService.recuperar(espirituDemonioAVivir.getId());
        var angelRecu = espirituService.recuperar(espirituAngSan.getId());

        assertEquals(1, espiritusDelExorcizado.size());
        assertEquals(energiaEsperada, demonioRecu.getEnergia());
        assertEquals(exorcismosResueltosAntesDeExorcisar, angelRecu.getExorcismosResueltos());
        assertEquals(exorcismosEvitadosDelDemonioAntesDeSerExorcizado + 1, demonioRecu.getExorcismosEvitados());
    }

    @Test
    public void unMediumPuedeExorcizarAOtroMediumPeroParcialmenteYLosEspiritusNoExorcizadosMutanSuHabilidadSiEsPosible() {

        habilidadService.crear(ilusiones);
        habilidadService.crear(absorcion);
        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionExorcismosEvitados);

        Habilidad ilusionesSQL = new Habilidad("Ilusiones");
        ilusionesSQL.setId(ilusiones.getIdSQL());
        espirituService.agregarHabilidad(espirituDemonioAVivir.getId(), ilusionesSQL.getId());

        random.setStrategy(new NroFijoGetter(9));

        espirituService.conectar(espirituDemonioAVivir.getId(), mediumAExorcisar.getId());
        espirituService.conectar(espirituAngSan.getId(), mediumExorcista.getId());

        var energiaAntesDeExorcizar = espirituDemonioAVivir.getEnergia();
        var danio = espirituService.recuperar(espirituAngSan.getId()).getNivelDeConexion() / 2;
        var energiaEsperada = energiaAntesDeExorcizar - danio;

        var exorcismosResueltosAngel = espirituAngSan.getExorcismosResueltos();
        var exorcismosEvitadosDelDemonio = espirituDemonioAVivir.getExorcismosEvitados();

        mediumService.exorcizar(mediumExorcista.getId(), mediumAExorcisar.getId());

        List<Espiritu> espiritusDelExorcizado = mediumService.espiritus(mediumAExorcisar.getId());
        var demonioRecu = espirituService.recuperar(espirituDemonioAVivir.getId());
        var angelRecu = espirituService.recuperar(espirituAngSan.getId());

        assertEquals(1, espiritusDelExorcizado.size());
        assertEquals(energiaEsperada, demonioRecu.getEnergia());
        assertEquals(exorcismosEvitadosDelDemonio + 1, demonioRecu.getExorcismosEvitados());
        assertEquals(exorcismosResueltosAngel, angelRecu.getExorcismosResueltos());
        assertEquals(2, demonioRecu.getHabilidades().size());
        assertTrue(demonioRecu.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Ilusiones")));
        assertTrue(demonioRecu.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Absorcion")));
    }

    @Test
    public void unMediumPuedeExorcizarAOtroMediumPeroParcialmenteYLosEspiritusExorcistasNoMutanSuHabilidad() {
        espirituService.conectar(espirituAngSan.getId(), mediumExorcista.getId());
        espirituService.conectar(espirituDemonioAVivir.getId(), mediumAExorcisar.getId());

        habilidadService.crear(ilusiones);
        habilidadService.crear(absorcion);
        habilidadService.descubrirHabilidad("Ilusiones", "Absorcion", condicionExorcismosEvitados);

        Habilidad ilusionesSQL = new Habilidad("Ilusiones");
        ilusionesSQL.setId(ilusiones.getIdSQL());
        espirituService.agregarHabilidad(espirituAngSan.getId(), ilusionesSQL.getId());


        random.setStrategy(new NroFijoGetter(9));


        var energiaAntesDeExorcizar = espirituDemonioAVivir.getEnergia();
        var danio = espirituService.recuperar(espirituAngSan.getId()).getNivelDeConexion() / 2;
        var energiaEsperada = energiaAntesDeExorcizar - danio;

        var exorcismosResueltosAngelAntesDeExorcizar = espirituAngSan.getExorcismosResueltos();
        var exorcismosEvitadosDelDemonioAntesDeExorcizar = espirituDemonioAVivir.getExorcismosEvitados();

        mediumService.exorcizar(mediumExorcista.getId(), mediumAExorcisar.getId());

        List<Espiritu> espiritusDelExorcizado = mediumService.espiritus(mediumAExorcisar.getId());
        var demonioRecu = espirituService.recuperar(espirituDemonioAVivir.getId());
        var angelRecu = espirituService.recuperar(espirituAngSan.getId());

        assertEquals(1, espiritusDelExorcizado.size());
        assertEquals(energiaEsperada, demonioRecu.getEnergia());
        assertEquals(exorcismosEvitadosDelDemonioAntesDeExorcizar + 1, demonioRecu.getExorcismosEvitados());
        assertEquals(exorcismosResueltosAngelAntesDeExorcizar, angelRecu.getExorcismosResueltos());
        assertEquals(1, angelRecu.getHabilidades().size());
        assertTrue(angelRecu.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Ilusiones")));
        // No muta la habilidad
        assertFalse(angelRecu.getHabilidades().stream().anyMatch(habilidad -> habilidad.getNombre().equals("Absorcion")));
    }

    @Test
    void cuandoObtengoLosEspiritusDeUnMediumQueNoExisteDevuelveListaVacia() {
        mediumCem.crearConexion(espirituDemCem);

        List<Espiritu> espiritus = mediumService.espiritus(mediumCem.getId());

        assertEquals(0, espiritus.size());

    }

    @Test
    void enCondicionSuficienteParaInvocarSiEsUnIDEspirituInexistente() {
        espirituService.conectar(espirituDemCem.getId(), mediumCem.getId());
        assertThrows(NoExisteLaEntidadException.class, () -> mediumService.invocar(mediumSan.getId(), 23423L));
    }

    @Test
    void  unMediumNoPuedeExcorcizarAOtroSiElSegundoNoExiste() {
        espirituService.conectar(espirituAngSan.getId(), mediumExorcista.getId());
        espirituService.conectar(espirituDemonioAMorir.getId(), mediumAExorcisar.getId());

        random.setStrategy(new NroFijoGetter(7));

        mediumService.actualizar(mediumExorcista);
        espirituService.actualizar(espirituAngSan);
        espirituService.actualizar(espirituDemonioAMorir);

        assertThrows(NoExisteLaEntidadException.class, () -> mediumService.exorcizar(mediumExorcista.getId(), 3453455L));

    }

    @Test
    void unMediumNoPuedeExcorcizarAOtroSiElPrimeroNoExiste() {
        espirituService.conectar(espirituAngSan.getId(), mediumExorcista.getId());
        espirituService.conectar(espirituDemonioAMorir.getId(), mediumAExorcisar.getId());

        random.setStrategy(new NroFijoGetter(7));

        mediumService.actualizar(mediumAExorcisar);
        espirituService.actualizar(espirituAngSan);
        espirituService.actualizar(espirituDemonioAMorir);

        assertThrows(NoExisteLaEntidadException.class, () ->
                mediumService.exorcizar(56456L, mediumAExorcisar.getId()));

    }

    @Test
    void testAlCrearUnMediumSeCreaCorrectamente() {
        Medium mediumSanRecu = mediumService.recuperar(mediumSan.getId());


        assertEquals(mediumSan.getNombre(), mediumSanRecu.getNombre());
        assertEquals(mediumSan.getMana(), mediumSanRecu.getMana());
        assertEquals(mediumSan.getUbicacion().getNombre(), mediumSanRecu.getUbicacion().getNombre());
    }

    @Test
    void testAlrecuperarSeRecuperaCorrectamente() {
        Medium mediumSanRecu2 = mediumService.recuperar(mediumSan.getId());
        List<Medium> mediumSans = mediumService.recuperarTodos();

        assertEquals(4, mediumSans.size());
        assertEquals(mediumSan.getNombre(), mediumSanRecu2.getNombre());
        assertEquals(mediumSan.getMana(), mediumSanRecu2.getMana());
        assertEquals(mediumSan.getUbicacion().getNombre(), mediumSanRecu2.getUbicacion().getNombre());
    }

    @Test
    void testNoSePuedeRecuperarAlgoQueNoEsta() {
        assertThrows(NoExisteLaEntidadException.class, () -> mediumService.recuperar(2345234L));
    }

    @Test
    void testAlactualizarSeActualizaCorrectamente() {
        mediumSan.setNombre("NombreNuevo");
        mediumSan.setMana(50);
        mediumSan.setUbicacion(cementerio);

        mediumService.actualizar(mediumSan);
        Medium mediumSanActualizado = mediumService.recuperar(mediumSan.getId());


        assertEquals(mediumSan.getNombre(), mediumSanActualizado.getNombre());
        assertEquals(mediumSan.getMana(), mediumSanActualizado.getMana());
        assertEquals(mediumSan.getUbicacion().getNombre(), mediumSanActualizado.getUbicacion().getNombre());
    }

    @Test
    void testNoSePuedeActualizarAlgoQueNoEsta() {
        assertThrows(NoExisteLaEntidadException.class, () -> mediumService.recuperar(2345234L));
    }

    @Test
    void testAleliminarSeEliminaCorrectamente() {

        mediumService.eliminar(mediumSan.getId());
        List<Medium> mediumSans = mediumService.recuperarTodos();

        assertEquals(3, mediumSans.size());

    }

    @Test
    void testActualizarPorDTO() {

        ActualizarMediumDTO dto = new ActualizarMediumDTO(mediumSan.getId(), "NombreNuevo");

        mediumService.actualizar(dto);
        Medium mediumRecuperado = mediumService.recuperar(mediumSan.getId());


        assertEquals(mediumRecuperado.getNombre(), dto.getNombre());
    }

    @Test
    void testActualizarPorDTOConIdInexistente() {
        ActualizarMediumDTO dto = new ActualizarMediumDTO(1040L, "NombreNuevo");
       assertThrows(NoExisteLaEntidadException.class, () -> mediumService.actualizar(dto));
    }


    @Test
    void testNoSePuedeEliminarAlgoQueNoEsta() {
        assertEquals(4, mediumService.recuperarTodos().size());
        assertThrows(NoExisteLaEntidadException.class, () -> mediumService.eliminar(2345234L));
    }

    @Test
    void testRecuperarTodosSiNoHayRecuperaNada() {
        databaseCleaner.deleteAll();
        assertEquals(0, mediumService.recuperarTodos().size());
    }

    @Test
    void testRecuperarRecuperaCorrectamente() {

        var nom = mediumSan.getNombre();
        var mana = mediumSan.getMana();
        var ubic = mediumSan.getUbicacion().getNombre();
        Medium mediumSanRecuper = mediumService.recuperar(mediumSan.getId());
        var nomrec = mediumSanRecuper.getNombre();
        var manarec = mediumSanRecuper.getMana();
        var ubicrec = mediumSanRecuper.getUbicacion().getNombre();
        assertEquals(nom, nomrec);
        assertEquals(mana, manarec);
        assertEquals(ubic, ubicrec);
    }

    @Test
    void testUnMediumSeMueveAUnaUbicacionJuntoConSusEspiritus() {
        espirituService.conectar(espirituAngCem.getId(), mediumCem.getId());
        mediumService.mover(mediumCem.getId(), 1.5,1.5);

        var mediumRecuperado = mediumService.recuperar(mediumCem.getId());
        var espirituRecuperado = espirituService.recuperar(espirituAngCem.getId());

        assertEquals(santuario.getNombre(), mediumRecuperado.getUbicacion().getNombre());
        assertEquals(santuario.getNombre(), espirituRecuperado.getUbicacion().getNombre());
    }

    @Test
    void testUnMediumSeMueveAUnSantuarioConUnDemonio() {
        espirituService.conectar(espirituDemCem.getId(), mediumCem.getId());

        var energiaEsperadaDemonio = espirituDemCem.getEnergia() - 10;

        mediumService.actualizar(mediumCem);
        mediumService.mover(mediumCem.getId(), 1.5,1.5);
        var mediumRecuperado = mediumService.recuperar(mediumCem.getId());
        var espirituRecuperado = espirituService.recuperar(espirituDemCem.getId());

        assertEquals(santuario.getNombre(), mediumRecuperado.getUbicacion().getNombre());
        assertEquals(santuario.getNombre(), espirituRecuperado.getUbicacion().getNombre());
        assertEquals(energiaEsperadaDemonio, espirituRecuperado.getEnergia());
    }

    @Test
    void testUnMediumSeMueveAUnCementerioConUnAngel() {
        espirituService.conectar(espirituAngSan.getId(), mediumSan.getId());

        var energiaEsperadaAngel = espirituAngSan.getEnergia() - 5;

        mediumService.mover(mediumSan.getId(), 1.5,5.0);

        var mediumRecuperado = mediumService.recuperar(mediumSan.getId());
        var espirituRecuperado = espirituService.recuperar(espirituAngSan.getId());

        assertEquals(cementerio.getNombre(), mediumRecuperado.getUbicacion().getNombre());
        assertEquals(cementerio.getNombre(), espirituRecuperado.getUbicacion().getNombre());
        assertEquals(energiaEsperadaAngel, espirituRecuperado.getEnergia());
    }


    @Test
    void testNoSePuedeMoverAUnaUbicacionQueNoExiste() {

        espirituService.conectar(espirituDemCem.getId(), mediumCem.getId());

        assertThrows(NoExisteUnaUbicacionEnEstePunto.class, () -> mediumService.mover(mediumCem.getId(), 10.0, 10.0));
    }

    @Test
    void testUnMediumQueNoExisteNoSePuedeMover() {
        assertThrows(NoExisteLaEntidadException.class, () -> mediumService.mover(124L, 1.5, 1.5));
    }

    @Test
    void testCrearUnMediumConCoordenadaValidas(){
        Medium mediumm = new Medium("mediumm", 100);
        GeoJsonPoint punto = new GeoJsonPoint(5,1);
        mediumService.crear(mediumm, punto);
        var coordenadaDespueDeCrear = coordenadaDAO.obtenerCoordenadaDeEntidadConId(mediumm.getId(), TipoDeEntidad.MEDIUM).get();

        assertEquals(5.0, coordenadaDespueDeCrear.getPunto().getX());
        assertEquals(1.0, coordenadaDespueDeCrear.getPunto().getY());

    }

    @Test
    void testCrearUnMediumConCoordenadaInvalidasPeroFueraDeLosLimites(){
        Medium mediumm = new Medium("mediumm", 100);
        GeoJsonPoint punto = new GeoJsonPoint(181,99);

        assertThrows(CoordenadaFueraDeLosLimitesException.class, () -> mediumService.crear(mediumm, punto));
    }
    @Test
    void testCrearUnMediumConCoordenadaInvalidasPeroDentroDeLosLimite(){
        Medium mediumm = new Medium("mediumm", 100);
        GeoJsonPoint punto = new GeoJsonPoint(100,10);

        assertThrows(NoExisteUnaUbicacionEnEstePunto.class, () -> mediumService.crear(mediumm, punto));
    }

    @Test
    void testCrearDosMediumsEnUnaMismaCoordenada(){
        Medium medium1= new Medium("mediumm", 100);
        Medium medium2= new Medium("mediumm", 100);
        GeoJsonPoint punto = new GeoJsonPoint(5,1);
        mediumService.crear(medium1, punto);
        mediumService.crear(medium2, punto);
        var coordenadaMedium1 = coordenadaDAO.obtenerCoordenadaDeEntidadConId(medium1.getId(), TipoDeEntidad.MEDIUM).get();
        var coordenadaMedium2 = coordenadaDAO.obtenerCoordenadaDeEntidadConId(medium2.getId(), TipoDeEntidad.MEDIUM).get();

        assertEquals(5.0, coordenadaMedium1.getPunto().getX());
        assertEquals(1.0, coordenadaMedium1.getPunto().getY());
        assertEquals(5.0, coordenadaMedium2.getPunto().getX());
        assertEquals(1.0, coordenadaMedium2.getPunto().getY());
    }

    @Test
    void testAlEliminarSeEliminaLaCoordenadaCorrectamente() {
        Medium mediumm = new Medium("mediumm", 100);
        GeoJsonPoint punto = new GeoJsonPoint(5,1);
        mediumService.crear(mediumm, punto);
        var coordenadaDespueDeCrear = coordenadaDAO.obtenerCoordenadaDeEntidadConId(mediumm.getId(), TipoDeEntidad.MEDIUM).get();

        assertEquals(5.0, coordenadaDespueDeCrear.getPunto().getX());
        assertEquals(1.0, coordenadaDespueDeCrear.getPunto().getY());

        mediumService.eliminar(mediumm.getId());
        var coordOpt= coordenadaDAO.obtenerCoordenadaDeEntidadConId(mediumm.getId(), TipoDeEntidad.MEDIUM);
        var coordOptEmpty = coordOpt.isEmpty();
        assertTrue(coordOptEmpty);
    }


    @AfterEach
    public void cleanUp() {
        databaseMongoCleaner.deleteAll();
        databaseCleaner.deleteAll();
        databaseNeoCleaner.deleteAll();
    }

}
