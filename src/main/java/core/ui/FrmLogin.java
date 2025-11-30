
package core.ui;

import core.datos.dto.UsuarioDTO;
import core.negocios.UsuarioNegocios;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de Login / Registro para SI-SALUD
 */
public class FrmLogin extends JFrame {

    private final UsuarioNegocios usuarioNegocios;

    // Componentes login
    private JTextField txtCorreoLogin;
    private JPasswordField txtClaveLogin;
    private JButton btnLogin;

    // Componentes registro
    private JTextField txtNombreReg;
    private JTextField txtCorreoReg;
    private JTextField txtTelefonoReg;
    private JPasswordField txtClaveReg;
    private JComboBox<String> cmbRolReg;
    private JButton btnRegistrar;

    // Botón cambio de clave
    private JButton btnCambiarClave;

    public FrmLogin() {
        this.usuarioNegocios = new UsuarioNegocios();
        initComponents();
    }

    private void initComponents() {
        setTitle("SI-SALUD - Login / Registro");
        setSize(750, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ========= PANEL LOGIN =========
        JPanel panelLogin = new JPanel(new GridLayout(3, 2, 8, 8));
        panelLogin.setBorder(BorderFactory.createTitledBorder("Iniciar sesión"));

        panelLogin.add(new JLabel("Correo:"));
        txtCorreoLogin = new JTextField();
        panelLogin.add(txtCorreoLogin);

        panelLogin.add(new JLabel("Contraseña:"));
        txtClaveLogin = new JPasswordField();
        panelLogin.add(txtClaveLogin);

        btnLogin = new JButton("Ingresar");
        panelLogin.add(new JLabel()); // celda vacía
        panelLogin.add(btnLogin);

        // ========= PANEL REGISTRO =========
        JPanel panelRegistro = new JPanel(new GridLayout(6, 2, 8, 8));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro"));

        panelRegistro.add(new JLabel("Nombre completo:"));
        txtNombreReg = new JTextField();
        panelRegistro.add(txtNombreReg);

        panelRegistro.add(new JLabel("Correo:"));
        txtCorreoReg = new JTextField();
        panelRegistro.add(txtCorreoReg);

        panelRegistro.add(new JLabel("Teléfono:"));
        txtTelefonoReg = new JTextField();
        panelRegistro.add(txtTelefonoReg);

        panelRegistro.add(new JLabel("Contraseña:"));
        txtClaveReg = new JPasswordField();
        panelRegistro.add(txtClaveReg);

        panelRegistro.add(new JLabel("Rol:"));
        cmbRolReg = new JComboBox<>(new String[]{
                "PACIENTE",
                "CUIDADOR",
                "DONANTE",
                "ADMIN",
                "MEDICO",
                "CLINICA"
        });
        panelRegistro.add(cmbRolReg);

        btnRegistrar = new JButton("Registrar");
        panelRegistro.add(new JLabel());
        panelRegistro.add(btnRegistrar);

        // ========= PANEL CENTRAL (LOGIN + REGISTRO) =========
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentro.add(panelLogin);
        panelCentro.add(panelRegistro);

        // ========= PANEL SUR (CAMBIAR CLAVE) =========
        btnCambiarClave = new JButton("Cambiar contraseña");
        JPanel panelSur = new JPanel();
        panelSur.add(btnCambiarClave);

        // ========= LAYOUT GENERAL =========
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panelCentro, BorderLayout.CENTER);
        getContentPane().add(panelSur, BorderLayout.SOUTH);

        // ========= EVENTOS =========
        btnLogin.addActionListener(e -> hacerLogin());
        btnRegistrar.addActionListener(e -> hacerRegistro());
        btnCambiarClave.addActionListener(e -> abrirDialogoCambioClave());
    }

    // =================== LÓGICA LOGIN ===================

    private void hacerLogin() {
        String correo = txtCorreoLogin.getText().trim();
        String clave = new String(txtClaveLogin.getPassword());

        if (correo.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar correo y contraseña.");
            return;
        }

        UsuarioDTO usuario = usuarioNegocios.login(correo, clave);

        if (usuario != null && usuario.isActivo()) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario.getNombre());

            // Aquí según idRol abres la ventana correspondiente:
            // 1 = PACIENTE, 2 = CUIDADOR, 3 = DONANTE, 4 = ADMIN, 5 = MEDICO, 6 = CLINICA
            switch (usuario.getIdRol()) {
                case 1:
                    // new FrmCampañasPaciente(usuario).setVisible(true);
                    break;
                case 3:
                    // new FrmDonacion(usuario).setVisible(true);
                    break;
                case 4:
                    // new FrmCampañasAdmin(usuario).setVisible(true);
                    break;
                // etc...
                default:
                    JOptionPane.showMessageDialog(this,
                            "Rol aún no asociado a una pantalla. idRol = " + usuario.getIdRol());
            }

            // this.dispose(); // Si quieres cerrar el login al entrar
        } else {
            JOptionPane.showMessageDialog(this,
                    "Correo, contraseña o estado del usuario inválidos.");
        }
    }

    // =================== LÓGICA REGISTRO ===================

    private void hacerRegistro() {
        String nombre = txtNombreReg.getText().trim();
        String correo = txtCorreoReg.getText().trim();
        String telefono = txtTelefonoReg.getText().trim();
        String clave = new String(txtClaveReg.getPassword());
        String rolTexto = (String) cmbRolReg.getSelectedItem();

        if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nombre, correo y contraseña son obligatorios.");
            return;
        }

        int idRol = mapearRolATabla(rolTexto);

        if (idRol <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Rol inválido. Verifique la configuración de roles.");
            return;
        }

        UsuarioDTO u = new UsuarioDTO();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setTelefono(telefono);
        u.setClave(clave);
        u.setIdRol(idRol);

        boolean ok = usuarioNegocios.registrarUsuario(u);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el usuario.");
        }
    }

    /**
     * Mapea el texto del rol al idRol entero.
     * IMPORTANTE: estos valores deben coincidir con tu tabla de roles en BD.
     */
    private int mapearRolATabla(String rolTexto) {
        switch (rolTexto) {
            case "PACIENTE":
                return 1;
            case "CUIDADOR":
                return 2;
            case "DONANTE":
                return 3;
            case "ADMIN":
                return 4;
            case "MEDICO":
                return 5;
            case "CLINICA":
                return 6;
            default:
                return 0;
        }
    }

    // =================== CAMBIO DE CONTRASEÑA ===================

    private void abrirDialogoCambioClave() {
        DlgCambioClave dlg = new DlgCambioClave(this, true, usuarioNegocios);
        dlg.setVisible(true);
    }

    // =================== MAIN PARA PROBAR ===================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }

    // =========================================================
    // =============== DIALOGO CAMBIO DE CLAVE =================
    // =========================================================

    /**
     * Diálogo modal para cambiar la contraseña.
     * Pide: idUsuario, clave actual y clave nueva.
     * Usa UsuarioNegocios.cambiarClave(...)
     */
    private static class DlgCambioClave extends JDialog {

        private final UsuarioNegocios usuarioNegocios;

        private JTextField txtIdUsuario;
        private JPasswordField txtClaveActual;
        private JPasswordField txtClaveNueva;
        private JButton btnCambiar;

        public DlgCambioClave(Frame owner, boolean modal, UsuarioNegocios usuarioNegocios) {
            super(owner, modal);
            this.usuarioNegocios = usuarioNegocios;
            initComponents();
        }

        private void initComponents() {
            setTitle("Cambiar contraseña");
            setSize(400, 230);
            setLocationRelativeTo(getOwner());

            JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            panel.add(new JLabel("ID Usuario:"));
            txtIdUsuario = new JTextField();
            panel.add(txtIdUsuario);

            panel.add(new JLabel("Clave actual:"));
            txtClaveActual = new JPasswordField();
            panel.add(txtClaveActual);

            panel.add(new JLabel("Clave nueva:"));
            txtClaveNueva = new JPasswordField();
            panel.add(txtClaveNueva);

            btnCambiar = new JButton("Cambiar");
            panel.add(new JLabel());
            panel.add(btnCambiar);

            getContentPane().add(panel);

            btnCambiar.addActionListener(e -> cambiarClave());
        }

        private void cambiarClave() {
            try {
                int idUsuario = Integer.parseInt(txtIdUsuario.getText().trim());
                String claveActual = new String(txtClaveActual.getPassword());
                String claveNueva = new String(txtClaveNueva.getPassword());

                if (claveNueva == null || claveNueva.length() < 4) {
                    JOptionPane.showMessageDialog(this,
                            "La nueva contraseña debe tener al menos 4 caracteres.");
                    return;
                }

                boolean ok = usuarioNegocios.cambiarClave(idUsuario, claveActual, claveNueva);

                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Contraseña cambiada correctamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo cambiar la contraseña. Verifique el ID y la clave actual.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID Usuario debe ser un número entero.");
            }
        }
    }
}