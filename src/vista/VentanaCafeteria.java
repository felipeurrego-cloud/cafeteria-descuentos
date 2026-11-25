package vista;

import datos.ConexionBD;
import logica.CalculadoraDescuento;
import modelo.Bebida;
import modelo.Comida;
import modelo.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCafeteria extends JFrame {

    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbDetalle;
    private JTextField txtMonto;
    private JButton btnCalcular;
    private JButton btnLimpiar;
    private JTextArea txtResultado;

    private CalculadoraDescuento calculadora;
    private ConexionBD conexionBD;

    public VentanaCafeteria() {
        calculadora = new CalculadoraDescuento();
        conexionBD = new ConexionBD();

        setTitle("Cafetería - Gestión de Descuentos");
        setSize(560, 390);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Panel superior: tipo, detalle y precio base
        JPanel panelEntrada = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));

        cmbTipo = new JComboBox<>(new String[]{"Bebida", "Comida"});
        cmbDetalle = new JComboBox<>(new String[]{"Normal", "Grande"});
        txtMonto = new JTextField(12);
        txtMonto.setPreferredSize(new Dimension(120, 26));

        panelEntrada.add(new JLabel("Tipo:"));
        panelEntrada.add(cmbTipo);
        panelEntrada.add(new JLabel("Detalle:"));
        panelEntrada.add(cmbDetalle);
        panelEntrada.add(new JLabel("Precio base ($):"));
        panelEntrada.add(txtMonto);

        cmbTipo.addActionListener(e -> actualizarOpcionesDetalle());

        // Panel central: botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        btnCalcular = new JButton("Calcular Descuento");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnCalcular);
        panelBotones.add(btnLimpiar);

        // Panel inferior: resultados y estado de conexión
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados y Estado de Conexión"));
        txtResultado = new JTextArea(9, 42);
        txtResultado.setEditable(false);
        panelResultados.add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        panelPrincipal.add(panelEntrada, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelResultados, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Probar conexión JDBC al iniciar la aplicación
        String mensajeConexion = conexionBD.probarConexion();
        txtResultado.setText(mensajeConexion + "\n");

        // Evento Calcular Descuento
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarCalculo();
            }
        });

        // Evento Limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtMonto.setText("");
                txtMonto.requestFocus();
            }
        });
    }

    private void actualizarOpcionesDetalle() {
        cmbDetalle.removeAllItems();
        if ("Bebida".equals(cmbTipo.getSelectedItem())) {
            cmbDetalle.addItem("Normal");
            cmbDetalle.addItem("Grande");
        } else {
            cmbDetalle.addItem("En local");
            cmbDetalle.addItem("Para llevar");
        }
    }

    private void procesarCalculo() {
        try {
            double precioBase = Double.parseDouble(txtMonto.getText().trim());
            if (precioBase < 0) {
                txtResultado.append("Ingrese un precio mayor o igual a 0.\n");
                return;
            }

            String tipo = (String) cmbTipo.getSelectedItem();
            String detalle = (String) cmbDetalle.getSelectedItem();

            Producto producto;
            if ("Bebida".equals(tipo)) {
                producto = new Bebida("Bebida Cafetería", precioBase, detalle);
            } else {
                boolean paraLlevar = "Para llevar".equals(detalle);
                producto = new Comida("Comida Cafetería", precioBase, paraLlevar);
            }

            double montoAjustado = producto.calcularPrecioFinal();
            double totalFinal = calculadora.aplicarReglaLambda(montoAjustado);

            txtResultado.append(
                String.format("[%s - %s] Base: $%.2f | Subtotal: $%.2f | Final: $%.2f%n",
                    tipo, detalle, precioBase, montoAjustado, totalFinal)
            );

            String estadoGuardado = conexionBD.guardarPedido(tipo, detalle, montoAjustado, totalFinal);
            txtResultado.append(estadoGuardado + "\n");

        } catch (NumberFormatException ex) {
            txtResultado.append("Error: ingrese un número válido.\n");
        }
    }
}