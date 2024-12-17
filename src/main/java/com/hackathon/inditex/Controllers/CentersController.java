package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Services.CentersService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

  @PostMapping
  public ResponseEntity<MessageResponse> createNewLogisticsCenter(@RequestBody @Validated Center center) {
    centersService.createNewLogisticsCenter(center);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new MessageResponse("Logistics center created successfully."));
  }

  @GetMapping
  public ResponseEntity<Collection<Center>> retrieveAllLogisticsCenters() {
    return ResponseEntity.ok(centersService.retrieveAllLogisticsCenters());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<MessageResponse> updateDetailsOfAnExistingLogisticsCenter(@PathVariable Long id,
      @RequestBody @Validated Center center) {
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, center);
    return ResponseEntity.ok(new MessageResponse("Logistics center updated successfully."));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteALogisticsCenter(@PathVariable Long id) {
    centersService.deleteALogisticsCenter(id);
    return ResponseEntity.ok(new MessageResponse("Logistics center deleted successfully."));
  }

  public record MessageResponse(String message) {

  }
}
