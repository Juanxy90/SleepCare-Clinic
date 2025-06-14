package Modelo;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class TratamientoDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarTratamiento(Tratamiento trat) {
        String sqlTratamiento = "INSERT INTO tratamiento (id, nombre, descripcion) VALUES (?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();

            // Validaciones de unicidad
            if (existeValorUnico("id", trat.getId())) {
                errores.append("- El ID ya está registrado.\n");
            }

            if (existeValorUnico("nombre", trat.getNombre())) {
                errores.append("- El nombre ya está registrado.\n");
            }

            // Mostrar errores acumulados
            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el tratamiento por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de registro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Insertar en tratamiento
            ps = con.prepareStatement(sqlTratamiento);
            ps.setString(1, trat.getId());
            ps.setString(2, trat.getNombre());
            ps.setString(3, trat.getDescripcion());           
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el tratamiento: " + e.getMessage());
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

    public List<Tratamiento> listarTratamiento() {
        List<Tratamiento> listaTt = new ArrayList<>();
        String sql = "SELECT t.id, t.nombre, t.descripcion FROM tratamiento t ";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Tratamiento tt = new Tratamiento();
                tt.setId(rs.getString("id"));
                tt.setNombre(rs.getString("nombre"));
                tt.setDescripcion(rs.getString("descripcion"));
                listaTt.add(tt);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaTt;
    }

    public boolean eliminarTratamiento(String id) {
        String sqlTratamiento = "DELETE FROM tratamiento WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sqlTratamiento);
            ps.setString(1, id);
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

    public boolean modificarTratamiento(Tratamiento tt, String idAnterior) {
        String sqlUsuario = "UPDATE tratamiento SET id = ?, nombre = ?, descripcion = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            if (!tt.getId().equals(idAnterior)) {
                if (existeValorUnico("id", tt.getId())) {
                    errores.append("- El nuevo ID ya está registrado.\n");
                }
            }

            if (!nombreCoincide(tt.getNombre(), idAnterior)) {
                if (existeValorUnico("nombre", tt.getNombre())) {
                    errores.append("- El nombre del tratamiento ya está registrado.\n");
                }
            }

            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo actualizar el tratamiento por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de actualización",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, tt.getId());
            ps.setString(2, tt.getNombre());
            ps.setString(3, tt.getDescripcion());
            ps.setString(4, idAnterior);

            int filasTratamiento = ps.executeUpdate();

            if (filasTratamiento == 0) {
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
        if (!campo.equals("id") && !campo.equals("nombre")) {
            throw new IllegalArgumentException("Campo no válido: " + campo);
        }

        String sql = "SELECT COUNT(*) FROM tratamiento WHERE " + campo + " = ?";
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

    private boolean nombreCoincide(String nombre, String idOriginal) {
        String sql = "SELECT nombre FROM tratamiento WHERE id = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, idOriginal);
            rs = ps.executeQuery();
            if (rs.next()) {
                return nombre.equals(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error validando tratamiento original: " + e.getMessage());
        }
        return false;
    }

    public Tratamiento buscarPorId(String id) {
        Tratamiento tratamiento = null;
        String sql = "SELECT t.id, t.nombre, t.descripcion FROM tratamiento t WHERE t.id = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tratamiento = new Tratamiento();
                tratamiento.setId(rs.getString("id"));
                tratamiento.setNombre(rs.getString("nombre"));
                tratamiento.setDescripcion(rs.getString("descripcion"));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tratamiento: " + e.getMessage());
        }

        return tratamiento;
    }
}