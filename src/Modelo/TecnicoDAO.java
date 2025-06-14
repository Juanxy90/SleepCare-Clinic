package Modelo;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class TecnicoDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarTecnico(Tecnico tec) {
        String sqlTelefono = "INSERT INTO telefono (documento, numero) VALUES (?, ?)";
        String sqlUsuario = "INSERT INTO usuario (usuario, contrasenia, correo, primerNombre, segundoNombre, primerApellido, segundoApellido, documento, fechaNacimiento, sexo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        String sqlTecnico = "INSERT INTO tecnico (documento, numeroCredencial) VALUES (?,?)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();

            // Validaciones de unicidad
            if (existeValorUnico("usuario", tec.getUsuario())) {
                errores.append("- El nombre de usuario ya está registrado.\n");
            }

            if (existeValorUnico("correo", tec.getCorreo())) {
                errores.append("- El correo electrónico ya está registrado.\n");
            }

            if (existeValorUnico("documento", tec.getDocumento())) {
                errores.append("- El documento ya está registrado.\n");
            }

            // Validar credencial
            String sqlCredencial = "SELECT COUNT(*) FROM tecnico WHERE numeroCredencial = ?";
            ps = con.prepareStatement(sqlCredencial);
            ps.setString(1, tec.getNumeroCredencial());
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                errores.append("- El número de credencial ya está registrado.\n");
            }

            // Validar teléfono
            String sqlTelefonoCheck = "SELECT COUNT(*) FROM telefono WHERE numero = ?";
            ps = con.prepareStatement(sqlTelefonoCheck);
            ps.setString(1, tec.getTelefono());
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                errores.append("- El número de teléfono ya está registrado.\n");
            }

            // Mostrar errores acumulados
            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el técnico por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de registro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Insertar en usuario
            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, tec.getUsuario());
            ps.setString(2, tec.getContrasenia());
            ps.setString(3, tec.getCorreo());
            ps.setString(4, tec.getPrimerNombre());
            ps.setString(5, tec.getSegundoNombre());
            ps.setString(6, tec.getPrimerApellido());
            ps.setString(7, tec.getSegundoApellido());
            ps.setString(8, tec.getDocumento());
            ps.setDate(9, java.sql.Date.valueOf(tec.getFechaNacimiento()));                            
            ps.setString(10, tec.getSexo());
            ps.executeUpdate();

            // Insertar en teléfono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, tec.getDocumento());
            ps.setString(2, tec.getTelefono());
            ps.executeUpdate();

            // Insertar en médico
            ps = con.prepareStatement(sqlTecnico);
            ps.setString(1, tec.getDocumento());
            ps.setString(2, tec.getNumeroCredencial());
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el técnico: " + e.getMessage());
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

    public List<Tecnico> listarTecnico() {
        List<Tecnico> listaTc = new ArrayList<>();
        // Usamos JOIN para traer datos de usuario + tecnico + telefono
        String sql = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, u.fechaNacimiento, u.sexo, tel.numero AS telefono, tec.numeroCredencial "
                + "FROM usuario u "
                + "INNER JOIN tecnico tec ON u.documento = tec.documento "
                + "INNER JOIN telefono tel ON u.documento = tel.documento";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Tecnico tc = new Tecnico();
                tc.setUsuario(rs.getString("usuario"));
                tc.setContrasenia(rs.getString("contrasenia"));
                tc.setCorreo(rs.getString("correo"));
                tc.setPrimerNombre(rs.getString("primerNombre"));
                tc.setSegundoNombre(rs.getString("segundoNombre"));
                tc.setPrimerApellido(rs.getString("primerApellido"));
                tc.setSegundoApellido(rs.getString("segundoApellido"));
                tc.setDocumento(rs.getString("documento"));
                tc.setNumeroCredencial(rs.getString("numeroCredencial"));                
                tc.setTelefono(rs.getString("telefono"));
                tc.setSexo(rs.getString("sexo"));
                java.sql.Date fechaNacimientoSQL = rs.getDate("fechaNacimiento");
                if (fechaNacimientoSQL != null) {
                    LocalDate fechaNacimiento = fechaNacimientoSQL.toLocalDate(); // ✅ SOLO esto
                    tc.setFechaNacimiento(fechaNacimiento);
                } else {
                    tc.setFechaNacimiento(null);
                }                
                listaTc.add(tc);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaTc;
    }

    public boolean eliminarTecnico(String documento) {
        String sqlTelefono = "DELETE FROM telefono WHERE documento = ?";
        String sqlTecnico = "DELETE FROM tecnico WHERE documento = ?";
        String sqlUsuario = "DELETE FROM usuario WHERE documento = ?";
        try {
            con = cn.getConnection();

            // Eliminar primero de telefono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, documento);
            ps.executeUpdate();

            // Eliminar luego de tecnico
            ps = con.prepareStatement(sqlTecnico);
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

    public boolean modificarTecnico(Tecnico tc, String documentoAnterior) {
        String sqlUsuario = "UPDATE usuario SET usuario = ?, contrasenia = ?, correo = ?, primerNombre = ?, segundoNombre = ?, primerApellido = ?, segundoApellido = ?, sexo = ?, fechaNacimiento = ?, documento = ? WHERE documento = ?";
        String sqlTecnico = "UPDATE tecnico SET numeroCredencial = ? WHERE documento = ?";
        String sqlTelefono = "UPDATE telefono SET numero = ? WHERE documento = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            // Validaciones
            if (!tc.getDocumento().equals(documentoAnterior)) {
                if (existeValorUnico("documento", tc.getDocumento())) {
                    errores.append("- El nuevo documento ya está registrado.\n");
                }
            }

            if (!usuarioCoincide(tc.getUsuario(), documentoAnterior)) {
                if (existeValorUnico("usuario", tc.getUsuario())) {
                    errores.append("- El nombre de usuario ya está registrado.\n");
                }
            }

            if (!correoCoincide(tc.getCorreo(), documentoAnterior)) {
                if (existeValorUnico("correo", tc.getCorreo())) {
                    errores.append("- El correo electrónico ya está registrado.\n");
                }
            }

            if (!credencialCoincide(tc.getNumeroCredencial(), documentoAnterior)) {
                if (credencialYaExiste(tc.getNumeroCredencial())) {
                    errores.append("- El número de credencial ya está registrado.\n");
                }
            }

            if (!telefonoCoincide(tc.getTelefono(), documentoAnterior)) {
                if (telefonoYaExiste(tc.getTelefono())) {
                    errores.append("- El número de teléfono ya está registrado.\n");
                }
            }

            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo actualizar el técnico por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de actualización",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // ✅ 1. Actualiza la tabla PADRE (usuario) — cambia el documento aquí
            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, tc.getUsuario());
            ps.setString(2, tc.getContrasenia());
            ps.setString(3, tc.getCorreo());
            ps.setString(4, tc.getPrimerNombre());
            ps.setString(5, tc.getSegundoNombre());
            ps.setString(6, tc.getPrimerApellido());
            ps.setString(7, tc.getSegundoApellido());
            ps.setString(8, tc.getSexo());
            ps.setDate(9, java.sql.Date.valueOf(tc.getFechaNacimiento())); // Convertimos LocalDate a java.sql.Date
            ps.setString(10, tc.getDocumento());
            ps.setString(11, documentoAnterior);
            int filasUsuario = ps.executeUpdate();

            // ✅ 2. Ya que documento se actualizó, MySQL actualiza automáticamente en las tablas hijas
            // Solo actualizamos los otros campos, pero NO el documento
            ps = con.prepareStatement(sqlTecnico);
            ps.setString(1, tc.getNumeroCredencial());
            ps.setString(2, tc.getDocumento());
            int filasTecnico = ps.executeUpdate();

            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, tc.getTelefono());
            ps.setString(2, tc.getDocumento());
            int filasTelefono = ps.executeUpdate();

            if (filasUsuario == 0 || filasTecnico == 0 || filasTelefono == 0) {
                System.out.println("filasUsuario=" + filasUsuario + ", filasTecnico=" + filasTecnico + ", filasTelefono=" + filasTelefono);
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

    private boolean credencialCoincide(String credencial, String documentoOriginal) {
        String sql = "SELECT numeroCredencial FROM tecnico WHERE documento = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, documentoOriginal);
            rs = ps.executeQuery();
            if (rs.next()) {
                return credencial.equals(rs.getString("numeroCredencial"));
            }
        } catch (SQLException e) {
            System.out.println("Error validando credencial original: " + e.getMessage());
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

    private boolean credencialYaExiste(String credencial) {
        String sql = "SELECT COUNT(*) FROM tecnico WHERE numeroCredencial = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, credencial);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error validando existencia de credencial: " + e.getMessage());
            return true;
        }
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

    public Tecnico buscarPorDocumento(String documento) {
        Tecnico tecnico = null;
        String sql = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, "
                + "u.documento, u.fechaNacimiento, u.sexo, tel.numero AS telefono, tec.numeroCredencial "
                + "FROM usuario u "
                + "INNER JOIN tecnico tec ON u.documento = tec.documento "
                + "INNER JOIN telefono tel ON u.documento = tel.documento "
                + "WHERE u.documento = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tecnico = new Tecnico();
                tecnico.setUsuario(rs.getString("usuario"));
                tecnico.setContrasenia(rs.getString("contrasenia"));
                tecnico.setCorreo(rs.getString("correo"));
                tecnico.setPrimerNombre(rs.getString("primerNombre"));
                tecnico.setSegundoNombre(rs.getString("segundoNombre"));
                tecnico.setPrimerApellido(rs.getString("primerApellido"));
                tecnico.setSegundoApellido(rs.getString("segundoApellido"));
                tecnico.setDocumento(rs.getString("documento"));
                tecnico.setFechaNacimiento(rs.getDate("fechaNacimiento").toLocalDate()); // Convertimos java.sql.Date a LocalDate                
                tecnico.setSexo(rs.getString("sexo"));
                tecnico.setTelefono(rs.getString("telefono"));
                tecnico.setNumeroCredencial(rs.getString("numeroCredencial"));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar técnico: " + e.getMessage());
        }

        return tecnico;
    }
}