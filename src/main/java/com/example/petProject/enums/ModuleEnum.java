package com.example.petProject.enums;

public enum ModuleEnum {
    BOOK(),AUTHOR(),PUBLISHER(), EMPTY();
//    private String label;

   /* ModuleName(String label) {
        this.label = label;
    }*/
 /*   public String getLabel() {
        return label;
    }*/
    public String getName() {
        return name();
    }
}