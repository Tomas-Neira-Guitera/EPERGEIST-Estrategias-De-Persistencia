package ar.edu.unq.epersgeist.servicios.impl;


import ar.edu.unq.epersgeist.helper.DatabaseCleanerService;
import ar.edu.unq.epersgeist.helper.DatabaseMongoCleaner;
import ar.edu.unq.epersgeist.helper.DatabaseNeoCleaner;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.reportes.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteUnSantuarioCorruptoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private UbicacionServiceImpl ubicacionService;

    @Autowired
    private EspirituServiceImpl espirituService;

    @Autowired
    private MediumServiceImpl mediumService;

    @Autowired
    private DatabaseCleanerService databaseCleaner;
    @Autowired
    private DatabaseNeoCleaner databaseNeoCleaner;
    @Autowired
    private DatabaseMongoCleaner databaseMongoCleaner;
    @Test
    public void testGetSantuarioMasCorrupto() {
        Ubicacion montania = new Santuario("Montania", 20);
        Ubicacion bosque = new Santuario("Bosque", 20);

        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        Area area40Mon= new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 2), new GeoJsonPoint(6, 2), new GeoJsonPoint(6, 0), new GeoJsonPoint(4, 0));
        ubicacionService.crear(bosque, area00Bos);
        ubicacionService.crear(montania, area40Mon);

        Espiritu demonio1 = new Demonio("Demonio1",  60);
        Espiritu demonio2 = new Demonio("Demonio2",  70);
        Espiritu angel1 = new Angel("Angel1",  80);
        Espiritu angel2 = new Angel("Angel2", 90);
        Espiritu angel3 = new Angel("Angel3", 100);
        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);
        var puntoEspiritusMontania = new GeoJsonPoint(4.5,1.5);

        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(angel1, puntoEspiritusBosque);
        espirituService.crear(angel2, puntoEspiritusMontania);
        espirituService.crear(angel3, puntoEspiritusMontania);

        Ubicacion santuarioMasCorrupto = reportService.getSantuarioMasCorrupto();
        assertEquals(bosque.getId(), santuarioMasCorrupto.getId());
    }

    @Test
    public void testGetSantuarioMasCorruptoSinSantuariosExistentes() {
        Ubicacion bosque = new Cementerio("Bosque", 20);

        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        ubicacionService.crear(bosque, area00Bos);

        Espiritu demonio1 = new Demonio("Demonio1", 60);
        Espiritu demonio2 = new Demonio("Demonio2",  70);
        Espiritu angel1 = new Angel("Angel1", 80);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(angel1, puntoEspiritusBosque);

        assertThrows(NoExisteUnSantuarioCorruptoException.class, () -> reportService.getSantuarioMasCorrupto());
    }

    @Test
    public void testGetSantuarioMasCorruptoConUnCementerioYUnSantuarioVacio() {
        Ubicacion montania = new Santuario("Montania", 20);
        Ubicacion bosque = new Cementerio("Bosque", 20);

        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        Area area40Mon= new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 2), new GeoJsonPoint(6, 2), new GeoJsonPoint(6, 0), new GeoJsonPoint(4, 0));
        ubicacionService.crear(bosque, area00Bos);
        ubicacionService.crear(montania, area40Mon);

        Espiritu demonio1 = new Demonio("Demonio1", 60);
        Espiritu demonio2 = new Demonio("Demonio2",70);
        Espiritu angel1 = new Angel("Angel1",80);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(angel1, puntoEspiritusBosque);

        assertThrows(NoExisteUnSantuarioCorruptoException.class, () -> reportService.getSantuarioMasCorrupto());
    }

    @Test
    public void testGetSantuarioMasCorruptoEntreSantuariosEmpatados() {
        Ubicacion montania = new Santuario("Montania", 20);
        Ubicacion bosque = new Santuario("Bosque", 20);

        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        Area area40Mon= new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 2), new GeoJsonPoint(6, 2), new GeoJsonPoint(6, 0), new GeoJsonPoint(4, 0));
        ubicacionService.crear(bosque, area00Bos);
        ubicacionService.crear(montania, area40Mon);

        Espiritu demonio1 = new Demonio("Demonio1", 60);
        Espiritu demonio2 = new Demonio("Demonio2",  70);
        Espiritu angel1 = new Angel("Angel1",  80);
        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);
        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(angel1, puntoEspiritusBosque);

        Espiritu demonio3 = new Demonio("Demonio3",60);
        Espiritu demonio4 = new Demonio("Demonio4", 70);
        Espiritu angel2 = new Angel("Angel2", 80);
        var puntoEspiritusMontania = new GeoJsonPoint(4.5,1.5);
        espirituService.crear(demonio3 , puntoEspiritusMontania);
        espirituService.crear(demonio4 , puntoEspiritusMontania);
        espirituService.crear(angel2, puntoEspiritusMontania);


        Ubicacion santuarioMasCorrupto = reportService.getSantuarioMasCorrupto();
        assertEquals(bosque.getId(), santuarioMasCorrupto.getId());
    }

    @Test
    public void testGetMediumEndemoniadoEnUbicacion() {
        Ubicacion bosque = new Santuario("Bosque", 20);
        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        ubicacionService.crear(bosque, area00Bos);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);

        Medium mediumEndemoniado = new Medium("Medium1", 20);
        Medium guido = new Medium("Guido", 50);
        Espiritu demonio1 = new Demonio("Demonio1", 60);
        Espiritu demonio2 = new Demonio("Demonio2", 70);
        Espiritu demonio3 = new Demonio("Duko", 80);
        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(demonio3 , puntoEspiritusBosque);
        mediumService.crear(mediumEndemoniado , puntoEspiritusBosque);
        mediumService.crear(guido , puntoEspiritusBosque);

        espirituService.conectar(demonio1.getId(), mediumEndemoniado.getId());
        espirituService.conectar(demonio2.getId(), mediumEndemoniado.getId());
        espirituService.conectar(demonio3.getId(), guido.getId());

        Medium mediumsEndemoniadoRecuperado = reportService.getMediumEndemoniadoEn(bosque.getId());
        assertEquals(mediumsEndemoniadoRecuperado.getId(), mediumEndemoniado.getId());
    }

    @Test
    public void testGetMediumEndemoniadoEnUbicacionSinMediumsEndemoniados() {
        Ubicacion bosque = new Santuario("Bosque", 20);
        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        ubicacionService.crear(bosque, area00Bos);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);

        Medium guido = new Medium("Guido", 50);
        Espiritu angel1 = new Angel("Angel1",  60);
        Espiritu angel2 = new Angel("Angel2",  70);
        espirituService.crear(angel1, puntoEspiritusBosque);
        espirituService.crear(angel2, puntoEspiritusBosque);
        mediumService.crear(guido , puntoEspiritusBosque);

        espirituService.conectar(angel1.getId(), guido.getId());
        espirituService.conectar(angel2.getId(), guido.getId());

        assertNull(reportService.getMediumEndemoniadoEn(bosque.getId()));
    }

    @Test
    public void testGetMediumEndemoniadoEnCasoDeEmpateEnLaMismaUbicacion() {
        Ubicacion bosque = new Santuario("Bosque", 20);
        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        ubicacionService.crear(bosque, area00Bos);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);

        Medium mediumEndemoniado = new Medium("Medium1", 20);
        Medium guidoModoDiablo = new Medium("Guido", 50);

        Espiritu demonio1 = new Demonio("Demonio1", 60);
        Espiritu demonio2 = new Demonio("Demonio2", 70);
        Espiritu demonio3 = new Demonio("Duko", 80);
        Espiritu demonio4 = new Demonio("Ysy A", 90);
        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(demonio3 , puntoEspiritusBosque);
        espirituService.crear(demonio4 , puntoEspiritusBosque);
        mediumService.crear(guidoModoDiablo , puntoEspiritusBosque);
        mediumService.crear(mediumEndemoniado , puntoEspiritusBosque);

        espirituService.conectar(demonio1.getId(), mediumEndemoniado.getId());
        espirituService.conectar(demonio2.getId(), mediumEndemoniado.getId());
        espirituService.conectar(demonio3.getId(), guidoModoDiablo.getId());
        espirituService.conectar(demonio4.getId(), guidoModoDiablo.getId());
        // En caso de empate devuelve el primero ordenado por nombre alfabeticamente.
        Medium mediumsEndemoniadoRecuperado = reportService.getMediumEndemoniadoEn(bosque.getId());
        assertEquals(mediumsEndemoniadoRecuperado.getId(), guidoModoDiablo.getId());
    }

    @Test
    public void testCantidadDeDemoniosEnUbicacion() {
        Ubicacion bosque = new Santuario("Bosque", 20);
        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));

        ubicacionService.crear(bosque, area00Bos);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);

        Espiritu demonio1 = new Demonio("Demonio1",60);
        Espiritu demonio2 = new Demonio("Demonio2",70);
        Espiritu demonio3 = new Demonio("Demonio3",80);
        Espiritu angel1 = new Angel("Angel1",90);
        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(demonio3 , puntoEspiritusBosque);
        espirituService.crear(angel1 , puntoEspiritusBosque);

        int cantidadDeDemonios = reportService.cantidadDeDemoniosEn(bosque.getId());
        assertEquals(3, cantidadDeDemonios);
    }

    @Test
    public void testCantidadDeDemoniosEnUbicacionSinDemonios() {
        Ubicacion bosque = new Santuario("Bosque", 20);
        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));

        ubicacionService.crear(bosque, area00Bos);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);
        Espiritu angel1 = new Angel("Angel1", 90);
        Espiritu angel2 = new Angel("Angel2", 90);
        Espiritu angel3 = new Angel("Angel3", 90);
        espirituService.crear(angel1 , puntoEspiritusBosque);
        espirituService.crear(angel2 , puntoEspiritusBosque);
        espirituService.crear(angel3 , puntoEspiritusBosque);

        int cantidadDeDemonios = reportService.cantidadDeDemoniosEn(bosque.getId());
        assertEquals(0, cantidadDeDemonios);
    }

    @Test
    public void testCantidadDeDemoniosEnUnaUbicacionInexistente() {
        assertThrows(NoExisteLaEntidadException.class, () -> reportService.cantidadDeDemoniosEn(1L));
    }

    @Test
    public void testCantidadDeDemoniosLibresEnUbicacion() {
        Ubicacion bosque = new Santuario("Bosque", 20);
        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));

        ubicacionService.crear(bosque, area00Bos);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);
        Espiritu demonio1 = new Demonio("Demonio1", 60);
        Espiritu demonio2 = new Demonio("Demonio2", 70);
        Espiritu demonio3 = new Demonio("Demonio3", 80);
        Espiritu angel1 = new Angel("Angel1", 90);
        espirituService.crear(demonio1 , puntoEspiritusBosque);
        espirituService.crear(demonio2 , puntoEspiritusBosque);
        espirituService.crear(demonio3 , puntoEspiritusBosque);
        espirituService.crear(angel1 , puntoEspiritusBosque);

        Medium medium = new Medium("Medium1", 20);
        mediumService.crear(medium, puntoEspiritusBosque);

        espirituService.conectar(demonio3.getId(), medium.getId());

        int cantidadDeDemoniosLibres = reportService.cantidadDeDemoniosLibresEn(bosque.getId());
        assertEquals(2, cantidadDeDemoniosLibres);
    }

    @Test
    public void testCantidadDeDemoniosLibresEnUbicacionSinDemonios() {
        Ubicacion bosque = new Santuario("Bosque", 20);
        Area area00Bos= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));

        ubicacionService.crear(bosque, area00Bos);

        var puntoEspiritusBosque = new GeoJsonPoint(0.5, 0.5);
        Espiritu angel1 = new Angel("Angel1", 90);
        Espiritu angel2 = new Angel("Angel2", 90);
        espirituService.crear(angel1 , puntoEspiritusBosque);
        espirituService.crear(angel2 , puntoEspiritusBosque);

        Medium medium = new Medium("Medium1", 20);
        mediumService.crear(medium , puntoEspiritusBosque);

        espirituService.conectar(angel1.getId(), medium.getId());

        int cantidadDeDemoniosLibres = reportService.cantidadDeDemoniosLibresEn(bosque.getId());
        assertEquals(0, cantidadDeDemoniosLibres);
    }

    @Test
    public void testCantidadDeDemoniosLibresEnUbicacionInexistente() {
        assertThrows(NoExisteLaEntidadException.class, () -> reportService.cantidadDeDemoniosLibresEn(1L));
    }

    @Test
    public void testObtenerElReporteDelSantuarioMasCorrupto() {

        Ubicacion ubicacionEsperada = new Santuario("UbicacionEsperada", 20);
        Ubicacion bosque = new Santuario("bosque", 20);

        Area area00UbicEsp= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        Area area40Mon= new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 2), new GeoJsonPoint(6, 2), new GeoJsonPoint(6, 0), new GeoJsonPoint(4, 0));
        Area areaBosque= new Area(new GeoJsonPoint(10, 0), new GeoJsonPoint(10, 2), new GeoJsonPoint(12, 2), new GeoJsonPoint(12, 0), new GeoJsonPoint(10, 0));

        ubicacionService.crear(ubicacionEsperada, area00UbicEsp);
        ubicacionService.crear(bosque, areaBosque);
        var puntoUbicEsperada = new GeoJsonPoint(0.5, 0.5);
        var bosquePunto = new GeoJsonPoint(10.1, 1.5);

        Medium mediumEsperado = new Medium("MediumEsperado", 10, ubicacionEsperada);
        Medium medium1 = new Medium("Medium1", 10, ubicacionEsperada);
        mediumService.crear(medium1 , puntoUbicEsperada);
        mediumService.crear(mediumEsperado , puntoUbicEsperada);

        Espiritu espiritu1 = new Demonio("Espiritu1", 70);
        Espiritu espiritu2 = new Demonio("Espiritu2", 80);
        Espiritu espiritu3 = new Demonio("Espiritu3", 50);
        Espiritu espiritu4 = new Demonio("Espiritu4", 50);
        Espiritu espiritu5 = new Demonio("Espiritu5", 50);
        Espiritu espiritu7 = new Demonio("Espiritu7",  50);
        Espiritu espiritu8 = new Angel("Espiritu8", 50);
        Espiritu espiritu6 = new Demonio("Espiritu6", 50);
        Espiritu espiritu9 = new Demonio("Espiritu9", 50);
        espirituService.crear(espiritu1 , puntoUbicEsperada);
        espirituService.crear(espiritu2 , puntoUbicEsperada);
        espirituService.crear(espiritu3 , puntoUbicEsperada);
        espirituService.crear(espiritu4 , puntoUbicEsperada);
        espirituService.crear(espiritu5 , puntoUbicEsperada);
        espirituService.crear(espiritu6 , puntoUbicEsperada);
        espirituService.crear(espiritu7 , bosquePunto);
        espirituService.crear(espiritu8 , puntoUbicEsperada);
        espirituService.crear(espiritu9 , puntoUbicEsperada);

        espirituService.conectar(espiritu1.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu2.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu3.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu4.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu8.getId(), mediumEsperado.getId());

        espirituService.conectar(espiritu5.getId(), medium1.getId());
        espirituService.conectar(espiritu6.getId(), medium1.getId());

        int cantidadDeDemoniosEsperados = 7;
        int cantidadDeDemoniosLibresEsperados = 1;

        ReporteSantuarioMasCorrupto reporteEsperado = new ReporteSantuarioMasCorrupto(ubicacionEsperada.getNombre(), mediumEsperado, cantidadDeDemoniosEsperados, cantidadDeDemoniosLibresEsperados);
        ReporteSantuarioMasCorrupto reporteObtenido = reportService.santuarioCorrupto();

        assertEquals(reporteEsperado.getNombreSantuario(), reporteObtenido.getNombreSantuario());
        assertEquals(reporteEsperado.getMedium().getId(), reporteObtenido.getMedium().getId());
        assertEquals(reporteEsperado.getDemoniosTotales(), reporteObtenido.getDemoniosTotales());
        assertEquals(reporteEsperado.getDemoniosLibres(), reporteObtenido.getDemoniosLibres());
    }

    @Test
    public void testObtenerElReporteDelSantuarioMasCorruptoEnElCasoDeQueNoExistanSantuarios() {
        assertThrows(NoExisteUnSantuarioCorruptoException.class, () -> reportService.santuarioCorrupto());
    }

    @Test
    public void testObtenerElReporteDelSantuarioMasCorruptoEnElCasoDeQueNoExistanDemoniosEnNingunSantuario() {
        Ubicacion montania = new Santuario("Montania", 20);
        Area area40Mon= new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 2), new GeoJsonPoint(6, 2), new GeoJsonPoint(6, 0), new GeoJsonPoint(4, 0));
        ubicacionService.crear(montania, area40Mon);
        var puntoMontania = new GeoJsonPoint(4.5,1.5);

        Medium medium1 = new Medium("Medium1", 10);
        mediumService.crear(medium1, puntoMontania);

        Espiritu espiritu1 = new Angel("Espiritu1",70);
        espirituService.crear(espiritu1, puntoMontania);

        espirituService.conectar(espiritu1.getId(), medium1.getId());

        assertThrows(NoExisteUnSantuarioCorruptoException.class, () -> reportService.santuarioCorrupto());
    }

    @Test
    public void testObtenerElReporteDelSantuarioMasCorruptoEnElCasoQueHayaUnEmpateEntreLosSantuarios(){
        Ubicacion ubicacionEsperada = new Santuario("Bombonera", 20);
        Ubicacion ubicacion1 = new Santuario("Monumental", 20);
        Area area00Esp= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        Area area40Ubic= new Area(new GeoJsonPoint(4, 0), new GeoJsonPoint(4, 2), new GeoJsonPoint(6, 2), new GeoJsonPoint(6, 0), new GeoJsonPoint(4, 0));
        ubicacionService.crear(ubicacionEsperada, area00Esp);
        ubicacionService.crear(ubicacion1, area40Ubic);
        var puntoEspiritusEsperada = new GeoJsonPoint(0.5, 0.5);
        var puntoEspiritusUbic = new GeoJsonPoint(4.5,1.5);

        Espiritu espiritu1 = new Demonio("Espiritu1", 70);
        Espiritu espiritu2 = new Demonio("Espiritu2", 80);
        Espiritu espiritu3 = new Demonio("Espiritu3", 50);
        Espiritu espiritu4 = new Demonio("Espiritu4", 50);
        Espiritu espiritu5 = new Demonio("Espiritu5", 50);
        Espiritu espiritu6 = new Demonio("Espiritu6", 50);
        espirituService.crear(espiritu1, puntoEspiritusEsperada);
        espirituService.crear(espiritu2, puntoEspiritusEsperada);
        espirituService.crear(espiritu3, puntoEspiritusEsperada);
        espirituService.crear(espiritu4 , puntoEspiritusUbic);
        espirituService.crear(espiritu5 , puntoEspiritusUbic);
        espirituService.crear(espiritu6 , puntoEspiritusUbic);


        int cantidadDeDemoniosEsperados = 3;
        int cantidadDeDemoniosLibresEsperados = 3;
        Medium mediumEsperado = null;
        // En el caso de empate sobre el nivel de corrupcion de los SANTUARIOS, el reporte va a tener el Santuario que tenga el nombre ordenado alfabeticamente de la A-Z.
        ReporteSantuarioMasCorrupto reporteEsperado = new ReporteSantuarioMasCorrupto(ubicacionEsperada.getNombre(), mediumEsperado, cantidadDeDemoniosEsperados, cantidadDeDemoniosLibresEsperados);
        ReporteSantuarioMasCorrupto reporteObtenido = reportService.santuarioCorrupto();

        assertEquals(reporteEsperado.getNombreSantuario(), reporteObtenido.getNombreSantuario());
        assertEquals(reporteEsperado.getMedium(), reporteObtenido.getMedium());
        assertEquals(reporteEsperado.getDemoniosTotales(), reporteObtenido.getDemoniosTotales());
        assertEquals(reporteEsperado.getDemoniosLibres(), reporteObtenido.getDemoniosLibres());
    }

    @Test
    public void testObtenerElReporteDelSantuarioMasCorruptoEnCasoDeEmpateEntreMediums(){
        Ubicacion ubicacionEsperada = new Santuario("Bombonera", 20);
        Area area00Esp= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        ubicacionService.crear(ubicacionEsperada, area00Esp);
        var puntoEspiritusEsperada = new GeoJsonPoint(0.5, 0.5);


        Medium mediumEsperado = new Medium("Guido", 10);
        Medium medium1 = new Medium("Medium1", 10);
        mediumService.crear(medium1, puntoEspiritusEsperada);
        mediumService.crear(mediumEsperado, puntoEspiritusEsperada);

        Espiritu espiritu1 = new Demonio("Espiritu1", 70);
        Espiritu espiritu2 = new Demonio("Espiritu2", 80);
        Espiritu espiritu3 = new Demonio("Espiritu3", 50);
        Espiritu espiritu4 = new Demonio("Espiritu4", 50);
        Espiritu espiritu5 = new Demonio("Espiritu5", 50);
        Espiritu espiritu6 = new Demonio("Espiritu6", 50);
        Espiritu espiritu7 = new Angel("Espiritu7", 50);
        Espiritu espiritu8 = new Angel("Espiritu8", 50);
        Espiritu espiritu9 = new Demonio("Espiritu9", 50);
        espirituService.crear(espiritu1, puntoEspiritusEsperada);
        espirituService.crear(espiritu2, puntoEspiritusEsperada);
        espirituService.crear(espiritu3, puntoEspiritusEsperada);
        espirituService.crear(espiritu4, puntoEspiritusEsperada);
        espirituService.crear(espiritu5, puntoEspiritusEsperada);
        espirituService.crear(espiritu6, puntoEspiritusEsperada);
        espirituService.crear(espiritu7, puntoEspiritusEsperada);
        espirituService.crear(espiritu8, puntoEspiritusEsperada);
        espirituService.crear(espiritu9, puntoEspiritusEsperada);

        espirituService.conectar(espiritu1.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu2.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu3.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu4.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu7.getId(), mediumEsperado.getId());
        espirituService.conectar(espiritu8.getId(), mediumEsperado.getId());

        espirituService.conectar(espiritu5.getId(), medium1.getId());
        espirituService.conectar(espiritu6.getId(), medium1.getId());

        int cantidadDeDemoniosEsperados = 7;
        int cantidadDeDemoniosLibresEsperados = 1;
        // En el caso de empate sobre el nivel de corrupcion de los MEDIUMS, el reporte va a tener el medium que tenga el nombre ordenado alfabeticamente de la A-Z.
        ReporteSantuarioMasCorrupto reporteEsperado = new ReporteSantuarioMasCorrupto(ubicacionEsperada.getNombre(), mediumEsperado, cantidadDeDemoniosEsperados, cantidadDeDemoniosLibresEsperados);
        ReporteSantuarioMasCorrupto reporteObtenido = reportService.santuarioCorrupto();

        assertEquals(reporteEsperado.getNombreSantuario(), reporteObtenido.getNombreSantuario());
        assertEquals(reporteEsperado.getMedium().getId(), reporteObtenido.getMedium().getId());
        assertEquals(reporteEsperado.getDemoniosTotales(), reporteObtenido.getDemoniosTotales());
        assertEquals(reporteEsperado.getDemoniosLibres(), reporteObtenido.getDemoniosLibres());
    }

    @Test
    public void testObtenerElReporteDelSantuarioMasCorruptoEnElCasoQueNoExistanUnMediumConDemonios() {
        Ubicacion ubicacionEsperada = new Santuario("Bombonera", 20);
        Area area00Esp= new Area(new GeoJsonPoint(0, 0), new GeoJsonPoint(0, 2), new GeoJsonPoint(2, 2), new GeoJsonPoint(2, 0), new GeoJsonPoint(0, 0));
        ubicacionService.crear(ubicacionEsperada, area00Esp);
        var puntoEspiritusEsperada = new GeoJsonPoint(0.5, 0.5);

        Espiritu espiritu1 = new Demonio("Espiritu1", 70);
        Espiritu espiritu2 = new Demonio("Espiritu2", 80);
        Espiritu espiritu3 = new Demonio("Espiritu3", 50);
        Espiritu espiritu4 = new Demonio("Espiritu4", 50);
        Espiritu espiritu5 = new Demonio("Espiritu5", 50);

        espirituService.crear(espiritu1, puntoEspiritusEsperada);
        espirituService.crear(espiritu2, puntoEspiritusEsperada);
        espirituService.crear(espiritu3, puntoEspiritusEsperada);
        espirituService.crear(espiritu4, puntoEspiritusEsperada);
        espirituService.crear(espiritu5, puntoEspiritusEsperada);

        int cantidadDeDemoniosEsperados = 5;
        int cantidadDeDemoniosLibresEsperados = 5;
        Medium mediumEsperado = null;

        ReporteSantuarioMasCorrupto reporteEsperado = new ReporteSantuarioMasCorrupto(ubicacionEsperada.getNombre(), mediumEsperado, cantidadDeDemoniosEsperados, cantidadDeDemoniosLibresEsperados);
        ReporteSantuarioMasCorrupto reporteObtenido = reportService.santuarioCorrupto();

        assertEquals(reporteEsperado.getNombreSantuario(), reporteObtenido.getNombreSantuario());
        assertEquals(reporteEsperado.getMedium(), reporteObtenido.getMedium());
        assertEquals(reporteEsperado.getDemoniosTotales(), reporteObtenido.getDemoniosTotales());
        assertEquals(reporteEsperado.getDemoniosLibres(), reporteObtenido.getDemoniosLibres());
    }

    @AfterEach
    public void cleanUp() {
        databaseCleaner.deleteAll();
        databaseNeoCleaner.deleteAll();
        databaseMongoCleaner.deleteAll();

    }
}
