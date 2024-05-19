package com.example.petProject.repository;

import com.example.petProject.entity.Feature;
import com.example.petProject.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    @Query("SELECT f FROM Feature f WHERE f.featureId IN :ids")
    List<Feature> findByIds(List<Long> ids);

    @Query("SELECT f FROM Feature f WHERE f.featureId IN :id")
    Feature findById(Long id);

    @Query("SELECT f FROM Feature f WHERE f.featureName IN :name")
    Feature findByFeatureName(String name);

    @Query("SELECT f.featureName FROM Feature f")
    List<String> getAllFeature();

}
