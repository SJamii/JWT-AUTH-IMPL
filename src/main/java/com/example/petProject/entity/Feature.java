package com.example.petProject.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Table(name = "feature")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long featureId;

    @Column(name = "feature_name", columnDefinition = "TEXT")
    private String featureName;

   /* @Column(name = "url", columnDefinition = "TEXT")
    private String url;*/

    @Column(columnDefinition = "TEXT")
    private String privilegeType;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;

    @Column(name = "is_internal", nullable = false, columnDefinition = "boolean default false")
    private boolean isInternal;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToMany
    @JoinTable(
            name = "feature_dependency",
            joinColumns = @JoinColumn(name = "dependent_feature_id"),
            inverseJoinColumns = @JoinColumn(name = "depends_on_feature_id")
    )
    private List<Feature> dependentFeatures = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return isActive == feature.isActive && Objects.equals(featureId, feature.featureId) && Objects.equals(featureName,
                feature.featureName) && Objects.equals(privilegeType, feature.privilegeType) && Objects.equals(module, feature.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureId, featureName, privilegeType, isActive, module);
    }

    @Override
    public String toString() {
        return "Feature{" +
                "featureId=" + featureId +
                ", featureName='" + featureName + '\'' +
                ", privilegeType='" + privilegeType + '\'' +
                ", isActive=" + isActive +
                ", module=" + module +
                '}';
    }
}
