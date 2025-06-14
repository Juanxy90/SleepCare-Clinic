package Reportes;

import Modelo.Conexion;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class Pdf {

    // Método para contar usuarios por sexo
    private static Map<String, Integer> contarUsuariosSexo() {
        Map<String, Integer> sexCount = new HashMap<>();
        sexCount.put("Masculino", 0);
        sexCount.put("Femenino", 0);

        String query = "SELECT sexo, COUNT(*) as cantidad "
                + "FROM usuario "
                + "GROUP BY sexo";

        try (
                Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");

                if (sexo.equalsIgnoreCase("Masculino")) {
                    sexCount.put("Masculino", cantidad);
                } else if (sexo.equalsIgnoreCase("Femenino")) {
                    sexCount.put("Femenino", cantidad);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sexCount;
    }

    // Método para contar médicos por sexo
    private static Map<String, Integer> contarMedicosSexo() {
        Map<String, Integer> sexCount = new HashMap<>();
        sexCount.put("Masculino", 0);
        sexCount.put("Femenino", 0);

        String query = "SELECT u.sexo, COUNT(*) as cantidad "
                + "FROM medico m "
                + "JOIN usuario u ON m.documento = u.documento "
                + "GROUP BY u.sexo";

        try (
                Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");

                if (sexo.equalsIgnoreCase("Masculino")) {
                    sexCount.put("Masculino", cantidad);
                } else if (sexo.equalsIgnoreCase("Femenino")) {
                    sexCount.put("Femenino", cantidad);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sexCount;
    }

    // Método para contar técnicos por sexo
    private static Map<String, Integer> contarTecnicosSexo() {
        Map<String, Integer> sexCount = new HashMap<>();
        sexCount.put("Masculino", 0);
        sexCount.put("Femenino", 0);

        String query = "SELECT u.sexo, COUNT(*) as cantidad "
                + "FROM tecnico tec "
                + "JOIN usuario u ON tec.documento = u.documento "
                + "GROUP BY u.sexo";

        try (
                Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");

                if (sexo.equalsIgnoreCase("Masculino")) {
                    sexCount.put("Masculino", cantidad);
                } else if (sexo.equalsIgnoreCase("Femenino")) {
                    sexCount.put("Femenino", cantidad);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sexCount;
    }

    // Método para contar pacientes por sexo
    private static Map<String, Integer> contarPacientesSexo() {
        Map<String, Integer> sexCount = new HashMap<>();
        sexCount.put("Masculino", 0);
        sexCount.put("Femenino", 0);

        String query = "SELECT u.sexo, COUNT(*) as cantidad "
                + "FROM paciente p "
                + "JOIN usuario u ON p.documento = u.documento "
                + "GROUP BY u.sexo";

        try (
                Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String sexo = rs.getString("sexo");
                int cantidad = rs.getInt("cantidad");

                if (sexo.equalsIgnoreCase("Masculino")) {
                    sexCount.put("Masculino", cantidad);
                } else if (sexo.equalsIgnoreCase("Femenino")) {
                    sexCount.put("Femenino", cantidad);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sexCount;
    }

    // Método para generar y abrir el PDF
    public static void generarReporteCantidadUsuariosPorSexo() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/cantidad_usuarios_sexo.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Map<String, Integer> sexCount = contarUsuariosSexo();
            int masculino = sexCount.getOrDefault("Masculino", 0);
            int femenino = sexCount.getOrDefault("Femenino", 0);
            int total = masculino + femenino;

            double porcMasculino = total > 0 ? (masculino * 100.0 / total) : 0;
            double porcFemenino = total > 0 ? (femenino * 100.0 / total) : 0;

            Paragraph resultados = new Paragraph("\nCantidad de usuarios por sexo:\n\n");
            resultados.setFont(negrita);
            resultados.setSpacingBefore(10f);
            doc.add(resultados);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.addCell("Sexo");
            table.addCell("Cantidad");
            table.addCell("Porcentaje");

            table.addCell("Masculino");
            table.addCell(String.valueOf(masculino));
            table.addCell(String.format("%.2f %%", porcMasculino));

            table.addCell("Femenino");
            table.addCell(String.valueOf(femenino));
            table.addCell(String.format("%.2f %%", porcFemenino));

            PdfPCell totalCell = new PdfPCell(new Phrase("Total de usuarios: " + total));
            totalCell.setColspan(3);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            doc.add(table);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para generar y abrir el PDF
    public static void generarReporteCantidadMedicosPorSexo() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/cantidad_medicos_sexo.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Map<String, Integer> sexCount = contarMedicosSexo();
            int masculino = sexCount.getOrDefault("Masculino", 0);
            int femenino = sexCount.getOrDefault("Femenino", 0);
            int total = masculino + femenino;

            double porcMasculino = total > 0 ? (masculino * 100.0 / total) : 0;
            double porcFemenino = total > 0 ? (femenino * 100.0 / total) : 0;

            Paragraph resultados = new Paragraph("\nCantidad de médicos por sexo:\n\n");
            resultados.setFont(negrita);
            resultados.setSpacingBefore(10f);
            doc.add(resultados);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.addCell("Sexo");
            table.addCell("Cantidad");
            table.addCell("Porcentaje");

            table.addCell("Masculino");
            table.addCell(String.valueOf(masculino));
            table.addCell(String.format("%.2f %%", porcMasculino));

            table.addCell("Femenino");
            table.addCell(String.valueOf(femenino));
            table.addCell(String.format("%.2f %%", porcFemenino));

            PdfPCell totalCell = new PdfPCell(new Phrase("Total de médicos: " + total));
            totalCell.setColspan(3);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            doc.add(table);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para generar y abrir el PDF
    public static void generarReporteCantidadTecnicosPorSexo() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/cantidad_tecnicos_sexo.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Map<String, Integer> sexCount = contarTecnicosSexo();
            int masculino = sexCount.getOrDefault("Masculino", 0);
            int femenino = sexCount.getOrDefault("Femenino", 0);
            int total = masculino + femenino;

            double porcMasculino = total > 0 ? (masculino * 100.0 / total) : 0;
            double porcFemenino = total > 0 ? (femenino * 100.0 / total) : 0;

            Paragraph resultados = new Paragraph("\nCantidad de técnicos por sexo:\n\n");
            resultados.setFont(negrita);
            resultados.setSpacingBefore(10f);
            doc.add(resultados);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.addCell("Sexo");
            table.addCell("Cantidad");
            table.addCell("Porcentaje");

            table.addCell("Masculino");
            table.addCell(String.valueOf(masculino));
            table.addCell(String.format("%.2f %%", porcMasculino));

            table.addCell("Femenino");
            table.addCell(String.valueOf(femenino));
            table.addCell(String.format("%.2f %%", porcFemenino));

            PdfPCell totalCell = new PdfPCell(new Phrase("Total de técnicos: " + total));
            totalCell.setColspan(3);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            doc.add(table);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para generar y abrir el PDF
    public static void generarReporteCantidadPacientesPorSexo() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/cantidad_pacientes_sexo.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Map<String, Integer> sexCount = contarPacientesSexo();
            int masculino = sexCount.getOrDefault("Masculino", 0);
            int femenino = sexCount.getOrDefault("Femenino", 0);
            int total = masculino + femenino;

            double porcMasculino = total > 0 ? (masculino * 100.0 / total) : 0;
            double porcFemenino = total > 0 ? (femenino * 100.0 / total) : 0;

            Paragraph resultados = new Paragraph("\nCantidad de pacientes por sexo:\n\n");
            resultados.setFont(negrita);
            resultados.setSpacingBefore(10f);
            doc.add(resultados);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.addCell("Sexo");
            table.addCell("Cantidad");
            table.addCell("Porcentaje");

            table.addCell("Masculino");
            table.addCell(String.valueOf(masculino));
            table.addCell(String.format("%.2f %%", porcMasculino));

            table.addCell("Femenino");
            table.addCell(String.valueOf(femenino));
            table.addCell(String.format("%.2f %%", porcFemenino));

            PdfPCell totalCell = new PdfPCell(new Phrase("Total de pacientes: " + total));
            totalCell.setColspan(3);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            doc.add(table);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteTodosLosUsuarios() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/todos_los_usuarios.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nLista de todos los usuarios:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(11);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setWidths(new float[]{10, 10, 20, 10, 10, 10, 10, 15, 12, 12, 8});

            // Encabezados
            tabla.addCell("Usuario");
            tabla.addCell("Contraseña");
            tabla.addCell("Correo");
            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Teléfono");
            tabla.addCell("Fecha Nacimiento");
            tabla.addCell("Sexo");

            String query = "SELECT "
                    + "u.usuario, u.contrasenia, u.correo, "
                    + "u.primernombre, u.segundonombre, u.primerapellido, u.segundoapellido, "
                    + "u.documento, tel.numero AS telefono, "
                    + "u.fechanacimiento, u.sexo "
                    + "FROM usuario u "
                    + "LEFT JOIN telefono tel ON u.documento = tel.documento";

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("usuario"));
                    tabla.addCell(rs.getString("contrasenia"));
                    tabla.addCell(rs.getString("correo"));
                    tabla.addCell(rs.getString("primernombre"));
                    tabla.addCell(rs.getString("segundonombre"));
                    tabla.addCell(rs.getString("primerapellido"));
                    tabla.addCell(rs.getString("segundoapellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("telefono") != null ? rs.getString("telefono") : "N/A");
                    tabla.addCell(rs.getString("fechanacimiento"));
                    tabla.addCell(rs.getString("sexo"));
                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            // Total
            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarios: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteTodosLosMedicos() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/todos_los_medicos.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nLista de todos los médicos:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(12);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setWidths(new float[]{10, 10, 20, 10, 10, 10, 10, 15, 10, 12, 12, 8});

            // Encabezados
            tabla.addCell("Usuario");
            tabla.addCell("Contraseña");
            tabla.addCell("Correo");
            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Número Licencia");
            tabla.addCell("Teléfono");
            tabla.addCell("Fecha Nacimiento");
            tabla.addCell("Sexo");

            String query = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, m.numeroLicencia, u.fechaNacimiento, u.sexo, tel.numero AS telefono "
                    + "FROM usuario u "
                    + "INNER JOIN medico m ON u.documento = m.documento "
                    + "INNER JOIN telefono tel ON u.documento = tel.documento";

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("usuario"));
                    tabla.addCell(rs.getString("contrasenia"));
                    tabla.addCell(rs.getString("correo"));
                    tabla.addCell(rs.getString("primernombre"));
                    tabla.addCell(rs.getString("segundonombre"));
                    tabla.addCell(rs.getString("primerapellido"));
                    tabla.addCell(rs.getString("segundoapellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("numeroLicencia"));
                    tabla.addCell(rs.getString("telefono") != null ? rs.getString("telefono") : "N/A");
                    tabla.addCell(rs.getString("fechanacimiento"));
                    tabla.addCell(rs.getString("sexo"));
                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            // Total
            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de médicos: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteTodosLosTecnicos() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/todos_los_tecnicos.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nLista de todos los técnicos:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(12);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setWidths(new float[]{10, 10, 20, 10, 10, 10, 10, 15, 10, 12, 12, 8});

            // Encabezados
            tabla.addCell("Usuario");
            tabla.addCell("Contraseña");
            tabla.addCell("Correo");
            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Número Credencial");
            tabla.addCell("Teléfono");
            tabla.addCell("Fecha Nacimiento");
            tabla.addCell("Sexo");

            String query = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, tec.numeroCredencial, u.fechaNacimiento, u.sexo, tel.numero AS telefono "
                    + "FROM usuario u "
                    + "INNER JOIN tecnico tec ON u.documento = tec.documento "
                    + "INNER JOIN telefono tel ON u.documento = tel.documento";

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("usuario"));
                    tabla.addCell(rs.getString("contrasenia"));
                    tabla.addCell(rs.getString("correo"));
                    tabla.addCell(rs.getString("primernombre"));
                    tabla.addCell(rs.getString("segundonombre"));
                    tabla.addCell(rs.getString("primerapellido"));
                    tabla.addCell(rs.getString("segundoapellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("numeroCredencial"));
                    tabla.addCell(rs.getString("telefono") != null ? rs.getString("telefono") : "N/A");
                    tabla.addCell(rs.getString("fechanacimiento"));
                    tabla.addCell(rs.getString("sexo"));
                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            // Total
            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de técnicos: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteTodosLosPacientes() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/todos_los_pacientes.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nLista de todos los pacientes:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(11);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setWidths(new float[]{10, 10, 20, 10, 10, 10, 10, 15, 12, 12, 8});

            // Encabezados
            tabla.addCell("Usuario");
            tabla.addCell("Contraseña");
            tabla.addCell("Correo");
            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Teléfono");
            tabla.addCell("Fecha Nacimiento");
            tabla.addCell("Sexo");

            String query = "SELECT u.usuario, u.contrasenia, u.correo, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, u.fechaNacimiento, u.sexo, tel.numero AS telefono "
                    + "FROM usuario u "
                    + "INNER JOIN paciente p ON u.documento = p.documento "
                    + "INNER JOIN telefono tel ON u.documento = tel.documento";

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("usuario"));
                    tabla.addCell(rs.getString("contrasenia"));
                    tabla.addCell(rs.getString("correo"));
                    tabla.addCell(rs.getString("primernombre"));
                    tabla.addCell(rs.getString("segundonombre"));
                    tabla.addCell(rs.getString("primerapellido"));
                    tabla.addCell(rs.getString("segundoapellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("telefono") != null ? rs.getString("telefono") : "N/A");
                    tabla.addCell(rs.getString("fechanacimiento"));
                    tabla.addCell(rs.getString("sexo"));
                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            // Total
            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de pacientes: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteUsuariosPorEdad() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/usuarios_por_edad.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nCantidad de edades en los usuarios:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(50);
            tabla.setSpacingBefore(10f);
            tabla.setWidths(new float[]{1, 1});

            tabla.addCell("Edad");
            tabla.addCell("Cantidad");

            String query = """
                SELECT edad, cantidad
                FROM (
                    SELECT TIMESTAMPDIFF(YEAR, fechaNacimiento, CURDATE()) AS edad,
                           COUNT(*) AS cantidad
                    FROM usuario
                    GROUP BY edad
                ) AS sub
                ORDER BY edad DESC
                """;

            int totalPacientes = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int edad = rs.getInt("edad");
                    int cantidad = rs.getInt("cantidad");

                    tabla.addCell(String.valueOf(edad));
                    tabla.addCell(String.valueOf(cantidad));

                    totalPacientes += cantidad;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarios con edades repetidas: " + totalPacientes);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteUsuariosPorFechaNacimiento() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/usuarios_por_fecha_nacimiento.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de usuarios ordenados por fecha de nacimiento:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setWidths(new float[]{10, 10, 10, 10, 15, 12, 8});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Sexo");
            tabla.addCell("Fecha de Nacimiento");

            String query = """
                SELECT primerNombre, segundoNombre, primerApellido, segundoApellido, documento, sexo, fechaNacimiento
                FROM usuario
                ORDER BY fechaNacimiento DESC
            """;

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String primerNombre = rs.getString("primerNombre");
                    String segundoNombre = rs.getString("segundoNombre");
                    String primerApellido = rs.getString("primerApellido");
                    String segundoApellido = rs.getString("segundoApellido");
                    String documento = rs.getString("documento");
                    String sexo = rs.getString("sexo");
                    Date fechaNac = rs.getDate("fechaNacimiento");

                    tabla.addCell(primerNombre);
                    tabla.addCell(segundoNombre);
                    tabla.addCell(primerApellido);
                    tabla.addCell(segundoApellido);
                    tabla.addCell(documento);
                    tabla.addCell(sexo);
                    tabla.addCell(new SimpleDateFormat("yyyy-MM-dd").format(fechaNac));

                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarios: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteDiagnosticosConNombreCorto() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/diagnosticos_con_nombre_corto.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de diagnósticos con el nombre más corto:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setWidths(new float[]{10, 30, 60}); // Ancho para nombre y descripción

            tabla.addCell("ID");
            tabla.addCell("Nombre");
            tabla.addCell("Descripción");

            String query = """
            SELECT id, nombre, descripcion
            FROM diagnostico
            WHERE id IN (
                SELECT id
                FROM diagnostico
                WHERE LENGTH(nombre) < 15
            );
        """;

            int totalDiagnosticos = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");

                    tabla.addCell(id);
                    tabla.addCell(nombre);
                    tabla.addCell(descripcion);

                    totalDiagnosticos++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de diagnósticos con el nombre más corto: " + totalDiagnosticos);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReportePrimerNombreMasComun() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/primeros_nombres_comunes.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de la cantidad de usuarios con el primer nombre más común:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(50);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{3, 1});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Frecuencia");

            String query = """
            SELECT primerNombre, COUNT(*) AS frecuencia
            FROM usuario
            GROUP BY primerNombre
            ORDER BY frecuencia DESC
            LIMIT 10
        """;

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String primerNombre = rs.getString("primerNombre");
                    int frecuencia = rs.getInt("frecuencia");

                    tabla.addCell(primerNombre);
                    tabla.addCell(String.valueOf(frecuencia));

                    totalUsuarios += frecuencia;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarios con el primer nombre más común: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
            doc.close();

            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteSegundoNombreMasComun() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/segundos_nombres_comunes.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de la cantidad de usuarios con el segundo nombre más común:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(50);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{3, 1});

            tabla.addCell("Segundo Nombre");
            tabla.addCell("Frecuencia");

            String query = """
            SELECT segundoNombre, COUNT(*) AS frecuencia
            FROM usuario
            GROUP BY segundoNombre
            ORDER BY frecuencia DESC
            LIMIT 10
            """;

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String primerNombre = rs.getString("segundoNombre");
                    int frecuencia = rs.getInt("frecuencia");

                    tabla.addCell(primerNombre);
                    tabla.addCell(String.valueOf(frecuencia));

                    totalUsuarios += frecuencia;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarios con el segundo nombre más común: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
            doc.close();

            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteTecnicosCredencialImpar() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/tecnicos_credencial_impar.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nLista de técnicos cuyo número de credencial termina en número impar:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{3, 3, 3, 4, 4, 4});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("N° Credencial");

            String query = """
        SELECT u.primerNombre, u.segundoNombre, u.primerApellido, segundoApellido, u.documento, tec.numeroCredencial
        FROM tecnico tec
        JOIN usuario u ON tec.documento = u.documento
        WHERE RIGHT(tec.numeroCredencial, 1) IN ('1', '3', '5', '7', '9');
        """;

            int totalTecnicos = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("primerNombre"));
                    tabla.addCell(rs.getString("segundoNombre"));
                    tabla.addCell(rs.getString("primerApellido"));
                    tabla.addCell(rs.getString("segundoApellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("numeroCredencial"));
                    totalTecnicos++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de técnicos con número de credencial terminado en impar: " + totalTecnicos);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteUsuariosMasculinos() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/usuarios_masculinos.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de usuarios masculinos:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{3, 3, 3, 3, 4, 2});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Sexo");

            String query = """
            SELECT primerNombre, segundoNombre, primerApellido, segundoApellido, documento, sexo
            FROM usuario
            WHERE sexo = 'Masculino';
            """;

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("primerNombre"));
                    tabla.addCell(rs.getString("segundoNombre"));
                    tabla.addCell(rs.getString("primerApellido"));
                    tabla.addCell(rs.getString("segundoApellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("sexo"));
                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarios masculinos: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteUsuariasFemeninas() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/usuarias_femeninas.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de usuarias femeninas:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{3, 3, 3, 3, 4, 2});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Sexo");

            String query = """
            SELECT primerNombre, segundoNombre, primerApellido, segundoApellido, documento, sexo
            FROM usuario
            WHERE sexo = 'Femenino';
            """;

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("primerNombre"));
                    tabla.addCell(rs.getString("segundoNombre"));
                    tabla.addCell(rs.getString("primerApellido"));
                    tabla.addCell(rs.getString("segundoApellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("sexo"));
                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarias femeninas: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteTelefonosUsuariosMasculinos() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/telefonos_usuarios_masculinos.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de teléfonos asociados a usuarios de sexo masculino:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{4, 4, 4, 4, 3, 3, 3});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Teléfono");
            tabla.addCell("Sexo");

            String query = """
            SELECT u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, t.numero, u.sexo
            FROM telefono t
            JOIN usuario u ON t.documento = u.documento
            WHERE u.sexo = 'Masculino';
            """;

            int totalTelefonos = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("primerNombre"));
                    tabla.addCell(rs.getString("segundoNombre"));
                    tabla.addCell(rs.getString("primerApellido"));
                    tabla.addCell(rs.getString("segundoApellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("numero"));
                    tabla.addCell(rs.getString("sexo"));
                    totalTelefonos++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de teléfonos asociados a usuarios masculinos: " + totalTelefonos);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteTelefonosUsuariasFemeninas() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/telefonos_usuarias_femeninas.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de teléfonos asociados a usuarios de sexo femenino:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{4, 4, 4, 4, 3, 3, 3});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Teléfono");
            tabla.addCell("Sexo");

            String query = """
            SELECT u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, u.documento, t.numero, u.sexo
            FROM telefono t
            JOIN usuario u ON t.documento = u.documento
            WHERE u.sexo = 'Femenino';
            """;

            int totalTelefonos = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("primerNombre"));
                    tabla.addCell(rs.getString("segundoNombre"));
                    tabla.addCell(rs.getString("primerApellido"));
                    tabla.addCell(rs.getString("segundoApellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("numero"));
                    tabla.addCell(rs.getString("sexo"));
                    totalTelefonos++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de teléfonos asociados a usuarias femeninas: " + totalTelefonos);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteUsuariosAnoNacimientoMasComun() {
        try {
            File dir = new File("src/pdf");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File("src/pdf/usuarios_año_nacimiento_comun.pdf").getAbsoluteFile();
            FileOutputStream archivo = new FileOutputStream(file);
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Image img = Image.getInstance("src/img/logo.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Fecha: " + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            encabezado.addCell(img);
            encabezado.addCell("");
            encabezado.addCell("\nSleepCare Clinic\nCuidamos de tu sueño\n\nRUC: 26105-2025\nTeléfono: 6016068484\ncontacto@sleepcareclinic.com\nwww.sleepcareclinic.com\nDirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
            encabezado.addCell(fecha);
            doc.add(encabezado);

            Paragraph titulo = new Paragraph("\nListado de usuarios cuyo año de nacimiento es el más comúm:\n\n");
            titulo.setFont(negrita);
            titulo.setSpacingBefore(10f);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.setWidths(new float[]{3, 3, 3, 3, 4, 3});

            tabla.addCell("Primer Nombre");
            tabla.addCell("Segundo Nombre");
            tabla.addCell("Primer Apellido");
            tabla.addCell("Segundo Apellido");
            tabla.addCell("Documento");
            tabla.addCell("Fecha Nacimiento");

            String query = """
            SELECT primerNombre, segundoNombre, primerApellido, segundoApellido, documento, fechaNacimiento
            FROM usuario
            WHERE YEAR(fechaNacimiento) = (
                SELECT YEAR(fechaNacimiento)
                FROM usuario
                GROUP BY YEAR(fechaNacimiento)
                ORDER BY COUNT(*) DESC
                LIMIT 1
            );
        """;

            int totalUsuarios = 0;

            try (
                    Connection connection = Conexion.getConnection(); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    tabla.addCell(rs.getString("primerNombre"));
                    tabla.addCell(rs.getString("segundoNombre"));
                    tabla.addCell(rs.getString("primerApellido"));
                    tabla.addCell(rs.getString("segundoApellido"));
                    tabla.addCell(rs.getString("documento"));
                    tabla.addCell(rs.getString("fechaNacimiento"));
                    totalUsuarios++;
                }
            }

            doc.add(tabla);

            Paragraph total = new Paragraph();
            total.add(Chunk.NEWLINE);
            total.add("Total de usuarios nacidos en el año más común: " + totalUsuarios);
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            archivo.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
