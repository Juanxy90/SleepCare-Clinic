package Vista;

import Modelo.Diagnostico;
import Modelo.DiagnosticoDAO;
import java.util.List;
import Modelo.Medico;
import Modelo.MedicoDAO;
import Modelo.Paciente;
import Modelo.PacienteDAO;
import Modelo.Tecnico;
import Modelo.TecnicoDAO;
import Modelo.Tratamiento;
import Modelo.TratamientoDAO;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.event.*;
import java.awt.event.*;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.text.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import Reportes.Grafico;
import Reportes.Pdf;
import java.awt.Color;

public class Administrador extends javax.swing.JFrame {

    Medico md = new Medico();
    MedicoDAO medic = new MedicoDAO();
    Tecnico tc = new Tecnico();
    TecnicoDAO tec = new TecnicoDAO();
    Paciente pc = new Paciente();
    PacienteDAO pac = new PacienteDAO();
    Diagnostico dg = new Diagnostico();
    DiagnosticoDAO diag = new DiagnosticoDAO();
    Tratamiento tt = new Tratamiento();
    TratamientoDAO trat = new TratamientoDAO();
    DefaultTableModel modelo = new DefaultTableModel();

    public Administrador() {
        initComponents();
        this.setLocationRelativeTo(null);
        setTitle("Administrador | SleepCare Clinic");
        setResizable(false);
        jDateFechaNacimientoMedico.setDateFormatString("yyyy-MM-dd");
        jDateFechaNacimientoTecnico.setDateFormatString("yyyy-MM-dd");
        jDateFechaNacimientoPaciente.setDateFormatString("yyyy-MM-dd");
        txtAreaMotivoCita.setLineWrap(true);
        txtAreaMotivoCita.setWrapStyleWord(true);
        txtAreaDescripcionDiagnostico.setLineWrap(true);
        txtAreaDescripcionDiagnostico.setWrapStyleWord(true);
        txtAreaDescripcionTratamiento.setLineWrap(true);
        txtAreaDescripcionTratamiento.setWrapStyleWord(true);

        // Llenar combo box de sexo
        cboxSexoMedico.addItem("Masculino");
        cboxSexoMedico.addItem("Femenino");
        cboxSexoMedico.setSelectedIndex(-1);
        
        jTextArea1.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente
        jTextArea2.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente 
        jTextArea3.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente        
        jTextArea4.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente

        // Filtros para permitir solo números
        ((AbstractDocument) txtDocumentoMedico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtNumeroLicenciaMedico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtTelefonoMedico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtBuscarMedico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Buscar médico por documento al presionar Enter
        txtBuscarMedico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String doc = txtBuscarMedico.getText().trim();
                if (!doc.isEmpty()) {
                    Medico encontrado = medic.buscarPorDocumento(doc);
                    if (encontrado != null) {
                        limpiarTable(); // Limpia tabla actual

                        // Crear un nuevo modelo no editable
                        DefaultTableModel nuevoModelo = new DefaultTableModel(null, new Object[]{
                            "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre",
                            "P. Apellido", "S. Apellido", "Documento", "N- Licencia",
                            "Teléfono", "F. Nacimiento", "Sexo"
                        }) {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false; // Desactiva la edición
                            }
                        };

                        // Crear la fila con los datos del médico encontrado
                        Object[] fila = {
                            encontrado.getUsuario(),
                            encontrado.getContrasenia(),
                            encontrado.getCorreo(),
                            encontrado.getPrimerNombre(),
                            encontrado.getSegundoNombre(),
                            encontrado.getPrimerApellido(),
                            encontrado.getSegundoApellido(),
                            encontrado.getDocumento(),
                            encontrado.getNumeroLicencia(),
                            encontrado.getTelefono(),
                            encontrado.getFechaNacimiento(),
                            encontrado.getSexo()
                        };

                        nuevoModelo.addRow(fila); // Agrega la fila al nuevo modelo

                        // Asigna el nuevo modelo a la tabla
                        tableMedico.setModel(nuevoModelo);

                        // Selecciona la primera fila
                        tableMedico.setRowSelectionInterval(0, 0);

                        // Simula clic para llenar los campos
                        tableMedicoMouseClicked(new java.awt.event.MouseEvent(
                                tableMedico, 0, 0, 0, 0, 0, 1, false
                        ));
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Médico no encontrado.",
                                "Búsqueda fallida",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    limpiarTable(); // Si está vacío, limpia la tabla
                    listarMedico(); // Muestra todos los médicos
                }
            }
        });

        // Limpiar campos al cambiar de pestaña (si se cambia desde la pestaña de médicos)
        jTabbedPane1.addChangeListener(new ChangeListener() {
            private int previousTabIndex = jTabbedPane1.getSelectedIndex();

            @Override
            public void stateChanged(ChangeEvent e) {
                int currentIndex = jTabbedPane1.getSelectedIndex();
                // Limpiar si cambias a una pestaña diferente
                if (previousTabIndex != currentIndex) {
                    limpiarMedico();
                    limpiarTecnico();
                    limpiarPaciente();
                    limpiarDiagnostico();
                    limpiarTratamiento();
                }
                previousTabIndex = currentIndex;
            }
        });

        // Llenar combo box de sexo
        cboxSexoTecnico.addItem("Masculino");
        cboxSexoTecnico.addItem("Femenino");
        cboxSexoTecnico.setSelectedIndex(-1);

        // Filtros para permitir solo números
        ((AbstractDocument) txtDocumentoTecnico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtNumeroCredencialTecnico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtTelefonoTecnico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtBuscarTecnico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Buscar técnico por documento al presionar Enter
        txtBuscarTecnico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String doc = txtBuscarTecnico.getText().trim();
                if (!doc.isEmpty()) {
                    Tecnico encontrado = tec.buscarPorDocumento(doc);
                    if (encontrado != null) {
                        limpiarTable(); // Limpia tabla actual

                        // Crear un nuevo modelo no editable
                        DefaultTableModel nuevoModelo = new DefaultTableModel(null, new Object[]{
                            "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre",
                            "P. Apellido", "S. Apellido", "Documento", "N- Credencial",
                            "Teléfono", "F. Nacimiento", "Sexo"
                        }) {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false; // Desactiva la edición
                            }
                        };

                        // Crear la fila con los datos del técnico encontrado
                        Object[] fila = {
                            encontrado.getUsuario(),
                            encontrado.getContrasenia(),
                            encontrado.getCorreo(),
                            encontrado.getPrimerNombre(),
                            encontrado.getSegundoNombre(),
                            encontrado.getPrimerApellido(),
                            encontrado.getSegundoApellido(),
                            encontrado.getDocumento(),
                            encontrado.getNumeroCredencial(),
                            encontrado.getTelefono(),
                            encontrado.getFechaNacimiento(),
                            encontrado.getSexo()
                        };

                        nuevoModelo.addRow(fila); // Agrega la fila al nuevo modelo

                        // Asigna el nuevo modelo a la tabla
                        tableTecnico.setModel(nuevoModelo);

                        // Selecciona la primera fila
                        tableTecnico.setRowSelectionInterval(0, 0);

                        // Simula clic para llenar los campos
                        tableTecnicoMouseClicked(new java.awt.event.MouseEvent(
                                tableTecnico, 0, 0, 0, 0, 0, 1, false
                        ));
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Técnico no encontrado.",
                                "Búsqueda fallida",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    limpiarTable(); // Si está vacío, limpia la tabla
                    listarTecnico(); // Muestra todos los técnicos
                }
            }
        });

        // Llenar combo box de sexo
        cboxSexoPaciente.addItem("Masculino");
        cboxSexoPaciente.addItem("Femenino");
        cboxSexoPaciente.setSelectedIndex(-1);

        // Filtros para permitir solo números
        ((AbstractDocument) txtDocumentoPaciente.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtTelefonoPaciente.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtBuscarPaciente.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Buscar paciente por documento al presionar Enter
        txtBuscarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String doc = txtBuscarPaciente.getText().trim();
                if (!doc.isEmpty()) {
                    Paciente encontrado = pac.buscarPorDocumento(doc);
                    if (encontrado != null) {
                        limpiarTable(); // Limpia tabla actual

                        // Crear un nuevo modelo no editable
                        DefaultTableModel nuevoModelo = new DefaultTableModel(null, new Object[]{
                            "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre",
                            "P. Apellido", "S. Apellido", "Documento", "Teléfono",
                            "F. Nacimiento", "Sexo"
                        }) {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false; // Desactiva la edición
                            }
                        };

                        // Crear la fila con los datos del paciente encontrado
                        Object[] fila = {
                            encontrado.getUsuario(),
                            encontrado.getContrasenia(),
                            encontrado.getCorreo(),
                            encontrado.getPrimerNombre(),
                            encontrado.getSegundoNombre(),
                            encontrado.getPrimerApellido(),
                            encontrado.getSegundoApellido(),
                            encontrado.getDocumento(),
                            encontrado.getTelefono(),
                            encontrado.getFechaNacimiento(),
                            encontrado.getSexo()
                        };

                        nuevoModelo.addRow(fila); // Agrega la fila al nuevo modelo

                        // Asigna el nuevo modelo a la tabla
                        tablePaciente.setModel(nuevoModelo);

                        // Selecciona la primera fila
                        tablePaciente.setRowSelectionInterval(0, 0);

                        // Simula clic para llenar los campos
                        tablePacienteMouseClicked(new java.awt.event.MouseEvent(
                                tablePaciente, 0, 0, 0, 0, 0, 1, false
                        ));
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Paciente no encontrado.",
                                "Búsqueda fallida",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    limpiarTable(); // Si está vacío, limpia la tabla
                    listarPaciente(); // Muestra todos los pacientes
                }
            }
        });

        // Filtros para permitir solo números
        ((AbstractDocument) txtIdDiagnostico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtBuscarDiagnostico.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Buscar diagnóstico por id al presionar Enter
        txtBuscarDiagnostico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String doc = txtBuscarDiagnostico.getText().trim();
                if (!doc.isEmpty()) {
                    Diagnostico encontrado = diag.buscarPorId(doc);
                    if (encontrado != null) {
                        limpiarTable(); // Limpia tabla actual

                        // Crear un nuevo modelo no editable
                        DefaultTableModel nuevoModelo = new DefaultTableModel(null, new Object[]{
                            "ID", "Nombre", "Descripción"
                        }) {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false; // Desactiva la edición
                            }
                        };

                        // Crear la fila con los datos del diagnóstico encontrado
                        Object[] fila = {
                            encontrado.getId(),
                            encontrado.getNombre(),
                            encontrado.getDescripcion()
                        };

                        nuevoModelo.addRow(fila); // Agrega la fila al nuevo modelo

                        // Asigna el nuevo modelo a la tabla
                        tableDiagnostico.setModel(nuevoModelo);

                        // Selecciona la primera fila
                        tableDiagnostico.setRowSelectionInterval(0, 0);

                        // Simula clic para llenar los campos
                        tableDiagnosticoMouseClicked(new java.awt.event.MouseEvent(
                                tableDiagnostico, 0, 0, 0, 0, 0, 1, false
                        ));
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Diagnóstico no encontrado.",
                                "Búsqueda fallida",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    limpiarTable(); // Si está vacío, limpia la tabla
                    listarDiagnostico(); // Muestra todos los diagnósticos
                }
            }
        });

        // Filtros para permitir solo números
        ((AbstractDocument) txtIdTratamiento.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) txtBuscarTratamiento.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Buscar tratamiento por id al presionar Enter
        txtBuscarTratamiento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String doc = txtBuscarTratamiento.getText().trim();
                if (!doc.isEmpty()) {
                    Tratamiento encontrado = trat.buscarPorId(doc);
                    if (encontrado != null) {
                        limpiarTable(); // Limpia tabla actual

                        // Crear un nuevo modelo no editable
                        DefaultTableModel nuevoModelo = new DefaultTableModel(null, new Object[]{
                            "ID", "Nombre", "Descripción"
                        }) {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false; // Desactiva la edición
                            }
                        };

                        // Crear la fila con los datos del tratamiento encontrado
                        Object[] fila = {
                            encontrado.getId(),
                            encontrado.getNombre(),
                            encontrado.getDescripcion()
                        };

                        nuevoModelo.addRow(fila); // Agrega la fila al nuevo modelo

                        // Asigna el nuevo modelo a la tabla
                        tableTratamiento.setModel(nuevoModelo);

                        // Selecciona la primera fila
                        tableTratamiento.setRowSelectionInterval(0, 0);

                        // Simula clic para llenar los campos
                        tableTratamientoMouseClicked(new java.awt.event.MouseEvent(
                                tableTratamiento, 0, 0, 0, 0, 0, 1, false
                        ));
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Tratamiento no encontrado.",
                                "Búsqueda fallida",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    limpiarTable(); // Si está vacío, limpia la tabla
                    listarTratamiento(); // Muestra todos los diagnósticos
                }
            }
        });
    }

    public void listarMedico() {
        List<Medico> listarMd = medic.listarMedico();

        DefaultTableModel modelo = new DefaultTableModel(null, new Object[]{
            "Usuario", "Contraseña", "Correo", "P. Nombre", "S.Nombre",
            "P. Apellido", "S. Apellido", "Documento", "N- Licencia",
            "Teléfono", "F. Nacimiento", "Sexo"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita la edición en todas las celdas
            }
        };

        Object[] obj = new Object[12];
        for (int i = 0; i < listarMd.size(); i++) {
            obj[0] = listarMd.get(i).getUsuario();
            obj[1] = listarMd.get(i).getContrasenia();
            obj[2] = listarMd.get(i).getCorreo();
            obj[3] = listarMd.get(i).getPrimerNombre();
            obj[4] = listarMd.get(i).getSegundoNombre();
            obj[5] = listarMd.get(i).getPrimerApellido();
            obj[6] = listarMd.get(i).getSegundoApellido();
            obj[7] = listarMd.get(i).getDocumento();
            obj[8] = listarMd.get(i).getNumeroLicencia();
            obj[9] = listarMd.get(i).getTelefono();
            obj[10] = listarMd.get(i).getFechaNacimiento();
            obj[11] = listarMd.get(i).getSexo();
            modelo.addRow(obj);
        }

        tableMedico.setModel(modelo);
    }

    public void listarTecnico() {
        List<Tecnico> listarTc = tec.listarTecnico();

        DefaultTableModel modelo = new DefaultTableModel(null, new Object[]{
            "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre",
            "P. Apellido", "S. Apellido", "Documento", "N- Credencial",
            "Teléfono", "F. Nacimiento", "Sexo"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactiva la edición en todas las celdas
            }
        };

        Object[] obj = new Object[12];
        for (int i = 0; i < listarTc.size(); i++) {
            obj[0] = listarTc.get(i).getUsuario();
            obj[1] = listarTc.get(i).getContrasenia();
            obj[2] = listarTc.get(i).getCorreo();
            obj[3] = listarTc.get(i).getPrimerNombre();
            obj[4] = listarTc.get(i).getSegundoNombre();
            obj[5] = listarTc.get(i).getPrimerApellido();
            obj[6] = listarTc.get(i).getSegundoApellido();
            obj[7] = listarTc.get(i).getDocumento();
            obj[8] = listarTc.get(i).getNumeroCredencial();
            obj[9] = listarTc.get(i).getTelefono();
            obj[10] = listarTc.get(i).getFechaNacimiento();
            obj[11] = listarTc.get(i).getSexo();
            modelo.addRow(obj);
        }

        tableTecnico.setModel(modelo);
    }

    public void listarPaciente() {
        List<Paciente> listarPc = pac.listarPaciente();

        DefaultTableModel modelo = new DefaultTableModel(null, new Object[]{
            "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre",
            "P. Apellido", "S. Apellido", "Documento",
            "Teléfono", "F. Nacimiento", "Sexo"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita la edición en todas las celdas
            }
        };

        Object[] obj = new Object[11];
        for (int i = 0; i < listarPc.size(); i++) {
            obj[0] = listarPc.get(i).getUsuario();
            obj[1] = listarPc.get(i).getContrasenia();
            obj[2] = listarPc.get(i).getCorreo();
            obj[3] = listarPc.get(i).getPrimerNombre();
            obj[4] = listarPc.get(i).getSegundoNombre();
            obj[5] = listarPc.get(i).getPrimerApellido();
            obj[6] = listarPc.get(i).getSegundoApellido();
            obj[7] = listarPc.get(i).getDocumento();
            obj[8] = listarPc.get(i).getTelefono();
            obj[9] = listarPc.get(i).getFechaNacimiento();
            obj[10] = listarPc.get(i).getSexo();
            modelo.addRow(obj);
        }

        tablePaciente.setModel(modelo);
    }

    public void listarDiagnostico() {
        List<Diagnostico> listarDg = diag.listarDiagnostico();

        DefaultTableModel modelo = new DefaultTableModel(null, new Object[]{
            "ID", "Nombre", "Descripción"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactiva la edición en todas las celdas
            }
        };

        Object[] obj = new Object[3];
        for (int i = 0; i < listarDg.size(); i++) {
            obj[0] = listarDg.get(i).getId();
            obj[1] = listarDg.get(i).getNombre();
            obj[2] = listarDg.get(i).getDescripcion();
            modelo.addRow(obj);
        }

        tableDiagnostico.setModel(modelo);
    }

    public void listarTratamiento() {
        List<Tratamiento> listarTt = trat.listarTratamiento();

        DefaultTableModel modelo = new DefaultTableModel(null, new Object[]{
            "ID", "Nombre", "Descripción"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactiva la edición en todas las celdas
            }
        };

        Object[] obj = new Object[3];
        for (int i = 0; i < listarTt.size(); i++) {
            obj[0] = listarTt.get(i).getId();
            obj[1] = listarTt.get(i).getNombre();
            obj[2] = listarTt.get(i).getDescripcion();
            modelo.addRow(obj);
        }

        tableTratamiento.setModel(modelo);
    }

    public void limpiarTable() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel0 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnGestionarTecnicos = new javax.swing.JButton();
        btnGestionarPacientes = new javax.swing.JButton();
        btnGestionarCitas = new javax.swing.JButton();
        btnGestionarCatalogoDiagnosticos = new javax.swing.JButton();
        btnGestionarCatalogoTratamientos = new javax.swing.JButton();
        btnGestionarCatalogoDispositivos = new javax.swing.JButton();
        btnGenerarReportes = new javax.swing.JButton();
        btnPerfilAdmin = new javax.swing.JButton();
        btnGestionarMedicos = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtPrimerApellidoMedico = new javax.swing.JTextField();
        txtSegundoNombreMedico = new javax.swing.JTextField();
        txtCorreoMedico = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableMedico = new javax.swing.JTable();
        btnGuardarMedico = new javax.swing.JButton();
        btnActualizarMedico = new javax.swing.JButton();
        btnLimpiarMedico = new javax.swing.JButton();
        bntEliminarMedico = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtContraseniaMedico = new javax.swing.JTextField();
        txtPrimerNombreMedico = new javax.swing.JTextField();
        cboxSexoMedico = new javax.swing.JComboBox<>();
        txtUsuarioMedico = new javax.swing.JTextField();
        txtSegundoApellidoMedico = new javax.swing.JTextField();
        txtNumeroLicenciaMedico = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtDocumentoMedico = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtBuscarMedico = new javax.swing.JTextField();
        txtTelefonoMedico = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jDateFechaNacimientoMedico = new com.toedter.calendar.JDateChooser();
        jLabel77 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtPrimerApellidoTecnico = new javax.swing.JTextField();
        txtSegundoNombreTecnico = new javax.swing.JTextField();
        txtCorreoTecnico = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableTecnico = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtContraseniaTecnico = new javax.swing.JTextField();
        txtPrimerNombreTecnico = new javax.swing.JTextField();
        txtUsuarioTecnico = new javax.swing.JTextField();
        txtSegundoApellidoTecnico = new javax.swing.JTextField();
        txtTelefonoTecnico = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtDocumentoTecnico = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        cboxSexoTecnico = new javax.swing.JComboBox<>();
        txtNumeroCredencialTecnico = new javax.swing.JTextField();
        txtBuscarTecnico = new javax.swing.JTextField();
        btnGuardarTecnico = new javax.swing.JButton();
        btnActualizarTecnico = new javax.swing.JButton();
        btnLimpiarTecnico = new javax.swing.JButton();
        bntEliminarTecnico = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jDateFechaNacimientoTecnico = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtPrimerApellidoPaciente = new javax.swing.JTextField();
        txtSegundoNombrePaciente = new javax.swing.JTextField();
        txtCorreoPaciente = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        tablePaciente = new javax.swing.JTable();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        txtContraseniaPaciente = new javax.swing.JTextField();
        txtPrimerNombrePaciente = new javax.swing.JTextField();
        txtUsuarioPaciente = new javax.swing.JTextField();
        txtSegundoApellidoPaciente = new javax.swing.JTextField();
        txtTelefonoPaciente = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        txtDocumentoPaciente = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        cboxSexoPaciente = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        txtBuscarPaciente = new javax.swing.JTextField();
        btnGuardarPaciente = new javax.swing.JButton();
        btnLimpiarPaciente = new javax.swing.JButton();
        btnActualizarPaciente = new javax.swing.JButton();
        bntEliminarPaciente = new javax.swing.JButton();
        jDateFechaNacimientoPaciente = new com.toedter.calendar.JDateChooser();
        jLabel79 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableCita = new javax.swing.JTable();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        txtPacienteCita = new javax.swing.JTextField();
        txtIdCita = new javax.swing.JTextField();
        txtMedicoCita = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        bntEliminarCita = new javax.swing.JButton();
        btnActualizarCita = new javax.swing.JButton();
        btnLimpiarCita = new javax.swing.JButton();
        btnGuardarCita = new javax.swing.JButton();
        txtBuscarCita = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jDateFechaCita = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaMotivoCita = new javax.swing.JTextArea();
        jSpinHoraCita = new com.toedter.components.JSpinField();
        jSpinMinutoCita = new com.toedter.components.JSpinField();
        jPanel7 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tableDiagnostico = new javax.swing.JTable();
        txtIdDiagnostico = new javax.swing.JTextField();
        txtNombreDiagnostico = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        bntEliminarDiagnostico = new javax.swing.JButton();
        btnGuardarDiagnostico = new javax.swing.JButton();
        btnLimpiarDiagnostico = new javax.swing.JButton();
        btnActualizarDiagnostico = new javax.swing.JButton();
        txtBuscarDiagnostico = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaDescripcionDiagnostico = new javax.swing.JTextArea();
        txtUsuarioMedico1 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tableTratamiento = new javax.swing.JTable();
        txtBuscarTratamiento = new javax.swing.JTextField();
        btnGuardarTratamiento = new javax.swing.JButton();
        btnLimpiarTratamiento = new javax.swing.JButton();
        btnActualizarTratamiento = new javax.swing.JButton();
        bntEliminarTratamiento = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        txtNombreTratamiento = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        txtIdTratamiento = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaDescripcionTratamiento = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        txtIdDispositivo = new javax.swing.JTextField();
        txtMarcaDispositivo = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        txtModeloDispositivo = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtBuscarDispositivo = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        btnGuardarDispositivo = new javax.swing.JButton();
        btnLimpiarDispositivo = new javax.swing.JButton();
        btnActualizarDispositivo = new javax.swing.JButton();
        bntEliminarDispositivo = new javax.swing.JButton();
        jLabel83 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        btnGenerarReporteGraficoCantidadPacienteSexo = new javax.swing.JButton();
        btnGenerarReportePdfCantidadPacienteSexo = new javax.swing.JButton();
        btnGenerarReporteGraficoCantidadTecnicoSexo = new javax.swing.JButton();
        btnGenerarReportePdfCantidadTecnicoSexo = new javax.swing.JButton();
        btnGenerarReporteGraficoCantidadMedicoSexo = new javax.swing.JButton();
        btnGenerarReportePdfCantidadMedicoSexo = new javax.swing.JButton();
        btnGenerarReporteGraficoCantidadUsuarioSexo = new javax.swing.JButton();
        btnGenerarReportePdfCantidadUsuarioSexo1 = new javax.swing.JButton();
        jLabel85 = new javax.swing.JLabel();
        btnListarPacientes = new javax.swing.JButton();
        btnListarTecnicos = new javax.swing.JButton();
        btnListarMedicos = new javax.swing.JButton();
        btnListarUsuarios = new javax.swing.JButton();
        jLabel86 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        btnGenerarListaTelefonosUsuariosHombres = new javax.swing.JButton();
        jLabel93 = new javax.swing.JLabel();
        btnGenerarListaTelefonosUsuariosMujeres = new javax.swing.JButton();
        jLabel94 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btnGenerarListaUsuariosAnioNacimientoMasComun = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        btnListarDiagnosticosMasCorto = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        btnGenerarListadoUsuariosOrdenadosFechaNacimiento = new javax.swing.JButton();
        jLabel84 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        btnGenerarListaUsuariosHombres = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        btnGenerarListaUsuariosMujeres = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        btnGenerarCantidadGraficoEdadesUsuarios = new javax.swing.JButton();
        btnGenerarCantidadPdfEdadesUsuarios = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        btnGenerarListaCantidadUsuariosPrimerNombreMasComun = new javax.swing.JButton();
        jLabel95 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        btnGenerarListaCantidadUsuariosSegundoNombreMasComun = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        btnGenerarReportePdfCantidadUsuarioSexo9 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel0.setBackground(new java.awt.Color(51, 51, 51));
        jPanel0.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 153));

        btnGestionarTecnicos.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGestionarTecnicos.setText("Gestionar Técnicos");
        btnGestionarTecnicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarTecnicosActionPerformed(evt);
            }
        });

        btnGestionarPacientes.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGestionarPacientes.setText("Gestionar Pacientes");
        btnGestionarPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarPacientesActionPerformed(evt);
            }
        });

        btnGestionarCitas.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGestionarCitas.setText("Gestionar Citas");
        btnGestionarCitas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarCitasActionPerformed(evt);
            }
        });

        btnGestionarCatalogoDiagnosticos.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGestionarCatalogoDiagnosticos.setText("Gestionar Catálogo Diagnósticos");
        btnGestionarCatalogoDiagnosticos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarCatalogoDiagnosticosActionPerformed(evt);
            }
        });

        btnGestionarCatalogoTratamientos.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGestionarCatalogoTratamientos.setText("Gestionar Catálogo Tratamientos");
        btnGestionarCatalogoTratamientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarCatalogoTratamientosActionPerformed(evt);
            }
        });

        btnGestionarCatalogoDispositivos.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGestionarCatalogoDispositivos.setText("Gestionar Catálogo Dispositivos");
        btnGestionarCatalogoDispositivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarCatalogoDispositivosActionPerformed(evt);
            }
        });

        btnGenerarReportes.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGenerarReportes.setText("Generar Reportes");
        btnGenerarReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReportesActionPerformed(evt);
            }
        });

        btnPerfilAdmin.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnPerfilAdmin.setText("Perfil");
        btnPerfilAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilAdminActionPerformed(evt);
            }
        });

        btnGestionarMedicos.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 14)); // NOI18N
        btnGestionarMedicos.setText("Gestionar Médicos");
        btnGestionarMedicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarMedicosActionPerformed(evt);
            }
        });

        jPanel14.setBackground(new java.awt.Color(51, 51, 51));
        jPanel14.setPreferredSize(new java.awt.Dimension(1070, 80));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGestionarCitas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGestionarMedicos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGestionarTecnicos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGestionarPacientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGestionarCatalogoDiagnosticos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGestionarCatalogoTratamientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGestionarCatalogoDispositivos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGenerarReportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnPerfilAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarMedicos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarCitas, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarCatalogoDiagnosticos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarCatalogoTratamientos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarCatalogoDispositivos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGenerarReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPerfilAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/logo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(348, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(344, 344, 344))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(57, 57, 57))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab9", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel20.setText("N- Licencia:");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 100, -1));

        jLabel25.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel25.setText("Primer Nombre:");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, -1));

        jLabel26.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel26.setText("Segundo Nombre:");
        jPanel3.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));
        jPanel3.add(txtPrimerApellidoMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 210, -1));
        jPanel3.add(txtSegundoNombreMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 210, -1));
        jPanel3.add(txtCorreoMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 210, -1));

        tableMedico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre", "P. Apellido", "S. Apellido", "Documento", "N- Licencia", "Teléfono", "F. Nacimiento", "Sexo"
            }
        ));
        tableMedico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMedicoMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableMedico);

        jPanel3.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 650, 490));

        btnGuardarMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarMedico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarMedicoActionPerformed(evt);
            }
        });
        jPanel3.add(btnGuardarMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 40, 40));

        btnActualizarMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarMedico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarMedicoActionPerformed(evt);
            }
        });
        jPanel3.add(btnActualizarMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 40, 40));

        btnLimpiarMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnLimpiarMedico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarMedicoActionPerformed(evt);
            }
        });
        jPanel3.add(btnLimpiarMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 40, 40));

        bntEliminarMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        bntEliminarMedico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntEliminarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarMedicoActionPerformed(evt);
            }
        });
        jPanel3.add(bntEliminarMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 40, 40));

        jLabel27.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel27.setText("Primer Apellido:");
        jPanel3.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel28.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel28.setText("Segundo Apellido:");
        jPanel3.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        jLabel29.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel29.setText("Buscar:");
        jPanel3.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        jLabel30.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel30.setText("Correo:");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel31.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel31.setText("Usuario:");
        jPanel3.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel32.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel32.setText("Contraseña:");
        jPanel3.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        jPanel3.add(txtContraseniaMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 210, -1));
        jPanel3.add(txtPrimerNombreMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 210, -1));

        cboxSexoMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxSexoMedicoActionPerformed(evt);
            }
        });
        jPanel3.add(cboxSexoMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 210, -1));
        jPanel3.add(txtUsuarioMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));
        jPanel3.add(txtSegundoApellidoMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 210, -1));
        jPanel3.add(txtNumeroLicenciaMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 210, -1));

        jLabel33.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel33.setText("Documento:");
        jPanel3.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, -1));

        txtDocumentoMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocumentoMedicoActionPerformed(evt);
            }
        });
        jPanel3.add(txtDocumentoMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 210, -1));

        jLabel21.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel21.setText("Teléfono:");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 100, -1));

        txtBuscarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarMedicoActionPerformed(evt);
            }
        });
        jPanel3.add(txtBuscarMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 150, -1));
        jPanel3.add(txtTelefonoMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, -1));

        jLabel60.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel60.setText("Fecha Nacimiento:");
        jPanel3.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));
        jPanel3.add(jDateFechaNacimientoMedico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 210, -1));

        jLabel77.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel77.setText("Sexo:");
        jPanel3.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jTabbedPane1.addTab("tab1", jPanel3);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel37.setText("N- Credencial:");
        jPanel4.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 100, -1));

        jLabel38.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel38.setText("Primer Nombre:");
        jPanel4.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, -1));

        jLabel39.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel39.setText("Segundo Nombre:");
        jPanel4.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));
        jPanel4.add(txtPrimerApellidoTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 210, -1));
        jPanel4.add(txtSegundoNombreTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 210, -1));
        jPanel4.add(txtCorreoTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 210, -1));

        tableTecnico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre", "P. Apellido", "S. Apellido", "Documento", "N- Credencial", "Teléfono", "F. Nacimiento", "Sexo"
            }
        ));
        tableTecnico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTecnicoMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tableTecnico);

        jPanel4.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 650, 490));

        jLabel40.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel40.setText("Primer Apellido:");
        jPanel4.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel41.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel41.setText("Segundo Apellido:");
        jPanel4.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        jLabel42.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel42.setText("Teléfono:");
        jPanel4.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        jLabel43.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel43.setText("Correo:");
        jPanel4.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel44.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel44.setText("Usuario:");
        jPanel4.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel45.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel45.setText("Contraseña:");
        jPanel4.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        jPanel4.add(txtContraseniaTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 210, -1));

        txtPrimerNombreTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrimerNombreTecnicoActionPerformed(evt);
            }
        });
        jPanel4.add(txtPrimerNombreTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 210, -1));
        jPanel4.add(txtUsuarioTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));
        jPanel4.add(txtSegundoApellidoTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 210, -1));
        jPanel4.add(txtTelefonoTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, -1));

        jLabel46.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel46.setText("Documento:");
        jPanel4.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, -1));
        jPanel4.add(txtDocumentoTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 210, -1));

        jLabel34.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel34.setText("Sexo:");
        jPanel4.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jPanel4.add(cboxSexoTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 210, -1));
        jPanel4.add(txtNumeroCredencialTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 210, -1));

        txtBuscarTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarTecnicoActionPerformed(evt);
            }
        });
        jPanel4.add(txtBuscarTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 150, -1));

        btnGuardarTecnico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarTecnico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarTecnicoActionPerformed(evt);
            }
        });
        jPanel4.add(btnGuardarTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 40, 40));

        btnActualizarTecnico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarTecnico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarTecnicoActionPerformed(evt);
            }
        });
        jPanel4.add(btnActualizarTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 40, 40));

        btnLimpiarTecnico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnLimpiarTecnico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTecnicoActionPerformed(evt);
            }
        });
        jPanel4.add(btnLimpiarTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 40, 40));

        bntEliminarTecnico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        bntEliminarTecnico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntEliminarTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarTecnicoActionPerformed(evt);
            }
        });
        jPanel4.add(bntEliminarTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 40, 40));

        jLabel61.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel61.setText("Buscar:");
        jPanel4.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        jLabel78.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel78.setText("Fecha Nacimiento:");
        jPanel4.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));
        jPanel4.add(jDateFechaNacimientoTecnico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 210, -1));

        jTabbedPane1.addTab("tab2", jPanel4);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel65.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel65.setText("Primer Nombre:");
        jPanel5.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 120, -1));

        jLabel66.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel66.setText("Segundo Nombre:");
        jPanel5.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));
        jPanel5.add(txtPrimerApellidoPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, 210, -1));
        jPanel5.add(txtSegundoNombrePaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 210, -1));
        jPanel5.add(txtCorreoPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 210, -1));

        tablePaciente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Usuario", "Contraseña", "Correo", "P. Nombre", "S. Nombre", "P. Apellido", "S. Apellido", "Documento", "Teléfono", "F. Nacimiento", "Sexo"
            }
        ));
        tablePaciente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePacienteMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tablePaciente);

        jPanel5.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 650, 490));

        jLabel67.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel67.setText("Primer Apellido:");
        jPanel5.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        jLabel68.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel68.setText("Segundo Apellido:");
        jPanel5.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        jLabel69.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel69.setText("Teléfono:");
        jPanel5.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        jLabel70.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel70.setText("Correo:");
        jPanel5.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel71.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel71.setText("Usuario:");
        jPanel5.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel72.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel72.setText("Contraseña:");
        jPanel5.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        jPanel5.add(txtContraseniaPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 210, -1));
        jPanel5.add(txtPrimerNombrePaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 210, -1));
        jPanel5.add(txtUsuarioPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));
        jPanel5.add(txtSegundoApellidoPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 210, -1));
        jPanel5.add(txtTelefonoPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, -1));

        jLabel73.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel73.setText("Documento:");
        jPanel5.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 100, -1));
        jPanel5.add(txtDocumentoPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 210, -1));

        jLabel74.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel74.setText("Sexo:");
        jPanel5.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jPanel5.add(cboxSexoPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 210, -1));

        jLabel62.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel62.setText("Buscar:");
        jPanel5.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        txtBuscarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarPacienteActionPerformed(evt);
            }
        });
        jPanel5.add(txtBuscarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 150, -1));

        btnGuardarPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarPaciente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPacienteActionPerformed(evt);
            }
        });
        jPanel5.add(btnGuardarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 40, 40));

        btnLimpiarPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnLimpiarPaciente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarPacienteActionPerformed(evt);
            }
        });
        jPanel5.add(btnLimpiarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 40, 40));

        btnActualizarPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarPaciente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarPacienteActionPerformed(evt);
            }
        });
        jPanel5.add(btnActualizarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 40, 40));

        bntEliminarPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        bntEliminarPaciente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntEliminarPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarPacienteActionPerformed(evt);
            }
        });
        jPanel5.add(bntEliminarPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 40, 40));
        jPanel5.add(jDateFechaNacimientoPaciente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 210, -1));

        jLabel79.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel79.setText("Fecha Nacimiento:");
        jPanel5.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        jTabbedPane1.addTab("tab5", jPanel5);

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel47.setText("Hora:");
        jPanel6.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 120, -1));

        jLabel48.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel48.setText("Motivo:");
        jPanel6.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        tableCita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Paciente", "Fecha", "Hora", "Motivo", "Médico"
            }
        ));
        tableCita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCitaMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tableCita);

        jPanel6.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 650, 490));

        jLabel50.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel50.setText("Fecha:");
        jPanel6.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel51.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel51.setText("ID:");
        jPanel6.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel52.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel52.setText("Paciente:");
        jPanel6.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));
        jPanel6.add(txtPacienteCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 210, -1));
        jPanel6.add(txtIdCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));
        jPanel6.add(txtMedicoCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 210, -1));

        jLabel35.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel35.setText("Médico:");
        jPanel6.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        bntEliminarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        bntEliminarCita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntEliminarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarCitaActionPerformed(evt);
            }
        });
        jPanel6.add(bntEliminarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 40, 40));

        btnActualizarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarCita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarCitaActionPerformed(evt);
            }
        });
        jPanel6.add(btnActualizarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 40, 40));

        btnLimpiarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnLimpiarCita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCitaActionPerformed(evt);
            }
        });
        jPanel6.add(btnLimpiarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 40, 40));

        btnGuardarCita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarCita.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarCitaActionPerformed(evt);
            }
        });
        jPanel6.add(btnGuardarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 40, 40));

        txtBuscarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarCitaActionPerformed(evt);
            }
        });
        jPanel6.add(txtBuscarCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 150, -1));

        jLabel63.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel63.setText("Buscar:");
        jPanel6.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));
        jPanel6.add(jDateFechaCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 210, -1));

        txtAreaMotivoCita.setColumns(20);
        txtAreaMotivoCita.setRows(5);
        jScrollPane2.setViewportView(txtAreaMotivoCita);

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 210, 80));
        jPanel6.add(jSpinHoraCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 100, -1));
        jPanel6.add(jSpinMinutoCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 180, 100, -1));

        jTabbedPane1.addTab("tab3", jPanel6);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel53.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel53.setText("Nombre:");
        jPanel7.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 70, -1));

        tableDiagnostico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Descripción"
            }
        ));
        tableDiagnostico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDiagnosticoMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tableDiagnostico);

        jPanel7.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 650, 490));
        jPanel7.add(txtIdDiagnostico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));

        txtNombreDiagnostico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDiagnosticoActionPerformed(evt);
            }
        });
        jPanel7.add(txtNombreDiagnostico, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 210, -1));

        jLabel54.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel54.setText("ID:");
        jPanel7.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel36.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel36.setText("Descripción:");
        jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        bntEliminarDiagnostico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        bntEliminarDiagnostico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntEliminarDiagnostico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarDiagnosticoActionPerformed(evt);
            }
        });
        jPanel7.add(bntEliminarDiagnostico, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 40, 40));

        btnGuardarDiagnostico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarDiagnostico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarDiagnostico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarDiagnosticoActionPerformed(evt);
            }
        });
        jPanel7.add(btnGuardarDiagnostico, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 40, 40));

        btnLimpiarDiagnostico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnLimpiarDiagnostico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarDiagnostico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarDiagnosticoActionPerformed(evt);
            }
        });
        jPanel7.add(btnLimpiarDiagnostico, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 40, 40));

        btnActualizarDiagnostico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarDiagnostico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarDiagnostico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDiagnosticoActionPerformed(evt);
            }
        });
        jPanel7.add(btnActualizarDiagnostico, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 40, 40));

        txtBuscarDiagnostico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarDiagnosticoActionPerformed(evt);
            }
        });
        jPanel7.add(txtBuscarDiagnostico, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 150, -1));

        jLabel64.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel64.setText("Buscar:");
        jPanel7.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        txtAreaDescripcionDiagnostico.setColumns(20);
        txtAreaDescripcionDiagnostico.setRows(5);
        jScrollPane1.setViewportView(txtAreaDescripcionDiagnostico);

        jPanel7.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 210, 130));
        jPanel7.add(txtUsuarioMedico1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));

        jTabbedPane1.addTab("tab4", jPanel7);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableTratamiento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Descripción"
            }
        ));
        tableTratamiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTratamientoMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tableTratamiento);

        jPanel8.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 650, 490));

        txtBuscarTratamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarTratamientoActionPerformed(evt);
            }
        });
        jPanel8.add(txtBuscarTratamiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 150, -1));

        btnGuardarTratamiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarTratamiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarTratamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarTratamientoActionPerformed(evt);
            }
        });
        jPanel8.add(btnGuardarTratamiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 40, 40));

        btnLimpiarTratamiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnLimpiarTratamiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarTratamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarTratamientoActionPerformed(evt);
            }
        });
        jPanel8.add(btnLimpiarTratamiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 40, 40));

        btnActualizarTratamiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarTratamiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarTratamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarTratamientoActionPerformed(evt);
            }
        });
        jPanel8.add(btnActualizarTratamiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 40, 40));

        bntEliminarTratamiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        bntEliminarTratamiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntEliminarTratamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarTratamientoActionPerformed(evt);
            }
        });
        jPanel8.add(bntEliminarTratamiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 40, 40));

        jLabel75.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel75.setText("Buscar:");
        jPanel8.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        jLabel80.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel80.setText("Descripción:");
        jPanel8.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, -1, -1));

        txtNombreTratamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreTratamientoActionPerformed(evt);
            }
        });
        jPanel8.add(txtNombreTratamiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 210, -1));

        jLabel81.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel81.setText("Nombre:");
        jPanel8.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 70, -1));

        jLabel82.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel82.setText("ID:");
        jPanel8.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));
        jPanel8.add(txtIdTratamiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));

        txtAreaDescripcionTratamiento.setColumns(20);
        txtAreaDescripcionTratamiento.setRows(5);
        jScrollPane3.setViewportView(txtAreaDescripcionTratamiento);

        jPanel8.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 210, 130));

        jTabbedPane1.addTab("tab7", jPanel8);

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel9.add(txtIdDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 210, -1));

        txtMarcaDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarcaDispositivoActionPerformed(evt);
            }
        });
        jPanel9.add(txtMarcaDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, 210, -1));

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Marca", "Modelo"
            }
        ));
        jTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable10MouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTable10);

        jPanel9.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 650, 490));
        jPanel9.add(txtModeloDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 210, -1));

        jLabel49.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel49.setText("Modelo:");
        jPanel9.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jLabel56.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel56.setText("Marca:");
        jPanel9.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 70, -1));

        txtBuscarDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarDispositivoActionPerformed(evt);
            }
        });
        jPanel9.add(txtBuscarDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 150, -1));

        jLabel76.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel76.setText("Buscar:");
        jPanel9.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, -1, -1));

        btnGuardarDispositivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarDispositivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarDispositivoActionPerformed(evt);
            }
        });
        jPanel9.add(btnGuardarDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 40, 40));

        btnLimpiarDispositivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnLimpiarDispositivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiarDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarDispositivoActionPerformed(evt);
            }
        });
        jPanel9.add(btnLimpiarDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 40, 40));

        btnActualizarDispositivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarDispositivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDispositivoActionPerformed(evt);
            }
        });
        jPanel9.add(btnActualizarDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 40, 40));

        bntEliminarDispositivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        bntEliminarDispositivo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bntEliminarDispositivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntEliminarDispositivoActionPerformed(evt);
            }
        });
        jPanel9.add(bntEliminarDispositivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 40, 40));

        jLabel83.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel83.setText("ID:");
        jPanel9.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jTabbedPane1.addTab("tab6", jPanel9);

        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel87.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel87.setText("Cantidad de edades en los usuarios:");
        jPanel12.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, -1, -1));

        jLabel88.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel88.setText("Listado de usuarios hombres:");
        jPanel12.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        btnGenerarReporteGraficoCantidadPacienteSexo.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReporteGraficoCantidadPacienteSexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/torta.png"))); // NOI18N
        btnGenerarReporteGraficoCantidadPacienteSexo.setText("Pacientes");
        btnGenerarReporteGraficoCantidadPacienteSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteGraficoCantidadPacienteSexoActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReporteGraficoCantidadPacienteSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 180, 130, 50));

        btnGenerarReportePdfCantidadPacienteSexo.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReportePdfCantidadPacienteSexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarReportePdfCantidadPacienteSexo.setText("Pacientes");
        btnGenerarReportePdfCantidadPacienteSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReportePdfCantidadPacienteSexoActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReportePdfCantidadPacienteSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 130, 50));

        btnGenerarReporteGraficoCantidadTecnicoSexo.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReporteGraficoCantidadTecnicoSexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/torta.png"))); // NOI18N
        btnGenerarReporteGraficoCantidadTecnicoSexo.setText("Técnicos");
        btnGenerarReporteGraficoCantidadTecnicoSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteGraficoCantidadTecnicoSexoActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReporteGraficoCantidadTecnicoSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 130, 50));

        btnGenerarReportePdfCantidadTecnicoSexo.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReportePdfCantidadTecnicoSexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarReportePdfCantidadTecnicoSexo.setText("Técnicos");
        btnGenerarReportePdfCantidadTecnicoSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReportePdfCantidadTecnicoSexoActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReportePdfCantidadTecnicoSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 130, 50));

        btnGenerarReporteGraficoCantidadMedicoSexo.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReporteGraficoCantidadMedicoSexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/torta.png"))); // NOI18N
        btnGenerarReporteGraficoCantidadMedicoSexo.setText("Médicos");
        btnGenerarReporteGraficoCantidadMedicoSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteGraficoCantidadMedicoSexoActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReporteGraficoCantidadMedicoSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 130, 50));

        btnGenerarReportePdfCantidadMedicoSexo.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReportePdfCantidadMedicoSexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarReportePdfCantidadMedicoSexo.setText("Médicos");
        btnGenerarReportePdfCantidadMedicoSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReportePdfCantidadMedicoSexoActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReportePdfCantidadMedicoSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 130, 50));

        btnGenerarReporteGraficoCantidadUsuarioSexo.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReporteGraficoCantidadUsuarioSexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/torta.png"))); // NOI18N
        btnGenerarReporteGraficoCantidadUsuarioSexo.setText("General");
        btnGenerarReporteGraficoCantidadUsuarioSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteGraficoCantidadUsuarioSexoActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReporteGraficoCantidadUsuarioSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 130, 50));

        btnGenerarReportePdfCantidadUsuarioSexo1.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReportePdfCantidadUsuarioSexo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarReportePdfCantidadUsuarioSexo1.setText("General");
        btnGenerarReportePdfCantidadUsuarioSexo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReportePdfCantidadUsuarioSexo1ActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarReportePdfCantidadUsuarioSexo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 130, 50));

        jLabel85.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel85.setText("Cantidad de usuarios por sexo:");
        jPanel12.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));

        btnListarPacientes.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnListarPacientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnListarPacientes.setText("Pacientes");
        btnListarPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarPacientesActionPerformed(evt);
            }
        });
        jPanel12.add(btnListarPacientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 130, 50));

        btnListarTecnicos.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnListarTecnicos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnListarTecnicos.setText("Técnicos");
        btnListarTecnicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarTecnicosActionPerformed(evt);
            }
        });
        jPanel12.add(btnListarTecnicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 50));

        btnListarMedicos.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnListarMedicos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnListarMedicos.setText("Médicos");
        btnListarMedicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarMedicosActionPerformed(evt);
            }
        });
        jPanel12.add(btnListarMedicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 130, 50));

        btnListarUsuarios.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnListarUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnListarUsuarios.setText("General");
        btnListarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarUsuariosActionPerformed(evt);
            }
        });
        jPanel12.add(btnListarUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 50));

        jLabel86.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel86.setText("Listado de usuarios:");
        jPanel12.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel89.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel89.setText("Listado de usuarios cuyo año de nacimiento es el más común:");
        jPanel12.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 430, -1));

        jLabel92.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel92.setText("Listado de usuarios mujeres:");
        jPanel12.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, -1, -1));

        btnGenerarListaTelefonosUsuariosHombres.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaTelefonosUsuariosHombres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaTelefonosUsuariosHombres.setText(".pdf");
        btnGenerarListaTelefonosUsuariosHombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaTelefonosUsuariosHombresActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarListaTelefonosUsuariosHombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, 130, 50));

        jLabel93.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel93.setText("Listado de los télefonos de usuarios hombres:");
        jPanel12.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, -1, -1));

        btnGenerarListaTelefonosUsuariosMujeres.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaTelefonosUsuariosMujeres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaTelefonosUsuariosMujeres.setText(".pdf");
        btnGenerarListaTelefonosUsuariosMujeres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaTelefonosUsuariosMujeresActionPerformed(evt);
            }
        });
        jPanel12.add(btnGenerarListaTelefonosUsuariosMujeres, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 110, 130, 50));

        jLabel94.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel94.setText("Listado de los télefonos de usuarios mujeres:");
        jPanel12.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 90, -1, -1));

        jPanel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnGenerarListaUsuariosAnioNacimientoMasComun.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaUsuariosAnioNacimientoMasComun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaUsuariosAnioNacimientoMasComun.setText(".pdf");
        btnGenerarListaUsuariosAnioNacimientoMasComun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaUsuariosAnioNacimientoMasComunActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGenerarListaUsuariosAnioNacimientoMasComun, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(300, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(btnGenerarListaUsuariosAnioNacimientoMasComun, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 440, 80));

        jPanel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel91.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel91.setText("Listado de técnicos cuyo número de credencial termina en número impar: ");

        btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar.setText(".pdf");
        btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaPdfTecnicosUltimoNumeroTerminaImparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel91)
                    .addComponent(btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel91)
                .addGap(1, 1, 1)
                .addComponent(btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 410, 520, 80));

        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel90.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel90.setText("Listado de diagnósticos con el nombre más corto:");

        btnListarDiagnosticosMasCorto.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnListarDiagnosticosMasCorto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnListarDiagnosticosMasCorto.setText(".pdf");
        btnListarDiagnosticosMasCorto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarDiagnosticosMasCortoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel90)
                    .addComponent(btnListarDiagnosticosMasCorto, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel90)
                .addGap(1, 1, 1)
                .addComponent(btnListarDiagnosticosMasCorto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 440, 80));

        jPanel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnGenerarListadoUsuariosOrdenadosFechaNacimiento.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListadoUsuariosOrdenadosFechaNacimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListadoUsuariosOrdenadosFechaNacimiento.setText(".pdf");
        btnGenerarListadoUsuariosOrdenadosFechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListadoUsuariosOrdenadosFechaNacimientoActionPerformed(evt);
            }
        });

        jLabel84.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel84.setText("Listado de usuarios ordenados por fecha de nacimiento:");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84)
                    .addComponent(btnGenerarListadoUsuariosOrdenadosFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel84)
                .addGap(1, 1, 1)
                .addComponent(btnGenerarListadoUsuariosOrdenadosFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 440, 80));

        jPanel18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 226, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 150, 230));

        jPanel19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGenerarListaUsuariosHombres.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaUsuariosHombres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaUsuariosHombres.setText(".pdf");
        btnGenerarListaUsuariosHombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaUsuariosHombresActionPerformed(evt);
            }
        });
        jPanel19.add(btnGenerarListaUsuariosHombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, 50));

        jPanel12.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 290, 80));

        jPanel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGenerarListaUsuariosMujeres.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaUsuariosMujeres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaUsuariosMujeres.setText(".pdf");
        btnGenerarListaUsuariosMujeres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaUsuariosMujeresActionPerformed(evt);
            }
        });
        jPanel20.add(btnGenerarListaUsuariosMujeres, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, 50));

        jPanel12.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 90, 290, 80));

        jPanel21.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGenerarCantidadGraficoEdadesUsuarios.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarCantidadGraficoEdadesUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/torta.png"))); // NOI18N
        btnGenerarCantidadGraficoEdadesUsuarios.setText("Gráfico");
        btnGenerarCantidadGraficoEdadesUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarCantidadGraficoEdadesUsuariosActionPerformed(evt);
            }
        });
        jPanel21.add(btnGenerarCantidadGraficoEdadesUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 130, 50));

        btnGenerarCantidadPdfEdadesUsuarios.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarCantidadPdfEdadesUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarCantidadPdfEdadesUsuarios.setText(".pdf");
        btnGenerarCantidadPdfEdadesUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarCantidadPdfEdadesUsuariosActionPerformed(evt);
            }
        });
        jPanel21.add(btnGenerarCantidadPdfEdadesUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, 50));

        jPanel12.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 170, 290, 80));

        jPanel22.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnGenerarListaCantidadUsuariosPrimerNombreMasComun.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaCantidadUsuariosPrimerNombreMasComun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaCantidadUsuariosPrimerNombreMasComun.setText(".pdf");
        btnGenerarListaCantidadUsuariosPrimerNombreMasComun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaCantidadUsuariosPrimerNombreMasComunActionPerformed(evt);
            }
        });

        jLabel95.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel95.setText("Listado de la cantidad de usuarios con el primer nombre más común:");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel95)
                    .addComponent(btnGenerarListaCantidadUsuariosPrimerNombreMasComun, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel95)
                .addGap(1, 1, 1)
                .addComponent(btnGenerarListaCantidadUsuariosPrimerNombreMasComun, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 250, 520, 80));

        jPanel23.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 76, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 10, 320, 80));

        jPanel24.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 90, 320, -1));

        jPanel25.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 286, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 290, -1));

        jPanel26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel96.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel96.setText("Listado de la cantidad de usuarios con el segundo nombre más común:");

        btnGenerarListaCantidadUsuariosSegundoNombreMasComun.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarListaCantidadUsuariosSegundoNombreMasComun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarListaCantidadUsuariosSegundoNombreMasComun.setText(".pdf");
        btnGenerarListaCantidadUsuariosSegundoNombreMasComun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarListaCantidadUsuariosSegundoNombreMasComunActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel96)
                    .addComponent(btnGenerarListaCantidadUsuariosSegundoNombreMasComun, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jLabel96)
                .addGap(1, 1, 1)
                .addComponent(btnGenerarListaCantidadUsuariosSegundoNombreMasComun, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 330, 520, 80));

        jPanel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel97.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel97.setText("Listado de la cantidad de usuarios con el segundo nombre más común:");

        btnGenerarReportePdfCantidadUsuarioSexo9.setFont(new java.awt.Font("Vensim Sans Tamil", 1, 12)); // NOI18N
        btnGenerarReportePdfCantidadUsuarioSexo9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnGenerarReportePdfCantidadUsuarioSexo9.setText(".pdf");
        btnGenerarReportePdfCantidadUsuarioSexo9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReportePdfCantidadUsuarioSexo9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel97)
                    .addComponent(btnGenerarReportePdfCantidadUsuarioSexo9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel97)
                .addGap(1, 1, 1)
                .addComponent(btnGenerarReportePdfCantidadUsuarioSexo9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 330, 500, 80));

        jTabbedPane1.addTab("tab10", jPanel12);

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel99.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/logo2.png"))); // NOI18N
        jPanel10.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 10, -1, -1));

        jLabel100.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel100.setText("RUC: 26105-2025");
        jPanel10.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 60, -1, -1));

        jLabel101.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel101.setText("Teléfono: 6016068484");
        jPanel10.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, -1, -1));

        jLabel102.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel102.setText("www.sleepcareclinic.com");
        jPanel10.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 180, -1, -1));

        jScrollPane4.setBorder(null);
        jScrollPane4.setOpaque(false);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Nuestra Misión:\n\nBrindar atención médica especializada en medicina del sueño, basada en\nevidencia científica, tecnología avanzada y un enfoque humano y\npersonalizado.\n\nNuestra Visión:\n\nSer el centro líder en estudios y tratamiento de trastornos del sueño a\nnivel nacional, reconocido por nuestra excelencia médica y atención\nintegral.");
        jTextArea1.setBorder(null);
        jTextArea1.setOpaque(false);
        jScrollPane4.setViewportView(jTextArea1);

        jPanel10.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 270, 490, 220));

        jScrollPane6.setBorder(null);
        jScrollPane6.setOpaque(false);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jTextArea2.setRows(5);
        jTextArea2.setText("Nuestros Valores:\n\n- Compromiso con la salud       - Ética profesional\n- Calidad en la atención            - Innovación tecnológica\n- Empatía y respeto");
        jTextArea2.setBorder(null);
        jTextArea2.setOpaque(false);
        jScrollPane6.setViewportView(jTextArea2);

        jPanel10.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 440, 100));

        jLabel103.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel103.setText("Dirección: Carrera 25 # 44N - 50 | Armenia, Quindío");
        jPanel10.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 220, -1, -1));

        jLabel104.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel104.setText("contacto@sleepcareclinic.com");
        jPanel10.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 140, -1, -1));

        jLabel105.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jLabel105.setText("SleepCare Clinic");
        jPanel10.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, -1, -1));

        jScrollPane13.setBorder(null);
        jScrollPane13.setOpaque(false);

        jTextArea3.setEditable(false);
        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jTextArea3.setRows(5);
        jTextArea3.setText("¿Quiénes Somos?:\n\nSleepCare Clinic es una clínica especializada en el diagnóstico, tratamiento\ny seguimiento de trastornos del sueño. Desde nuestra fundación, hemos\nayudado a cientos de personas a mejorar su calidad de vida a través\nde un sueño saludable y reparador.\n\nContamos con un equipo multidisciplinario de médicos, especialistas en\nneurofisiología, técnicos en polisomnografía y profesionales de la salud\ncomprometidos con el bienestar del paciente.");
        jTextArea3.setBorder(null);
        jTextArea3.setOpaque(false);
        jScrollPane13.setViewportView(jTextArea3);

        jPanel10.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 500, 190));

        jScrollPane14.setBorder(null);
        jScrollPane14.setOpaque(false);

        jTextArea4.setEditable(false);
        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Vensim Sans Tamil", 0, 14)); // NOI18N
        jTextArea4.setRows(5);
        jTextArea4.setText("Nuestros Servicios:\n\n- Estudios de sueño (polisomnografía nocturna)\n- Diagnóstico de apnea del sueño\n- Tratamiento de insomnio, narcolepsia, parasomnias, entre otros\n- Monitoreo respiratorio domiciliario\n- Consulta especializada en medicina del sueño");
        jTextArea4.setBorder(null);
        jTextArea4.setOpaque(false);
        jScrollPane14.setViewportView(jTextArea4);

        jPanel10.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 440, 140));

        jTabbedPane1.addTab("tab8", jPanel10);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel0, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 260, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel0, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("tab1");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGestionarTecnicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarTecnicosActionPerformed
        // TODO add your handling code here:
        limpiarTable();
        listarTecnico();
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_btnGestionarTecnicosActionPerformed

    private void btnGestionarCatalogoTratamientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarCatalogoTratamientosActionPerformed
        limpiarTable();
        listarTratamiento();
        jTabbedPane1.setSelectedIndex(6);
    }//GEN-LAST:event_btnGestionarCatalogoTratamientosActionPerformed

    private void btnGestionarMedicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarMedicosActionPerformed
        // TODO add your handling code here:
        limpiarTable();
        listarMedico();
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btnGestionarMedicosActionPerformed

    private void btnGestionarPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarPacientesActionPerformed
        // TODO add your handling code here:
        limpiarTable();
        listarPaciente();
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_btnGestionarPacientesActionPerformed

    private void btnPerfilAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilAdminActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(9);
    }//GEN-LAST:event_btnPerfilAdminActionPerformed

    private void btnGestionarCitasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarCitasActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(4);

    }//GEN-LAST:event_btnGestionarCitasActionPerformed

    private void btnGestionarCatalogoDiagnosticosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarCatalogoDiagnosticosActionPerformed
        limpiarTable();
        listarDiagnostico();
        jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_btnGestionarCatalogoDiagnosticosActionPerformed

    private void btnGestionarCatalogoDispositivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarCatalogoDispositivosActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_btnGestionarCatalogoDispositivosActionPerformed

    private void btnGenerarReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReportesActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(8);
    }//GEN-LAST:event_btnGenerarReportesActionPerformed

    private void btnGenerarReporteGraficoCantidadPacienteSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteGraficoCantidadPacienteSexoActionPerformed
        Grafico grafico = new Grafico();
        grafico.mostrarGraficoPacientes();
    }//GEN-LAST:event_btnGenerarReporteGraficoCantidadPacienteSexoActionPerformed

    private void bntEliminarDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarDispositivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bntEliminarDispositivoActionPerformed

    private void btnActualizarDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDispositivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarDispositivoActionPerformed

    private void btnLimpiarDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarDispositivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarDispositivoActionPerformed

    private void btnGuardarDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarDispositivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarDispositivoActionPerformed

    private void txtBuscarDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarDispositivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarDispositivoActionPerformed

    private void jTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable10MouseClicked

    private void txtMarcaDispositivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarcaDispositivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMarcaDispositivoActionPerformed

    private void txtNombreTratamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreTratamientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreTratamientoActionPerformed

    private void bntEliminarTratamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarTratamientoActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtIdTratamiento.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar?");
            if (pregunta == JOptionPane.YES_OPTION) {
                String id = txtIdTratamiento.getText();
                boolean eliminado = trat.eliminarTratamiento(id);
                if (eliminado) {
                    limpiarTable();
                    limpiarTratamiento();
                    listarTratamiento();
                    JOptionPane.showMessageDialog(null, "¡Tratamiento eliminado con éxito!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el tratamiento.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un tratamiento para eliminar.");
        }
    }//GEN-LAST:event_bntEliminarTratamientoActionPerformed

    private void btnActualizarTratamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarTratamientoActionPerformed
        // Verificar si se ha seleccionado un tratamiento (documento no vacío)
        if (txtIdTratamiento.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un tratamiento para actualizar.");
            return;
        }

        // Verificar que todos los campos obligatorios estén completos
        if (txtIdTratamiento.getText().isEmpty()
                || txtNombreTratamiento.getText().isEmpty()
                || txtAreaDescripcionTratamiento.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Uno o más campos obligatorios están vacíos. Por favor, complete todos los campos.");
            return;
        }

        // Obtener el id original desde la tabla (asumiendo que el usuario ha seleccionado una fila)
        int filaSeleccionada = tableTratamiento.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un tratamiento válido para actualizar.");
            return;
        }

        // Obtener el id original desde la tabla (ajusta el índice 1 según la columna del documento en tu tabla)
        String idAnterior = tableTratamiento.getValueAt(filaSeleccionada, 0).toString();

        // Llenar el objeto tratamiento con los datos del formulario
        tt.setId(txtIdTratamiento.getText());
        tt.setNombre(txtNombreTratamiento.getText());
        tt.setDescripcion(txtAreaDescripcionTratamiento.getText());

        // Llamar al método modificarTratamiento con el documento anterior
        boolean resultado = trat.modificarTratamiento(tt, idAnterior);

        if (resultado) {
            limpiarTable();
            limpiarTratamiento();
            listarTratamiento();
            JOptionPane.showMessageDialog(null, "Tratamiento actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el tratamiento.");
        }
    }//GEN-LAST:event_btnActualizarTratamientoActionPerformed

    private void btnLimpiarTratamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTratamientoActionPerformed
        // Verifica si al menos un campo tiene contenido
        if (!"".equals(txtIdTratamiento.getText())
                || !"".equals(txtNombreTratamiento.getText())
                || !"".equals(txtAreaDescripcionTratamiento.getText())
                || !"".equals(txtBuscarTratamiento.getText())) {

            limpiarTratamiento();  // Limpia todos los campos
            JOptionPane.showMessageDialog(null, "¡Campos limpios con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos ya se encuentran vacíos.");
        }
    }//GEN-LAST:event_btnLimpiarTratamientoActionPerformed

    private void btnGuardarTratamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarTratamientoActionPerformed
        if (!"".equals(txtIdTratamiento.getText())
                && !"".equals(txtNombreTratamiento.getText())
                && !"".equals(txtAreaDescripcionTratamiento.getText())) {

            tt.setId(txtIdTratamiento.getText());
            tt.setNombre(txtNombreTratamiento.getText());
            tt.setDescripcion(txtAreaDescripcionTratamiento.getText());

            boolean exito = trat.registrarTratamiento(tt);
            if (exito) {
                limpiarTable();
                limpiarTratamiento();
                listarTratamiento();
                JOptionPane.showMessageDialog(null, "¡Tratamiento registrado con éxito!");
            } else {
                // El mensaje de error ya fue mostrado por el DAO
                System.out.println("Error: no se pudo registrar el tratamiento.");
                JOptionPane.showMessageDialog(null, "No se pudo registrar el tratamiento.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Uno o más campos están vacíos. Rectifique por favor.");
        }
    }//GEN-LAST:event_btnGuardarTratamientoActionPerformed

    private void txtBuscarTratamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarTratamientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarTratamientoActionPerformed

    private void tableTratamientoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTratamientoMouseClicked
        // TODO add your handling code here:

        int fila = tableTratamiento.rowAtPoint(evt.getPoint());
        txtIdTratamiento.setText(tableTratamiento.getValueAt(fila, 0).toString());
        txtNombreTratamiento.setText(tableTratamiento.getValueAt(fila, 1).toString());
        txtAreaDescripcionTratamiento.setText(tableTratamiento.getValueAt(fila, 2).toString());
    }//GEN-LAST:event_tableTratamientoMouseClicked

    private void txtBuscarDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarDiagnosticoActionPerformed

    }//GEN-LAST:event_txtBuscarDiagnosticoActionPerformed

    private void btnActualizarDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDiagnosticoActionPerformed
        // Verificar si se ha seleccionado un diagnóstico (documento no vacío)
        if (txtIdDiagnostico.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un diagnóstico para actualizar.");
            return;
        }

        // Verificar que todos los campos obligatorios estén completos
        if (txtIdDiagnostico.getText().isEmpty()
                || txtNombreDiagnostico.getText().isEmpty()
                || txtAreaDescripcionDiagnostico.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Uno o más campos obligatorios están vacíos. Por favor, complete todos los campos.");
            return;
        }

        // Obtener el id original desde la tabla (asumiendo que el usuario ha seleccionado una fila)
        int filaSeleccionada = tableDiagnostico.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un diagnóstico válido para actualizar.");
            return;
        }

        // Obtener el id original desde la tabla (ajusta el índice 1 según la columna del documento en tu tabla)
        String idAnterior = tableDiagnostico.getValueAt(filaSeleccionada, 0).toString();

        // Llenar el objeto diagnóstico con los datos del formulario
        dg.setId(txtIdDiagnostico.getText());
        dg.setNombre(txtNombreDiagnostico.getText());
        dg.setDescripcion(txtAreaDescripcionDiagnostico.getText());

        // Llamar al método modificarDiagnostico con el documento anterior
        boolean resultado = diag.modificarDiagnostico(dg, idAnterior);

        if (resultado) {
            limpiarTable();
            limpiarDiagnostico();
            listarDiagnostico();
            JOptionPane.showMessageDialog(null, "Diagnóstico actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el diagnóstico.");
        }
    }//GEN-LAST:event_btnActualizarDiagnosticoActionPerformed

    private void btnLimpiarDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarDiagnosticoActionPerformed
        // Verifica si al menos un campo tiene contenido
        if (!"".equals(txtIdDiagnostico.getText())
                || !"".equals(txtNombreDiagnostico.getText())
                || !"".equals(txtAreaDescripcionDiagnostico.getText())
                || !"".equals(txtBuscarDiagnostico.getText())) {

            limpiarDiagnostico();  // Limpia todos los campos
            JOptionPane.showMessageDialog(null, "¡Campos limpios con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos ya se encuentran vacíos.");
        }
    }//GEN-LAST:event_btnLimpiarDiagnosticoActionPerformed

    private void btnGuardarDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarDiagnosticoActionPerformed
        if (!"".equals(txtIdDiagnostico.getText())
                && !"".equals(txtNombreDiagnostico.getText())
                && !"".equals(txtAreaDescripcionDiagnostico.getText())) {

            dg.setId(txtIdDiagnostico.getText());
            dg.setNombre(txtNombreDiagnostico.getText());
            dg.setDescripcion(txtAreaDescripcionDiagnostico.getText());

            boolean exito = diag.registrarDiagnostico(dg);
            if (exito) {
                limpiarTable();
                limpiarDiagnostico();
                listarDiagnostico();
                JOptionPane.showMessageDialog(null, "¡Diagnostico registrado con éxito!");
            } else {
                // El mensaje de error ya fue mostrado por el DAO
                System.out.println("Error: no se pudo registrar el diagnóstico.");
                JOptionPane.showMessageDialog(null, "No se pudo registrar el diagnóstico.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Uno o más campos están vacíos. Rectifique por favor.");
        }
    }//GEN-LAST:event_btnGuardarDiagnosticoActionPerformed

    private void bntEliminarDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarDiagnosticoActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtIdDiagnostico.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar?");
            if (pregunta == JOptionPane.YES_OPTION) {
                String id = txtIdDiagnostico.getText();
                boolean eliminado = diag.eliminarDiagnostico(id);
                if (eliminado) {
                    limpiarTable();
                    limpiarDiagnostico();
                    listarDiagnostico();
                    JOptionPane.showMessageDialog(null, "¡Diagnóstico eliminado con éxito!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el diagnóstico.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un diagnóstico para eliminar.");
        }
    }//GEN-LAST:event_bntEliminarDiagnosticoActionPerformed

    private void txtNombreDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDiagnosticoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDiagnosticoActionPerformed

    private void tableDiagnosticoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDiagnosticoMouseClicked
        // TODO add your handling code here:

        int fila = tableDiagnostico.rowAtPoint(evt.getPoint());
        txtIdDiagnostico.setText(tableDiagnostico.getValueAt(fila, 0).toString());
        txtNombreDiagnostico.setText(tableDiagnostico.getValueAt(fila, 1).toString());
        txtAreaDescripcionDiagnostico.setText(tableDiagnostico.getValueAt(fila, 2).toString());
    }//GEN-LAST:event_tableDiagnosticoMouseClicked

    private void txtBuscarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarCitaActionPerformed

    private void btnGuardarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarCitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarCitaActionPerformed

    private void btnLimpiarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarCitaActionPerformed

    private void btnActualizarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarCitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarCitaActionPerformed

    private void bntEliminarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarCitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bntEliminarCitaActionPerformed

    private void tableCitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCitaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableCitaMouseClicked

    private void bntEliminarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarPacienteActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtDocumentoPaciente.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar?");
            if (pregunta == JOptionPane.YES_OPTION) {
                String documento = txtDocumentoPaciente.getText();
                boolean eliminado = pac.eliminarPaciente(documento);
                if (eliminado) {
                    limpiarTable();
                    limpiarPaciente();
                    listarPaciente();
                    JOptionPane.showMessageDialog(null, "¡Paciente eliminado con éxito!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el paciente.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un paciente para eliminar.");
        }
    }//GEN-LAST:event_bntEliminarPacienteActionPerformed

    private void btnActualizarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarPacienteActionPerformed
        // Verificar si se ha seleccionado un paciente (documento no vacío)
        if (txtDocumentoPaciente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un paciente para actualizar.");
            return;
        }

        // Verificar que todos los campos obligatorios estén completos
        if (txtUsuarioPaciente.getText().isEmpty()
                || txtContraseniaPaciente.getText().isEmpty()
                || txtCorreoPaciente.getText().isEmpty()
                || txtPrimerNombrePaciente.getText().isEmpty()
                || txtPrimerApellidoPaciente.getText().isEmpty()
                || txtSegundoApellidoPaciente.getText().isEmpty()
                || txtDocumentoPaciente.getText().isEmpty()
                || txtTelefonoPaciente.getText().isEmpty()
                || cboxSexoPaciente.getSelectedItem() == null
                || jDateFechaNacimientoPaciente.getDate() == null) { // Usar getDate()

            JOptionPane.showMessageDialog(null, "Uno o más campos obligatorios están vacíos. Por favor, complete todos los campos.");
            return;
        }

        // Verificar si se ha seleccionado una fila en la tabla
        int filaSeleccionada = tablePaciente.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un paciente válido para actualizar.");
            return;
        }

        // Obtener el documento original desde la tabla (ajusta el índice si es necesario)
        String documentoAnterior = tablePaciente.getValueAt(filaSeleccionada, 7).toString();

        // Llenar el objeto paciente con los datos del formulario
        pc.setUsuario(txtUsuarioPaciente.getText());
        pc.setContrasenia(txtContraseniaPaciente.getText());
        pc.setCorreo(txtCorreoPaciente.getText());
        pc.setPrimerNombre(txtPrimerNombrePaciente.getText());
        pc.setSegundoNombre(txtSegundoNombrePaciente.getText());
        pc.setPrimerApellido(txtPrimerApellidoPaciente.getText());
        pc.setSegundoApellido(txtSegundoApellidoPaciente.getText());
        pc.setDocumento(txtDocumentoPaciente.getText()); // Nuevo documento
        pc.setTelefono(txtTelefonoPaciente.getText());

        // Obtener la fecha desde el JDateChooser y convertirla a LocalDate
        Date fechaNacimiento = jDateFechaNacimientoPaciente.getDate();
        if (fechaNacimiento != null) {
            // Convertir Date a LocalDate
            LocalDate localDateNacimiento = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Validar que la fecha de nacimiento no sea futura
            if (localDateNacimiento.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser futura.");
                return;
            }

            pc.setFechaNacimiento(localDateNacimiento); // Usar LocalDate
        } else {
            JOptionPane.showMessageDialog(null, "Fecha de nacimiento inválida.");
            return;
        }
        pc.setSexo(cboxSexoPaciente.getSelectedItem().toString());

        // Llamar al método modificarPaciente con el documento anterior
        boolean resultado = pac.modificarPaciente(pc, documentoAnterior);

        if (resultado) {
            limpiarTable();
            limpiarPaciente();
            listarPaciente();
            JOptionPane.showMessageDialog(null, "Paciente actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el paciente.");
        }
    }//GEN-LAST:event_btnActualizarPacienteActionPerformed

    private void btnLimpiarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarPacienteActionPerformed
        // Verifica si al menos un campo tiene contenido
        if (!"".equals(txtUsuarioPaciente.getText())
                || !"".equals(txtContraseniaPaciente.getText())
                || !"".equals(txtCorreoPaciente.getText())
                || !"".equals(txtPrimerNombrePaciente.getText())
                || !"".equals(txtSegundoNombrePaciente.getText())
                || !"".equals(txtPrimerApellidoPaciente.getText())
                || !"".equals(txtSegundoApellidoPaciente.getText())
                || !"".equals(txtDocumentoPaciente.getText())
                || !"".equals(txtTelefonoPaciente.getText())
                || (cboxSexoPaciente.getSelectedItem() != null && !"".equals(cboxSexoPaciente.getSelectedItem().toString()))
                || !"".equals(txtBuscarPaciente.getText())
                || jDateFechaNacimientoPaciente.getDate() != null) {

            limpiarPaciente();  // Limpia todos los campos, incluyendo el JDateChooser
            JOptionPane.showMessageDialog(null, "¡Campos limpios con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos ya se encuentran vacíos.");
        }
    }//GEN-LAST:event_btnLimpiarPacienteActionPerformed

    private void btnGuardarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarPacienteActionPerformed
        if (!"".equals(txtUsuarioPaciente.getText())
                && !"".equals(txtContraseniaPaciente.getText())
                && !"".equals(txtCorreoPaciente.getText())
                && !"".equals(txtPrimerNombrePaciente.getText())
                && !"".equals(txtPrimerApellidoPaciente.getText())
                && !"".equals(txtSegundoApellidoPaciente.getText())
                && !"".equals(txtDocumentoPaciente.getText())
                && !"".equals(txtTelefonoPaciente.getText())
                && cboxSexoPaciente.getSelectedItem() != null
                && jDateFechaNacimientoPaciente.getDate() != null) {  // Validar si la fecha no es nula

            // Obtener y validar fecha
            Date fechaNacimiento = jDateFechaNacimientoPaciente.getDate();
            if (fechaNacimiento == null) {
                JOptionPane.showMessageDialog(null, "Fecha de nacimiento inválida.");
                return;
            }

            LocalDate localDateNacimiento = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (localDateNacimiento.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser futura.");
                return;
            }

            pc.setUsuario(txtUsuarioPaciente.getText());
            pc.setContrasenia(txtContraseniaPaciente.getText());
            pc.setCorreo(txtCorreoPaciente.getText());
            pc.setPrimerNombre(txtPrimerNombrePaciente.getText());
            pc.setSegundoNombre(txtSegundoNombrePaciente.getText());
            pc.setPrimerApellido(txtPrimerApellidoPaciente.getText());
            pc.setSegundoApellido(txtSegundoApellidoPaciente.getText());
            pc.setDocumento(txtDocumentoPaciente.getText());
            pc.setTelefono(txtTelefonoPaciente.getText());
            pc.setSexo(cboxSexoPaciente.getSelectedItem().toString());
            pc.setFechaNacimiento(localDateNacimiento);  // Usar LocalDate

            boolean exito = pac.registrarPaciente(pc);
            if (exito) {
                limpiarTable();
                limpiarPaciente();
                listarPaciente();
                JOptionPane.showMessageDialog(null, "¡Paciente registrado con éxito!");
            } else {
                // El mensaje de error ya fue mostrado por el DAO
                System.out.println("Error: no se pudo registrar el paciente.");
                JOptionPane.showMessageDialog(null, "No se pudo registrar el paciente.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Uno o más campos están vacíos. Rectifique por favor.");
        }
    }//GEN-LAST:event_btnGuardarPacienteActionPerformed

    private void txtBuscarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarPacienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarPacienteActionPerformed

    private void tablePacienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePacienteMouseClicked
        // TODO add your handling code here:

        int fila = tablePaciente.rowAtPoint(evt.getPoint());
        txtUsuarioPaciente.setText(tablePaciente.getValueAt(fila, 0).toString());
        txtContraseniaPaciente.setText(tablePaciente.getValueAt(fila, 1).toString());
        txtCorreoPaciente.setText(tablePaciente.getValueAt(fila, 2).toString());
        txtPrimerNombrePaciente.setText(tablePaciente.getValueAt(fila, 3).toString());
        txtSegundoNombrePaciente.setText(tablePaciente.getValueAt(fila, 4).toString());
        txtPrimerApellidoPaciente.setText(tablePaciente.getValueAt(fila, 5).toString());
        txtSegundoApellidoPaciente.setText(tablePaciente.getValueAt(fila, 6).toString());
        txtDocumentoPaciente.setText(tablePaciente.getValueAt(fila, 7).toString());
        txtTelefonoPaciente.setText(tablePaciente.getValueAt(fila, 8).toString());
        // Asegúrate de que estamos obteniendo la fecha de la columna correcta (columna 10)
        String fechaString = tablePaciente.getValueAt(fila, 9).toString();  // Columna 10: Fecha de Nacimiento
        try {
            LocalDate fechaLocalDate = LocalDate.parse(fechaString); // Usar LocalDate.parse
            java.util.Date fecha = java.sql.Date.valueOf(fechaLocalDate);
            jDateFechaNacimientoPaciente.setDate(fecha);  // Asignar al JDateChooser
        } catch (Exception e) {
            System.out.println("Error al convertir la fecha: " + e.getMessage());
        }
        // Asignar el sexo desde la columna 10
        cboxSexoPaciente.setSelectedItem(tablePaciente.getValueAt(fila, 10).toString()); // Columna 11: Sexo
    }//GEN-LAST:event_tablePacienteMouseClicked

    private void bntEliminarTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarTecnicoActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtDocumentoTecnico.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar?");
            if (pregunta == JOptionPane.YES_OPTION) {
                String documento = txtDocumentoTecnico.getText();
                boolean eliminado = tec.eliminarTecnico(documento);
                if (eliminado) {
                    limpiarTable();
                    limpiarTecnico();
                    listarTecnico();
                    JOptionPane.showMessageDialog(null, "¡Técnico eliminado con éxito!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el técnico.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un técnico para eliminar.");
        }
    }//GEN-LAST:event_bntEliminarTecnicoActionPerformed

    private void btnLimpiarTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarTecnicoActionPerformed
        // Verifica si al menos un campo tiene contenido
        if (!"".equals(txtUsuarioTecnico.getText())
                || !"".equals(txtContraseniaTecnico.getText())
                || !"".equals(txtCorreoTecnico.getText())
                || !"".equals(txtPrimerNombreTecnico.getText())
                || !"".equals(txtSegundoNombreTecnico.getText())
                || !"".equals(txtPrimerApellidoTecnico.getText())
                || !"".equals(txtSegundoApellidoTecnico.getText())
                || !"".equals(txtDocumentoTecnico.getText())
                || !"".equals(txtNumeroCredencialTecnico.getText())
                || !"".equals(txtTelefonoTecnico.getText())
                || (cboxSexoTecnico.getSelectedItem() != null && !"".equals(cboxSexoTecnico.getSelectedItem().toString()))
                || !"".equals(txtBuscarTecnico.getText())
                || jDateFechaNacimientoTecnico.getDate() != null) {

            limpiarTecnico();  // Limpia todos los campos, incluyendo el JDateChooser
            JOptionPane.showMessageDialog(null, "¡Campos limpios con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos ya se encuentran vacíos.");
        }
    }//GEN-LAST:event_btnLimpiarTecnicoActionPerformed

    private void btnActualizarTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarTecnicoActionPerformed
        if (txtDocumentoTecnico.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un técnico para actualizar.");
            return;
        }

        // Verificar que todos los campos obligatorios estén completos
        if (txtUsuarioTecnico.getText().isEmpty()
                || txtContraseniaTecnico.getText().isEmpty()
                || txtCorreoTecnico.getText().isEmpty()
                || txtPrimerNombreTecnico.getText().isEmpty()
                || txtPrimerApellidoTecnico.getText().isEmpty()
                || txtSegundoApellidoTecnico.getText().isEmpty()
                || txtDocumentoTecnico.getText().isEmpty()
                || txtTelefonoTecnico.getText().isEmpty()
                || txtNumeroCredencialTecnico.getText().isEmpty()
                || cboxSexoTecnico.getSelectedItem() == null
                || jDateFechaNacimientoTecnico.getDate() == null) {

            JOptionPane.showMessageDialog(null, "Uno o más campos obligatorios están vacíos. Por favor, complete todos los campos.");
            return;
        }

        int filaSeleccionada = tableTecnico.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un técnico válido para actualizar.");
            return;
        }

        String documentoAnterior = tableTecnico.getValueAt(filaSeleccionada, 7).toString();

        // Obtener y validar fecha
        Date fechaNacimiento = jDateFechaNacimientoTecnico.getDate();
        if (fechaNacimiento == null) {
            JOptionPane.showMessageDialog(null, "Fecha de nacimiento inválida.");
            return;
        }

        LocalDate localDateNacimiento = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (localDateNacimiento.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser futura.");
            return;
        }

        // Llenar el objeto Técnico
        tc.setUsuario(txtUsuarioTecnico.getText());
        tc.setContrasenia(txtContraseniaTecnico.getText());
        tc.setCorreo(txtCorreoTecnico.getText());
        tc.setPrimerNombre(txtPrimerNombreTecnico.getText());
        tc.setSegundoNombre(txtSegundoNombreTecnico.getText());
        tc.setPrimerApellido(txtPrimerApellidoTecnico.getText());
        tc.setSegundoApellido(txtSegundoApellidoTecnico.getText());
        tc.setDocumento(txtDocumentoTecnico.getText());
        tc.setTelefono(txtTelefonoTecnico.getText());
        tc.setNumeroCredencial(txtNumeroCredencialTecnico.getText());
        tc.setFechaNacimiento(localDateNacimiento);
        tc.setSexo(cboxSexoTecnico.getSelectedItem().toString());

        boolean resultado = tec.modificarTecnico(tc, documentoAnterior);

        if (resultado) {
            limpiarTable();
            limpiarTecnico();
            listarTecnico();
            JOptionPane.showMessageDialog(null, "Técnico actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el técnico.");
        }
    }//GEN-LAST:event_btnActualizarTecnicoActionPerformed

    private void btnGuardarTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarTecnicoActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtUsuarioTecnico.getText())
                && !"".equals(txtContraseniaTecnico.getText())
                && !"".equals(txtCorreoTecnico.getText())
                && !"".equals(txtPrimerNombreTecnico.getText())
                && !"".equals(txtPrimerApellidoTecnico.getText())
                && !"".equals(txtSegundoApellidoTecnico.getText())
                && !"".equals(txtDocumentoTecnico.getText())
                && !"".equals(txtNumeroCredencialTecnico.getText())
                && !"".equals(txtTelefonoTecnico.getText())
                && cboxSexoTecnico.getSelectedItem() != null
                && jDateFechaNacimientoTecnico.getDate() != null) {  // Validar si la fecha no es nula

            // Validar que la fecha no sea futura
            LocalDate fechaNacimiento = jDateFechaNacimientoTecnico.getDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (fechaNacimiento.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser futura.");
                return;
            }

            tc.setUsuario(txtUsuarioTecnico.getText());
            tc.setContrasenia(txtContraseniaTecnico.getText());
            tc.setCorreo(txtCorreoTecnico.getText());
            tc.setPrimerNombre(txtPrimerNombreTecnico.getText());
            tc.setSegundoNombre(txtSegundoNombreTecnico.getText());
            tc.setPrimerApellido(txtPrimerApellidoTecnico.getText());
            tc.setSegundoApellido(txtSegundoApellidoTecnico.getText());
            tc.setDocumento(txtDocumentoTecnico.getText());
            tc.setNumeroCredencial(txtNumeroCredencialTecnico.getText());
            tc.setTelefono(txtTelefonoTecnico.getText());
            tc.setSexo(cboxSexoTecnico.getSelectedItem().toString());
            tc.setFechaNacimiento(fechaNacimiento);

            boolean exito = tec.registrarTecnico(tc);
            if (exito) {
                limpiarTable();
                limpiarTecnico();
                listarTecnico();
                JOptionPane.showMessageDialog(null, "¡Técnico registrado con éxito!");
            } else {
                // El mensaje de error ya fue mostrado por el DAO
                System.out.println("Error: no se pudo registrar el técnico.");
                JOptionPane.showMessageDialog(null, "No se pudo registrar el técnico.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Uno o más campos están vacíos. Rectifique por favor.");
        }
    }//GEN-LAST:event_btnGuardarTecnicoActionPerformed

    private void txtBuscarTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarTecnicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarTecnicoActionPerformed

    private void txtPrimerNombreTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrimerNombreTecnicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrimerNombreTecnicoActionPerformed

    private void tableTecnicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTecnicoMouseClicked
        // TODO add your handling code here:

        int fila = tableTecnico.rowAtPoint(evt.getPoint());
        txtUsuarioTecnico.setText(tableTecnico.getValueAt(fila, 0).toString());
        txtContraseniaTecnico.setText(tableTecnico.getValueAt(fila, 1).toString());
        txtCorreoTecnico.setText(tableTecnico.getValueAt(fila, 2).toString());
        txtPrimerNombreTecnico.setText(tableTecnico.getValueAt(fila, 3).toString());
        txtSegundoNombreTecnico.setText(tableTecnico.getValueAt(fila, 4).toString());
        txtPrimerApellidoTecnico.setText(tableTecnico.getValueAt(fila, 5).toString());
        txtSegundoApellidoTecnico.setText(tableTecnico.getValueAt(fila, 6).toString());
        txtDocumentoTecnico.setText(tableTecnico.getValueAt(fila, 7).toString());
        txtNumeroCredencialTecnico.setText(tableTecnico.getValueAt(fila, 8).toString());
        txtTelefonoTecnico.setText(tableTecnico.getValueAt(fila, 9).toString());
        // Asegúrate de que estamos obteniendo la fecha de la columna correcta (columna 10)
        String fechaString = tableTecnico.getValueAt(fila, 10).toString();  // Columna 10: Fecha de Nacimiento
        try {
            LocalDate fechaLocalDate = LocalDate.parse(fechaString); // Usar LocalDate.parse
            java.util.Date fecha = java.sql.Date.valueOf(fechaLocalDate);
            jDateFechaNacimientoTecnico.setDate(fecha);  // Asignar al JDateChooser
        } catch (Exception e) {
            System.out.println("Error al convertir la fecha: " + e.getMessage());
        }

        // Asignar el sexo desde la columna 11
        cboxSexoTecnico.setSelectedItem(tableTecnico.getValueAt(fila, 11).toString()); // Columna 11: Sexo
    }//GEN-LAST:event_tableTecnicoMouseClicked

    private void txtBuscarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarMedicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMedicoActionPerformed

    private void txtDocumentoMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocumentoMedicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocumentoMedicoActionPerformed

    private void cboxSexoMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxSexoMedicoActionPerformed

    }//GEN-LAST:event_cboxSexoMedicoActionPerformed

    private void bntEliminarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntEliminarMedicoActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtDocumentoMedico.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar?");
            if (pregunta == JOptionPane.YES_OPTION) {
                String documento = txtDocumentoMedico.getText();
                boolean eliminado = medic.eliminarMedico(documento);
                if (eliminado) {
                    limpiarTable();
                    limpiarMedico();
                    listarMedico();
                    JOptionPane.showMessageDialog(null, "¡Médico eliminado con éxito!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el médico.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un médico para eliminar.");
        }
    }//GEN-LAST:event_bntEliminarMedicoActionPerformed

    private void btnLimpiarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarMedicoActionPerformed
        // Verifica si al menos un campo tiene contenido
        if (!"".equals(txtUsuarioMedico.getText())
                || !"".equals(txtContraseniaMedico.getText())
                || !"".equals(txtCorreoMedico.getText())
                || !"".equals(txtPrimerNombreMedico.getText())
                || !"".equals(txtSegundoNombreMedico.getText())
                || !"".equals(txtPrimerApellidoMedico.getText())
                || !"".equals(txtSegundoApellidoMedico.getText())
                || !"".equals(txtDocumentoMedico.getText())
                || !"".equals(txtNumeroLicenciaMedico.getText())
                || !"".equals(txtTelefonoMedico.getText())
                || (cboxSexoMedico.getSelectedItem() != null && !"".equals(cboxSexoMedico.getSelectedItem().toString()))
                || !"".equals(txtBuscarMedico.getText())
                || jDateFechaNacimientoMedico.getDate() != null) {

            limpiarMedico();  // Limpia todos los campos, incluyendo el JDateChooser
            JOptionPane.showMessageDialog(null, "¡Campos limpios con éxito!");
        } else {
            JOptionPane.showMessageDialog(null, "Los campos ya se encuentran vacíos.");
        }
    }//GEN-LAST:event_btnLimpiarMedicoActionPerformed

    private void btnActualizarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarMedicoActionPerformed
        // Verificar si se ha seleccionado un médico (documento no vacío)
        if (txtDocumentoMedico.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un médico para actualizar.");
            return;
        }

        // Verificar que todos los campos obligatorios estén completos
        if (txtUsuarioMedico.getText().isEmpty()
                || txtContraseniaMedico.getText().isEmpty()
                || txtCorreoMedico.getText().isEmpty()
                || txtPrimerNombreMedico.getText().isEmpty()
                || txtPrimerApellidoMedico.getText().isEmpty()
                || txtSegundoApellidoMedico.getText().isEmpty()
                || txtDocumentoMedico.getText().isEmpty()
                || txtTelefonoMedico.getText().isEmpty()
                || txtNumeroLicenciaMedico.getText().isEmpty()
                || cboxSexoMedico.getSelectedItem() == null
                || jDateFechaNacimientoMedico.getDate() == null) {

            JOptionPane.showMessageDialog(null, "Uno o más campos obligatorios están vacíos. Por favor, complete todos los campos.");
            return;
        }

        // Obtener el documento original desde la tabla (asumiendo que el usuario ha seleccionado una fila)
        int filaSeleccionada = tableMedico.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado un médico válido para actualizar.");
            return;
        }

        // Obtener el documento original desde la tabla (ajusta el índice 7 según la columna del documento en tu tabla)
        String documentoAnterior = tableMedico.getValueAt(filaSeleccionada, 7).toString();

        // Llenar el objeto Medico con los datos del formulario
        md.setUsuario(txtUsuarioMedico.getText());
        md.setContrasenia(txtContraseniaMedico.getText());
        md.setCorreo(txtCorreoMedico.getText());
        md.setPrimerNombre(txtPrimerNombreMedico.getText());
        md.setSegundoNombre(txtSegundoNombreMedico.getText());
        md.setPrimerApellido(txtPrimerApellidoMedico.getText());
        md.setSegundoApellido(txtSegundoApellidoMedico.getText());
        md.setDocumento(txtDocumentoMedico.getText());
        md.setTelefono(txtTelefonoMedico.getText());
        md.setNumeroLicencia(txtNumeroLicenciaMedico.getText());

        // Obtener la fecha desde el JDateChooser y convertirla a LocalDate
        Date fechaNacimiento = jDateFechaNacimientoMedico.getDate();
        if (fechaNacimiento != null) {
            LocalDate localDateNacimiento = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Validar que no sea futura
            if (localDateNacimiento.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser futura.");
                return;
            }

            md.setFechaNacimiento(localDateNacimiento);
        } else {
            JOptionPane.showMessageDialog(null, "Fecha de nacimiento inválida.");
            return;
        }

        md.setSexo(cboxSexoMedico.getSelectedItem().toString());

        // Llamar al método modificarMedico con el documento anterior
        boolean resultado = medic.modificarMedico(md, documentoAnterior);

        if (resultado) {
            limpiarTable();
            limpiarMedico();
            listarMedico();
            JOptionPane.showMessageDialog(null, "Médico actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el médico.");
        }
    }//GEN-LAST:event_btnActualizarMedicoActionPerformed

    private void btnGuardarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarMedicoActionPerformed
        if (!"".equals(txtUsuarioMedico.getText())
                && !"".equals(txtContraseniaMedico.getText())
                && !"".equals(txtCorreoMedico.getText())
                && !"".equals(txtPrimerNombreMedico.getText())
                && !"".equals(txtPrimerApellidoMedico.getText())
                && !"".equals(txtSegundoApellidoMedico.getText())
                && !"".equals(txtDocumentoMedico.getText())
                && !"".equals(txtNumeroLicenciaMedico.getText())
                && !"".equals(txtTelefonoMedico.getText())
                && cboxSexoMedico.getSelectedItem() != null
                && jDateFechaNacimientoMedico.getDate() != null) {  // Validar si la fecha no es nula

            md.setUsuario(txtUsuarioMedico.getText());
            md.setContrasenia(txtContraseniaMedico.getText());
            md.setCorreo(txtCorreoMedico.getText());
            md.setPrimerNombre(txtPrimerNombreMedico.getText());
            md.setSegundoNombre(txtSegundoNombreMedico.getText());
            md.setPrimerApellido(txtPrimerApellidoMedico.getText());
            md.setSegundoApellido(txtSegundoApellidoMedico.getText());
            md.setDocumento(txtDocumentoMedico.getText());
            md.setNumeroLicencia(txtNumeroLicenciaMedico.getText());
            md.setTelefono(txtTelefonoMedico.getText());
            md.setSexo(cboxSexoMedico.getSelectedItem().toString());

            // Convertir la fecha a LocalDate
            LocalDate fechaNacimiento = new java.sql.Date(jDateFechaNacimientoMedico.getDate().getTime()).toLocalDate();

            // Validar que la fecha de nacimiento sea igual o anterior a hoy
            if (fechaNacimiento.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser futura.");
                return;
            }

            md.setFechaNacimiento(fechaNacimiento);

            boolean exito = medic.registrarMedico(md);
            if (exito) {
                limpiarTable();
                limpiarMedico();
                listarMedico();
                JOptionPane.showMessageDialog(null, "¡Médico registrado con éxito!");
            } else {
                // El mensaje de error ya fue mostrado por el DAO
                System.out.println("Error: no se pudo registrar el médico.");
                JOptionPane.showMessageDialog(null, "No se pudo registrar el médico.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Uno o más campos están vacíos. Rectifique por favor.");
        }
    }//GEN-LAST:event_btnGuardarMedicoActionPerformed

    private void tableMedicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMedicoMouseClicked

        int fila = tableMedico.rowAtPoint(evt.getPoint());
        txtUsuarioMedico.setText(tableMedico.getValueAt(fila, 0).toString());
        txtContraseniaMedico.setText(tableMedico.getValueAt(fila, 1).toString());
        txtCorreoMedico.setText(tableMedico.getValueAt(fila, 2).toString());
        txtPrimerNombreMedico.setText(tableMedico.getValueAt(fila, 3).toString());
        txtSegundoNombreMedico.setText(tableMedico.getValueAt(fila, 4).toString());
        txtPrimerApellidoMedico.setText(tableMedico.getValueAt(fila, 5).toString());
        txtSegundoApellidoMedico.setText(tableMedico.getValueAt(fila, 6).toString());
        txtDocumentoMedico.setText(tableMedico.getValueAt(fila, 7).toString());
        txtNumeroLicenciaMedico.setText(tableMedico.getValueAt(fila, 8).toString());
        txtTelefonoMedico.setText(tableMedico.getValueAt(fila, 9).toString());

        // Asegúrate de que estamos obteniendo la fecha de la columna correcta (columna 10)
        String fechaString = tableMedico.getValueAt(fila, 10).toString();  // Columna 10: Fecha de Nacimiento
        try {
            LocalDate fechaLocalDate = LocalDate.parse(fechaString); // Usar LocalDate.parse
            java.util.Date fecha = java.sql.Date.valueOf(fechaLocalDate);
            jDateFechaNacimientoMedico.setDate(fecha);  // Asignar al JDateChooser
        } catch (Exception e) {
            System.out.println("Error al convertir la fecha: " + e.getMessage());
        }

        // Asignar el sexo desde la columna 11
        cboxSexoMedico.setSelectedItem(tableMedico.getValueAt(fila, 11).toString()); // Columna 11: Sexo
    }//GEN-LAST:event_tableMedicoMouseClicked

    private void btnGenerarReportePdfCantidadMedicoSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReportePdfCantidadMedicoSexoActionPerformed
        // TODO add your handling code here:
        Pdf.generarReporteCantidadMedicosPorSexo();
    }//GEN-LAST:event_btnGenerarReportePdfCantidadMedicoSexoActionPerformed

    private void btnListarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarUsuariosActionPerformed
        Pdf.generarReporteTodosLosUsuarios();
    }//GEN-LAST:event_btnListarUsuariosActionPerformed

    private void btnGenerarReporteGraficoCantidadUsuarioSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteGraficoCantidadUsuarioSexoActionPerformed
        Grafico grafico = new Grafico();
        grafico.mostrarGraficoUsuarios();
    }//GEN-LAST:event_btnGenerarReporteGraficoCantidadUsuarioSexoActionPerformed

    private void btnGenerarReportePdfCantidadPacienteSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReportePdfCantidadPacienteSexoActionPerformed
        Pdf.generarReporteCantidadPacientesPorSexo();
    }//GEN-LAST:event_btnGenerarReportePdfCantidadPacienteSexoActionPerformed

    private void btnGenerarReporteGraficoCantidadMedicoSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteGraficoCantidadMedicoSexoActionPerformed
        Grafico grafico = new Grafico();
        grafico.mostrarGraficoMedicos();
    }//GEN-LAST:event_btnGenerarReporteGraficoCantidadMedicoSexoActionPerformed

    private void btnGenerarReportePdfCantidadTecnicoSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReportePdfCantidadTecnicoSexoActionPerformed
        Pdf.generarReporteCantidadTecnicosPorSexo();
    }//GEN-LAST:event_btnGenerarReportePdfCantidadTecnicoSexoActionPerformed

    private void btnGenerarReporteGraficoCantidadTecnicoSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteGraficoCantidadTecnicoSexoActionPerformed
        Grafico grafico = new Grafico();
        grafico.mostrarGraficoTecnicos();
    }//GEN-LAST:event_btnGenerarReporteGraficoCantidadTecnicoSexoActionPerformed

    private void btnGenerarReportePdfCantidadUsuarioSexo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReportePdfCantidadUsuarioSexo1ActionPerformed
        Pdf.generarReporteCantidadUsuariosPorSexo();
    }//GEN-LAST:event_btnGenerarReportePdfCantidadUsuarioSexo1ActionPerformed

    private void btnListarMedicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarMedicosActionPerformed
        Pdf.generarReporteTodosLosMedicos();
    }//GEN-LAST:event_btnListarMedicosActionPerformed

    private void btnListarTecnicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarTecnicosActionPerformed
        Pdf.generarReporteTodosLosTecnicos();
    }//GEN-LAST:event_btnListarTecnicosActionPerformed

    private void btnListarPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarPacientesActionPerformed
        Pdf.generarReporteTodosLosPacientes();
    }//GEN-LAST:event_btnListarPacientesActionPerformed

    private void btnGenerarCantidadPdfEdadesUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarCantidadPdfEdadesUsuariosActionPerformed
        Pdf.generarReporteUsuariosPorEdad();
    }//GEN-LAST:event_btnGenerarCantidadPdfEdadesUsuariosActionPerformed

    private void btnGenerarCantidadGraficoEdadesUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarCantidadGraficoEdadesUsuariosActionPerformed
        Grafico grafico = new Grafico();
        grafico.mostrarGraficoUsuariosPorEdad();
    }//GEN-LAST:event_btnGenerarCantidadGraficoEdadesUsuariosActionPerformed

    private void btnGenerarListadoUsuariosOrdenadosFechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListadoUsuariosOrdenadosFechaNacimientoActionPerformed
        Pdf.generarReporteUsuariosPorFechaNacimiento();
    }//GEN-LAST:event_btnGenerarListadoUsuariosOrdenadosFechaNacimientoActionPerformed

    private void btnListarDiagnosticosMasCortoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarDiagnosticosMasCortoActionPerformed
        Pdf.generarReporteDiagnosticosConNombreCorto();
    }//GEN-LAST:event_btnListarDiagnosticosMasCortoActionPerformed

    private void btnGenerarListaUsuariosHombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaUsuariosHombresActionPerformed
        Pdf.generarReporteUsuariosMasculinos();
    }//GEN-LAST:event_btnGenerarListaUsuariosHombresActionPerformed

    private void btnGenerarListaUsuariosMujeresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaUsuariosMujeresActionPerformed
        Pdf.generarReporteUsuariasFemeninas();
    }//GEN-LAST:event_btnGenerarListaUsuariosMujeresActionPerformed

    private void btnGenerarListaUsuariosAnioNacimientoMasComunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaUsuariosAnioNacimientoMasComunActionPerformed
        Pdf.generarReporteUsuariosAnoNacimientoMasComun();
    }//GEN-LAST:event_btnGenerarListaUsuariosAnioNacimientoMasComunActionPerformed

    private void btnGenerarListaTelefonosUsuariosHombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaTelefonosUsuariosHombresActionPerformed
        Pdf.generarReporteTelefonosUsuariosMasculinos();
    }//GEN-LAST:event_btnGenerarListaTelefonosUsuariosHombresActionPerformed

    private void btnGenerarListaTelefonosUsuariosMujeresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaTelefonosUsuariosMujeresActionPerformed
        Pdf.generarReporteTelefonosUsuariasFemeninas();
    }//GEN-LAST:event_btnGenerarListaTelefonosUsuariosMujeresActionPerformed

    private void btnGenerarListaCantidadUsuariosPrimerNombreMasComunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaCantidadUsuariosPrimerNombreMasComunActionPerformed
        Pdf.generarReportePrimerNombreMasComun();
    }//GEN-LAST:event_btnGenerarListaCantidadUsuariosPrimerNombreMasComunActionPerformed

    private void btnGenerarListaPdfTecnicosUltimoNumeroTerminaImparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaPdfTecnicosUltimoNumeroTerminaImparActionPerformed
        Pdf.generarReporteTecnicosCredencialImpar();
    }//GEN-LAST:event_btnGenerarListaPdfTecnicosUltimoNumeroTerminaImparActionPerformed

    private void btnGenerarListaCantidadUsuariosSegundoNombreMasComunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarListaCantidadUsuariosSegundoNombreMasComunActionPerformed
        Pdf.generarReporteSegundoNombreMasComun();
    }//GEN-LAST:event_btnGenerarListaCantidadUsuariosSegundoNombreMasComunActionPerformed

    private void btnGenerarReportePdfCantidadUsuarioSexo9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReportePdfCantidadUsuarioSexo9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGenerarReportePdfCantidadUsuarioSexo9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Administrador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bntEliminarCita;
    private javax.swing.JButton bntEliminarDiagnostico;
    private javax.swing.JButton bntEliminarDispositivo;
    private javax.swing.JButton bntEliminarMedico;
    private javax.swing.JButton bntEliminarPaciente;
    private javax.swing.JButton bntEliminarTecnico;
    private javax.swing.JButton bntEliminarTratamiento;
    private javax.swing.JButton btnActualizarCita;
    private javax.swing.JButton btnActualizarDiagnostico;
    private javax.swing.JButton btnActualizarDispositivo;
    private javax.swing.JButton btnActualizarMedico;
    private javax.swing.JButton btnActualizarPaciente;
    private javax.swing.JButton btnActualizarTecnico;
    private javax.swing.JButton btnActualizarTratamiento;
    private javax.swing.JButton btnGenerarCantidadGraficoEdadesUsuarios;
    private javax.swing.JButton btnGenerarCantidadPdfEdadesUsuarios;
    private javax.swing.JButton btnGenerarListaCantidadUsuariosPrimerNombreMasComun;
    private javax.swing.JButton btnGenerarListaCantidadUsuariosSegundoNombreMasComun;
    private javax.swing.JButton btnGenerarListaPdfTecnicosUltimoNumeroTerminaImpar;
    private javax.swing.JButton btnGenerarListaTelefonosUsuariosHombres;
    private javax.swing.JButton btnGenerarListaTelefonosUsuariosMujeres;
    private javax.swing.JButton btnGenerarListaUsuariosAnioNacimientoMasComun;
    private javax.swing.JButton btnGenerarListaUsuariosHombres;
    private javax.swing.JButton btnGenerarListaUsuariosMujeres;
    private javax.swing.JButton btnGenerarListadoUsuariosOrdenadosFechaNacimiento;
    private javax.swing.JButton btnGenerarReporteGraficoCantidadMedicoSexo;
    private javax.swing.JButton btnGenerarReporteGraficoCantidadPacienteSexo;
    private javax.swing.JButton btnGenerarReporteGraficoCantidadTecnicoSexo;
    private javax.swing.JButton btnGenerarReporteGraficoCantidadUsuarioSexo;
    private javax.swing.JButton btnGenerarReportePdfCantidadMedicoSexo;
    private javax.swing.JButton btnGenerarReportePdfCantidadPacienteSexo;
    private javax.swing.JButton btnGenerarReportePdfCantidadTecnicoSexo;
    private javax.swing.JButton btnGenerarReportePdfCantidadUsuarioSexo1;
    private javax.swing.JButton btnGenerarReportePdfCantidadUsuarioSexo9;
    private javax.swing.JButton btnGenerarReportes;
    private javax.swing.JButton btnGestionarCatalogoDiagnosticos;
    private javax.swing.JButton btnGestionarCatalogoDispositivos;
    private javax.swing.JButton btnGestionarCatalogoTratamientos;
    private javax.swing.JButton btnGestionarCitas;
    private javax.swing.JButton btnGestionarMedicos;
    private javax.swing.JButton btnGestionarPacientes;
    private javax.swing.JButton btnGestionarTecnicos;
    private javax.swing.JButton btnGuardarCita;
    private javax.swing.JButton btnGuardarDiagnostico;
    private javax.swing.JButton btnGuardarDispositivo;
    private javax.swing.JButton btnGuardarMedico;
    private javax.swing.JButton btnGuardarPaciente;
    private javax.swing.JButton btnGuardarTecnico;
    private javax.swing.JButton btnGuardarTratamiento;
    private javax.swing.JButton btnLimpiarCita;
    private javax.swing.JButton btnLimpiarDiagnostico;
    private javax.swing.JButton btnLimpiarDispositivo;
    private javax.swing.JButton btnLimpiarMedico;
    private javax.swing.JButton btnLimpiarPaciente;
    private javax.swing.JButton btnLimpiarTecnico;
    private javax.swing.JButton btnLimpiarTratamiento;
    private javax.swing.JButton btnListarDiagnosticosMasCorto;
    private javax.swing.JButton btnListarMedicos;
    private javax.swing.JButton btnListarPacientes;
    private javax.swing.JButton btnListarTecnicos;
    private javax.swing.JButton btnListarUsuarios;
    private javax.swing.JButton btnPerfilAdmin;
    private javax.swing.JComboBox<String> cboxSexoMedico;
    private javax.swing.JComboBox<String> cboxSexoPaciente;
    private javax.swing.JComboBox<String> cboxSexoTecnico;
    private com.toedter.calendar.JDateChooser jDateFechaCita;
    private com.toedter.calendar.JDateChooser jDateFechaNacimientoMedico;
    private com.toedter.calendar.JDateChooser jDateFechaNacimientoPaciente;
    private com.toedter.calendar.JDateChooser jDateFechaNacimientoTecnico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel0;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private com.toedter.components.JSpinField jSpinHoraCita;
    private com.toedter.components.JSpinField jSpinMinutoCita;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTable tableCita;
    private javax.swing.JTable tableDiagnostico;
    private javax.swing.JTable tableMedico;
    private javax.swing.JTable tablePaciente;
    private javax.swing.JTable tableTecnico;
    private javax.swing.JTable tableTratamiento;
    private javax.swing.JTextArea txtAreaDescripcionDiagnostico;
    private javax.swing.JTextArea txtAreaDescripcionTratamiento;
    private javax.swing.JTextArea txtAreaMotivoCita;
    private javax.swing.JTextField txtBuscarCita;
    private javax.swing.JTextField txtBuscarDiagnostico;
    private javax.swing.JTextField txtBuscarDispositivo;
    private javax.swing.JTextField txtBuscarMedico;
    private javax.swing.JTextField txtBuscarPaciente;
    private javax.swing.JTextField txtBuscarTecnico;
    private javax.swing.JTextField txtBuscarTratamiento;
    private javax.swing.JTextField txtContraseniaMedico;
    private javax.swing.JTextField txtContraseniaPaciente;
    private javax.swing.JTextField txtContraseniaTecnico;
    private javax.swing.JTextField txtCorreoMedico;
    private javax.swing.JTextField txtCorreoPaciente;
    private javax.swing.JTextField txtCorreoTecnico;
    private javax.swing.JTextField txtDocumentoMedico;
    private javax.swing.JTextField txtDocumentoPaciente;
    private javax.swing.JTextField txtDocumentoTecnico;
    private javax.swing.JTextField txtIdCita;
    private javax.swing.JTextField txtIdDiagnostico;
    private javax.swing.JTextField txtIdDispositivo;
    private javax.swing.JTextField txtIdTratamiento;
    private javax.swing.JTextField txtMarcaDispositivo;
    private javax.swing.JTextField txtMedicoCita;
    private javax.swing.JTextField txtModeloDispositivo;
    private javax.swing.JTextField txtNombreDiagnostico;
    private javax.swing.JTextField txtNombreTratamiento;
    private javax.swing.JTextField txtNumeroCredencialTecnico;
    private javax.swing.JTextField txtNumeroLicenciaMedico;
    private javax.swing.JTextField txtPacienteCita;
    private javax.swing.JTextField txtPrimerApellidoMedico;
    private javax.swing.JTextField txtPrimerApellidoPaciente;
    private javax.swing.JTextField txtPrimerApellidoTecnico;
    private javax.swing.JTextField txtPrimerNombreMedico;
    private javax.swing.JTextField txtPrimerNombrePaciente;
    private javax.swing.JTextField txtPrimerNombreTecnico;
    private javax.swing.JTextField txtSegundoApellidoMedico;
    private javax.swing.JTextField txtSegundoApellidoPaciente;
    private javax.swing.JTextField txtSegundoApellidoTecnico;
    private javax.swing.JTextField txtSegundoNombreMedico;
    private javax.swing.JTextField txtSegundoNombrePaciente;
    private javax.swing.JTextField txtSegundoNombreTecnico;
    private javax.swing.JTextField txtTelefonoMedico;
    private javax.swing.JTextField txtTelefonoPaciente;
    private javax.swing.JTextField txtTelefonoTecnico;
    private javax.swing.JTextField txtUsuarioMedico;
    private javax.swing.JTextField txtUsuarioMedico1;
    private javax.swing.JTextField txtUsuarioPaciente;
    private javax.swing.JTextField txtUsuarioTecnico;
    // End of variables declaration//GEN-END:variables

    private void limpiarMedico() {
        txtUsuarioMedico.setText("");
        txtContraseniaMedico.setText("");
        txtCorreoMedico.setText("");
        txtPrimerNombreMedico.setText("");
        txtSegundoNombreMedico.setText("");
        txtPrimerApellidoMedico.setText("");
        txtSegundoApellidoMedico.setText("");
        txtDocumentoMedico.setText("");
        txtNumeroLicenciaMedico.setText("");
        txtTelefonoMedico.setText("");
        jDateFechaNacimientoMedico.setDate(null); // Limpia el campo
        jDateFechaNacimientoMedico.setDateFormatString("yyyy-MM-dd");
        cboxSexoMedico.setSelectedIndex(-1);
        txtBuscarMedico.setText("");

        // Asegúrate de que la interfaz gráfica se actualice
        txtUsuarioMedico.revalidate();
        txtContraseniaMedico.revalidate();
        txtCorreoMedico.revalidate();
        txtPrimerNombreMedico.revalidate();
        txtSegundoNombreMedico.revalidate();
        txtPrimerApellidoMedico.revalidate();
        txtSegundoApellidoMedico.revalidate();
        txtDocumentoMedico.revalidate();
        txtNumeroLicenciaMedico.revalidate();
        jDateFechaNacimientoMedico.revalidate();
        cboxSexoMedico.revalidate();
        txtTelefonoMedico.revalidate();
        txtBuscarMedico.revalidate();

        txtUsuarioMedico.repaint();
        txtContraseniaMedico.repaint();
        txtCorreoMedico.repaint();
        txtPrimerNombreMedico.repaint();
        txtSegundoNombreMedico.repaint();
        txtPrimerApellidoMedico.repaint();
        txtSegundoApellidoMedico.repaint();
        txtDocumentoMedico.repaint();
        txtNumeroLicenciaMedico.repaint();
        txtTelefonoMedico.repaint();
        jDateFechaNacimientoMedico.repaint();
        cboxSexoMedico.repaint();
        txtBuscarMedico.repaint();
    }

    private void limpiarTecnico() {
        txtUsuarioTecnico.setText("");
        txtContraseniaTecnico.setText("");
        txtCorreoTecnico.setText("");
        txtPrimerNombreTecnico.setText("");
        txtSegundoNombreTecnico.setText("");
        txtPrimerApellidoTecnico.setText("");
        txtSegundoApellidoTecnico.setText("");
        txtDocumentoTecnico.setText("");
        txtNumeroCredencialTecnico.setText("");
        txtTelefonoTecnico.setText("");
        jDateFechaNacimientoTecnico.setDate(null); // Limpia el campo
        jDateFechaNacimientoTecnico.setDateFormatString("yyyy-MM-dd");
        cboxSexoTecnico.setSelectedIndex(-1);
        txtBuscarTecnico.setText("");

        // Asegúrate de que la interfaz gráfica se actualice
        txtUsuarioTecnico.revalidate();
        txtContraseniaTecnico.revalidate();
        txtCorreoTecnico.revalidate();
        txtPrimerNombreTecnico.revalidate();
        txtSegundoNombreTecnico.revalidate();
        txtPrimerApellidoTecnico.revalidate();
        txtSegundoApellidoTecnico.revalidate();
        txtDocumentoTecnico.revalidate();
        txtNumeroCredencialTecnico.revalidate();
        txtTelefonoTecnico.revalidate();
        jDateFechaNacimientoTecnico.revalidate();
        cboxSexoTecnico.revalidate();
        txtBuscarTecnico.revalidate();

        txtUsuarioTecnico.repaint();
        txtContraseniaTecnico.repaint();
        txtCorreoTecnico.repaint();
        txtPrimerNombreTecnico.repaint();
        txtSegundoNombreTecnico.repaint();
        txtPrimerApellidoTecnico.repaint();
        txtSegundoApellidoTecnico.repaint();
        txtDocumentoTecnico.repaint();
        txtNumeroCredencialTecnico.repaint();
        txtTelefonoTecnico.repaint();
        jDateFechaNacimientoTecnico.repaint();
        cboxSexoTecnico.repaint();
        txtBuscarTecnico.repaint();
    }

    private void limpiarPaciente() {
        txtUsuarioPaciente.setText("");
        txtContraseniaPaciente.setText("");
        txtCorreoPaciente.setText("");
        txtPrimerNombrePaciente.setText("");
        txtSegundoNombrePaciente.setText("");
        txtPrimerApellidoPaciente.setText("");
        txtSegundoApellidoPaciente.setText("");
        txtDocumentoPaciente.setText("");
        txtTelefonoPaciente.setText("");
        jDateFechaNacimientoPaciente.setDate(null); // Limpia el campo
        jDateFechaNacimientoPaciente.setDateFormatString("yyyy-MM-dd");
        cboxSexoPaciente.setSelectedIndex(-1);
        txtBuscarPaciente.setText("");

        // Asegúrate de que la interfaz gráfica se actualice
        txtUsuarioPaciente.revalidate();
        txtContraseniaPaciente.revalidate();
        txtCorreoPaciente.revalidate();
        txtPrimerNombrePaciente.revalidate();
        txtSegundoNombrePaciente.revalidate();
        txtPrimerApellidoPaciente.revalidate();
        txtSegundoApellidoPaciente.revalidate();
        txtDocumentoPaciente.revalidate();
        txtTelefonoPaciente.revalidate();
        jDateFechaNacimientoPaciente.revalidate();
        cboxSexoPaciente.revalidate();
        txtBuscarPaciente.revalidate();

        txtUsuarioPaciente.repaint();
        txtContraseniaPaciente.repaint();
        txtCorreoPaciente.repaint();
        txtPrimerNombrePaciente.repaint();
        txtSegundoNombrePaciente.repaint();
        txtPrimerApellidoPaciente.repaint();
        txtSegundoApellidoPaciente.repaint();
        txtDocumentoPaciente.repaint();
        txtTelefonoPaciente.repaint();
        jDateFechaNacimientoPaciente.repaint();
        cboxSexoPaciente.repaint();
        txtBuscarPaciente.repaint();
    }

    private void limpiarDiagnostico() {
        txtIdDiagnostico.setText("");
        txtNombreDiagnostico.setText("");
        txtAreaDescripcionDiagnostico.setText("");
        txtBuscarDiagnostico.setText("");

        // Asegúrate de que la interfaz gráfica se actualice
        txtIdDiagnostico.revalidate();
        txtNombreDiagnostico.revalidate();
        txtAreaDescripcionDiagnostico.revalidate();
        txtBuscarDiagnostico.revalidate();

        txtIdDiagnostico.repaint();
        txtNombreDiagnostico.repaint();
        txtAreaDescripcionDiagnostico.repaint();
        txtBuscarDiagnostico.repaint();
    }

    private void limpiarTratamiento() {
        txtIdTratamiento.setText("");
        txtNombreTratamiento.setText("");
        txtAreaDescripcionTratamiento.setText("");
        txtBuscarTratamiento.setText("");

        // Asegúrate de que la interfaz gráfica se actualice
        txtIdTratamiento.revalidate();
        txtNombreTratamiento.revalidate();
        txtAreaDescripcionTratamiento.revalidate();
        txtBuscarTratamiento.revalidate();

        txtIdTratamiento.repaint();
        txtNombreTratamiento.repaint();
        txtAreaDescripcionTratamiento.repaint();
        txtBuscarTratamiento.repaint();
    }
}
