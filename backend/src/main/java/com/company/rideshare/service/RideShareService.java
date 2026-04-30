package com.company.rideshare.service;

import com.company.rideshare.model.Domain.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RideShareService {
    private final List<Employee> employees = new ArrayList<>();
    private final List<RideRequest> rides = new ArrayList<>();

    public Employee createEmployee(EmployeeCreate payload) {
        if (employees.stream().anyMatch(e -> e.email().equalsIgnoreCase(payload.email()))) {
            throw new IllegalArgumentException("Employee email already exists");
        }
        Employee employee = new Employee(employees.size() + 1, payload.name(), payload.email(), payload.phone(), payload.department(), payload.gender());
        employees.add(employee);
        return employee;
    }

    public List<Employee> listEmployees() { return employees; }

    public RideRequest createRide(RideRequestCreate payload) {
        employeeById(payload.employeeId());
        RideRequest ride = new RideRequest(rides.size() + 1, payload.employeeId(), payload.pickup(), payload.dropoff(), payload.departureTime(),
                payload.seatsNeeded(), payload.rideType(), payload.genderPreference(), payload.smokingAllowed(), payload.notes(), LocalDateTime.now());
        rides.add(ride);
        return ride;
    }

    public List<RideRequest> listRides() { return rides; }

    public List<Match> matches(int minScore) {
        List<Match> results = new ArrayList<>();
        for (int i = 0; i < rides.size(); i++) for (int j = i + 1; j < rides.size(); j++) {
            Match m = compatibility(rides.get(i), rides.get(j));
            if (m != null && m.score() >= minScore) results.add(m);
        }
        results.sort((a, b) -> Integer.compare(b.score(), a.score()));
        return results;
    }

    public Map<String, Object> analytics() {
        Map<String, Integer> byDepartment = new HashMap<>();
        for (RideRequest ride : rides) {
            Employee e = employeeById(ride.employeeId());
            byDepartment.put(e.department(), byDepartment.getOrDefault(e.department(), 0) + 1);
        }
        Map<String, Integer> prefs = new HashMap<>();
        for (Preference p : Preference.values()) prefs.put(p.name(), 0);
        for (RideRequest ride : rides) prefs.put(ride.genderPreference().name(), prefs.get(ride.genderPreference().name()) + 1);

        return Map.of("totalEmployees", employees.size(), "totalRideRequests", rides.size(), "requestsByDepartment", byDepartment, "genderPreferenceSplit", prefs);
    }

    private Employee employeeById(int id) {
        return employees.stream().filter(e -> e.id() == id).findFirst().orElseThrow(() -> new NoSuchElementException("Employee not found"));
    }

    private Match compatibility(RideRequest a, RideRequest b) {
        Employee ea = employeeById(a.employeeId());
        Employee eb = employeeById(b.employeeId());
        if (!prefCompatible(a.genderPreference(), ea.gender(), eb.gender())) return null;
        if (!prefCompatible(b.genderPreference(), eb.gender(), ea.gender())) return null;

        int score = 25;
        List<String> reasons = new ArrayList<>(List.of("Gender preferences are mutually compatible"));
        long mins = Math.abs(Duration.between(a.departureTime(), b.departureTime()).toMinutes());
        if (mins > 45) return null;
        if (mins <= 10) { score += 25; reasons.add("Departure times are very close"); } else { score += 12; reasons.add("Departure times are reasonably close"); }
        if (a.pickup().equalsIgnoreCase(b.pickup())) { score += 20; reasons.add("Same pickup location"); }
        if (a.dropoff().equalsIgnoreCase(b.dropoff())) { score += 20; reasons.add("Same dropoff location"); }
        if (a.rideType() == b.rideType()) { score += 10; reasons.add("Same ride type"); }
        if (a.smokingAllowed() == b.smokingAllowed()) { score += 5; reasons.add("Smoking preference matches"); }
        return new Match(a, b, score, reasons);
    }

    private boolean prefCompatible(Preference p, Gender requester, Gender other) {
        return switch (p) {
            case ANY -> true;
            case FEMALE_ONLY -> other == Gender.FEMALE;
            case MALE_ONLY -> other == Gender.MALE;
            case SAME_GENDER -> requester == other;
        };
    }
}
