package com.example.petProject.service.impl;

import com.example.petProject.entity.Feature;
import com.example.petProject.entity.Role;
import com.example.petProject.enums.FeatureEnum;
import com.example.petProject.exception.ResourceCannotBeDeletedException;
import com.example.petProject.repository.FeatureRepository;
import com.example.petProject.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FeatureRepository featureRepository;

    @Override
    @Transactional
    public void deleteFeature(Long id) {
        Feature feature = featureRepository.findById(id);
//        Feature tempFeature = feature.getDependentFeatures().get(0);
//        feature.getDependentFeatures().remove(tempFeature);
        feature.getDependentFeatures().clear();

        featureRepository.saveAndFlush(feature);
       /* Feature feature = featureRepository.findById(id);
        if (feature != null) {
            Feature subFeature = feature.getSubFeature();
            if (subFeature != null) {
                subFeature.getDependsOnFeatures().remove(feature);
                feature.setSubFeature(null);
                featureRepository.delete(feature);
                //Edit Role
                List<Role> roles = feature.getRoles();
                for(Role role : roles) {
                    role.getFeatures().remove(feature);
                }


                if(subFeature.getDependsOnFeatures().isEmpty()) {
                    featureRepository.delete(subFeature);
                }
            } else if(feature.getDependsOnFeatures().size() == 0) {
                List<Role> roles = feature.getRoles();
                for(Role role : roles) {
                    role.getFeatures().remove(feature);
                }
                featureRepository.delete(feature);
            } else {
                throw new ResourceCannotBeDeletedException("Feature with this id no: " + id + " cannot be deleted");
            }
        }*/


        /*Feature feature = featureRepository.findById(id);
        if (feature != null) {
            feature.getDependentFeature()
                subFeature.getDependsOnFeatures().remove(feature);
                feature.setSubFeature(null);
                featureRepository.delete(feature);
                //Edit Role
                List<Role> roles = feature.getRoles();
                for(Role role : roles) {
                    role.getFeatures().remove(feature);
                }


                if(subFeature.getDependsOnFeatures().isEmpty()) {
                    featureRepository.delete(subFeature);
                }

        }*/

    }

    @Override
    @Transactional
    public void addDependency(FeatureEnum[] dependentFeatures, Feature dependesOnFeature) {
        for(FeatureEnum dependentFeature : dependentFeatures) {
            Optional<Feature> optionalFeature = Optional.ofNullable(featureRepository.findByFeatureName(dependentFeature.getName()));
            if (optionalFeature.isPresent()) {
                Feature feature = optionalFeature.get();
                feature.getDependentFeatures().add(dependesOnFeature);
//                featureRepository.saveAndFlush(feature);
            }
        }
    }


    @Override
    @Transactional
    public void disableFeature(Long id) {
       /* Feature feature = featureRepository.findById(id);
        if(feature != null){
            feature.setActive(Boolean.FALSE);
            Feature subFeature = feature.getSubFeature();
            if (subFeature != null) {
                if(subFeature.getDependsOnFeatures().size() == 1) {
                    subFeature.setActive(Boolean.FALSE);
                } else {
                    AtomicReference<Boolean> checkParentFeatureIsActive = new AtomicReference<>(Boolean.FALSE);
                    subFeature.getDependsOnFeatures().forEach(f -> {
                        if(f.isActive() == Boolean.TRUE) {
                            checkParentFeatureIsActive.set(Boolean.TRUE);
                        }
                    });
                    subFeature.setActive(checkParentFeatureIsActive.get());
                }
                featureRepository.save(feature);
            }
        }*/
    }
}
