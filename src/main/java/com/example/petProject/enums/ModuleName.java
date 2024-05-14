package com.example.petProject.enums;

public enum ModuleName {
    BOOK(),AUTHOR(),PUBLISHER();
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