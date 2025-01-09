package com.hackathon.inditex.Services;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

  public String checkHealth() {
    return "API is working";
  }
}
