package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.helper.*;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MediumControllerTest {

    @Autowired
    private MockMVCMediumController mockMVCMediumController;

    @Autowired
    private MockMVCUbicacionController mockMVCUbicacionController;

    @Autowired
    private DatabaseCleanerService databaseCleaner;

    private Medium joaco;
    private Medium nacho;

    private Ubicacion elBosque;

    private Area elbosqueArea;

    private GeoJsonPoint joacoPunto;
    private GeoJsonPoint nachoPunto;


    private Long joacoId;
    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;
    @Autowired
    private DatabaseNeoCleaner databaseNeoCleaner;

    @BeforeEach
    public void prepare() throws Throwable {
        elBosque = new Santuario("El Bosque", 50);
        elbosqueArea = new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        mockMVCUbicacionController.guardarUbicacion(elBosque, elbosqueArea);

        joaco = new Medium("Medium", 100, elBosque);
        nacho = new Medium("Nacho", 100, elBosque);
        joacoPunto = new GeoJsonPoint(0.66, 0.66);
        nachoPunto = new GeoJsonPoint(0.88, 0.88);

        joacoId = mockMVCMediumController.guardarMedium(joaco, joacoPunto).getId();
        mockMVCMediumController.guardarMedium(nacho, nachoPunto);
    }

    @Test void testCrearMedium() throws Throwable {
        GeoJsonPoint nuevoMedPunto = new GeoJsonPoint(0.99, 0.99);
        Medium nuevoMed = new Medium("El  Nuevo", 100, elBosque);

        mockMVCMediumController.guardarMedium(nuevoMed, nuevoMedPunto);
        var elRecuNuevo = mockMVCMediumController.recuperarMedium(nuevoMed.getId());

        Assertions.assertEquals(nuevoMed.getNombre(),elRecuNuevo.getNombre());
        Assertions.assertEquals(nuevoMed.getMana(),elRecuNuevo.getMana());
        assertNotNull(elRecuNuevo.getId());


    }
    @Test
    public void testRecuperarMedium() throws Throwable {
        MediumDTO elRecuJoaco = mockMVCMediumController.recuperarMedium(joacoId);

        Assertions.assertEquals(joaco.getNombre(), elRecuJoaco.getNombre());
        Assertions.assertEquals(joaco.getMana(),elRecuJoaco.getMana());
        assertNotNull(elRecuJoaco.getId());
    }



    @Test
    public void testGetAllEspiritus() throws Throwable {
        var mediums = mockMVCMediumController.recuperarTodos();

        Assertions.assertEquals(2, mediums.size());
    }


    @Test
    public void testEliminarCorrectamente() throws Throwable {
        MediumDTO joacoRecu = mockMVCMediumController.recuperarMedium(joacoId);
        assertNotNull(joacoRecu.getId());

        mockMVCMediumController.eliminarMedium(joacoId);

        assertThrows(NoExisteLaEntidadException.class, () -> mockMVCMediumController.recuperarMedium(joacoId));
    }



    @AfterEach
    public void cleanUp() {
        databaseCleaner.deleteAll();
        databaseNeoCleaner.deleteAll();
        databaseMongoCleaner.deleteAll();
    }

}