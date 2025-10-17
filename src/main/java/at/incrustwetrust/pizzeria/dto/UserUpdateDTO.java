package at.incrustwetrust.pizzeria.dto;

import at.incrustwetrust.pizzeria.entity.CountryCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @Size(min = 5, message = "Username must be at least 5 characters")
    private String username;

    @Email
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String zipcode;
    private String salutation;
    private String salutationDetail;
    private String country;

    // Optional: password, only if provided
    @Size(min = 12)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getSalutationDetail() {
        return salutationDetail;
    }

    public void setSalutationDetail(String salutationDetail) {
        this.salutationDetail = salutationDetail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(CountryCode country) {
        this.country = country.toString();
    }
}
