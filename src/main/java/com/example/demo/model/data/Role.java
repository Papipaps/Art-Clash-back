package com.example.demo.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document("roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
  @Id
  private String id;

  private String name;

  public Role(String name) {
    this.name = name;
  }


}