package com.hackathon.inditex.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Services.CentersService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CentersController.class)
public class CentersControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CentersService centersService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void createNewLogisticsCenter_ReturnsCreated() throws Exception {
    Center center = new Center();
    Mockito.doNothing().when(centersService).createNewLogisticsCenter(Mockito.any(Center.class));

    mockMvc.perform(post("/api/centers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(center)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Logistics center created successfully."));
  }

  @Test
  public void retrieveAllLogisticsCenters_ReturnsOk() throws Exception {
    List<Center> centers = new ArrayList<>();
    centers.add(new Center());
    Mockito.when(centersService.retrieveAllLogisticsCenters()).thenReturn(centers);

    mockMvc.perform(get("/api/centers")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  public void updateDetailsOfAnExistingLogisticsCenter_ReturnsOk() throws Exception {
    Long id = 1L;
    Center center = new Center();
    Mockito.doNothing().when(centersService)
        .updateDetailsOfAnExistingLogisticsCenter(Mockito.eq(id), Mockito.any(Center.class));

    mockMvc.perform(patch("/api/centers/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(center)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Logistics center updated successfully."));
  }

  @Test
  public void deleteALogisticsCenter_ReturnsOk() throws Exception {
    Long id = 1L;
    Mockito.doNothing().when(centersService).deleteALogisticsCenter(id);

    mockMvc.perform(delete("/api/centers/{id}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Logistics center deleted successfully."));
  }

}
