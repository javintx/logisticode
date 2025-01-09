package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Services.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

  private final HealthCheckService healthCheckService;

  @Autowired
  public HealthCheckController(HealthCheckService healthCheckService) {
    this.healthCheckService = healthCheckService;
  }

  @GetMapping
  public String healthCheck() {
    return healthCheckService.checkHealth();
  }
}