package com.example.petProject.config;


import com.example.petProject.constants.Constants;
import com.example.petProject.customAnnotation.ModuleAndDependentModule;
import com.example.petProject.customAnnotation.ModuleName;
import com.example.petProject.entity.Feature;
import com.example.petProject.entity.Module;
import com.example.petProject.enums.FeatureEnum;
import com.example.petProject.enums.ModuleEnum;
import com.example.petProject.repository.FeatureRepository;
import com.example.petProject.repository.ModuleRepository;
import com.example.petProject.service.FeatureService;
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
public class ApplicationStartupListenerForAllPreAuthorize implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ModuleRepository moduleRepository;
    private final FeatureRepository featureRepository;
    private final FeatureService featureService;
//    private final List<Module> moduleList = new ArrayList<>();
//    private final List<Feature> featureList = new ArrayList<>();

    public ApplicationStartupListenerForAllPreAuthorize(ModuleRepository moduleRepository,
                                                        FeatureRepository featureRepository, FeatureService featureService) {
        this.moduleRepository = moduleRepository;
        this.featureRepository = featureRepository;
        this.featureService = featureService;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String packageName = Constants.packageName;

        List<PreAuthorizeInfo> preAuthMethods = null;
        try {
            GlobalValue.featureListWithName = featureRepository.getAllFeature();
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
        featureService.deleteFeature(1L);

//        featureService.deleteFeature(1L);
//        featureService.deleteFeature(6L);

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
            ModuleEnum classLevelModuleName = null;
            if(clazz.isAnnotationPresent(ModuleName.class)) {
                classLevelModuleName = clazz.getAnnotation(ModuleName.class).name();
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                String featureName = null;
                if (method.isAnnotationPresent(PreAuthorize.class)) {
                    PreAuthorize preAuth = method.getAnnotation(PreAuthorize.class);
                    featureName = preAuth.value();
                    preAuthMethods.add(new PreAuthorizeInfo(method, featureName));
                }
                FeatureEnum[] dependentFeatures = null;
                ModuleEnum methodLevelModuleName = null;
                boolean isInternal = false;
                if (method.isAnnotationPresent(ModuleAndDependentModule.class)) {
                    ModuleAndDependentModule dependentModuleAnnotation = method.getAnnotation(ModuleAndDependentModule.class);
                    methodLevelModuleName = dependentModuleAnnotation.moduleName().getName() == ModuleEnum.EMPTY.getName() ? classLevelModuleName : dependentModuleAnnotation.moduleName();
                    dependentFeatures = dependentModuleAnnotation.dependentFeatures();
                    isInternal = dependentModuleAnnotation.isInternal();
                }
                if(featureName != null && methodLevelModuleName != null && dependentFeatures != null) {
                    seedDataToFeatureAndModuleTable(extractPermission(featureName),
                            methodLevelModuleName,
                            dependentFeatures, isInternal);
                }
            }
        }
        return preAuthMethods;
    }

    private void seedDataToFeatureAndModuleTable(String featureName, ModuleEnum moduleName, FeatureEnum[] dependentFeatures, boolean isInternal) {
        if(featureName != null && !GlobalValue.featureListWithName.contains(featureName)) {
            Module module = new Module();
            module.setModuleName(moduleName.getName());
            module.setActive(Boolean.TRUE);
            moduleRepository.save(module);

            //feature
            Feature feature = new Feature();
            feature.setFeatureName(featureName);
            feature.setActive(Boolean.TRUE);
            feature.setPrivilegeType(featureName);
            feature.setModule(module);
            feature.setInternal(isInternal);
            featureRepository.save(feature);
            featureService.addDependency(dependentFeatures, feature);
            module.getFeatures().add(feature);
//            moduleRepository.save(module);
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
