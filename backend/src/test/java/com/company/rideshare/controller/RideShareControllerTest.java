package com.company.rideshare.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RideShareControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void canCreateEmployeeAndRideAndGetMatches() throws Exception {
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content("""
            {"name":"Ava","email":"ava@corp.com","phone":"111","department":"HR","gender":"FEMALE"}
            """)).andExpect(status().isOk());

        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content("""
            {"name":"Mia","email":"mia@corp.com","phone":"222","department":"HR","gender":"FEMALE"}
            """)).andExpect(status().isOk());

        mvc.perform(post("/api/rides").contentType(MediaType.APPLICATION_JSON).content("""
            {"employeeId":1,"pickup":"A","dropoff":"B","departureTime":"2026-05-01T09:00:00","seatsNeeded":1,"rideType":"OFFICE_COMMUTE","genderPreference":"FEMALE_ONLY","smokingAllowed":false}
            """)).andExpect(status().isOk());

        mvc.perform(post("/api/rides").contentType(MediaType.APPLICATION_JSON).content("""
            {"employeeId":2,"pickup":"A","dropoff":"B","departureTime":"2026-05-01T09:05:00","seatsNeeded":1,"rideType":"OFFICE_COMMUTE","genderPreference":"ANY","smokingAllowed":false}
            """)).andExpect(status().isOk());

        mvc.perform(get("/api/matches")).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("score")));
    }
}
