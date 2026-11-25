package logica;

public class CalculadoraDescuento {

    public double aplicarReglaLambda(double monto) {
        DescuentoFuncional descuento = total -> {
            if (total > 10.0) {
                return total * 0.85; // 15% de descuento
            } else {
                return total; // 0% de descuento
            }
        };
        return descuento.calcular(monto);
    }
}