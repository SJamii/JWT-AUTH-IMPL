package com.example.petProject.repository;

import com.example.petProject.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query("SELECT m FROM Module m WHERE m.moduleName IN :name")
    Module findByModuleName(String name);
}
