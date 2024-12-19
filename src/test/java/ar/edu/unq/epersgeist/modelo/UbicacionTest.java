package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exceptions.EnergiaFueraDeLosLimitesException;
import ar.edu.unq.epersgeist.modelo.exceptions.EntidadModeloConNombreNullException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UbicacionTest {

    private Ubicacion ubicacionPrueba;

    @BeforeEach
    void setUp() {
        ubicacionPrueba = new Santuario("Casa", 20);
    }

    @Test
    void testGetterYSetterDeUbicacion() {
        ubicacionPrueba.setNombre("Trabajo");
        assert (ubicacionPrueba.getNombre().equals("Trabajo"));
    }

    @Test
    void testUbicacionConNombreNullException() {
        assertThrows(EntidadModeloConNombreNullException.class, () -> new Santuario("", 20));
    }

    @Test
    void unaUbicacionNOPuedeTenerEnergiaNegativa(){
        assertThrows(EnergiaFueraDeLosLimitesException.class, () -> new Santuario("Casa", -1));
    }

    @Test
    void unaUbicacionNOPuedeTenerMasDe100DeEnergia(){
        assertThrows(EnergiaFueraDeLosLimitesException.class, () -> new Santuario("Casa", 101));
    }


}