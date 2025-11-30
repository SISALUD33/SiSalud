package core.ui;

import core.negocios.CampañaNegocios;
import core.datos.dto.CampañaDTO;
import core.datos.dto.ClinicaDTO;
import core.datos.dto.ProfesionalSaludDTO;
import core.datos.dto.CuidadorDTO;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class DlgCrearCampania extends JDialog {

    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JTextField txtMeta;
    private JComboBox<ClinicaDTO> cmbClinica;
    private JComboBox<ProfesionalSaludDTO> cmbMedico;
    private JComboBox<CuidadorDTO> cmbCuidador;
    private JTextField txtTelefonoMedico;

    private boolean creada = false;
    private CampañaDTO campañaCreada;

    private final CampañaNegocios campañaNegocios = new CampañaNegocios();

    public DlgCrearCampania(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        cargarClinicas();
        cargarCuidadores();
    }

    public boolean fueCreada() {
        return creada;
    }

    public CampañaDTO getCampañaCreada() {
        return campañaCreada;
    }

    // ================== UI ==========================
    private void initComponents() {

        setTitle("Crear nueva campaña");
        setSize(550, 520);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Título:");
        txtTitulo = new JTextField(25);

        JLabel lblDescripcion = new JLabel("Descripción:");
        txtDescripcion = new JTextArea(5, 25);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);

        JLabel lblMeta = new JLabel("Meta (COP):");
        txtMeta = new JTextField(25);

        JLabel lblClinica = new JLabel("Clínica:");
        cmbClinica = new JComboBox<>();
        cmbClinica.setPreferredSize(new Dimension(250, 25));
        cmbClinica.addActionListener(e -> cargarProfesionalesSegunClinica());

        JLabel lblMedico = new JLabel("Profesional salud:");
        cmbMedico = new JComboBox<>();
        cmbMedico.setPreferredSize(new Dimension(250, 25));

        JLabel lblCuidador = new JLabel("Cuidador:");
        cmbCuidador = new JComboBox<>();
        cmbCuidador.setPreferredSize(new Dimension(250, 25));

        JLabel lblTelMedico = new JLabel("Teléfono médico (+57):");
        txtTelefonoMedico = new JTextField(25);
        txtTelefonoMedico.setText("+57");

        JButton btnCrear = new JButton("Crear");
        JButton btnCancelar = new JButton("Cancelar");

        btnCrear.addActionListener(e -> crearCampaña());
        btnCancelar.addActionListener(e -> dispose());

        // ==== add components ====

        gbc.gridx = 0; gbc.gridy = 0; add(lblTitulo, gbc);
        gbc.gridx = 1; add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(lblDescripcion, gbc);
        gbc.gridx = 1; add(scrollDesc, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(lblMeta, gbc);
        gbc.gridx = 1; add(txtMeta, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(lblClinica, gbc);
        gbc.gridx = 1; add(cmbClinica, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(lblMedico, gbc);
        gbc.gridx = 1; add(cmbMedico, gbc);

        gbc.gridx = 0; gbc.gridy = 5; add(lblCuidador, gbc);
        gbc.gridx = 1; add(cmbCuidador, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(lblTelMedico, gbc);
        gbc.gridx = 1; add(txtTelefonoMedico, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnCrear);
        pnlButtons.add(btnCancelar);

        add(pnlButtons, gbc);
    }

    // ================== CARGAR DATOS ==========================

    private void cargarClinicas() {
        cmbClinica.removeAllItems();
        List<ClinicaDTO> lista = campañaNegocios.listarClinicas();

        for (ClinicaDTO c : lista) {
            cmbClinica.addItem(c);
        }
    }

    private void cargarProfesionalesSegunClinica() {
        cmbMedico.removeAllItems();

        ClinicaDTO c = (ClinicaDTO) cmbClinica.getSelectedItem();
        if (c == null) return;

        List<ProfesionalSaludDTO> lista = campañaNegocios.listarProfesionalesPorClinica(c.getIdClinica());

        for (ProfesionalSaludDTO p : lista) {
            cmbMedico.addItem(p);
        }
    }

    private void cargarCuidadores() {
        cmbCuidador.removeAllItems();
        List<CuidadorDTO> lista = campañaNegocios.listarCuidadores();

        for (CuidadorDTO c : lista) {
            cmbCuidador.addItem(c);
        }
    }

    // ================== LÓGICA CREAR ==========================

    private void crearCampaña() {

        if (txtTitulo.getText().trim().isEmpty() ||
            txtDescripcion.getText().trim().isEmpty() ||
            txtMeta.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Complete los campos obligatorios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double meta;
        try {
            meta = Double.parseDouble(txtMeta.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "La meta debe ser un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ========== Construir DTO ==========
        CampañaDTO dto = new CampañaDTO();
        dto.setTitulo(txtTitulo.getText().trim());
        dto.setDescripcion(txtDescripcion.getText().trim());
        dto.setMeta(meta);

        // estos son opcionales
        ClinicaDTO clinica = (ClinicaDTO) cmbClinica.getSelectedItem();
        ProfesionalSaludDTO medico = (ProfesionalSaludDTO) cmbMedico.getSelectedItem();
        CuidadorDTO cuidador = (CuidadorDTO) cmbCuidador.getSelectedItem();

        dto.setIdClinica(clinica != null ? clinica.getIdClinica() : null);
        dto.setIdProfesionalSalud(medico != null ? medico.getIdProfesional() : null);
        dto.setIdCuidador(cuidador != null ? cuidador.getIdCuidador() : null);

        String telefonoMedico = txtTelefonoMedico.getText().trim();

        // ========== Pasar a negocios ==========
        boolean ok = campañaNegocios.registrarCampaña(dto, telefonoMedico);

        if (!ok) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar campaña.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        creada = true;
        campañaCreada = dto;

        JOptionPane.showMessageDialog(this,
                "Campaña creada exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
