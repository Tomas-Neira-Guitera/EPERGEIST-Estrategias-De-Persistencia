package ar.edu.unq.epersgeist.helper;

import ar.edu.unq.epersgeist.controller.dto.medium.CrearMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearPuntoDTO;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
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

@Component
public class MockMVCMediumController {

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

    private MediumDTO guardarMedium(Medium medium, GeoJsonPoint punto, HttpStatus expectedStatus) throws Throwable {
        CrearPuntoDTO puntoDTO = CrearPuntoDTO.desdeModelo(punto);
        CrearMediumDTO body = CrearMediumDTO.desdeModelo(medium, puntoDTO);
        String json = objectMapper.writeValueAsString(body);

        String mediumDTOString = (
                performRequest(MockMvcRequestBuilders.post("/medium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                        .andReturn().getResponse().getContentAsString()
        );

        MediumDTO mediumDTO = objectMapper.readValue(mediumDTOString, MediumDTO.class);

        medium.setId(mediumDTO.getId());

        return MediumDTO.desdeModelo(medium);
    }

    public MediumDTO guardarMedium(Medium medium, GeoJsonPoint punto) throws Throwable {
        return guardarMedium(medium, punto, HttpStatus.OK);
    }

    public MediumDTO recuperarMedium(Long mediumId) throws Throwable {
        String json = performRequest(MockMvcRequestBuilders.get("/medium/" + mediumId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(json, MediumDTO.class);
        return dto;
    }

    public Collection<MediumDTO> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/medium"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<MediumDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MediumDTO.class)
        );

        return dtos;
    }

    public void eliminarMedium(Long mediumId) throws Throwable {
        performRequest(MockMvcRequestBuilders.delete("/medium/" + mediumId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}