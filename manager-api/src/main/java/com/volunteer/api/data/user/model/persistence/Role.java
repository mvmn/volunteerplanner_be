package com.volunteer.api.data.user.model.persistence;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_role")
public class Role implements Serializable {

  private static final long serialVersionUID = 8854898984097615669L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_generator")
  @SequenceGenerator(name = "user_role_generator", sequenceName = "user_role_id_seq", allocationSize = 1)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

}
