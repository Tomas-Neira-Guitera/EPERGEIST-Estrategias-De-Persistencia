package ar.edu.unq.epersgeist.helper;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearAreaDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearPuntoDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Component
public class MockMVCUbicacionController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Throwable {
        try {
            return mockMvc.perform(requestBuilder);
        } catch (ServletException e) {
            throw e.getCause();
        }
    }

    private UbicacionDTO guardarUbicacion(Ubicacion ubicacion, Area area, HttpStatus expectedStatus) throws Throwable {
        GeoJsonPoint primerPuntoGeoJson = new GeoJsonPoint(area.getPoligono().getPoints().get(0).getX(), area.getPoligono().getPoints().get(0).getY());
        GeoJsonPoint segundoPuntoGeoJson = new GeoJsonPoint(area.getPoligono().getPoints().get(1).getX(), area.getPoligono().getPoints().get(1).getY());
        GeoJsonPoint tercerPuntoGeoJson = new GeoJsonPoint(area.getPoligono().getPoints().get(2).getX(), area.getPoligono().getPoints().get(2).getY());
        GeoJsonPoint cuartoPuntoGeoJson = new GeoJsonPoint(area.getPoligono().getPoints().get(3).getX(), area.getPoligono().getPoints().get(3).getY());
        GeoJsonPoint puntoUnificadorGeoJson = new GeoJsonPoint(area.getPoligono().getPoints().get(4).getX(), area.getPoligono().getPoints().get(4).getY());

        CrearPuntoDTO primerPunto = CrearPuntoDTO.desdeModelo(primerPuntoGeoJson);
        CrearPuntoDTO segundoPunto = CrearPuntoDTO.desdeModelo(segundoPuntoGeoJson);
        CrearPuntoDTO tercerPunto = CrearPuntoDTO.desdeModelo(tercerPuntoGeoJson);
        CrearPuntoDTO cuartoPunto = CrearPuntoDTO.desdeModelo(cuartoPuntoGeoJson);
        CrearPuntoDTO puntoUnificador = CrearPuntoDTO.desdeModelo(puntoUnificadorGeoJson);

        List<CrearPuntoDTO> areaPointsDTO = area.getPoligono().getPoints()
                .stream()
                .map(punto -> CrearPuntoDTO.desdeModelo(new GeoJsonPoint(punto.getX(), punto.getY())))
                .toList();

        List<CrearPuntoDTO> others = areaPointsDTO.subList(4, areaPointsDTO.size());
        List<CrearPuntoDTO> points = List.of(primerPunto, segundoPunto, tercerPunto, cuartoPunto);
        List<CrearPuntoDTO> areaPointsUnida = Stream.concat(points.stream(), others.stream()).toList();
        CrearAreaDTO areaDTO = CrearAreaDTO.desdeModelo(areaPointsUnida);



        CrearUbicacionDTO body = CrearUbicacionDTO.desdeModelo(ubicacion, areaDTO);
        String json = objectMapper.writeValueAsString(body);

        String ubicacionDTOString =
                performRequest(MockMvcRequestBuilders.post("/ubicacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                        .andReturn().getResponse().getContentAsString();

        UbicacionDTO ubicacionDTO = objectMapper.readValue(ubicacionDTOString, UbicacionDTO.class);

        ubicacion.setId(ubicacionDTO.getId());
        area.setIdUbicacion(ubicacionDTO.getId());

        return UbicacionDTO.desdeModelo(ubicacion);
    }

    public UbicacionDTO guardarUbicacion(Ubicacion ubicacion, Area area) throws Throwable {
        return guardarUbicacion(ubicacion, area, HttpStatus.OK);
    }

    public UbicacionDTO recuperarUbicacion(Long ubicacionId) throws Throwable {
        String json = performRequest(MockMvcRequestBuilders.get("/ubicacion/" + ubicacionId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(json, UbicacionDTO.class);
        return dto;
    }

    public Collection<UbicacionDTO> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/ubicacion"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<UbicacionDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UbicacionDTO.class)
        );

        return dtos;
    }

    public void eliminarUbicacion(Long ubicacionId) throws Throwable {
        performRequest(MockMvcRequestBuilders.delete("/ubicacion/" + ubicacionId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
