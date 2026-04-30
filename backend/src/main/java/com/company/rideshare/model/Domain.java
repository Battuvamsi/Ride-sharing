package com.company.rideshare.model;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public class Domain {
    public enum Gender { FEMALE, MALE, NON_BINARY, PREFER_NOT_TO_SAY }
    public enum RideType { OFFICE_COMMUTE, AIRPORT, EVENT, EMERGENCY }
    public enum Preference { ANY, FEMALE_ONLY, MALE_ONLY, SAME_GENDER }

    public record Employee(int id, String name, String email, String phone, String department, Gender gender) {}
    public record RideRequest(int id, int employeeId, String pickup, String dropoff, LocalDateTime departureTime,
                              int seatsNeeded, RideType rideType, Preference genderPreference,
                              boolean smokingAllowed, String notes, LocalDateTime createdAt) {}
    public record Match(RideRequest rideA, RideRequest rideB, int score, List<String> reasons) {}

    public record EmployeeCreate(@NotBlank String name, @Email @NotBlank String email, @NotBlank String phone,
                                 @NotBlank String department, @NotNull Gender gender) {}
    public record RideRequestCreate(int employeeId, @NotBlank String pickup, @NotBlank String dropoff,
                                    @NotNull LocalDateTime departureTime, @Min(1) @Max(6) int seatsNeeded,
                                    @NotNull RideType rideType, @NotNull Preference genderPreference,
                                    boolean smokingAllowed, String notes) {}
}
