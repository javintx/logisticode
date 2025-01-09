package com.hackathon.inditex.Controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.hackathon.inditex.DTO.CenterResponse;
import com.hackathon.inditex.DTO.MessageResponse;
import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Services.CentersService;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/centers")
public class CentersController {

  private final CentersService centersService;

  @Autowired
  public CentersController(CentersService centersService) {
    this.centersService = centersService;
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse createNewLogisticsCenter(@RequestBody Center center) {
    centersService.createNewLogisticsCenter(center);
    return new MessageResponse("Logistics center created successfully.");
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public Collection<CenterResponse> retrieveAllLogisticsCenters() {
    return centersService.retrieveAllLogisticsCenters().stream().map(CenterResponse::of).toList();
  }

  @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public MessageResponse updateDetailsOfAnExistingLogisticsCenter(@PathVariable Long id,
      @RequestBody Map<String, String> requestUpdate) {
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, requestUpdate);
    return new MessageResponse("Logistics center updated successfully.");
  }

  @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  public MessageResponse deleteALogisticsCenter(@PathVariable Long id) {
    centersService.deleteALogisticsCenter(id);
    return new MessageResponse("Logistics center deleted successfully.");
  }

}
