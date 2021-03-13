package com.bgpark.springbootfirstclasscollection.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Embeddable
@NoArgsConstructor
public class Stations {

    @OneToMany(mappedBy = "line", cascade = PERSIST)
    private List<Station> stations = new ArrayList<>();

    public Station getLastStation() {
        validSize();
        hasValidName();
        return stations.get(stations.size() - 1);
    }

    private void validSize() {
        if(stations.size() < 3) {
            throw new IllegalArgumentException("역의 길이는 3보다 작을 수 없습니다");
        }
    }

    private void hasValidName() {
        if(!hasValidStation()) {
            throw new IllegalArgumentException("강남역을 반드시 포함해야 합니다");
        }
    }

    private boolean hasValidStation() {
        return stations.stream()
                .anyMatch(station -> station.getName().equals("강남역"));
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }


}
