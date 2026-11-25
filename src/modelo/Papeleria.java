package modelo;

public class Papeleria extends Producto {
    private String marca;
    private int stock;

    public Papeleria(String codigo, String nombre, double precio, String marca, int stock) {
        super(codigo, nombre, precio);
        setMarca(marca);
        setStock(stock);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            System.out.println("La marca no puede estar vacia.");
        } else {
            this.marca = marca.trim();
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println("El stock no puede ser negativo.");
        } else {
            this.stock = stock;
        }
    }

    @Override
    public String getTipo() {
        return "Papeleria";
    }
}
