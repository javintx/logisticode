package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Repositories.CentersRepository;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CentersService {

  private final CentersRepository centersRepository;

  @Autowired
  public CentersService(CentersRepository centersRepository) {
    this.centersRepository = centersRepository;
  }

  private static void verifyLoadVsCapacityOf(Center center) {
    if (center.getCurrentLoad() > center.getMaxCapacity()) {
      throw new LoadCapacityExceededException();
    }
  }

  public void createNewLogisticsCenter(Center center) {
    verifyExistingCoordinatesOf(center);
    verifyLoadVsCapacityOf(center);
    centersRepository.save(center);
  }

  private void verifyExistingCoordinatesOf(Center center) {
    if (centersRepository.findCenterByCoordinates(center.getCoordinates()).isPresent()) {
      throw new LocationAlreadyInUseException();
    }
  }

  public Collection<Center> retrieveAllLogisticsCenters() {
    return centersRepository.findAll();
  }

  public void updateDetailsOfAnExistingLogisticsCenter(Long id, Map<String, String> updateValue) {
    verifyExistenceOfCenterBy(id);

    var center = centersRepository.getReferenceById(id);

    updateValue.forEach((key, value) -> {
      switch (key) {
        case "name" -> center.setName(value);
        case "capacity" -> center.setCapacity(value);
        case "status" -> center.setStatus(value);
        case "currentLoad" -> {
          center.setCurrentLoad(Integer.valueOf(value));
          verifyLoadVsCapacityOf(center);
        }
        case "maxCapacity" -> {
          center.setMaxCapacity(Integer.valueOf(value));
          verifyLoadVsCapacityOf(center);
        }
        case "latitude" -> {
          var coordinate = center.getCoordinates();
          coordinate.setLatitude(Double.valueOf(value));
          center.setCoordinates(coordinate);
        }
        case "longitude" -> {
          var coordinate = center.getCoordinates();
          coordinate.setLongitude(Double.valueOf(value));
          center.setCoordinates(coordinate);
        }
      }
    });

    centersRepository.save(center);
  }

  public void deleteALogisticsCenter(Long id) {
    centersRepository.deleteById(id);
  }

  private void verifyExistenceOfCenterBy(Long id) {
    if (!centersRepository.existsById(id)) {
      throw new CenterNotFoundException();
    }
  }

  public static class LocationAlreadyInUseException extends RuntimeException {

    public LocationAlreadyInUseException() {
      super("There is already a logistics center in that position.");
    }
  }

  public static class LoadCapacityExceededException extends RuntimeException {

    public LoadCapacityExceededException() {
      super("Current load cannot exceed max capacity.");
    }
  }

  public static class CenterNotFoundException extends RuntimeException {

    public CenterNotFoundException() {
      super("Center not found.");
    }
  }

}
