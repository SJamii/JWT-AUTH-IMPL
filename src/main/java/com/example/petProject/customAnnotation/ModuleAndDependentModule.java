package com.example.petProject.customAnnotation;

import com.example.petProject.enums.ModuleName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModuleAndDependentModule {
    ModuleName moduleName();
    ModuleName[] dependentModules();
}
