package core.ui;

import core.negocios.CampañaNegocios;
import core.datos.dto.CampañaDTO;
import core.datos.dto.ClinicaDTO;
import core.datos.dto.ProfesionalSaludDTO;
import core.datos.dto.CuidadorDTO;
import core.negocios.ClinicaNegocios;
import core.negocios.CuidadorNegocios;
import core.negocios.ProfesionalSaludNegocios;

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

    private final CampañaNegocios campañaNegocios;
    private final ClinicaNegocios clinicaNegocios;
    private final ProfesionalSaludNegocios profesionalNegocios;
    private final CuidadorNegocios cuidadorNegocios;
    private final Integer idPaciente;

    public DlgCrearCampania(Frame owner, boolean modal, Integer idPaciente, CampañaNegocios campañaNegocios) {
        super(owner, modal);
        this.idPaciente = idPaciente;
        this.campañaNegocios = campañaNegocios;

        this.clinicaNegocios = new ClinicaNegocios();
        this.profesionalNegocios = new ProfesionalSaludNegocios();
        this.cuidadorNegocios = new CuidadorNegocios();

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
        gbc.insets = new Insets(5, 5, 5, 5);
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
        cmbClinica.addActionListener(e -> cargarProfesionalesPorClinica());

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
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblTitulo, gbc);
        gbc.gridx = 1;
        add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblDescripcion, gbc);
        gbc.gridx = 1;
        add(scrollDesc, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblMeta, gbc);
        gbc.gridx = 1;
        add(txtMeta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblClinica, gbc);
        gbc.gridx = 1;
        add(cmbClinica, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lblMedico, gbc);
        gbc.gridx = 1;
        add(cmbMedico, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(lblCuidador, gbc);
        gbc.gridx = 1;
        add(cmbCuidador, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(lblTelMedico, gbc);
        gbc.gridx = 1;
        add(txtTelefonoMedico, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnCrear);
        pnlButtons.add(btnCancelar);

        add(pnlButtons, gbc);
    }

    // ================== CARGAR DATOS ==========================
    private void cargarClinicas() {
        cmbClinica.removeAllItems();
        cmbClinica.addItem(null); // opción “ninguna / seleccione”

        var clinicas = clinicaNegocios.listarTodas();   // usa sp_clinica_listar()
        if (clinicas != null) {
            for (ClinicaDTO c : clinicas) {
                cmbClinica.addItem(c);
            }
        }

        // cuando se cambie la clínica, recargamos médicos
        cmbClinica.addActionListener(e -> cargarProfesionalesPorClinica());
    }

    private void cargarProfesionalesPorClinica() {
        cmbMedico.removeAllItems();
        cmbMedico.addItem(null);

        ClinicaDTO seleccionada = (ClinicaDTO) cmbClinica.getSelectedItem();
        if (seleccionada == null) {
            return;
        }

        var medicos = profesionalNegocios.listarPorClinica(seleccionada.getIdClinica());
        if (medicos != null) {
            for (ProfesionalSaludDTO p : medicos) {
                cmbMedico.addItem(p);
            }
        }
    }

    private void cargarCuidadores() {
        cmbCuidador.removeAllItems();
        cmbCuidador.addItem(null);

        // Opción simple: listar todos los cuidadores
        var cuidadores = cuidadorNegocios.listarTodos();   // sp_cuidador_listar()
        if (cuidadores != null) {
            for (CuidadorDTO c : cuidadores) {
                cmbCuidador.addItem(c);
            }
        }
    }    // ================== LÓGICA CREAR ==========================

    private void crearCampaña() {

        if (txtTitulo.getText().trim().isEmpty()
                || txtDescripcion.getText().trim().isEmpty()
                || txtMeta.getText().trim().isEmpty()) {

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

        ClinicaDTO cliSel = (ClinicaDTO) cmbClinica.getSelectedItem();
        ProfesionalSaludDTO medSel = (ProfesionalSaludDTO) cmbMedico.getSelectedItem();
        CuidadorDTO cuiSel = (CuidadorDTO) cmbCuidador.getSelectedItem();

        CampañaDTO c = new CampañaDTO();
        c.setTitulo(txtTitulo.getText().trim());
        c.setDescripcion(txtDescripcion.getText().trim());
        c.setMeta(Double.parseDouble(txtMeta.getText().trim()));
        c.setIdPaciente(idPaciente);
        c.setIdClinica(cliSel != null ? cliSel.getIdClinica() : null);
        c.setIdProfesionalSalud(medSel != null ? medSel.getIdProfesional() : null);
        c.setIdCuidador(cuiSel != null ? cuiSel.getIdCuidador() : null);

        String telefonoMedico = txtTelefonoMedico.getText().trim();

        // ========== Pasar a negocios ==========
        boolean ok = campañaNegocios.registrarCampaña(c, telefonoMedico);

        if (!ok) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar campaña.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        creada = true;
        campañaCreada = c;

        JOptionPane.showMessageDialog(this,
                "Campaña creada exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
