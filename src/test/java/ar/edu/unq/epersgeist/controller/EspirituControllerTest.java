package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
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
public class EspirituControllerTest {

    @Autowired
    private MockMVCEspirituController mockMVCEspirituController;

    @Autowired
    private MockMVCUbicacionController mockMVCUbicacionController;

    @Autowired
    private DatabaseCleanerService databaseCleaner;

    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;

    @Autowired
    private UbicacionService ubicacionService;

    private Espiritu guido;
    private Espiritu valen;

    private Ubicacion playa;
    private Area playaArea;

    private Long guidoId;

    private GeoJsonPoint guidoPunto;
    private GeoJsonPoint valenPunto;

    @BeforeEach
    public void prepare() throws Throwable {

        playaArea = new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        playa = new Santuario("Playa", 20);
        mockMVCUbicacionController.guardarUbicacion(playa, playaArea);

        guidoPunto = new GeoJsonPoint(1, 1);
        valenPunto = new GeoJsonPoint(0.8, 0.8);

        guido = new Angel("Guido", 100);
        valen = new Demonio("ValenEndemoniado", 100);

        guidoId = mockMVCEspirituController.guardarEspiritu(guido, guidoPunto).getId();
        mockMVCEspirituController.guardarEspiritu(valen, valenPunto);
    }

    @Test void testCrearEspiritu() throws Throwable {
        GeoJsonPoint estipiruPunto = new GeoJsonPoint(0.5, 0.5);
        Espiritu estipiruCreado = new Angel("ElEstipiru", 100);

        mockMVCEspirituController.guardarEspiritu(estipiruCreado, estipiruPunto);
        var estipiruRecu = mockMVCEspirituController.recuperarEspiritu(estipiruCreado.getId());

        Assertions.assertEquals(estipiruCreado.getNombre(),estipiruRecu.getNombre());
        Assertions.assertEquals(estipiruCreado.getEnergia(),estipiruRecu.getEnergia());
        assertNotNull(estipiruRecu.getId());

    }

    @Test
    public void testRecuperarEspiritu() throws Throwable {
        EspirituDTO elRecuGuido = mockMVCEspirituController.recuperarEspiritu(guidoId);

        Assertions.assertEquals(guido.getNombre(), elRecuGuido.getNombre());
        Assertions.assertEquals(guido.getEnergia(),elRecuGuido.getEnergia());
        assertNotNull(elRecuGuido.getId());
    }

    @Test
    public void testGetAllEspiritus() throws Throwable {
        var espiritus = mockMVCEspirituController.recuperarTodos();

        Assertions.assertEquals(2, espiritus.size());
    }

    @Test
    public void testEliminarCorrectamente() throws Throwable {
        EspirituDTO guidoRecu = mockMVCEspirituController.recuperarEspiritu(guidoId);
        assertNotNull(guidoRecu.getId());

        mockMVCEspirituController.eliminarEspiritu(guidoId);

        assertThrows(NoExisteLaEntidadException.class, () -> mockMVCEspirituController.recuperarEspiritu(guidoId));
    }

    @AfterEach
    public void cleanUp() {
        databaseMongoCleaner.deleteAll();
        databaseCleaner.deleteAll();
    }

}