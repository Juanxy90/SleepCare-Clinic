package Modelo;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class MedicoDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarMedico(Medico med) {
        String sqlTelefono = "INSERT INTO telefono (documento, numero) VALUES (?, ?)";
        String sqlUsuario = "INSERT INTO usuario (usuario, contrasenia, correo, primerNombre, segundoNombre, primerApellido, segundoApellido, documento, fechaNacimiento, sexo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        String sqlMedico = "INSERT INTO medico (documento, numeroLicencia) VALUES (?,?)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();

            // Validaciones de unicidad
            if (existeValorUnico("usuario", med.getUsuario())) {
                errores.append("- El nombre de usuario ya está registrado.\n");
            }

            if (existeValorUnico("correo", med.getCorreo())) {
                errores.append("- El correo electrónico ya está registrado.\n");
            }

            if (existeValorUnico("documento", med.getDocumento())) {
                errores.append("- El documento ya está registrado.\n");
            }

            // Validar licencia
            String sqlLicencia = "SELECT COUNT(*) FROM medico WHERE numeroLicencia = ?";
            ps = con.prepareStatement(sqlLicencia);
            ps.setString(1, med.getNumeroLicencia());
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                errores.append("- El número de licencia ya está registrado.\n");
            }

            // Validar teléfono
            String sqlTelefonoCheck = "SELECT COUNT(*) FROM telefono WHERE numero = ?";
            ps = con.prepareStatement(sqlTelefonoCheck);
            ps.setString(1, med.getTelefono());
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                errores.append("- El número de teléfono ya está registrado.\n");
            }

            // Mostrar errores acumulados
            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el médico por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de registro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Insertar en usuario
            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, med.getUsuario());
            ps.setString(2, med.getContrasenia());
            ps.setString(3, med.getCorreo());
            ps.setString(4, med.getPrimerNombre());
            ps.setString(5, med.getSegundoNombre());
            ps.setString(6, med.getPrimerApellido());
            ps.setString(7, med.getSegundoApellido());
            ps.setString(8, med.getDocumento());
            ps.setDate(9, java.sql.Date.valueOf(med.getFechaNacimiento()));                
            ps.setString(10, med.getSexo());            
            ps.executeUpdate();

            // Insertar en teléfono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, med.getDocumento());
            ps.setString(2, med.getTelefono());
            ps.executeUpdate();

            // Insertar en médico
            ps = con.prepareStatement(sqlMedico);
            ps.setString(1, med.getDocumento());
            ps.setString(2, med.getNumeroLicencia());
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el médico: " + e.getMessage());
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

    public List<Medico> listarMedico() {
        List<Medico> listaMd = new ArrayList<>();
        // Usamos JOIN para traer datos de usuario + medico + telefono
        String sql = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, u.fechaNacimiento, u.sexo, tel.numero AS telefono, m.numeroLicencia "
                + "FROM usuario u "
                + "INNER JOIN medico m ON u.documento = m.documento "
                + "INNER JOIN telefono tel ON u.documento = tel.documento";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Medico md = new Medico();
                md.setUsuario(rs.getString("usuario"));
                md.setContrasenia(rs.getString("contrasenia"));
                md.setCorreo(rs.getString("correo"));
                md.setPrimerNombre(rs.getString("primerNombre"));
                md.setSegundoNombre(rs.getString("segundoNombre"));
                md.setPrimerApellido(rs.getString("primerApellido"));
                md.setSegundoApellido(rs.getString("segundoApellido"));
                md.setDocumento(rs.getString("documento"));
                md.setNumeroLicencia(rs.getString("numeroLicencia"));                
                md.setTelefono(rs.getString("telefono"));
                md.setSexo(rs.getString("sexo"));
                java.sql.Date fechaNacimientoSQL = rs.getDate("fechaNacimiento");
                if (fechaNacimientoSQL != null) {
                    LocalDate fechaNacimiento = fechaNacimientoSQL.toLocalDate(); // ✅ SOLO esto
                    md.setFechaNacimiento(fechaNacimiento);
                } else {
                    md.setFechaNacimiento(null);
                }                
                listaMd.add(md);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaMd;
    }

    public boolean eliminarMedico(String documento) {
        String sqlTelefono = "DELETE FROM telefono WHERE documento = ?";
        String sqlMedico = "DELETE FROM medico WHERE documento = ?";
        String sqlUsuario = "DELETE FROM usuario WHERE documento = ?";
        try {
            con = cn.getConnection();

            // Eliminar primero de telefono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, documento);
            ps.executeUpdate();

            // Eliminar luego de medico
            ps = con.prepareStatement(sqlMedico);
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

    public boolean modificarMedico(Medico md, String documentoAnterior) {
        String sqlUsuario = "UPDATE usuario SET usuario = ?, contrasenia = ?, correo = ?, primerNombre = ?, segundoNombre = ?, primerApellido = ?, segundoApellido = ?, fechaNacimiento = ?, sexo = ?, documento = ? WHERE documento = ?";
        String sqlMedico = "UPDATE medico SET numeroLicencia = ? WHERE documento = ?";
        String sqlTelefono = "UPDATE telefono SET numero = ? WHERE documento = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            // Validar documento si cambió
            if (!md.getDocumento().equals(documentoAnterior)) {
                if (existeValorUnico("documento", md.getDocumento())) {
                    errores.append("- El nuevo documento ya está registrado.\n");
                }
            }

            // Validar usuario si cambió
            if (!usuarioCoincide(md.getUsuario(), documentoAnterior)) {
                if (existeValorUnico("usuario", md.getUsuario())) {
                    errores.append("- El nombre de usuario ya está registrado.\n");
                }
            }

            // Validar correo si cambió
            if (!correoCoincide(md.getCorreo(), documentoAnterior)) {
                if (existeValorUnico("correo", md.getCorreo())) {
                    errores.append("- El correo electrónico ya está registrado.\n");
                }
            }

            // Validar licencia si cambió
            if (!licenciaCoincide(md.getNumeroLicencia(), documentoAnterior)) {
                if (licenciaYaExiste(md.getNumeroLicencia())) {
                    errores.append("- El número de licencia ya está registrado.\n");
                }
            }

            // Validar teléfono si cambió
            if (!telefonoCoincide(md.getTelefono(), documentoAnterior)) {
                if (telefonoYaExiste(md.getTelefono())) {
                    errores.append("- El número de teléfono ya está registrado.\n");
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
            ps.setString(1, md.getUsuario());
            ps.setString(2, md.getContrasenia());
            ps.setString(3, md.getCorreo());
            ps.setString(4, md.getPrimerNombre());
            ps.setString(5, md.getSegundoNombre());
            ps.setString(6, md.getPrimerApellido());
            ps.setString(7, md.getSegundoApellido());
            ps.setDate(8, java.sql.Date.valueOf(md.getFechaNacimiento())); // Convertimos LocalDate a java.sql.Date            
            ps.setString(9, md.getSexo());
            ps.setString(10, md.getDocumento());
            ps.setString(11, documentoAnterior);
            int filasUsuario = ps.executeUpdate();

            // Actualizar médico
            ps = con.prepareStatement(sqlMedico);
            ps.setString(1, md.getNumeroLicencia());
            ps.setString(2, md.getDocumento());
            int filasMedico = ps.executeUpdate();

            // Actualizar teléfono
            ps = con.prepareStatement(sqlTelefono);
            ps.setString(1, md.getTelefono());
            ps.setString(2, md.getDocumento());
            int filasTelefono = ps.executeUpdate();

            if (filasUsuario == 0 || filasMedico == 0 || filasTelefono == 0) {
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

    private boolean licenciaCoincide(String licencia, String documentoOriginal) {
        String sql = "SELECT numeroLicencia FROM medico WHERE documento = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, documentoOriginal);
            rs = ps.executeQuery();
            if (rs.next()) {
                return licencia.equals(rs.getString("numeroLicencia"));
            }
        } catch (SQLException e) {
            System.out.println("Error validando licencia original: " + e.getMessage());
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

    private boolean licenciaYaExiste(String licencia) {
        String sql = "SELECT COUNT(*) FROM medico WHERE numeroLicencia = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, licencia);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error validando existencia de licencia: " + e.getMessage());
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

    public Medico buscarPorDocumento(String documento) {
        Medico medico = null;
        String sql = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, "
                + "u.documento, u.fechaNacimiento, u.sexo, tel.numero AS telefono, m.numeroLicencia "
                + "FROM usuario u "
                + "INNER JOIN medico m ON u.documento = m.documento "
                + "INNER JOIN telefono tel ON u.documento = tel.documento "
                + "WHERE u.documento = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                medico = new Medico();
                medico.setUsuario(rs.getString("usuario"));
                medico.setContrasenia(rs.getString("contrasenia"));
                medico.setCorreo(rs.getString("correo"));
                medico.setPrimerNombre(rs.getString("primerNombre"));
                medico.setSegundoNombre(rs.getString("segundoNombre"));
                medico.setPrimerApellido(rs.getString("primerApellido"));
                medico.setSegundoApellido(rs.getString("segundoApellido"));
                medico.setDocumento(rs.getString("documento"));
                medico.setFechaNacimiento(rs.getDate("fechaNacimiento").toLocalDate()); // Convertimos java.sql.Date a LocalDate                
                medico.setSexo(rs.getString("sexo"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setNumeroLicencia(rs.getString("numeroLicencia"));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar médico: " + e.getMessage());
        }

        return medico;
    }
}