package modelo;

public class LibroPapel extends Producto {
    private String autor;
    private int stock;

    public LibroPapel(String codigo, String nombre, double precio, String autor, int stock) {
        super(codigo, nombre, precio);
        setAutor(autor);
        setStock(stock);
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            System.out.println("El autor no puede estar vacio.");
        } else {
            this.autor = autor.trim();
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
        return "Libro de papel";
    }
}
