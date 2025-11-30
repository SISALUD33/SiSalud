package core.ui;

import core.datos.dto.CampañaDTO;
import core.datos.dto.UsuarioDTO;
import core.negocios.CampañaNegocios;
import core.negocios.PacienteNegocios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Pantalla principal para el rol PACIENTE. Muestra las campañas del paciente y
 * permite gestionarlas.
 */
public class FrmCampañasPaciente extends JFrame {

    private final UsuarioDTO usuario;
    private final CampañaNegocios campañaNegocios;

    private JTable tblCampanias;
    private JButton btnCrear;
    private JButton btnCerrar;
    private JButton btnRefrescar;

    private Integer idPaciente;

    public FrmCampañasPaciente(UsuarioDTO usuario) {
        this.usuario = usuario;
        this.campañaNegocios = new CampañaNegocios();

        initComponents();

        // Resolver o crear paciente a partir del usuario logueado
        PacienteNegocios pacienteNegocios = new PacienteNegocios();
        this.idPaciente = pacienteNegocios.obtenerOCrearPacientePorUsuario(usuario.getIdUsuario());

        if (this.idPaciente == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo asociar un PACIENTE a este usuario.\n"
                    + "Contacte al administrador.");
            btnCrear.setEnabled(false);
            btnCerrar.setEnabled(false);
        } else {
            cargarCampaniasPaciente();
        }
    }

    private void initComponents() {
        setTitle("SI-SALUD - Mis campañas (Paciente: " + usuario.getNombre() + ")");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Tabla de campañas =====
        tblCampanias = new JTable();
        JScrollPane scroll = new JScrollPane(tblCampanias);

        // ===== Botones =====
        btnCrear = new JButton("Crear campaña");
        btnCerrar = new JButton("Cerrar campaña");
        btnRefrescar = new JButton("Refrescar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrear);
        panelBotones.add(btnCerrar);
        panelBotones.add(btnRefrescar);

        // ===== Layout general =====
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // ===== Eventos =====
        btnRefrescar.addActionListener(e -> cargarCampaniasPaciente());
        btnCerrar.addActionListener(e -> cerrarCampaniaSeleccionada());

        // TODO: en la parte B conectamos este botón a DlgCrearCampaña
        btnCrear.addActionListener(e -> abrirDialogoCrearCampania());

    }

    // ================== LÓGICA DE NEGOCIOS ==================
    /**
     * Carga las campañas del paciente desde la capa de negocios y las muestra
     * en la tabla.
     */
    private void cargarCampaniasPaciente() {
        List<CampañaDTO> campañas = campañaNegocios.obtenerCampaniasPorPaciente(idPaciente);

        String[] columnas = {
            "ID", "Título", "Meta", "Recaudado",
            "Estado", "Fecha creación"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla solo lectura
            }
        };

        for (CampañaDTO c : campañas) {
            Object[] fila = {
                c.getIdCampania(),
                c.getTitulo(),
                c.getMeta(),
                c.getRecaudado(),
                c.getEstado(),
                c.getFechaCreacion()
            };
            modelo.addRow(fila);
        }

        tblCampanias.setModel(modelo);
    }

    /**
     * Cambia el estado de la campaña seleccionada a CERRADA.
     */
    private void cerrarCampaniaSeleccionada() {
        int fila = tblCampanias.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una campaña.");
            return;
        }

        int idCampania = (int) tblCampanias.getValueAt(fila, 0);
        String estadoActual = (String) tblCampanias.getValueAt(fila, 4);

        if ("CERRADA".equalsIgnoreCase(estadoActual)) {
            JOptionPane.showMessageDialog(this, "La campaña ya está cerrada.");
            return;
        }

        int resp = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de cerrar la campaña seleccionada?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (resp != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = campañaNegocios.cambiarEstadoCampania(idCampania, "CERRADA");

        if (ok) {
            JOptionPane.showMessageDialog(this, "Campaña cerrada correctamente.");
            cargarCampaniasPaciente();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo cerrar la campaña.");
        }
    }

    // ====== MAIN opcional para probar esta pantalla sola ======
    public static void main(String[] args) {
        // SOLO PARA PRUEBAS: creamos un usuario dummy
        UsuarioDTO u = new UsuarioDTO();
        u.setIdUsuario(1);
        u.setNombre("Paciente Prueba");
        u.setIdRol(1);
        u.setActivo(true);

        SwingUtilities.invokeLater(() -> new FrmCampañasPaciente(u).setVisible(true));
    }

    private void abrirDialogoCrearCampania() {
        DlgCrearCampania dlg = new DlgCrearCampania(this, true);
        dlg.setVisible(true);

        if (dlg.fueCreada()) {
            cargarCampaniasPaciente();
        }

    }
}
