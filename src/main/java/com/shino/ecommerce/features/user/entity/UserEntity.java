// package com.shino.ecommerce.features.user.entity;

// import java.time.LocalDate;
// import java.time.LocalDateTime;

// import com.shino.ecommerce.features.auth.entity.AuthEntity;
// import com.shino.ecommerce.features.user.enums.GenderEnum;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "user")
// public class UserEntity {
//     @Id
//     @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
//     private Long id;
//     @OneToOne
//     @JoinColumn(name = "auth_id", referencedColumnName = "id")
//     private AuthEntity auth;
//     private String name;
//     private GenderEnum gender;
//     private LocalDate dateOfBirth;
//     private String citizenCard;
//     private String phoneNumber;
//     private String address;
//     private String city;
//     private String state;
//     private String country;
//     private String zipCode;
//     private LocalDateTime createdAt;
//     private LocalDateTime updatedAt;

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public AuthEntity getAuth() {
//         return auth;
//     }

//     public void setAuth(AuthEntity auth) {
//         this.auth = auth;
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public GenderEnum getGender() {
//         return gender;
//     }

//     public void setGender(GenderEnum gender) {
//         this.gender = gender;
//     }

//     public LocalDate getDateOfBirth() {
//         return dateOfBirth;
//     }

//     public void setDateOfBirth(LocalDate dateOfBirth) {
//         this.dateOfBirth = dateOfBirth;
//     }

//     public String getCitizenCard() {
//         return citizenCard;
//     }

//     public void setCitizenCard(String citizenCard) {
//         this.citizenCard = citizenCard;
//     }

//     public String getPhoneNumber() {
//         return phoneNumber;
//     }

//     public void setPhoneNumber(String phoneNumber) {
//         this.phoneNumber = phoneNumber;
//     }

//     public String getAddress() {
//         return address;
//     }

//     public void setAddress(String address) {
//         this.address = address;
//     }

//     public String getCity() {
//         return city;
//     }

//     public void setCity(String city) {
//         this.city = city;
//     }

//     public String getState() {
//         return state;
//     }

//     public void setState(String state) {
//         this.state = state;
//     }

//     public String getCountry() {
//         return country;
//     }

//     public void setCountry(String country) {
//         this.country = country;
//     }

//     public String getZipCode() {
//         return zipCode;
//     }

//     public void setZipCode(String zipCode) {
//         this.zipCode = zipCode;
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public LocalDateTime getUpdatedAt() {
//         return updatedAt;
//     }

//     public void setUpdatedAt(LocalDateTime updatedAt) {
//         this.updatedAt = updatedAt;
//     }

// }
