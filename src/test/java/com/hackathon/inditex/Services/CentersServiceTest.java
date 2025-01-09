package com.hackathon.inditex.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import com.hackathon.inditex.Repositories.CentersRepository;
import com.hackathon.inditex.Exceptions.CenterNotFoundException;
import com.hackathon.inditex.Exceptions.LoadCapacityExceededException;
import com.hackathon.inditex.Exceptions.LocationAlreadyInUseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    Center center = new Center();
    center.setCurrentLoad(110);
    center.setMaxCapacity(100);

    assertThrows(LoadCapacityExceededException.class, () -> centersService.createNewLogisticsCenter(center));
  }

  @Test
  void testCreateNewLogisticsCenter_LocationAlreadyInUseException() {
    Center center = new Center();
    center.setCurrentLoad(90);
    center.setMaxCapacity(100);
    when(centersRepository.findCenterByCoordinates(center.getCoordinates())).thenReturn(Optional.of(center));

    assertThrows(LocationAlreadyInUseException.class, () -> centersService.createNewLogisticsCenter(center));
  }

  @Test
  void testCreateNewLogisticsCenter_Success() {
    Center center = new Center();
    center.setCurrentLoad(90);
    center.setMaxCapacity(100);
    when(centersRepository.findCenterByCoordinates(center.getCoordinates())).thenReturn(Optional.empty());

    centersService.createNewLogisticsCenter(center);
  }

  @Test
  void testRetrieveAllLogisticsCenters() {
    List<Center> centers = Arrays.asList(new Center(), new Center());
    when(centersRepository.findAll()).thenReturn(centers);

    Collection<Center> result = centersService.retrieveAllLogisticsCenters();

    assertEquals(centers.size(), result.size());
    assertTrue(result.containsAll(centers));
  }

  @Test
  void testUpdateDetailsOfAnExistingLogisticsCenter_Success() {
    var id = 1L;
    var center = new Center();
    center.setName("New Center");
    center.setCapacity("200");
    center.setStatus("Active");
    center.setCurrentLoad(50);
    center.setMaxCapacity(200);
    center.setCoordinates(new Coordinates(10.0, 10.0));

    when(centersRepository.getReferenceById(id)).thenReturn(center);

    centersService.updateDetailsOfAnExistingLogisticsCenter(id, Map.of("status", "AVAILABLE"));

    assertEquals("AVAILABLE", center.getStatus());
  }

  @Test
  void testDeleteALogisticsCenter_Success() {
    Long id = 1L;
    when(centersRepository.existsById(id)).thenReturn(true);

    centersService.deleteALogisticsCenter(id);

    verify(centersRepository, times(1)).deleteById(id);
  }

  @Test
  void testDeleteALogisticsCenter_CenterNotFoundException() {
    Long id = 1L;
    when(centersRepository.existsById(id)).thenReturn(false);

    assertThrows(CenterNotFoundException.class, () -> centersService.deleteALogisticsCenter(id));
  }
}