package projekt.biblioteka.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Miasto nie może być puste")
    private String city;
    @NotBlank(message = "Ulica nie może być pusta")
    private String street;
    @NotBlank(message = "Numer budynku nie może być pusty")
    private String buildingNumber;

    private String apartmentNumber;
    @NotBlank(message = "Kod pocztowy nie może być pusty")
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}", message = "Kod pocztowy powinien być w formacie 11-111")
    private String zipCode;
    @OneToOne(mappedBy = "address")
    private User user;

}
