package com.volunteer.api.data.model.persistence;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "verification_code",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "code_type"},
        name = "uq_verification_code_user_type")})
public class VerificationCode implements Serializable {

  public static enum VerificationCodeType {
    PHONE, EMAIL;
  }

  private static final long serialVersionUID = 9128533637930197553L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_code_generator")
  @SequenceGenerator(name = "verification_code_generator",
      sequenceName = "verification_code_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(nullable = false, name = "created_at")
  private Long createdAt;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private VPUser user;

  @Column(nullable = false)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "code_type", nullable = false)
  private VerificationCodeType type;
}
