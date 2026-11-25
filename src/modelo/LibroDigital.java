package modelo;

public class LibroDigital extends Producto {
    private String autor;
    private String formato;

    public LibroDigital(String codigo, String nombre, double precio, String autor, String formato) {
        super(codigo, nombre, precio);
        setAutor(autor);
        setFormato(formato);
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        if (formato == null
                || (!formato.equalsIgnoreCase("PDF") && !formato.equalsIgnoreCase("EPUB"))) {
            System.out.println("El formato debe ser PDF o EPUB.");
        } else {
            this.formato = formato.toUpperCase();
        }
    }

    @Override
    public String getTipo() {
        return "Libro digital";
    }
}
