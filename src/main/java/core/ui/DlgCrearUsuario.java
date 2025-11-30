package core.ui;

import core.datos.dto.UsuarioDTO;
import core.datos.dto.ClinicaDTO;
import core.negocios.UsuarioNegocios;
import core.negocios.ClinicaNegocios;
import core.negocios.PacienteNegocios;
import core.negocios.CuidadorNegocios;
import core.negocios.ProfesionalSaludNegocios;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DlgCrearUsuario extends JDialog {

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JPasswordField txtClave;
    private JComboBox<String> cbRol;

    // Paneles condicionales
    private JPanel panelClinica;
    private JPanel panelMedico;
    private JPanel panelPaciente;

    // Campos clínica
    private JTextField txtClinicaNombre;
    private JTextField txtClinicaNit;
    private JTextField txtClinicaDireccion;
    private JTextField txtClinicaTel;
    private JTextField txtClinicaCorreo;

    // Campos médico
    private JComboBox<String> cbClinicaMedico;
    private JTextField txtEspecialidad;
    private JTextField txtRegistro;

    // Campos paciente
    private JComboBox<String> cbClinicaPaciente;
    private JTextArea txtObservaciones;

    private boolean creado = false;

    private final UsuarioNegocios usuarioNegocios;
    private final PacienteNegocios pacienteNegocios;

    public DlgCrearUsuario(Frame parent, boolean modal) {
        super(parent, modal);
        usuarioNegocios = new UsuarioNegocios();
        pacienteNegocios = new PacienteNegocios();

        initComponents();
        cargarClinicasEnCombos();
    }

    public boolean fueCreado() {
        return creado;
    }

    private void initComponents() {
        setTitle("Crear nuevo usuario");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(20, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtTelefono = new JTextField("+57 ");
        txtClave = new JPasswordField();

        cbRol = new JComboBox<>(new String[]{
            "Seleccione un rol",
            "Paciente",
            "Cuidador",
            "Donante",
            "Médico",
            "Clínica",
            "Administrador"
        });

        // ---- Panel Clínica ----
        panelClinica = new JPanel(new GridLayout(5, 2, 5, 5));
        panelClinica.setBorder(BorderFactory.createTitledBorder("Datos Clínica"));
        txtClinicaNombre = new JTextField();
        txtClinicaNit = new JTextField();
        txtClinicaDireccion = new JTextField();
        txtClinicaTel = new JTextField();
        txtClinicaCorreo = new JTextField();
        panelClinica.add(new JLabel("Nombre:"));
        panelClinica.add(txtClinicaNombre);
        panelClinica.add(new JLabel("NIT:"));
        panelClinica.add(txtClinicaNit);
        panelClinica.add(new JLabel("Dirección:"));
        panelClinica.add(txtClinicaDireccion);
        panelClinica.add(new JLabel("Teléfono:"));
        panelClinica.add(txtClinicaTel);
        panelClinica.add(new JLabel("Correo:"));
        panelClinica.add(txtClinicaCorreo);
        panelClinica.setVisible(false);

        // ---- Panel Médico ----
        panelMedico = new JPanel(new GridLayout(3, 2, 5, 5));
        panelMedico.setBorder(BorderFactory.createTitledBorder("Datos del Médico"));
        cbClinicaMedico = new JComboBox<>();
        txtEspecialidad = new JTextField();
        txtRegistro = new JTextField();
        panelMedico.add(new JLabel("Clínica:"));
        panelMedico.add(cbClinicaMedico);
        panelMedico.add(new JLabel("Especialidad:"));
        panelMedico.add(txtEspecialidad);
        panelMedico.add(new JLabel("Registro Médico:"));
        panelMedico.add(txtRegistro);
        panelMedico.setVisible(false);

        // ---- Panel Paciente ----
        panelPaciente = new JPanel(new GridLayout(2, 2, 5, 5));
        panelPaciente.setBorder(BorderFactory.createTitledBorder("Datos Paciente"));
        cbClinicaPaciente = new JComboBox<>();
        txtObservaciones = new JTextArea(3, 10);
        panelPaciente.add(new JLabel("Clínica (opcional):"));
        panelPaciente.add(cbClinicaPaciente);
        panelPaciente.add(new JLabel("Observaciones:"));
        panelPaciente.add(new JScrollPane(txtObservaciones));
        panelPaciente.setVisible(false);

        // Eventos dinámicos
        cbRol.addActionListener(e -> actualizarPaneles());

        // FORM BASE
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Correo:"));
        panelForm.add(txtCorreo);
        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Clave:"));
        panelForm.add(txtClave);
        panelForm.add(new JLabel("Rol:"));
        panelForm.add(cbRol);

        // Unimos todo
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(panelForm, BorderLayout.NORTH);
        contenedor.add(panelClinica, BorderLayout.CENTER);
        contenedor.add(panelMedico, BorderLayout.SOUTH);

        JPanel contenedor2 = new JPanel(new BorderLayout());
        contenedor2.add(contenedor, BorderLayout.CENTER);
        contenedor2.add(panelPaciente, BorderLayout.SOUTH);

        add(contenedor2, BorderLayout.CENTER);

        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarUsuario());
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarPaneles() {
        String rol = cbRol.getSelectedItem().toString();

        panelClinica.setVisible(rol.equals("Clínica"));
        panelMedico.setVisible(rol.equals("Médico"));
        panelPaciente.setVisible(rol.equals("Paciente"));

        repaint();
        revalidate();
    }

    private void cargarClinicasEnCombos() {
        // Aquí iría la llamada real a sp_clinica_listar cuando creemos ClinicaNegocios.
        // Por ahora solo ejemplo:
        cbClinicaMedico.addItem("1 - Clínica Ejemplo");
        cbClinicaPaciente.addItem("1 - Clínica Ejemplo");
    }

    private void guardarUsuario() {

        // Validaciones
        if (txtNombre.getText().isBlank()
                || txtCorreo.getText().isBlank()
                || txtClave.getPassword().length == 0
                || cbRol.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.");
            return;
        }

        UsuarioDTO u = new UsuarioDTO();
        u.setNombre(txtNombre.getText());
        u.setCorreo(txtCorreo.getText());
        u.setTelefono(txtTelefono.getText());
        u.setClave(new String(txtClave.getPassword()));
        u.setIdRol(cbRol.getSelectedIndex()); // El índice coincide con tu diseño

        // Crear usuario y obtener ID
        Integer idUsuario = usuarioNegocios.registrarUsuarioYRetornarID(u);

        if (idUsuario == null) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el usuario.");
            return;
        }

        // CREAR ENTIDAD SEGÚN ROL
        switch (cbRol.getSelectedItem().toString()) {

            case "Paciente":
                pacienteNegocios.obtenerOCrearPacientePorUsuario(idUsuario);
                break;

            case "Cuidador":
                // TODO: cuidadorNegocios.crear(idUsuario)
                break;

            case "Médico":
                // TODO: profesionalNegocios.crear(...)
                break;

            case "Clínica":
                // TODO: clinicaNegocios.crear(...)
                break;

            default:
                break;
        }

        creado = true;
        JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
        dispose();
    }
}
