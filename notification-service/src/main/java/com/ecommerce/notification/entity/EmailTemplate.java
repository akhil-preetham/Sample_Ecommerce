package com.ecommerce.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import com.ecommerce.common.entity.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "email_templates", indexes = {
    @Index(name = "idx_template_name", columnList = "template_name", unique = true)
})
public class EmailTemplate extends BaseEntity {

    @Id
    private String id;

    @Column(name = "template_name", nullable = false, unique = true)
    private String templateName;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "body", nullable = false, columnDefinition = "LONGTEXT")
    private String body;

    @Column(name = "variables", columnDefinition = "TEXT")
    private String variables;
}