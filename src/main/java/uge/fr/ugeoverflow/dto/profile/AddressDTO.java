package uge.fr.ugeoverflow.dto.profile;

import uge.fr.ugeoverflow.model.Address;

public class AddressDTO {

    private String street;
    private String city;
    private String country;
    private String zipCode;

    public AddressDTO() {
    }

    public AddressDTO(String street, String city, String country, String zipCode) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
    }

    public AddressDTO(Address address) {
        if (address != null) {
            this.street = address.getStreet() == null ? "" : address.getStreet();
            this.city = address.getCity() == null ? "" : address.getCity();
            this.country = address.getCountry() == null ? "" : address.getCountry();
            this.zipCode = address.getZipCode() == null ? "" : address.getZipCode();
        }
    }

    public static AddressDTO fromAddress(Address address) {
        var addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return this.street + ", " + this.city + ", " + this.zipCode + ", " + this.country + ".";
    }
}
