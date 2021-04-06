package de.dreimu.minecraft.plugins.apis.guiapi;

// Die Klasse für FunctionDeclarationException, wird aufgerufen, wenn eine Funktion falsch deklariert sein sollte.
public class FunctionDeclarationException extends Exception{

    // Die Versionsnummer?
    private static final long serialVersionUID = 1L;

    // Der Creator
    public FunctionDeclarationException(String errorMessage) {
        super(errorMessage);
    }
}