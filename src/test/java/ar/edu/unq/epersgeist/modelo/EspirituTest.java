package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.random.NroFijoGetter;
import ar.edu.unq.epersgeist.modelo.random.Random;
import ar.edu.unq.epersgeist.modelo.random.RandomGetter;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;

import static org.junit.jupiter.api.Assertions.*;


public class EspirituTest {

    private Angel jorge;
    private Demonio demonio;
    private Santuario quilmes;
    private Cementerio recoleta;
    private Medium medium;
    private Random random;
    private Demonio demonioDebilitado;
    private Angel angelDebilitado;

    @BeforeEach
    void setUp(){
        quilmes = new Santuario("Quilmes", 10);
        recoleta = new Cementerio("Recoleta", 10);

        jorge = new Angel("Jorge"
                , quilmes
                , 50);

        demonio = new Demonio("Demonio", quilmes, 50);

        medium = new Medium("Melli", 100, quilmes);

        demonioDebilitado = new Demonio("DemonioDebilitado", quilmes, 10);

        angelDebilitado = new Angel("AngelDebilitado", quilmes, 10);

        random = Random.getInstance();

    }

    @Test
    void unEspirituInicialmenteNoTieneMediumYSuNivelDeConexionEs0(){
        var espiritu = new Angel("Espiritu", quilmes, 100);
        assertEquals(quilmes.getNombre(), espiritu.getUbicacion().getNombre());
        assertEquals("Espiritu", espiritu.getNombre());
        assertEquals(0, espiritu.getNivelDeConexion());
        assertEquals(100, espiritu.getEnergia());
        assertEquals(0, espiritu.getExorcismosEvitados());
        assertEquals(0, espiritu.getExorcismosResueltos());
        assertNull(espiritu.getMedium());
    }

    @Test
    void unEspirituNoPuedeSerCreadoConEnergiaNegativa(){
        assertThrows(EnergiaFueraDeLosLimitesException.class, () -> new Angel("Espiritu", quilmes, -1));
    }

    @Test
    void unEspirituNoPuedeSerCreadoConMasDe100DeEnergia(){
        assertThrows(EnergiaFueraDeLosLimitesException.class, () -> new Angel("Espiritu", quilmes, 101));
    }

    @Test
    void cuandoUnEspirituSeConectaAUnMediumSuNivelDeConexionEsEl20PorcientoDelManaDelMedium(){
         var espirituTest = new Angel("Espiritu", quilmes, 50);
         var mediumTest = new Medium("Medium", 100, quilmes);

         var nivelDeConexion = (int) Math.floor(medium.getMana() * 0.2) ;
         espirituTest.conectarseAMedium(mediumTest);

         assertEquals(nivelDeConexion, espirituTest.getNivelDeConexion());
    }

    @Test
    void cuandoUnEspirituSeConectaAUnMediumNoPuedeTenerMasDe100DeConexion(){

        var mediumTest = new Medium("Medium", 800, quilmes);

        jorge.conectarseAMedium(mediumTest);

        assertEquals(100, jorge.getNivelDeConexion());
    }

    @Test
    void unEspirituNoPuedeConectarseAUnMediumSiYaSeEncuentraConectadoAOtro(){
        jorge.conectarseAMedium(medium);
        var conexionInicial = jorge.getNivelDeConexion();
        var otroMedium = new Medium("Otro", 100, quilmes);

        assertEquals(conexionInicial, jorge.getNivelDeConexion());
        assertThrows(EspirituConectadoException.class, () -> jorge.conectarseAMedium(otroMedium));
    }

    @Test
    void unEspirituPierdeEnergia(){
        var energiaInicial = jorge.getEnergia();
        jorge.disminuirEnergia(10);
        assertEquals(energiaInicial - 10, jorge.getEnergia());
    }

    @Test
    void unAngelNoPuedeTenerMenosDe0DeEnergia(){
        jorge.disminuirEnergia(1000);
        jorge.conectarseAMedium(medium);
        assertEquals(0 , jorge.getEnergia());
        assertEquals(medium, jorge.getMedium());
    }

    @Test
    void unEspirituNoPuedePerderEnergiaNegativa(){
        assertThrows(CantidadNegativaException.class, () -> jorge.disminuirEnergia(-1));
    }

    @Test
    void siLaEnergiaDeUnDemonioBajaDe0PorUnAtaqueEsteSeDesconectaDelMedium(){
        var demonio = new Demonio("Demonio", quilmes, 50);
        demonio.conectarseAMedium(medium);
        demonio.disminuirEnergiaPorAtaque(1000);
        assertNull(demonio.getMedium());
        assertEquals(0, demonio.getNivelDeConexion());
    }

    @Test
    void unEspirituAumentaSuEnergia(){
        var energiaInicial = jorge.getEnergia();
        jorge.aumentarEnergia(10);
        assertEquals(energiaInicial + 10, jorge.getEnergia());
    }

    @Test
    void unEspirituNoPuedeTenerMasDe100DeEnergia(){
        jorge.aumentarEnergia(10000);
        assertEquals(100, jorge.getEnergia());
    }

    @Test
    void unEspirituNoPuedeAumentarUnaCantidadDeEnergiaNegativa(){
        assertThrows(CantidadNegativaException.class, () -> jorge.aumentarEnergia(-1));
    }

    @Test
    void unEspirituEsInvocadoEnUnaUbicacion(){
        var palermo = new Santuario("Palermo", 20);
        var ubicacionInicial = jorge.getUbicacion();
        jorge.invocarseEn(palermo);
        assertEquals(palermo, jorge.getUbicacion());
        assertNotEquals(ubicacionInicial, jorge.getUbicacion());
    }

    @Test
    void unDemonioNoPuedeSerInvocadoEnUnSantuario(){
        assertThrows( InvocacionDeDemonioEnSantuarioException.class, () -> demonio.invocarseEn(quilmes));
    }

    @Test
    void unAngelNoPuedeSerInvocadoEnUnCementerio(){
        assertThrows( InvocacionDeAngelEnCementerioException.class, () -> jorge.invocarseEn(recoleta));
    }

    @Test
    void cuandoUnAngelAtacaAUnDemonioConExitoYElDemonioPierdeTodaSuEnergiaEsteSeDesconectaDeSuMedium(){
        var mediumTest = new Medium("Mamado", 300, quilmes);

        jorge.conectarseAMedium(mediumTest);
        var jorgeEnergiaInicial = jorge.getEnergia();

        var demonio = new Demonio("Demonio", quilmes, 8);
        demonio.conectarseAMedium(medium);

        random.setStrategy(new NroFijoGetter(9));

        jorge.atacarA(demonio);

        assertEquals(jorgeEnergiaInicial - 10, jorge.getEnergia());
        assertEquals(0, demonio.getEnergia());
        assertEquals(0, demonio.getNivelDeConexion());
        assertNull(demonio.getMedium());
    }

    @Test
    void unAngelAtacaConExitoAUnDemonioPeroNoDrenaTodaSuEnergia(){
        var mediumTest = new Medium("Mamado", 300, quilmes);

        jorge.conectarseAMedium(mediumTest);
        var jorgeEnergiaInicial = jorge.getEnergia();

        var demonio = new Demonio("Demonio", quilmes, 100);
        var demonioEnergiaInicial = demonio.getEnergia();
        var danio = jorge.getNivelDeConexion() / 2;

        demonio.conectarseAMedium(medium);

        random.setStrategy(new NroFijoGetter(9));

        jorge.atacarA(demonio);

        assertEquals(jorgeEnergiaInicial - 10, jorge.getEnergia());
        assertEquals(demonioEnergiaInicial - danio, demonio.getEnergia());
        assertNotNull(demonio.getMedium());
    }

    @Test
    void unAngelAtacaAUnDemonioSinExito(){

        var mediumTest = new Medium("Mamado", 300, quilmes);

        jorge.conectarseAMedium(mediumTest);
        var jorgeEnergiaInicial = jorge.getEnergia();

        var demonio = new Demonio("Demonio", quilmes, 100);

        demonio.conectarseAMedium(medium);
        var demonioEnergiaEsperada = demonio.getEnergia();

        random.setStrategy(new NroFijoGetter(1));

        jorge.atacarA(demonio);

        assertEquals(demonioEnergiaEsperada, demonio.getEnergia());
        assertEquals(jorgeEnergiaInicial - 10, jorge.getEnergia());
    }

    @Test
    void unAngelNoAtacaSiNoTieneEnergiaSuficiente(){
        var angelDebil = new Angel("AngelDebil", quilmes, 5);
        var demonio = new Demonio("Demonio", quilmes, 50);

        angelDebil.atacarA(demonio);

        assertEquals(5, angelDebil.getEnergia());
        assertEquals(50, demonio.getEnergia());

    }
    @Test
    void testEspirituConNombreNullException() {
        assertThrows(EntidadModeloConNombreNullException.class, () -> new Angel("", quilmes, 50));
    }

    @Test
    void unEspirituDominaAOtro() {
        jorge.dominar(demonioDebilitado);

        assertTrue(demonioDebilitado.estaDominado());
        assertEquals(jorge, demonioDebilitado.getDominador());
        assertTrue(jorge.getEspiritusDominados().stream().anyMatch(e -> e.equals(demonioDebilitado)));
    }

    @Test
    void unEspirituNoPuedeDominarAOtroSiYaEstaDominado() {
        jorge.dominar(demonioDebilitado);
        assertThrows(EspirituDominadoException.class, () -> demonio.dominar(demonioDebilitado));
    }

    @Test
    void unEspirituNoPuedeDominarAOtroSiYaEstaConectadoAUnMedium() {
        demonioDebilitado.conectarseAMedium(medium);
        assertThrows(EspirituConectadoException.class, () -> jorge.dominar(demonioDebilitado));
    }

    @Test
    void unEspirituNoPuedeDominarAOtroSiNoEstaDebilitado() {
        assertThrows(EspirituNoDebilitadoException.class, () -> jorge.dominar(demonio));
        assertTrue(demonio.getEnergia() >= 50);
    }

    @Test
    void unEspirituNoPuedeDominarASuDominador(){
        demonioDebilitado.dominar(angelDebilitado);
        assertThrows(EspirituNoPuedeDominarASuDominadorException.class, () -> angelDebilitado.dominar(demonioDebilitado));
    }

    @Test
    void unEspirituPuedeDominarAunqueSeEncuentreDominado(){
        jorge.dominar(demonioDebilitado);
        demonioDebilitado.dominar(angelDebilitado);

        assertEquals(jorge, demonioDebilitado.getDominador());
        assertEquals(demonioDebilitado, angelDebilitado.getDominador());
    }


    @AfterEach
    void tearDown(){
        random.setStrategy(new RandomGetter());
    }

}
