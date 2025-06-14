package Modelo;

public class Login {

    private String usuario;
    private String contrasenia;
    private String tipo;
    private String documento;

    public Login() {
    }

    public Login(String usuario, String contrasenia, String tipo, String documento) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.tipo = tipo;
        this.documento = documento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

}