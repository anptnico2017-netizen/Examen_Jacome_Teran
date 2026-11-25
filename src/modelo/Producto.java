package modelo;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;

    public Producto(String codigo, String nombre, double precio) {
        setCodigo(codigo);
        setNombre(nombre);
        setPrecio(precio);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println("El codigo no puede estar vacio.");
        } else {
            this.codigo = codigo.trim();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
        } else {
            this.nombre = nombre.trim();
        }
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            System.out.println("El precio debe ser mayor que cero.");
        } else {
            this.precio = precio;
        }
    }

    public String getTipo() {
        return "Producto";
    }
}
