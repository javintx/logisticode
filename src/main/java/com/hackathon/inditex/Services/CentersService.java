package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Repositories.CentersRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CentersService {

  private final CentersRepository centersRepository;

  @Autowired
  public CentersService(CentersRepository centersRepository) {
    this.centersRepository = centersRepository;
  }

  private static void verifyCurrentLoadVsMaxCapacityOf(Integer currentLoad, Integer maxCapacity) {
    if (currentLoad > maxCapacity) {
      throw new LoadCapacityExceededException();
    }
  }

  public void createNewLogisticsCenter(Center center) {
    verifyCapacity(center.getCapacity());
    verifyCurrentLoadVsMaxCapacityOf(center.getCurrentLoad(), center.getMaxCapacity());
    verifyExistingCoordinatesOf(center);
    centersRepository.save(center);
  }

  private void verifyCapacity(String capacity) {
    if (!capacity.matches("^(?!.*(.).*\1)[BMS]{1,3}$")) {
      throw new UnknownCapacitySizeException();
    }
  }

  private void verifyExistingCoordinatesOf(Center center) {
    if (centersRepository.findCenterByCoordinates(center.getCoordinates()).isPresent()) {
      throw new LocationAlreadyInUseException();
    }
  }

  public Collection<Center> retrieveAllLogisticsCenters() {
    return centersRepository.findAll();
  }

  public void updateDetailsOfAnExistingLogisticsCenter(Long id, Center center) {
    var savedCenter = centersRepository.findById(id).orElseThrow(CenterNotFoundException::new);

    if (center.getName() != null) {
      savedCenter.setName(center.getName());
    }

    if (center.getCapacity() != null) {
      verifyCapacity(center.getCapacity());
      savedCenter.setCapacity(center.getCapacity());
    }

    if (center.getStatus() != null) {
      savedCenter.setStatus(center.getStatus());
    }

    if (center.getCurrentLoad() != null) {
      verifyCurrentLoadVsMaxCapacityOf(center.getCurrentLoad(), savedCenter.getMaxCapacity());
      savedCenter.setCurrentLoad(center.getCurrentLoad());
    }

    if (center.getMaxCapacity() != null) {
      verifyCurrentLoadVsMaxCapacityOf(savedCenter.getCurrentLoad(), center.getMaxCapacity());
      savedCenter.setMaxCapacity(center.getMaxCapacity());
    }

    if (center.getCoordinates() != null) {
      verifyMoreThanOneExistingCoordinatesFor(id, center.getCoordinates());
      savedCenter.setCoordinates(center.getCoordinates());
    }

    centersRepository.save(savedCenter);
  }

  private void verifyMoreThanOneExistingCoordinatesFor(Long id, Coordinates coordinates) {
    centersRepository.findCenterByCoordinates(coordinates)
        .filter(existingCenter -> !existingCenter.getId().equals(id))
        .ifPresent(existingCenter -> {
          throw new LocationAlreadyInUseException();
        });
  }

  public void deleteALogisticsCenter(Long id) {
    verifyExistenceOfCenterBy(id);
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

  public static class UnknownCapacitySizeException extends RuntimeException {

    public UnknownCapacitySizeException() {
      super("Unknown capacity size.");
    }
  }

}
