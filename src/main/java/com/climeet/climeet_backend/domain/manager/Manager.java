package com.climeet.climeet_backend.domain.manager;

import com.climeet.climeet_backend.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Manager extends User {

    @NotNull
    private String LoginId;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String email;

    @NotNull
    private String businessRegistrationImageUrl;

    private Boolean isRegistered = false;
}