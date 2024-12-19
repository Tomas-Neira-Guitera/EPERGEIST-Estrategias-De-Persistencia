package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.helper.DatabaseNeoCleaner;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.exceptions.NoSeEncontroSnapshotException;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import ar.edu.unq.epersgeist.utils.Snapshot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EstadisticaServiceTest {

    @Autowired
    private EspirituService espirituService;

    @Autowired
    private EstadisticaService estadisticaService;

    @Autowired
    private DatabaseCleanerService databaseCleaner;

    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;

    @Autowired
    private UbicacionService ubicacionService;

    private Demonio demonioFuerte;
    private Demonio demonioDebilitado;

    private Angel angelDebilitado;
    private Angel angelFuerte;
    private Demonio demonioDebilitado2;


    private Area bernalArea;
    private Ubicacion bernal;

    private Ubicacion playa;
    private Area playaArea;
    @Autowired
    private DatabaseNeoCleaner databaseNeoCleaner;

    @BeforeEach
    public void setUp() {
        demonioFuerte = new Demonio("DemonioFuerte", 100);
        demonioDebilitado = new Demonio("DemonioDebilitado", 45);
        demonioDebilitado2 = new Demonio("DemonioDebilitado2", 45);

        angelDebilitado = new Angel("AngelDebilitado", 45);
        angelFuerte = new Angel("AngelFuerte", 100);

        playaArea = new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 1), new GeoJsonPoint(1, 1), new GeoJsonPoint(1, 0), new GeoJsonPoint(0, 0));
        playa = new Santuario("Playa", 20);
        ubicacionService.crear(playa, playaArea);

        bernalArea = new Area(new GeoJsonPoint(10, 10), new GeoJsonPoint(10, 11), new GeoJsonPoint(11, 11), new GeoJsonPoint(11, 10), new GeoJsonPoint(10, 10));
        bernal = new Santuario("Bernal", 20);
        ubicacionService.crear(bernal, bernalArea);
    }



    @Test
    public void obtenerLaUbicacionMasDominada(){
        GeoJsonPoint puntoDeAngelDebilitado = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(angelDebilitado, puntoDeAngelDebilitado);


        GeoJsonPoint puntoDeDemonioFuerte = new GeoJsonPoint(0.5, 0.5 + 0.018);
        espirituService.crear(demonioDebilitado2, puntoDeDemonioFuerte);
        espirituService.crear(demonioFuerte, puntoDeDemonioFuerte);

        espirituService.dominar(demonioFuerte.getId(), angelDebilitado.getId());
        espirituService.dominar(angelDebilitado.getId(), demonioDebilitado2.getId());

        GeoJsonPoint puntoDeAngelFuerte = new GeoJsonPoint(10.5, 10.5);
        espirituService.crear(angelFuerte, puntoDeAngelFuerte);

        GeoJsonPoint puntoDeDemonioDebilitado = new GeoJsonPoint(10.5, 10.5 + 0.018);
        espirituService.crear(demonioDebilitado, puntoDeDemonioDebilitado);

        espirituService.dominar(angelFuerte.getId(), demonioDebilitado.getId());

        LocalDateTime hoy = LocalDateTime.now();
        LocalDateTime ayer = hoy.minusDays(1);

        var ubi = estadisticaService.ubicacionMasDominada(ayer , hoy);

        assertEquals(bernal.getNombre(), ubi.getNombre());
    }

    @Test
    public void obtenerAlEspirituMasDominante(){
        GeoJsonPoint puntoDeAngelDebilitado = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(angelDebilitado, puntoDeAngelDebilitado);


        GeoJsonPoint puntoDeDemonioFuerte = new GeoJsonPoint(0.5, 0.5 + 0.018);
        espirituService.crear(demonioDebilitado2, puntoDeAngelDebilitado);
        espirituService.crear(demonioFuerte, puntoDeDemonioFuerte);

        espirituService.dominar(demonioFuerte.getId(), angelDebilitado.getId());
        espirituService.dominar(demonioFuerte.getId(), demonioDebilitado2.getId());

        GeoJsonPoint puntoDeAngelFuerte = new GeoJsonPoint(10.5, 10.5);
        espirituService.crear(angelFuerte, puntoDeAngelFuerte);

        GeoJsonPoint puntoDeDemonioDebilitado = new GeoJsonPoint(10.5, 10.5 + 0.018);
        espirituService.crear(demonioDebilitado, puntoDeDemonioDebilitado);

        espirituService.dominar(angelFuerte.getId(), demonioDebilitado.getId());

        LocalDateTime hoy = LocalDateTime.now();
        LocalDateTime ayer = hoy.minusDays(1);

        Espiritu espiritu = estadisticaService.espirituMasDominante(ayer , hoy);

            assertEquals(espiritu.getNombre(), demonioFuerte.getNombre());
    }

    @Test
    public void testNoSePuedeObtenerUnSnapshotQueNoExiste(){
        assertThrows(NoSeEncontroSnapshotException.class, () -> estadisticaService.obtenerSnapshot(LocalDate.now()));
    }

    @Test
    public void testSeCreaUnSnapshotCorrectamente() {
        estadisticaService.crearSnapshot();
        Snapshot snapShotRecuperado = estadisticaService.obtenerSnapshot(LocalDate.now());

        assertEquals(3, snapShotRecuperado.getSqlData().size());
        assertEquals(2, snapShotRecuperado.getMongoData().size());
        assertEquals(1, snapShotRecuperado.getNeoData().size());
        assertEquals(LocalDate.now(), snapShotRecuperado.getFecha());
    }



    @AfterEach
    public void cleanUp() {
        databaseMongoCleaner.deleteAll();
        databaseNeoCleaner.deleteAll();
        databaseCleaner.deleteAll();
    }
}
