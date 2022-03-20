package com.volunteer.api.data.model.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "region")
public class Region {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_generator")
  @SequenceGenerator(name = "region_generator", sequenceName = "region_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

}
