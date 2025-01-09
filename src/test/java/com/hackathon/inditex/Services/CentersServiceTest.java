package com.hackathon.inditex.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Exceptions.CenterNotFoundException;
import com.hackathon.inditex.Exceptions.LoadCapacityExceededException;
import com.hackathon.inditex.Exceptions.LocationAlreadyInUseException;
import com.hackathon.inditex.Repositories.CentersRepository;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CentersServiceTest {

  @Mock
  private CentersRepository centersRepository;
  @InjectMocks
  private CentersService centersService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateNewLogisticsCenter_LoadCapacityExceededException() {
    var center = new Center();
    center.setCurrentLoad(110);
    center.setMaxCapacity(100);

    assertThrows(LoadCapacityExceededException.class, () -> centersService.createNewLogisticsCenter(center));
  }

  @Test
  void testCreateNewLogisticsCenter_LocationAlreadyInUseException() {
    var center = new Center();
    center.setCurrentLoad(90);
    center.setMaxCapacity(100);
    when(centersRepository.findCenterByCoordinates(center.getCoordinates())).thenReturn(Optional.of(center));

    assertThrows(LocationAlreadyInUseException.class, () -> centersService.createNewLogisticsCenter(center));
  }

  @Test
  void testCreateNewLogisticsCenter_Success() {
    var center = new Center();
    center.setCurrentLoad(90);
    center.setMaxCapacity(100);
    when(centersRepository.findCenterByCoordinates(center.getCoordinates())).thenReturn(Optional.empty());

    centersService.createNewLogisticsCenter(center);
  }

  @Test
  void testRetrieveAllLogisticsCenters() {
    var centers = Arrays.asList(new Center(), new Center());
    when(centersRepository.findAll()).thenReturn(centers);

    var result = centersService.retrieveAllLogisticsCenters();

    assertEquals(centers.size(), result.size());
    assertTrue(result.containsAll(centers));
  }

  @Test
  void testUpdateDetailsOfAnExistingLogisticsCenter_Success() {
    var id = 1L;
    var center = new Center();
    center.setName("New Center");
    center.setCapacity("XL");
    center.setStatus("Active");
    center.setCurrentLoad(50);
    center.setMaxCapacity(200);
    center.setCoordinates(new Coordinates(10.0, 10.0));

    when(centersRepository.findById(id)).thenReturn(Optional.of(center));

    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("name", "new name"));
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("capacity", "S"));
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("status", "AVAILABLE"));
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("currentLoad", "5"));
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("maxCapacity", "15"));
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("latitude", "5.5"));
    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("longitude", "5.5"));

    assertEquals("new name", center.getName());
    assertEquals("S", center.getCapacity());
    assertEquals("AVAILABLE", center.getStatus());
    assertEquals(5, center.getCurrentLoad());
    assertEquals(15, center.getMaxCapacity());
    assertEquals(5.5, center.getCoordinates().getLatitude());
    assertEquals(5.5, center.getCoordinates().getLongitude());
  }

  @Test
  void testUpdateDetailsOfAnExistingLogisticsCenter_CenterNotFoundException() {
    var id = 1L;
    when(centersRepository.findById(id)).thenReturn(Optional.empty());
    assertThrows(CenterNotFoundException.class,
        () -> centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("something", "potato")));
  }

  @Test
  void testDeleteALogisticsCenter_Success() {
    var id = 1L;
    doNothing().when(centersRepository).deleteById(id);

    centersService.deleteALogisticsCenter(id);

    verify(centersRepository, times(1)).deleteById(id);
  }
}