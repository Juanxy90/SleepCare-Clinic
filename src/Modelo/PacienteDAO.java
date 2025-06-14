package Modelo;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class PacienteDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarPaciente(Paciente pac) {
        String sqlTelefono = "INSERT INTO telefono (documento, numero) VALUES (?, ?)";
        String sqlUsuario = "INSERT INTO usuario (usuario, contrasenia, correo, primerNombre, segundoNombre, primerApellido, segundoApellido, documento, fechaNacimiento, sexo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        String sqlPaciente = "INSERT INTO paciente (documento) VALUES (?)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();

            // Validaciones de unicidad
            if (existeValorUnico("usuario", pac.getUsuario())) {
                errores.append("- El nombre de usuario ya está registrado.\n");
            }

            if (existeValorUnico("correo", pac.getCorreo())) {
                errores.append("- El correo electrónico ya está registrado.\n");
            }

            if (existeValorUnico("documento", pac.getDocumento())) {
                errores.append("- El documento ya está registrado.\n");
            }

            // Mostrar errores acumulados
            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el paciente por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de registro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Insertar en usuario
            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, pac.getUsuario());
            ps.setString(2, pac.getContrasenia());
            ps.setString(3, pac.getCorreo());
            ps.setString(4, pac.getPrimerNombre());
            ps.setString(5, pac.getSegundoNombre());
            ps.setString(6, pac.getPrimerApellido());
            ps.setString(7, pac.getSegundoApellido());
            ps.setString(8, pac.getDocumento());
            ps.setDate(9, java.sql.Date.valueOf(pac.getFechaNacimiento()));                            
            ps.setString(10, pac.getSexo());
            ps.executeUpdate();

            // Insertar en teléfono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, pac.getDocumento());
            ps.setString(2, pac.getTelefono());
            ps.executeUpdate();

            // Insertar en paciente
            ps = con.prepareStatement(sqlPaciente);
            ps.setString(1, pac.getDocumento());
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el paciente: " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    public List<Paciente> listarPaciente() {
        List<Paciente> listaPc = new ArrayList<>();
        // Usamos JOIN para traer datos de usuario + paciente + telefono
        String sql = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, u.fechaNacimiento, u.sexo, tel.numero AS telefono "
                + "FROM usuario u "
                + "INNER JOIN paciente pac ON u.documento = pac.documento "
                + "INNER JOIN telefono tel ON u.documento = tel.documento";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Paciente pc = new Paciente();
                pc.setUsuario(rs.getString("usuario"));
                pc.setContrasenia(rs.getString("contrasenia"));
                pc.setCorreo(rs.getString("correo"));
                pc.setPrimerNombre(rs.getString("primerNombre"));
                pc.setSegundoNombre(rs.getString("segundoNombre"));
                pc.setPrimerApellido(rs.getString("primerApellido"));
                pc.setSegundoApellido(rs.getString("segundoApellido"));
                pc.setDocumento(rs.getString("documento"));
                pc.setTelefono(rs.getString("telefono"));
                pc.setSexo(rs.getString("sexo"));
                java.sql.Date fechaNacimientoSQL = rs.getDate("fechaNacimiento");
                if (fechaNacimientoSQL != null) {
                    LocalDate fechaNacimiento = fechaNacimientoSQL.toLocalDate(); // ✅ SOLO esto
                    pc.setFechaNacimiento(fechaNacimiento);
                } else {
                    pc.setFechaNacimiento(null);
                }                
                listaPc.add(pc);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaPc;
    }

    public boolean eliminarPaciente(String documento) {
        String sqlTelefono = "DELETE FROM telefono WHERE documento = ?";
        String sqlPaciente = "DELETE FROM paciente WHERE documento = ?";
        String sqlUsuario = "DELETE FROM usuario WHERE documento = ?";
        try {
            con = cn.getConnection();

            // Eliminar primero de telefono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, documento);
            ps.executeUpdate();

            // Eliminar luego de paciente
            ps = con.prepareStatement(sqlPaciente);
            ps.setString(1, documento);
            ps.executeUpdate();

            // Finalmente eliminar de usuario
            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, documento);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

        public boolean modificarPaciente(Paciente pc, String documentoAnterior) {
        String sqlUsuario = "UPDATE usuario SET usuario = ?, contrasenia = ?, correo = ?, primerNombre = ?, segundoNombre = ?, primerApellido = ?, segundoApellido = ?, fechaNacimiento = ?, sexo = ?, documento = ? WHERE documento = ?";
        String sqlPaciente = "UPDATE paciente SET documento = ? WHERE documento = ?";
        String sqlTelefono = "UPDATE telefono SET numero = ? WHERE documento = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            // Validar documento si cambió
            if (!pc.getDocumento().equals(documentoAnterior)) {
                if (existeValorUnico("documento", pc.getDocumento())) {
                    errores.append("- El nuevo documento ya está registrado.\n");
                }
            }

            // Validar usuario si cambió
            if (!usuarioCoincide(pc.getUsuario(), documentoAnterior)) {
                if (existeValorUnico("usuario", pc.getUsuario())) {
                    errores.append("- El nombre de usuario ya está registrado.\n");
                }
            }

            // Validar correo si cambió
            if (!correoCoincide(pc.getCorreo(), documentoAnterior)) {
                if (existeValorUnico("correo", pc.getCorreo())) {
                    errores.append("- El correo electrónico ya está registrado.\n");
                }
            }

            // Mostrar errores acumulados
            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo actualizar el médico por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de actualización",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Actualizar usuario
            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, pc.getUsuario());
            ps.setString(2, pc.getContrasenia());
            ps.setString(3, pc.getCorreo());
            ps.setString(4, pc.getPrimerNombre());
            ps.setString(5, pc.getSegundoNombre());
            ps.setString(6, pc.getPrimerApellido());
            ps.setString(7, pc.getSegundoApellido());
            ps.setDate(8, java.sql.Date.valueOf(pc.getFechaNacimiento())); // Convertimos LocalDate a java.sql.Date            
            ps.setString(9, pc.getSexo());
            ps.setString(10, pc.getDocumento());
            ps.setString(11, documentoAnterior);
            int filasUsuario = ps.executeUpdate();

            // Actualizar teléfono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, pc.getTelefono());
            ps.setString(2, pc.getDocumento());
            int filasTelefono = ps.executeUpdate();

            if (filasUsuario == 0 || filasTelefono == 0) {
                throw new SQLException("No se actualizaron todas las tablas correctamente.");
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    private boolean existeValorUnico(String campo, String valor) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE " + campo + " = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, valor);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error validando " + campo + ": " + e.getMessage());
        }
        return false;
    }

    private boolean usuarioCoincide(String usuario, String documentoOriginal) {
        String sql = "SELECT usuario FROM usuario WHERE documento = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, documentoOriginal);
            rs = ps.executeQuery();
            if (rs.next()) {
                return usuario.equals(rs.getString("usuario"));
            }
        } catch (SQLException e) {
            System.out.println("Error validando usuario original: " + e.getMessage());
        }
        return false;
    }

    private boolean correoCoincide(String correo, String documentoOriginal) {
        String sql = "SELECT correo FROM usuario WHERE documento = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, documentoOriginal);
            rs = ps.executeQuery();
            if (rs.next()) {
                return correo.equals(rs.getString("correo"));
            }
        } catch (SQLException e) {
            System.out.println("Error validando correo original: " + e.getMessage());
        }
        return false;
    }

    private boolean telefonoCoincide(String telefono, String documentoOriginal) {
        String sql = "SELECT numero FROM telefono WHERE documento = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, documentoOriginal);
            rs = ps.executeQuery();
            if (rs.next()) {
                return telefono.equals(rs.getString("numero"));
            }
        } catch (SQLException e) {
            System.out.println("Error validando teléfono original: " + e.getMessage());
        }
        return false;
    }

    private boolean telefonoYaExiste(String telefono) {
        String sql = "SELECT COUNT(*) FROM telefono WHERE numero = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, telefono);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error validando existencia de teléfono: " + e.getMessage());
            return true;
        }
    }

    public Paciente buscarPorDocumento(String documento) {
        Paciente paciente = null;
        String sql = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, "
                + "u.documento, u.fechaNacimiento, u.sexo, tel.numero AS telefono "
                + "FROM usuario u "
                + "INNER JOIN paciente pac ON u.documento = pac.documento "
                + "INNER JOIN telefono tel ON u.documento = tel.documento "
                + "WHERE u.documento = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                paciente = new Paciente();
                paciente.setUsuario(rs.getString("usuario"));
                paciente.setContrasenia(rs.getString("contrasenia"));
                paciente.setCorreo(rs.getString("correo"));
                paciente.setPrimerNombre(rs.getString("primerNombre"));
                paciente.setSegundoNombre(rs.getString("segundoNombre"));
                paciente.setPrimerApellido(rs.getString("primerApellido"));
                paciente.setSegundoApellido(rs.getString("segundoApellido"));
                paciente.setDocumento(rs.getString("documento"));
                paciente.setFechaNacimiento(rs.getDate("fechaNacimiento").toLocalDate()); // Convertimos java.sql.Date a LocalDate                
                paciente.setSexo(rs.getString("sexo"));
                paciente.setTelefono(rs.getString("telefono"));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar paciente: " + e.getMessage());
        }

        return paciente;
    }
}