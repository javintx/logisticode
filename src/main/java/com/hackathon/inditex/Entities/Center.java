package com.hackathon.inditex.Entities;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "centers", uniqueConstraints = @UniqueConstraint(columnNames = {"latitude", "longitude"}))
public class Center {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String capacity;

  private String status;

  private Integer currentLoad;

  private Integer maxCapacity;

  @Embedded
  private Coordinates coordinates;
}
