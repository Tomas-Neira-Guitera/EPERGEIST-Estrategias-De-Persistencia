package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ritual.Ritual;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RitualServiceTest {

    @Autowired
    private RitualServiceImpl ritualService;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private final List<Object> listaTearDown = new ArrayList<>();

    @Test
    public void testCrearYRecuperarRitual() {
        ritualService.crear("Ritual 1", "guido", "epers, acid", "1");

        Ritual ritual = ritualService.recuperar("1");
        listaTearDown.add(ritual);

        assertEquals("Ritual 1", ritual.getNombre());
    }

    @Test
    public void testRecuperarRitualConIdInexistnte(){
        assertThrows(NoExisteLaEntidadException.class, () -> {
            ritualService.recuperar("1");
        });
    }

    @Test
    public void  testRecuperarTodosLosRituales(){
        ritualService.crear("Ritual 1", "guido", "epers", "1");
        ritualService.crear("Ritual 2", "nahue", "acid", "2");

        List<Ritual> rituales = ritualService.recuperarTodos();

        listaTearDown.addAll(rituales);

        assertEquals(2, rituales.size());
    }

    @Test
    public void testRecuperarTodosLosRitualesYNoHayNinguno(){
        List<Ritual> rituales = ritualService.recuperarTodos();
        assertEquals(0, rituales.size());
    }


    @Test
    public void testEliminarRitual(){
        ritualService.crear("Ritual 1", "guido","palabra1, palabra2", "1");

        ritualService.eliminar("1");

        assertThrows(NoExisteLaEntidadException.class, () -> {
            ritualService.recuperar("1");
        });
    }

    @Test
    public void testEliminarRitualInexistente(){
        assertThrows(NoExisteLaEntidadException.class, () -> {
            ritualService.eliminar("1");
        });
    }

    @Test
    public void testActualizarRitual(){
        ritualService.crear("Ritual 1", "nahue", "palabra1, palabra2", "1");

        ritualService.actualizar("1", "Ritual 2");

        Ritual ritualActualizado = ritualService.recuperar("1");

        listaTearDown.add(ritualActualizado);

        assertEquals("Ritual 2", ritualActualizado.getNombre());
    }

    @Test
    public void testActualizarRitualInexistente(){
        assertThrows(NoExisteLaEntidadException.class, () -> {
            ritualService.actualizar("1", "Ritual 2");
        });
    }

    @Test
    public void testObtenerElRitualMasPoderoso(){
        ritualService.crear("Ritual 1", "guido", "guido", "1");
        ritualService.crear("Ritual 2", "nahue", "epers, acid", "2");

        Ritual ritualDeGuido = ritualService.recuperar("1");
        Ritual ritualDeNahue = ritualService.recuperar("2");
        Ritual ritualMasPoderoso = ritualService.obtenerElRitualMasPoderoso();

        listaTearDown.add(ritualDeGuido);
        listaTearDown.add(ritualDeNahue);

        assertEquals("Ritual 1", ritualMasPoderoso.getNombre());
    }

    @AfterEach
    public void tearDown() {
        if (!listaTearDown.isEmpty()) {
            dynamoDBMapper.batchDelete(listaTearDown);
        }
    }


}
