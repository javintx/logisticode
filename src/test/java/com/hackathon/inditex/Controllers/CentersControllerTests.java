package com.hackathon.inditex.Controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.inditex.Controllers.CentersController.CenterCreationRequest;
import com.hackathon.inditex.Controllers.CentersController.CenterUpdateRequest;
import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
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
    center.setName("Center A");
    center.setCapacity("MS");
    center.setStatus("AVAILABLE");
    center.setMaxCapacity(5);
    center.setCurrentLoad(4);
    center.setCoordinates(new Coordinates(10.0, 10.0));
    Mockito.doNothing().when(centersService).createNewLogisticsCenter(Mockito.any(Center.class));

    var centerRequest = new CenterCreationRequest();
    centerRequest.setName("Center A");
    centerRequest.setCapacity("MS");
    centerRequest.setStatus("AVAILABLE");
    centerRequest.setCurrentLoad(4);
    centerRequest.setMaxCapacity(5);
    centerRequest.setCoordinates(new Coordinates(10.0, 10.0));

    mockMvc.perform(post("/api/centers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(centerRequest)))
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
    var center = new Center();
    center.setStatus("AVAILABLE");
    Mockito.doNothing().when(centersService)
        .updateDetailsOfAnExistingLogisticsCenter(Mockito.eq(id), Mockito.any(Center.class));

    var centerUpdateRequest = new CenterUpdateRequest();
    centerUpdateRequest.setStatus("AVAILABLE");

    mockMvc.perform(patch("/api/centers/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(centerUpdateRequest)))
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
