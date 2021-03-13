package com.bgpark.springbootfirstclasscollection.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private Stations stations;

    public Station getLastStation() {
        return stations.getLastStation();
    }

    public Line(Stations stations) {
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }
}
