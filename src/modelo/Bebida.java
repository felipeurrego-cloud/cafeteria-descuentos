package modelo;

public class Bebida extends Producto {
    private String tamano;

    public Bebida(String nombre, double precio, String tamano) {
        super(nombre, precio);
        this.tamano = tamano;
    }

    @Override
    public double calcularPrecioFinal() {
        if ("Grande".equalsIgnoreCase(tamano)) {
            return getPrecio() + 1.0;
        }
        return getPrecio();
    }
}
