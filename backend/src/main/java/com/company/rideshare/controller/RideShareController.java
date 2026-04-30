package com.company.rideshare.controller;

import com.company.rideshare.model.Domain.*;
import com.company.rideshare.service.RideShareService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RideShareController {
    private final RideShareService service;

    public RideShareController(RideShareService service) {
        this.service = service;
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody EmployeeCreate payload) { return service.createEmployee(payload); }

    @GetMapping("/employees")
    public List<Employee> listEmployees() { return service.listEmployees(); }

    @PostMapping("/rides")
    public RideRequest createRide(@Valid @RequestBody RideRequestCreate payload) { return service.createRide(payload); }

    @GetMapping("/rides")
    public List<RideRequest> listRides() { return service.listRides(); }

    @GetMapping("/matches")
    public List<Match> matches(@RequestParam(defaultValue = "40") int minScore) { return service.matches(minScore); }

    @GetMapping("/analytics")
    public Map<String, Object> analytics() { return service.analytics(); }
}
