package Modelo;

import java.time.LocalDate;

public class Paciente extends Usuario{

    public Paciente() {
        super();
    }

    public Paciente(String usuario, String contrasenia, String correo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String documento, String telefono, String sexo, LocalDate fechaNacimiento) {
        super(usuario, contrasenia, correo, primerNombre, segundoNombre, primerApellido, segundoApellido, documento, telefono, sexo, fechaNacimiento);
    }
}
