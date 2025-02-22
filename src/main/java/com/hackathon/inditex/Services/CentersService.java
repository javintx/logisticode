package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Exceptions.CenterNotFoundException;
import com.hackathon.inditex.Exceptions.LoadCapacityExceededException;
import com.hackathon.inditex.Exceptions.LocationAlreadyInUseException;
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
    if (isLoadExceedingCapacity(center)) {
      throw new LoadCapacityExceededException();
    }
  }

  private static boolean isLoadExceedingCapacity(Center center) {
    return center.getCurrentLoad() > center.getMaxCapacity();
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
    var center = getCenterById(id);
    updateCenterFields(center, updateValue);
    centersRepository.save(center);
  }

  private Center getCenterById(Long id) {
    return centersRepository.findById(id).orElseThrow(CenterNotFoundException::new);
  }

  private void updateCenterFields(Center center, Map<String, String> updateValue) {
    updateValue.forEach((key, value) -> updateCenterField(center, key, value));
  }

  private void updateCenterField(Center center, String key, String value) {
    switch (key) {
      case "name" -> center.setName(value);
      case "capacity" -> center.setCapacity(value);
      case "status" -> center.setStatus(value);
      case "currentLoad" -> updateCurrentLoad(center, value);
      case "maxCapacity" -> updateMaxCapacity(center, value);
      case "latitude" -> updateLatitude(center, value);
      case "longitude" -> updateLongitude(center, value);
    }
  }

  private void updateCurrentLoad(Center center, String value) {
    center.setCurrentLoad(Integer.valueOf(value));
    verifyLoadVsCapacityOf(center);
  }

  private void updateMaxCapacity(Center center, String value) {
    center.setMaxCapacity(Integer.valueOf(value));
    verifyLoadVsCapacityOf(center);
  }

  private void updateLatitude(Center center, String value) {
    var coordinate = center.getCoordinates();
    coordinate.setLatitude(Double.valueOf(value));
    center.setCoordinates(coordinate);
  }

  private void updateLongitude(Center center, String value) {
    var coordinate = center.getCoordinates();
    coordinate.setLongitude(Double.valueOf(value));
    center.setCoordinates(coordinate);
  }

  public void deleteALogisticsCenter(Long id) {
    centersRepository.deleteById(id);
  }

}
