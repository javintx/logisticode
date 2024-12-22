package com.hackathon.inditex.Controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Services.CentersService;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public ResponseEntity<Map<String, String>> createNewLogisticsCenter(@RequestBody Center center) {
    centersService.createNewLogisticsCenter(center);
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Logistics center created successfully."));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<Center>> retrieveAllLogisticsCenters() {
    return ResponseEntity.ok(centersService.retrieveAllLogisticsCenters());
  }

  @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> updateDetailsOfAnExistingLogisticsCenter(@PathVariable Long id, @RequestBody Map<String, String> requestUpdate) {
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, requestUpdate);
    return ResponseEntity.ok(Map.of("message", "Logistics center updated successfully."));
  }

  @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> deleteALogisticsCenter(@PathVariable Long id) {
    centersService.deleteALogisticsCenter(id);
    return ResponseEntity.ok(Map.of("message", "Logistics center deleted successfully."));
  }
}
