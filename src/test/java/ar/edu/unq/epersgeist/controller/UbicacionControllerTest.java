package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.helper.MockMVCUbicacionController;
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

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UbicacionControllerTest {

    @Autowired
    private MockMVCUbicacionController mockMVCUbicacionController;

    @Autowired
    private DatabaseCleanerService databaseCleaner;

    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;

    private Ubicacion elBosque;
    private Area elBosqueArea;

    private Long elBosqueId;

    @BeforeEach
    public void prepare() throws Throwable {
        elBosqueArea = new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 1), new GeoJsonPoint(5, 1), new GeoJsonPoint(5, 0), new GeoJsonPoint(4, 0));
        elBosque = new Santuario("El Bosque", 20);
        elBosqueId = mockMVCUbicacionController.guardarUbicacion(elBosque, elBosqueArea).getId();
    }

    @Test void testCrearUbicacion() throws Throwable {
        Area ubiArea = new Area(new GeoJsonPoint(-1, 0), new GeoJsonPoint(-1, 1), new GeoJsonPoint(-2, 1), new GeoJsonPoint(-2, 0), new GeoJsonPoint(-1, 0));
        Ubicacion ubi = new Santuario("La Ubi", 100);

        mockMVCUbicacionController.guardarUbicacion(ubi, ubiArea);
        var ubiRecu = mockMVCUbicacionController.recuperarUbicacion(ubi.getId());

        assertEquals(ubi.getNombre(),ubiRecu.getNombre());
        assertEquals(ubi.getEnergia(),ubiRecu.getEnergia());
        assertEquals(ubi.getClass().getSimpleName(),ubiRecu.getTipoUbicacion());
        assertEquals(ubiArea.getIdUbicacion(), ubiRecu.getId());
        assertNotNull(ubiRecu.getId());

    }
    @Test
    public void testRecuperarUbicacion() throws Throwable {
        UbicacionDTO bosqueRecuperado = mockMVCUbicacionController.recuperarUbicacion(elBosqueId);

        assertEquals(elBosque.getNombre(), bosqueRecuperado.getNombre());
    }

    @Test
    public void testEliminarCorrectamente() throws Throwable {
        UbicacionDTO bosqueRecu = mockMVCUbicacionController.recuperarUbicacion(elBosqueId);
        assertNotNull(bosqueRecu.getId());

        mockMVCUbicacionController.eliminarUbicacion(elBosqueId);

        assertThrows(NoExisteLaEntidadException.class, () -> mockMVCUbicacionController.recuperarUbicacion(elBosqueId));
    }




    @AfterEach
    public void cleanUp() {
        databaseMongoCleaner.deleteAll();
        databaseCleaner.deleteAll();
    }

}