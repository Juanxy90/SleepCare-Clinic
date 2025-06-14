package Reportes;

import Modelo.Conexion;
import java.awt.BorderLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Grafico {

    // Método para obtener los datos de los usuarios
    public DefaultPieDataset obtenerDatosDeUsuarios() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT u.sexo, COUNT(*) as cantidad "
                    + "FROM usuario u "
                    + "GROUP BY u.sexo;";
            Conexion cn = new Conexion();
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No se encontraron registros en la base de datos.");
                return dataset; // Retorna dataset vacío si no hay registros
            }

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");
                dataset.setValue(sexo, cantidad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, con);
        }
        return dataset;
    }

    // Método para obtener los datos de los médicos
    public DefaultPieDataset obtenerDatosDeMedicos() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT u.sexo, COUNT(*) as cantidad "
                    + "FROM medico m "
                    + "JOIN usuario u ON m.documento = u.documento "
                    + "GROUP BY u.sexo;";
            Conexion cn = new Conexion();
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No se encontraron registros en la base de datos.");
                return dataset; // Retorna dataset vacío si no hay registros
            }

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");
                dataset.setValue(sexo, cantidad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, con);
        }
        return dataset;
    }

    // Método para obtener los datos de los técnicos
    public DefaultPieDataset obtenerDatosDeTecnicos() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT u.sexo, COUNT(*) as cantidad "
                    + "FROM tecnico tec "
                    + "JOIN usuario u ON tec.documento = u.documento "
                    + "GROUP BY u.sexo;";
            Conexion cn = new Conexion();
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No se encontraron registros en la base de datos.");
                return dataset; // Retorna dataset vacío si no hay registros
            }

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");
                dataset.setValue(sexo, cantidad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, con);
        }
        return dataset;
    }

    // Método para obtener los datos de los pacientes
    public DefaultPieDataset obtenerDatosDePacientes() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT u.sexo, COUNT(*) as cantidad "
                    + "FROM paciente p "
                    + "JOIN usuario u ON p.documento = u.documento "
                    + "GROUP BY u.sexo;";
            Conexion cn = new Conexion();
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No se encontraron registros en la base de datos.");
                return dataset; // Retorna dataset vacío si no hay registros
            }

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");
                dataset.setValue(sexo, cantidad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, con);
        }
        return dataset;
    }

    // Método para obtener los datos de usuarios por edad (agrupados)
    public DefaultPieDataset obtenerDatosDeUsuariosPorEdad() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = """
            SELECT edad, cantidad
            FROM (
                SELECT TIMESTAMPDIFF(YEAR, fechaNacimiento, CURDATE()) AS edad,
                       COUNT(*) AS cantidad
                FROM usuario
                GROUP BY edad
            ) AS sub
            ORDER BY edad DESC
            """;
            Conexion cn = new Conexion();
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No se encontraron registros en la base de datos.");
                return dataset;
            }

            while (rs.next()) {
                String edad = rs.getString("edad") + " años";
                int cantidad = rs.getInt("cantidad");
                dataset.setValue(edad, cantidad);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, con);
        }
        return dataset;
    }

    // Método para crear el gráfico con los datos obtenidos
    private JFreeChart crearGrafico(DefaultPieDataset dataset, String titulo) {
        JFreeChart chart = ChartFactory.createPieChart(
                titulo, // Título del gráfico
                dataset, // Datos
                true, // Leyenda
                true, // Tooltips
                false // URLs
        );

        // Personalizar el gráfico
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Masculino", new java.awt.Color(0, 102, 204));  // Azul
        plot.setSectionPaint("Femenino", new java.awt.Color(255, 99, 132));  // Rojo

        // Establecer el generador de etiquetas
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0} - {1} ({2})", // Formato: {0} es la categoría, {1} es la cantidad, {2} es el porcentaje
                java.text.NumberFormat.getNumberInstance(), // Formato de cantidad
                java.text.NumberFormat.getPercentInstance() // Formato de porcentaje
        );
        plot.setLabelGenerator(labelGenerator);

        return chart;
    }

    // Método para mostrar el gráfico de usuarios
    public void mostrarGraficoUsuarios() {
        DefaultPieDataset dataset = obtenerDatosDeUsuarios();
        if (dataset.getItemCount() == 0) {
            mostrarError("No se encontraron datos de usuarios para mostrar.");
            return;
        }

        JFreeChart chart = crearGrafico(dataset, "Cantidad de Usuarios por Sexo");
        mostrarGraficoEnVentana(chart, "Usuarios por Sexo (Gráfico Estadístico) | SleepCare Clinic");
    }

    // Método para mostrar el gráfico de médicos
    public void mostrarGraficoMedicos() {
        DefaultPieDataset dataset = obtenerDatosDeMedicos();
        if (dataset.getItemCount() == 0) {
            mostrarError("No se encontraron datos de médicos para mostrar.");
            return;
        }

        JFreeChart chart = crearGrafico(dataset, "Cantidad de Médicos por Sexo");
        mostrarGraficoEnVentana(chart, "Médicos Por Sexo (Gráfico Estadístico) | SleepCare Clinic");
    }

    // Método para mostrar el gráfico de técnicos
    public void mostrarGraficoTecnicos() {
        DefaultPieDataset dataset = obtenerDatosDeTecnicos();
        if (dataset.getItemCount() == 0) {
            mostrarError("No se encontraron datos de técnicos para mostrar.");
            return;
        }

        JFreeChart chart = crearGrafico(dataset, "Cantidad de Técnicos por Sexo");
        mostrarGraficoEnVentana(chart, "Técnicos por Sexo (Gráfico Estadístico) | SleepCare Clinic");
    }

    // Método para mostrar el gráfico de pacientes
    public void mostrarGraficoPacientes() {
        DefaultPieDataset dataset = obtenerDatosDePacientes();
        if (dataset.getItemCount() == 0) {
            mostrarError("No se encontraron datos de pacientes para mostrar.");
            return;
        }

        JFreeChart chart = crearGrafico(dataset, "Cantidad de Pacientes por Sexo");
        mostrarGraficoEnVentana(chart, "Pacientes por Sexo (Gráfico Estadístico) | SleepCare Clinic");
    }

    // Método para mostrar gráfico de usuarios por edad
    public void mostrarGraficoUsuariosPorEdad() {
        DefaultPieDataset dataset = obtenerDatosDeUsuariosPorEdad();
        if (dataset.getItemCount() == 0) {
            mostrarError("No se encontraron datos de usuarios por edad para mostrar.");
            return;
        }

        JFreeChart chart = crearGrafico(dataset, "Cantidad de Edades en los Usuarios");
        mostrarGraficoEnVentana(chart, "Cantidad de Edades en los Usuarios (Gráfico Estadístico) | SleepCare Clinic");
    }

    // Método para mostrar el gráfico en una nueva ventana
    private void mostrarGraficoEnVentana(JFreeChart chart, String title) {
        // Crear un panel para mostrar el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));  // Ajustar el tamaño

        // Crear una nueva ventana para mostrar el gráfico
        JFrame graficoFrame = new JFrame(title);
        graficoFrame.setLayout(new BorderLayout());
        graficoFrame.add(chartPanel, BorderLayout.CENTER);
        graficoFrame.setSize(800, 600);
        graficoFrame.setLocationRelativeTo(null); // Centrar la ventana
        graficoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar ventana cuando se haga click en la X
        graficoFrame.setVisible(true); // Hacer visible la ventana
        graficoFrame.setResizable(false); // ⛔ Evita maximizar o cambiar tamaño
    }

    // Método para mostrar mensajes de error
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }

    // Método para cerrar los recursos de la base de datos
    private void cerrarRecursos(ResultSet rs, PreparedStatement ps, Connection con) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
