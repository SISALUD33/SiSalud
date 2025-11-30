
package core.ui;


import core.datos.dto.UsuarioDTO;
import core.negocios.UsuarioNegocios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmAdminUsuarios extends JFrame {

    private JTable tblUsuarios;
    private JButton btnCrear;
    private JButton btnActivarDesactivar;
    private JButton btnRefrescar;

    private final UsuarioNegocios usuarioNegocios;

    public FrmAdminUsuarios() {
        this.usuarioNegocios = new UsuarioNegocios();

        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        setTitle("SI-SALUD - Administración de Usuarios");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tblUsuarios = new JTable();
        JScrollPane scroll = new JScrollPane(tblUsuarios);

        btnCrear = new JButton("Crear Usuario");
        btnActivarDesactivar = new JButton("Activar / Desactivar");
        btnRefrescar = new JButton("Refrescar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnActivarDesactivar);
        panelBotones.add(btnRefrescar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnRefrescar.addActionListener(e -> cargarUsuarios());
        btnCrear.addActionListener(e -> abrirDialogoCrearUsuario());
        btnActivarDesactivar.addActionListener(e -> cambiarEstadoUsuarioSeleccionado());
    }

    // ========================= CARGA DE DATA =============================

    private void cargarUsuarios() {
        List<UsuarioDTO> lista = usuarioNegocios.listarUsuarios();

        String[] columnas = {
            "ID", "Nombre", "Correo", "Teléfono", "Rol", "Activo", "Fecha Registro"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (UsuarioDTO u : lista) {
            Object[] fila = {
                u.getIdUsuario(),
                u.getNombre(),
                u.getCorreo(),
                u.getTelefono(),
                obtenerNombreRol(u.getIdRol()),
                u.isActivo() ? "Sí" : "No",
                u.getFechaRegistro()
            };
            modelo.addRow(fila);
        }

        tblUsuarios.setModel(modelo);
    }

    private String obtenerNombreRol(int idRol) {
        switch (idRol) {
            case 1: return "Paciente";
            case 2: return "Cuidador";
            case 3: return "Donante";
            case 4: return "Médico";
            case 5: return "Clínica";
            case 99: return "Administrador";
            default: return "Rol desconocido";
        }
    }

    // ========================= ACCIONES =================================

    private void abrirDialogoCrearUsuario() {
        DlgCrearUsuario dlg = new DlgCrearUsuario(this, true);
        dlg.setVisible(true);

        if (dlg.fueCreado()) {
            cargarUsuarios();
        }
    }

    private void cambiarEstadoUsuarioSeleccionado() {
        int fila = tblUsuarios.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idUsuario = (int) tblUsuarios.getValueAt(fila, 0);
        boolean activo = "Sí".equals(tblUsuarios.getValueAt(fila, 5));

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de " + (activo ? "desactivar" : "activar") + " este usuario?",
                "Confirmar acción",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) return;

        boolean ok = usuarioNegocios.cambiarEstadoUsuario(idUsuario, !activo);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Usuario actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cambiar el estado del usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====================== MAIN PARA PRUEBAS ==========================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmAdminUsuarios().setVisible(true));
    }
 }