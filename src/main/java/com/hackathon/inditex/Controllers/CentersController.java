package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Services.CentersService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping
  public ResponseEntity<?> createNewLogisticsCenter(@RequestBody CenterCreationRequest centerCreationRequest) {
    centerCreationRequest.verify();
    centersService.createNewLogisticsCenter(centerCreationRequest.toCenter());
    return ResponseEntity.status(201).body(new MessageResponse("Logistics center created successfully."));
  }

  @GetMapping
  public ResponseEntity<?> retrieveAllLogisticsCenters() {
    return ResponseEntity.ok(centersService.retrieveAllLogisticsCenters());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateDetailsOfAnExistingLogisticsCenter(@PathVariable Long id,
      @RequestBody CenterUpdateRequest centerUpdateRequest) {
    centerUpdateRequest.verify();
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, centerUpdateRequest.toCenter());
    return ResponseEntity.ok(new MessageResponse("Logistics center updated successfully."));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteALogisticsCenter(@PathVariable Long id) {
    centersService.deleteALogisticsCenter(id);
    return ResponseEntity.ok(new MessageResponse("Logistics center deleted successfully."));
  }

  @Setter
  @Getter
  @NoArgsConstructor
  public static final class CenterCreationRequest {

    private String name;
    private String capacity;
    private String status;
    private Integer currentLoad;
    private Integer maxCapacity;
    private Coordinates coordinates;

    void verify() {
      if (name == null
          || capacity == null
          || status == null
          || currentLoad == null
          || maxCapacity == null
          || coordinates == null) {
        throw new BadCenterException();
      }
    }

    Center toCenter() {
      var center = new Center();
      center.setName(name);
      center.setCapacity(capacity);
      center.setStatus(status);
      center.setCurrentLoad(currentLoad);
      center.setMaxCapacity(maxCapacity);
      center.setCoordinates(coordinates);
      return center;
    }


  }

  public static final class CenterUpdateRequest {

    private String name;
    private String capacity;
    private String status;
    private Integer currentLoad;
    private Integer maxCapacity;
    private Coordinates coordinates;

    public CenterUpdateRequest() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getCapacity() {
      return capacity;
    }

    public void setCapacity(String capacity) {
      this.capacity = capacity;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public Integer getCurrentLoad() {
      return currentLoad;
    }

    public void setCurrentLoad(Integer currentLoad) {
      this.currentLoad = currentLoad;
    }

    public Integer getMaxCapacity() {
      return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
      this.maxCapacity = maxCapacity;
    }

    public Coordinates getCoordinates() {
      return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
      this.coordinates = coordinates;
    }

    void verify() {
      int nonNullFields = 0;
      if (name != null) {
        nonNullFields++;
      }
      if (capacity != null) {
        nonNullFields++;
      }
      if (status != null) {
        nonNullFields++;
      }
      if (currentLoad != null) {
        nonNullFields++;
      }
      if (maxCapacity != null) {
        nonNullFields++;
      }
      if (coordinates != null) {
        nonNullFields++;
      }

      if (nonNullFields != 1) {
        throw new BadCenterException();
      }
    }

    Center toCenter() {
      var center = new Center();
      center.setName(name);
      center.setCapacity(capacity);
      center.setStatus(status);
      center.setCurrentLoad(currentLoad);
      center.setMaxCapacity(maxCapacity);
      center.setCoordinates(coordinates);
      return center;
    }
  }

  public static class BadCenterException extends RuntimeException {

    public BadCenterException() {
      super("Bad Center.");
    }
  }

  public record MessageResponse(String message) {

  }
}
