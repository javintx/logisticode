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

  private static void verifyLoadVsCapacityOf(Center center) {
    if (center.getCurrentLoad() > center.getMaxCapacity()) {
      throw new LoadCapacityExceededException();
    }
  }

  public void createNewLogisticsCenter(Center center) {
    verifyLoadVsCapacityOf(center);
    verifyExistingCoordinatesOf(center);
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

  public void updateDetailsOfAnExistingLogisticsCenter(Long id, Center center) {
    verifyExistenceOfCenterBy(id);
    verifyLoadVsCapacityOf(center);
    verifyMoreThanOneExistingCoordinatesFor(id, center.getCoordinates());
    center.setId(id);
    centersRepository.save(center);
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

}
