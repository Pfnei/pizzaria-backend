package at.incrustwetrust.pizzeria.dto;

import at.incrustwetrust.pizzeria.entity.Salutation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserCreateDTO {

    @NotBlank
    @Size(min = 5,message = "Username must be at least 5 characters long")
    private String username;

    @NotBlank
    @Size(min = 12, message = "Password must be at least 12 characters long")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*[@$!%*?&].*", message = "Password must contain at least one special character")
    private String password;

    private String salutation;

    @Size(max = 30, message = "Salutation can be at most 30 characters long")
    private String salutationDetail;

    private String firstName;
    private String lastName;

    @Email
    @NotBlank
    private String email;

    private String phoneNumber;
    private String address;

    @Size(min = 2, max = 10)
    private String zipcode;

    private String city;
    private String country;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(Salutation salutation) {
        this.salutation = salutation.toString();
    }

    public String getSalutationDetail() {
        return salutationDetail;
    }

    public void setSalutationDetail(String salutationDetail) {
        this.salutationDetail = salutationDetail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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
}
