package at.incrustwetrust.pizzeria.dto;

import at.incrustwetrust.pizzeria.entity.Order;

import java.time.Instant;
import java.util.List;

public class UserResponseDTO {

    private String userId;
    private String username;
    private String salutation;
    private String salutationDetail;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String city;
    private String country;
    private boolean isActive;
    private boolean isAdmin;
    private Instant createdAt;

    private String createdById;

    private Instant lastUpdatedAt;

    private String lastUpdatedById;

    private List<Order> orders;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getLastUpdatedById() {
        return lastUpdatedById;
    }

    public void setLastUpdatedById(String lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orderIds) {
        this.orders = orders;
    }
}
