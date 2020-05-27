package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
public interface AlertRepository extends JpaRepository<Alert,Long> {

    List<Alert> findAllByAlertOwner(String alertOwner);

    Optional<Alert> findByIdAndAlertOwner(Long id, String owner);
}
