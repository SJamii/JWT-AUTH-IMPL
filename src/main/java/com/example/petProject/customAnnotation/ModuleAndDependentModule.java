package com.example.petProject.customAnnotation;

import com.example.petProject.enums.FeatureEnum;
import com.example.petProject.enums.ModuleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.Boolean.FALSE;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModuleAndDependentModule {
    ModuleEnum moduleName() default ModuleEnum.EMPTY;
    FeatureEnum[] dependentFeatures();
    boolean isInternal() default false;
}
