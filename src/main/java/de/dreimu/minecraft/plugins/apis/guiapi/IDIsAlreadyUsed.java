package de.dreimu.minecraft.plugins.apis.guiapi;

public class IDIsAlreadyUsed extends Exception{
    
    private static final long serialVersionUID = 1L;

    public IDIsAlreadyUsed(String errorMessage) {
        super(errorMessage);
    }
}
