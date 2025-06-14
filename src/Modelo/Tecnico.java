package Modelo;

import java.time.LocalDate;

public class Tecnico extends Usuario {
    
    private String numeroCredencial;

    public Tecnico() {
        super();
    }

    public Tecnico(String numeroCredencial) {
        this.numeroCredencial = numeroCredencial;
    }

    public Tecnico(String numeroCredencial, String usuario, String contrasenia, String correo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String documento, String telefono, String sexo, LocalDate fechaNacimiento) {
        super(usuario, contrasenia, correo, primerNombre, segundoNombre, primerApellido, segundoApellido, documento, telefono, sexo, fechaNacimiento);
        this.numeroCredencial = numeroCredencial;
    }

    public String getNumeroCredencial() {
        return numeroCredencial;
    }

    public void setNumeroCredencial(String numeroCredencial) {
        this.numeroCredencial = numeroCredencial;
    }
}