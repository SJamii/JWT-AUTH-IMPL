package com.example.petProject.config;

import com.example.petProject.entity.Feature;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class GlobalValue {
    public static List<String> featureListWithName;
}
