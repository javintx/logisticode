package com.hackathon.inditex.Repositories;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Coordinates;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentersRepository extends JpaRepository<Center, Long> {

  Optional<Center> findCenterByCoordinates(Coordinates coordinates);

}
