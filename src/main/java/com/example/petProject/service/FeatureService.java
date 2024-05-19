package com.example.petProject.service;

import com.example.petProject.entity.Feature;
import com.example.petProject.enums.FeatureEnum;

public interface FeatureService {
    void disableFeature(Long id);
    void deleteFeature(Long id);
    void addDependency(FeatureEnum[] dependentFeatures, Feature dependesOnFeature);
}
