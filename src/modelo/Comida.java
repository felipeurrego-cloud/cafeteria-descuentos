package modelo;

public class Comida extends Producto {
    private boolean paraLlevar;

    public Comida(String nombre, double precio, boolean paraLlevar) {
        super(nombre, precio);
        this.paraLlevar = paraLlevar;
    }

    public boolean isParaLlevar() {
        return paraLlevar;
    }

    @Override
    public double calcularPrecioFinal() {
        if (paraLlevar) {
            return getPrecio() + 0.5;
        }
        return getPrecio();
    }
}