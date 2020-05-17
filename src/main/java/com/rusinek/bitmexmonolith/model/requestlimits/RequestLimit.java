package com.rusinek.bitmexmonolith.model.requestlimits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

/**
 * Created by Adrian Rusinek on 08.05.2020
 **/
@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
class RequestLimit {

    private long blockadeActivatedAt;
    private long apiReadyToUse;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

}
