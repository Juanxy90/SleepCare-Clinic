package Modelo;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DiagnosticoDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarDiagnostico(Diagnostico diag) {
        String sqlDiagnostico = "INSERT INTO diagnostico (id, nombre, descripcion) VALUES (?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();

            // Validaciones de unicidad
            if (existeValorUnico("id", diag.getId())) {
                errores.append("- El ID ya está registrado.\n");
            }

            if (existeValorUnico("nombre", diag.getNombre())) {
                errores.append("- El nombre ya está registrado.\n");
            }

            // Mostrar errores acumulados
            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo registrar el diagnóstico por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de registro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Insertar en diagnóstico
            ps = con.prepareStatement(sqlDiagnostico);
            ps.setString(1, diag.getId());
            ps.setString(2, diag.getNombre());
            ps.setString(3, diag.getDescripcion());           
            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el diagóstico: " + e.getMessage());
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

    public List<Diagnostico> listarDiagnostico() {
        List<Diagnostico> listaDg = new ArrayList<>();
        String sql = "SELECT d.id, d.nombre, d.descripcion FROM diagnostico d ";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Diagnostico dg = new Diagnostico();
                dg.setId(rs.getString("id"));
                dg.setNombre(rs.getString("nombre"));
                dg.setDescripcion(rs.getString("descripcion"));
                listaDg.add(dg);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaDg;
    }

    public boolean eliminarDiagnostico(String id) {
        String sqlDiagnostico = "DELETE FROM diagnostico WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sqlDiagnostico);
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

    public boolean modificarDiagnostico(Diagnostico dg, String idAnterior) {
        String sqlUsuario = "UPDATE diagnostico SET id = ?, nombre = ?, descripcion = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        StringBuilder errores = new StringBuilder();

        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            if (!dg.getId().equals(idAnterior)) {
                if (existeValorUnico("id", dg.getId())) {
                    errores.append("- El nuevo ID ya está registrado.\n");
                }
            }

            if (!nombreCoincide(dg.getNombre(), idAnterior)) {
                if (existeValorUnico("nombre", dg.getNombre())) {
                    errores.append("- El nombre del diagnóstico ya está registrado.\n");
                }
            }

            if (errores.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo actualizar el diagnóstico por los siguientes motivos:\n\n" + errores.toString(),
                        "Error de actualización",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            ps = con.prepareStatement(sqlUsuario);
            ps.setString(1, dg.getId());
            ps.setString(2, dg.getNombre());
            ps.setString(3, dg.getDescripcion());
            ps.setString(4, idAnterior);

            int filasDiagnostico = ps.executeUpdate();

            if (filasDiagnostico == 0) {
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

        String sql = "SELECT COUNT(*) FROM diagnostico WHERE " + campo + " = ?";
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
        String sql = "SELECT nombre FROM diagnostico WHERE id = ?";
        try {
            ps = cn.getConnection().prepareStatement(sql);
            ps.setString(1, idOriginal);
            rs = ps.executeQuery();
            if (rs.next()) {
                return nombre.equals(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error validando diagnóstico original: " + e.getMessage());
        }
        return false;
    }

    public Diagnostico buscarPorId(String id) {
        Diagnostico diagnostico = null;
        String sql = "SELECT d.id, d.nombre, d.descripcion FROM diagnostico d WHERE d.id = ?";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                diagnostico = new Diagnostico();
                diagnostico.setId(rs.getString("id"));
                diagnostico.setNombre(rs.getString("nombre"));
                diagnostico.setDescripcion(rs.getString("descripcion"));
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar diagnóstico: " + e.getMessage());
        }

        return diagnostico;
    }
}
