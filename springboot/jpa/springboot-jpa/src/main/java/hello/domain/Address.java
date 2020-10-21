package hello.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
