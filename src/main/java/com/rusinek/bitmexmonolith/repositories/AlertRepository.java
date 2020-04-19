package com.rusinek.bitmexmonolith.repositories;

import com.rusinek.bitmexmonolith.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Adrian Rusinek on 19.03.2020
 **/
public interface AlertRepository extends JpaRepository<Alert,Long> {

    Iterable<Alert> findAllByAlertOwner(String alertOwner);
}
