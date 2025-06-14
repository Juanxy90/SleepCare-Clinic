package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public Login log(String usuario, String contrasenia) {
        Login l = new Login();

        try {
            con = Conexion.getConnection();

            // Validar si es administrador
            String sqlAdmin = "SELECT * FROM administrador WHERE usuario = ? AND contrasenia = ?";
            ps = con.prepareStatement(sqlAdmin);
            ps.setString(1, usuario);
            ps.setString(2, contrasenia);
            rs = ps.executeQuery();
            if (rs.next()) {
                l.setUsuario(rs.getString("usuario"));
                l.setContrasenia(rs.getString("contrasenia"));
                l.setTipo("admin");
                return l;
            }

            // Validar si es usuario regular
            String sql = "SELECT * FROM usuario WHERE usuario = ? AND contrasenia = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasenia);
            rs = ps.executeQuery();

            if (rs.next()) {
                l.setUsuario(rs.getString("usuario"));
                l.setContrasenia(rs.getString("contrasenia"));
                l.setDocumento(rs.getString("documento"));

                String documento = rs.getString("documento");

                // Verificar si es paciente
                String sqlPaciente = "SELECT * FROM paciente WHERE documento = ?";
                PreparedStatement psPaciente = con.prepareStatement(sqlPaciente);
                psPaciente.setString(1, documento);
                ResultSet rsPaciente = psPaciente.executeQuery();
                if (rsPaciente.next()) {
                    l.setTipo("paciente");
                    return l;
                }

                // Verificar si es médico
                String sqlMedico = "SELECT * FROM medico WHERE documento = ?";
                PreparedStatement psMedico = con.prepareStatement(sqlMedico);
                psMedico.setString(1, documento);
                ResultSet rsMedico = psMedico.executeQuery();
                if (rsMedico.next()) {
                    l.setTipo("medico");
                    return l;
                }

                // Verificar si es técnico
                String sqlTecnico = "SELECT * FROM tecnico WHERE documento = ?";
                PreparedStatement psTecnico = con.prepareStatement(sqlTecnico);
                psTecnico.setString(1, documento);
                ResultSet rsTecnico = psTecnico.executeQuery();
                if (rsTecnico.next()) {
                    l.setTipo("tecnico");
                    return l;
                }

                l.setTipo("desconocido");
            }

        } catch (SQLException e) {
            System.out.println("Error en log(): " + e.getMessage());
        }

        return l;
    }
}