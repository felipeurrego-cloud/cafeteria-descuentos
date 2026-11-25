package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexionBD {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "1234";

    public String probarConexion() {
        try {
            Class.forName(DRIVER);

            try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                if (conn != null) {
                    return "Conexión JDBC exitosa a MySQL (cafeteria_db).";
                }
                return "No fue posible establecer la conexión con MySQL.";
            } catch (SQLException e) {
                return "Error de conexión MySQL (SQLException): " + e.getMessage();
            }

        } catch (ClassNotFoundException e) {
            return "Driver de MySQL no encontrado (ClassNotFoundException): " + e.getMessage();
        }
    }

    public String guardarPedido(String tipoProducto, String detalle, double montoInicial, double totalFinal) {
        String sql = "INSERT INTO pedidos (tipo_producto, detalle, monto_inicial, total_final) VALUES (?, ?, ?, ?)";

        try {
            Class.forName(DRIVER);

            try (Connection conn = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, tipoProducto);
                ps.setString(2, detalle);
                ps.setDouble(3, montoInicial);
                ps.setDouble(4, totalFinal);
                ps.executeUpdate();

                return "Pedido guardado en MySQL.";
            } catch (SQLException e) {
                return "Error al guardar en MySQL (SQLException): " + e.getMessage();
            }

        } catch (ClassNotFoundException e) {
            return "Driver de MySQL no encontrado (ClassNotFoundException): " + e.getMessage();
        }
    }
}