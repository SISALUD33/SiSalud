package core.ui;

import core.datos.dto.CampañaDTO;
import core.datos.dto.UsuarioDTO;
import core.datos.dto.DonacionDTO;
import core.negocios.CampañaNegocios;
import core.negocios.DonacionNegocios;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FrmDonacion extends JFrame {

    private final UsuarioDTO usuario;
    private final CampañaNegocios campañaNegocios;
    private final DonacionNegocios donacionNegocios;

    // Componentes UI
    private JComboBox<CampañaDTO> cmbCampañas;
    private JTextField txtMonto;
    private JComboBox<String> cmbMedioPago;
    private JTextField txtEntidadBancaria;
    private JTextField txtDestinoRecursos;
    private JTextField txtReferenciaPago;
    private JButton btnDonar;
    private JButton btnCerrar;

    public FrmDonacion(UsuarioDTO usuario) {
        this.usuario = usuario;
        this.campañaNegocios = new CampañaNegocios();
        this.donacionNegocios = new DonacionNegocios();

        initComponents();
        cargarCampañasAprobadas();
    }

    private void initComponents() {
        setTitle("SI-SALUD - Donaciones (" + usuario.getNombre() + ")");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Campaña
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Campaña:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        cmbCampañas = new JComboBox<>();
        panel.add(cmbCampañas, gbc);
        gbc.gridwidth = 1;
        fila++;

        // Monto
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Monto (COP):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        txtMonto = new JTextField();
        panel.add(txtMonto, gbc);
        fila++;

        // Medio de pago
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Medio de pago:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        cmbMedioPago = new JComboBox<>(new String[]{
            "TARJETA",
            "PSE",
            "TRANSFERENCIA",
            "EFECTIVO"
        });
        panel.add(cmbMedioPago, gbc);
        fila++;

        // Entidad bancaria
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Entidad bancaria:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        txtEntidadBancaria = new JTextField();
        panel.add(txtEntidadBancaria, gbc);
        fila++;

        // Destino recursos (opcional)
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Destino recursos (opcional):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        txtDestinoRecursos = new JTextField();
        panel.add(txtDestinoRecursos, gbc);
        fila++;

        // Referencia pago (opcional)
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Referencia pago (opcional):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        txtReferenciaPago = new JTextField();
        panel.add(txtReferenciaPago, gbc);
        fila++;

        // Botones
        JPanel panelBotones = new JPanel();
        btnDonar = new JButton("Donar");
        btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnDonar);
        panelBotones.add(btnCerrar);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        panel.add(panelBotones, gbc);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);

        // Eventos
        btnDonar.addActionListener(e -> hacerDonacion());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void cargarCampañasAprobadas() {
        List<CampañaDTO> campañas = campañaNegocios.obtenerCampañasAprobadas();
        cmbCampañas.removeAllItems();

        if (campañas == null || campañas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay campañas aprobadas disponibles para donar.");
            return;
        }

        for (CampañaDTO c : campañas) {
            cmbCampañas.addItem(c);
        }
    }

    private void hacerDonacion() {
        CampañaDTO campañaSeleccionada = (CampañaDTO) cmbCampañas.getSelectedItem();
        if (campañaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una campaña.");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor que cero.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto inválido.");
            return;
        }

        String medioPago = (String) cmbMedioPago.getSelectedItem();
        String entidadBancaria = txtEntidadBancaria.getText().trim();

        if (entidadBancaria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La entidad bancaria es obligatoria.");
            return;
        }

        String destino = txtDestinoRecursos.getText().trim();
        String referencia = txtReferenciaPago.getText().trim();

        // Armar DTO
        DonacionDTO d = new DonacionDTO();
        d.setIdCampana(campañaSeleccionada.getIdCampania());   // ojo: idCampana SIN ñ
        d.setIdUsuarioDonante(usuario.getIdUsuario());
        d.setMonto(monto);
        d.setMedioPago(medioPago);
        d.setEntidadBancaria(entidadBancaria);
        d.setDestinoRecursos(destino.isEmpty() ? null : destino);
        d.setReferenciaPago(referencia.isEmpty() ? null : referencia);

        String celularDonante = usuario.getTelefono();
        boolean ok = donacionNegocios.registrarDonacion(d, celularDonante);
        if (ok) {
            JOptionPane.showMessageDialog(this, "¡Gracias por su donación!");
            // aquí podrías limpiar campos o cerrar
            txtMonto.setText("");
            txtReferenciaPago.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la donación.");
        }
    }

    // Para pruebas rápidas
    public static void main(String[] args) {
        UsuarioDTO u = new UsuarioDTO();
        u.setIdUsuario(3);
        u.setNombre("Donante Prueba");
        u.setIdRol(3);

        SwingUtilities.invokeLater(() -> new FrmDonacion(u).setVisible(true));
    }
}
