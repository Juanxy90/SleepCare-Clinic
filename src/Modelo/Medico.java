package Modelo;

import java.time.LocalDate;

public class Medico extends Usuario {
    
    private String numeroLicencia;

    public Medico() {
        super();
    }

    public Medico(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public Medico(String numeroLicencia, String usuario, String contrasenia, String correo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String documento, String telefono, String sexo, LocalDate fechaNacimiento) {
        super(usuario, contrasenia, correo, primerNombre, segundoNombre, primerApellido, segundoApellido, documento, telefono, sexo, fechaNacimiento);
        this.numeroLicencia = numeroLicencia;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }
}