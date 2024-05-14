package com.example.petProject.config;


import com.example.petProject.constants.Constants;
import com.example.petProject.customAnnotation.ModuleAndDependentModule;
import com.example.petProject.entity.Feature;
import com.example.petProject.entity.Module;
import com.example.petProject.enums.ModuleName;
import com.example.petProject.repository.FeatureRepository;
import com.example.petProject.repository.ModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;


@Component
public class ApplicationStartupListnerForAllPreAuthorize implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ModuleRepository moduleRepository;
    private final FeatureRepository featureRepository;

//    private final List<Module> moduleList = new ArrayList<>();
//    private final List<Feature> featureList = new ArrayList<>();

    public ApplicationStartupListnerForAllPreAuthorize(ModuleRepository moduleRepository,
                                                       FeatureRepository featureRepository) {
        this.moduleRepository = moduleRepository;
        this.featureRepository = featureRepository;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String packageName = Constants.packageName;

        List<PreAuthorizeInfo> preAuthMethods = null;
        try {
            preAuthMethods = findPreAuthorizeMethods(packageName);
        } catch (ClassNotFoundException e) {
            logger.error("---- Error ----- ", e.getException());
        }

        logger.info("Found PreAuthorize annotations on methods:");
        for (PreAuthorizeInfo info : preAuthMethods) {
           logger.info(info.method.getName() + " ----- >>>> in class <<<<------" + info.method.getDeclaringClass().getSimpleName());
            logger.info(">>>>>>>>>>>>>   PreAuthorize value:  <<<<<<<<<< " + extractPermission(info.value));
        }

        // let's try deleting
//        Optional<Feature> feature = Optional.ofNullable(featureRepository.findById(6L));
       /* Optional<Feature> feature = Optional.ofNullable(featureRepository.findById(1L));
        if (feature.isPresent()) {
            Feature featureObj = feature.get();
            List<Feature> temp = featureObj.getDependsOnFeature();
            if(temp.size() > 1) {
                logger.info("Found multiple dependencies on feature!! Not Possible To Delete");
            } else {
               *//* for(Feature f : featureObj.getDependsOnFeature()) {
                    f.getDependsOnFeature().remove(feature.get());
                }*//*
//                featureObj.getDependsOnFeature().clear();
//                featureObj.getDependsOnFeature().remove(featureObj.getDependsOnFeature());
//                featureRepository.delete(featureObj);
            }
        }
*/


    }

    public static String extractPermission(String preauthorizeValue) {
        if (preauthorizeValue == null || !preauthorizeValue.startsWith("hasAuthority('")) {
            return null;
        }
        // Split the string at the opening and closing parenthesis
        String[] parts = preauthorizeValue.split("('|'\\))");

        return parts[1];
    }

    public List<PreAuthorizeInfo> findPreAuthorizeMethods(String packageName) throws ClassNotFoundException {
        List<PreAuthorizeInfo> preAuthMethods = new ArrayList<>();

        Class<?>[] classes = getClasses(packageName);
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                String featureName = null;
                if (method.isAnnotationPresent(PreAuthorize.class)) {
                    PreAuthorize preAuth = method.getAnnotation(PreAuthorize.class);
                    featureName = preAuth.value();
                    preAuthMethods.add(new PreAuthorizeInfo(method, featureName));
                }
                ModuleName[] dependentModule = null;
                ModuleName moduleName = null;
                if (method.isAnnotationPresent(ModuleAndDependentModule.class)) {
                    ModuleAndDependentModule dependentModuleAnnotation = method.getAnnotation(ModuleAndDependentModule.class);
                    moduleName = dependentModuleAnnotation.moduleName();
                    dependentModule = dependentModuleAnnotation.dependentModules();
                }
                if(featureName != null && moduleName != null && dependentModule != null) {
                    seedDataToFeatureAndModuleTable(extractPermission(featureName), moduleName, dependentModule);
                }
            }
        }
        return preAuthMethods;
    }

    private void seedDataToFeatureAndModuleTable(String featureName, ModuleName moduleName, ModuleName[] dependentModules) {
        //module
        if(featureName != null) {
            Module module = new Module();
            module.setModuleName(moduleName.getName());
            module.setActive(Boolean.TRUE);
            List<Module> parentModules = new ArrayList<>();

            for(ModuleName dependentModule : dependentModules) {
                Optional<Module> optionalModule = Optional.ofNullable(moduleRepository.findByModuleName(dependentModule.getName()));
                if (optionalModule.isPresent()) {
                    parentModules.add(optionalModule.get());
//                Module parentModule = optionalModule.get();
                }
            }
            module.setSubmodules(parentModules);
            moduleRepository.save(module);



            //feature
            Feature feature = new Feature();
            feature.setFeatureName(featureName);
            feature.setActive(Boolean.TRUE);
            feature.setPrivilegeType(featureName);
            feature.setModule(module);
            featureRepository.save(feature);




        }
    }

    private static Class<?>[] getClasses(String packageName) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File file : new File(classLoader.getResource(path).getPath()).listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }

    static class PreAuthorizeInfo {
        public final Method method;
        public final String value;

        public PreAuthorizeInfo(Method method, String value) {
            this.method = method;
            this.value = value;
        }
    }
}
