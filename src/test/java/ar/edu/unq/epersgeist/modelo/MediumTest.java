package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.random.NroFijoGetter;
import ar.edu.unq.epersgeist.modelo.random.Random;
import ar.edu.unq.epersgeist.modelo.random.RandomGetter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediumTest {

    private Medium medium;
    private Medium medium2;
    private Medium mediumManoso;
    private Espiritu joseEndemoniado;
    private Espiritu joseRealmenteEndemoniado;
    private Espiritu joseAngelical;
    private Espiritu joseAngelical2;
    private Espiritu joseEnElBosque;
    private Ubicacion casa;
    private Ubicacion bosque;
    private Ubicacion santuario;



    @BeforeEach
    void setUp(){


        casa = new Cementerio("casa", 20);
        bosque = new Santuario("Bosque", 20);
        santuario = new Santuario("Bosque", 20);
        medium = new Medium("augusto",100, casa);
        medium2 = new Medium("nahue",100, casa);
        mediumManoso = new Medium("mediumManoso", 300, casa);
        joseAngelical = new Angel("joseAngelical", casa,100);
        joseAngelical2 = new Angel("joseAngelical2", casa, 50);
        joseEndemoniado = new Demonio("joseEndemoniado", casa,20);
        joseRealmenteEndemoniado = new Demonio("joseRealmenteEndemoniado", casa,28);
        joseEnElBosque = new Angel("joseEnElBosque", santuario,100);
        Random random = Random.getInstance();
        random.setStrategy(new NroFijoGetter(7));

    }

    @Test
    public void getterYSetterDeMedium(){
        medium.setNombre("tomi");
        assert(medium.getNombre().equals("tomi"));
    }


    @Test
    public void unMediumSeInicializaSinEspiritus(){
        assert(medium.getEspiritus().isEmpty());
    }


    @Test
    public void unMediumPuedeConectarseAUnEspiritu(){
        medium.crearConexion(joseAngelical);
        assert(medium.getEspiritus().size() == 1);
    }


    @Test
    public void unMediumPuedeObtenerSusEspiritusFiltradosPorAngeles(){
        medium.crearConexion(joseAngelical);
        assert(medium.getAngeles().size() == 1);
    }

    @Test
    public void unMediumPuedeObtenerSusEspiritusFiltradosPorDemonios(){
        medium.crearConexion(joseEndemoniado);
        assert(medium.getDemonios().size() == 1);
    }

    @Test
    public void unMediumNoPuedeExorcizarSiNoTieneAlMenosUnEspirituAngel(){
        assertThrows(ExorcistaSinAngelesException.class, () -> medium.exorcizar(medium));
    }

    @Test
    public void unMediumNoPuedeExorcizarAUnMediumSinDemonios() {
        medium.crearConexion(joseAngelical);
        var energiaInicial = joseAngelical.getEnergia();
        medium.exorcizar(medium2);
        assertEquals(energiaInicial, joseAngelical.getEnergia());
    }

    @Test
    public void UnMediumPuedeExorcizarAOtroMediumTotalmente(){

        mediumManoso.crearConexion(joseAngelical);
        mediumManoso.crearConexion(joseAngelical2);
        medium2.crearConexion(joseEndemoniado);
        medium2.crearConexion(joseRealmenteEndemoniado);

        var exorcismosEvitadosDemonio1 = joseEndemoniado.getExorcismosEvitados();
        var exorcismosEvitadosDemonio2 = joseRealmenteEndemoniado.getExorcismosEvitados();

        var exorcismosResueltosAngel1 = joseAngelical.getExorcismosResueltos();
        var exorcismosResueltosAngel2 = joseAngelical2.getExorcismosResueltos();

        mediumManoso.exorcizar(medium2);

        assertEquals(0,medium2.getEspiritus().size());
        assertEquals(90,joseAngelical.getEnergia());
        assertEquals(40,joseAngelical2.getEnergia());
        assertEquals(exorcismosResueltosAngel1 + 1,joseAngelical.getExorcismosResueltos());
        assertEquals(exorcismosResueltosAngel2 + 1,joseAngelical2.getExorcismosResueltos());
        assertEquals(exorcismosEvitadosDemonio1, joseEndemoniado.getExorcismosEvitados());
        assertEquals(exorcismosEvitadosDemonio2, joseRealmenteEndemoniado.getExorcismosEvitados());
    }

    @Test
    public void unMediumPuedeExorcizarAOtroMediumPeroParcialmente(){ // es decir sin eliminar todos sus demonios.
        mediumManoso.crearConexion(joseAngelical);
        medium2.crearConexion(joseEndemoniado);
        medium2.crearConexion(joseRealmenteEndemoniado);

        var exorcismosEvitadosDemonio1 = joseEndemoniado.getExorcismosEvitados();
        var exorcismosEvitadosDemonio2 = joseRealmenteEndemoniado.getExorcismosEvitados();

        var exorcismosResueltosAngel1 = joseAngelical.getExorcismosResueltos();


        mediumManoso.exorcizar(medium2);

        // Aclaracion: joseEndemoniado si es exorcizado y por eso se elimina de la lista de espiritus de medium2.
        assertEquals(1,medium2.getEspiritus().size());
        assertEquals(90,joseAngelical.getEnergia());
        assertEquals(exorcismosResueltosAngel1,joseAngelical.getExorcismosResueltos());
        assertEquals(exorcismosEvitadosDemonio2 + 1, joseRealmenteEndemoniado.getExorcismosEvitados());
        assertEquals(exorcismosEvitadosDemonio1, joseEndemoniado.getExorcismosEvitados());
        assertNull(joseEndemoniado.getMedium());
    }

    @Test
    public void unMediumPuedeIntentarExorcizarAOtroYUnicamenteReducirEnergiaDeSuDemonio() {
        mediumManoso.crearConexion(joseAngelical);


        Espiritu joseEndemoniadoFuerte = new Demonio("joseEndemoniadoFuerte", casa, 100);
        medium2.crearConexion(joseEndemoniadoFuerte);

        mediumManoso.exorcizar(medium2);

        assertEquals(70,joseEndemoniadoFuerte.getEnergia());
        assertEquals(1,medium2.getEspiritus().size());
    }

    @Test
    public void cuandoUnAngelExorcizaAlUltimoDemonioLosAngelesSiguientesNoAtacan(){
        mediumManoso.crearConexion(joseAngelical);
        mediumManoso.crearConexion(joseAngelical2);
        medium2.crearConexion(joseEndemoniado);

        mediumManoso.exorcizar(medium2);

        assertEquals(0,medium2.getEspiritus().size());
        assertEquals(90,joseAngelical.getEnergia());
        assertEquals(50,joseAngelical2.getEnergia());
    }

    @Test
    public void unMediumDescansaEnUnCementerio() {

        medium.crearConexion(joseAngelical);
        medium.crearConexion(joseEndemoniado);

        var energiaEsperadaMedium = medium.getMana() + (casa.getEnergia() / 2);
        var energiaEsperadaDemonio = joseEndemoniado.getEnergia() + casa.getEnergia();
        var energiaEsperadaAngel = joseAngelical.getEnergia();

        medium.descansar();


        assertEquals(energiaEsperadaMedium, medium.getMana());
        assertEquals(energiaEsperadaAngel, joseAngelical.getEnergia());
        assertEquals(energiaEsperadaDemonio, joseEndemoniado.getEnergia());
    }

    @Test
    public void unMediumDescansaEnUnSantuario() {


        var angelSanto = new Angel("angelSanto", santuario, 20);
        var deminioSanto = new Demonio("demonioSanto", santuario, 20);
        var mediumSanto = new Medium("mediumSanto", 20, santuario);

        mediumSanto.crearConexion(angelSanto);
        mediumSanto.crearConexion(deminioSanto);

        var energiaEsperadaMedium = (int) (mediumSanto.getMana() + (santuario.getEnergia() * 1.5));
        var energiaEsperadaDemonio = deminioSanto.getEnergia();
        var energiaEsperadaAngel = angelSanto.getEnergia() + santuario.getEnergia();

        mediumSanto.descansar();


        assertEquals(energiaEsperadaMedium, mediumSanto.getMana());
        assertEquals(energiaEsperadaAngel, angelSanto.getEnergia());
        assertEquals(energiaEsperadaDemonio, deminioSanto.getEnergia());
    }


    @Test
    public void unMediumPuedeInvocarUnEspirituSiTieneComoMinimo10DeManaYElEspirituSeEncuentraLibreEnSuMismaUbicacion() {

        medium.invocar(joseEndemoniado);

        assertEquals(90, medium.getMana());
        assertEquals("casa",joseEndemoniado.getUbicacion().getNombre());
    }

    @Test
    public void unMediumNoPuedeInvocarUnEspirituSiNoTieneComoMinimo10DeMana() {


        medium.setMana(5);
        medium.invocar(joseEnElBosque);

        assertEquals("Bosque",joseEnElBosque.getUbicacion().getNombre());
    }

    @Test
    public void unMediumNoPuedeInvocarUnEspirituSiElEspirituNoEstaLibre(){
        medium2.crearConexion(joseEndemoniado);

        assertThrows(EspirituConectadoException.class, () -> medium.invocar(joseEndemoniado));
    }

    @Test
    public void unMediumNoPuedeCrearConexionUnEspirituSiElEspirituNoEstaEnLaMismaUbicacion(){
        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> medium.crearConexion(joseEnElBosque));
    }


    @Test
    public void unMediumPuedeDesconectarseAUnEspirituUnaVezConectado() {
        medium.crearConexion(joseAngelical);

        medium.desconectarseDeEspiritu(joseAngelical);

        assertEquals(0, medium.getEspiritus().size());
    }



    @Test
    public void unMediumNoPuedeSerInicializadoConManaNegativo() {
        assertThrows(ManaNegativoException.class, () -> new Medium("tomi", -1, casa));
    }

    @Test
    void testMediumConNombreNullException() {
        assertThrows(EntidadModeloConNombreNullException.class, () -> new Medium("", 100, casa));

    }

    @Test
    void testMediumSeMueveAUnaUbicacionJuntoConSusEspiritus(){
        medium.crearConexion(joseAngelical);

        assertNotEquals(bosque.getNombre(), medium.getUbicacion().getNombre());
        assertNotEquals(bosque.getNombre(), joseAngelical.getUbicacion().getNombre());

        medium.mover(bosque);

        assertEquals(bosque.getNombre(), medium.getUbicacion().getNombre());
        assertEquals(bosque.getNombre(), joseAngelical.getUbicacion().getNombre());
    }

    @Test
    void siUnMediumSeMueveAUnSantuarioSusDemoniosPierden10DeEnergia(){
        medium.crearConexion(joseAngelical);
        medium.crearConexion(joseEndemoniado);

        var energiaEsperadaDemonio = joseEndemoniado.getEnergia() - 10;
        var energiaEsperadaAngel = joseAngelical.getEnergia();

        medium.mover(bosque);

        assertEquals(energiaEsperadaDemonio, joseEndemoniado.getEnergia());
        assertEquals(energiaEsperadaAngel, joseAngelical.getEnergia());
    }

    @Test
    void siUnMediumSeMueveAUnCementerioSusAngelesPierden5DeEnergia(){

        var cementerio = new Cementerio("cementerio", 20);

        medium.crearConexion(joseAngelical);
        medium.crearConexion(joseEndemoniado);



        var energiaEsperadaDemonio = joseEndemoniado.getEnergia();
        var energiaEsperadaAngel = joseAngelical.getEnergia() - 5;

        medium.mover(cementerio);

        assertEquals(energiaEsperadaDemonio, joseEndemoniado.getEnergia());
        assertEquals(energiaEsperadaAngel, joseAngelical.getEnergia());
    }

    @Test
    void siUnMediumSeQuiereMoverAUnaUbicacionEnLaQueEstaNoPasaNada(){
        var cementerio = new Cementerio("cementerio", 20);
        var mediumCementerio = new Medium("medium", 100, cementerio);
        var angel = new Angel("angel", cementerio, 100);
        var demonio = new Demonio("demonio", cementerio, 100);

        mediumCementerio.crearConexion(angel);
        mediumCementerio.crearConexion(demonio);


        var energiaEsperadaDemonio = demonio.getEnergia();
        var energiaEsperadaAngel = angel.getEnergia();

        mediumCementerio.mover(cementerio);

        assertEquals(energiaEsperadaDemonio, demonio.getEnergia());
        assertEquals(energiaEsperadaAngel, angel.getEnergia());
    }

    @Test
    void siUnMediumSeMueveAUnSantuarioYSuDemonioTieneMenosDe10DeEnergiaSoloQuedaEnCero(){

        var demonio = new Demonio("joseEndemoniado", casa,6);

        medium.crearConexion(joseAngelical);
        medium.crearConexion(demonio);

        var energiaEsperadaAngel = joseAngelical.getEnergia();

        medium.mover(bosque);

        assertEquals(0, demonio.getEnergia());
        assertEquals(energiaEsperadaAngel, joseAngelical.getEnergia());
    }

    @Test
    void siUnMediumSeMueveAUnCementerioYSuAngelTieneMenosDe5DeEnergiaSoloQuedaEnCero(){

        var cementerio = new Cementerio("cementerio", 20);
        var angel = new Angel("joseEndemoniado", casa,3);

        medium.crearConexion(angel);
        medium.crearConexion(joseEndemoniado);


        var energiaEsperadaDemonio = joseEndemoniado.getEnergia();

        medium.mover(cementerio);

        assertEquals(energiaEsperadaDemonio, joseEndemoniado.getEnergia());
        assertEquals(0, angel.getEnergia());
    }


    @AfterEach
    void tearDown(){
        Random.getInstance().setStrategy(new RandomGetter());
    }


}