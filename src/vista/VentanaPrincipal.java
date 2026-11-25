package vista;

import modelo.LibroDigital;
import modelo.LibroPapel;
import modelo.Papeleria;
import modelo.Producto;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {
    private ArrayList<Producto> productos;
    private ArrayList<Cliente> clientes;
    private ArrayList<ProductoVenta> carrito;

    private JComboBox<String> tipoProducto;
    private JTextField codigo;
    private JTextField nombre;
    private JTextField precio;
    private JTextField autor;
    private JTextField marca;
    private JTextField stock;
    private JComboBox<String> formato;
    private JPanel datosExtra;
    private JTable tablaProductos;
    private DefaultTableModel modeloTablaProductos;
    private JTextField textoBusqueda;

    private JComboBox<Producto> productoVenta;
    private JSpinner cantidadVenta;
    private JComboBox<Cliente> clienteVenta;
    private JTable tablaCarrito;
    private DefaultTableModel modeloTablaCarrito;

    public VentanaPrincipal() {
        productos = new ArrayList<>();
        clientes = new ArrayList<>();
        carrito = new ArrayList<>();
        cargarDatosIniciales();

        setTitle("Libreria y papeleria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new java.awt.Dimension(850, 600));
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Arial", Font.BOLD, 18));
        pestanas.addTab("Productos", crearPanelProductos());
        pestanas.addTab("Nueva venta", crearPanelVenta());
        add(pestanas);
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(new BorderLayout(8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar producto"));

        JPanel campos = new JPanel(new GridLayout(0, 2, 8, 8));
        tipoProducto = new JComboBox<>(new String[]{"Libro de papel", "Libro digital", "Papeleria"});
        codigo = new JTextField();
        nombre = new JTextField();
        precio = new JTextField();
        campos.add(new JLabel("Tipo de producto:"));
        campos.add(tipoProducto);
        agregarCampo(campos, "Codigo:", codigo);
        agregarCampo(campos, "Nombre:", nombre);
        agregarCampo(campos, "Precio:", precio);

        datosExtra = new JPanel(new GridLayout(0, 2, 8, 8));
        actualizarCamposExtra();
        tipoProducto.addActionListener(e -> actualizarCamposExtra());

        JButton botonRegistrar = crearBoton("Registrar producto");
        botonRegistrar.addActionListener(e -> registrarProducto());

        JPanel parteSuperior = new JPanel(new BorderLayout(8, 8));
        parteSuperior.add(campos, BorderLayout.NORTH);
        parteSuperior.add(datosExtra, BorderLayout.CENTER);
        parteSuperior.add(botonRegistrar, BorderLayout.SOUTH);
        formulario.add(parteSuperior, BorderLayout.CENTER);

        modeloTablaProductos = new DefaultTableModel(
                new String[]{"Tipo", "Codigo", "Nombre", "Autor / marca", "Formato", "Precio", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTablaProductos);
        prepararTabla(tablaProductos);

        JPanel busqueda = new JPanel(new BorderLayout(8, 8));
        textoBusqueda = new JTextField();
        JButton botonBuscar = crearBoton("Buscar por nombre");
        JButton botonMostrarTodos = crearBoton("Mostrar todos");
        botonBuscar.addActionListener(e -> mostrarProductos(textoBusqueda.getText()));
        botonMostrarTodos.addActionListener(e -> {
            textoBusqueda.setText("");
            mostrarProductos("");
        });
        JPanel botonesBusqueda = new JPanel(new GridLayout(1, 2, 8, 8));
        botonesBusqueda.add(botonBuscar);
        botonesBusqueda.add(botonMostrarTodos);
        busqueda.add(new JLabel("Escriba una parte del nombre:"), BorderLayout.NORTH);
        busqueda.add(textoBusqueda, BorderLayout.CENTER);
        busqueda.add(botonesBusqueda, BorderLayout.EAST);

        JPanel lista = new JPanel(new BorderLayout(8, 8));
        lista.setBorder(BorderFactory.createTitledBorder("Productos disponibles y stock"));
        lista.add(busqueda, BorderLayout.NORTH);
        lista.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(lista, BorderLayout.CENTER);
        mostrarProductos("");
        return panel;
    }

    private JPanel crearPanelVenta() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel seleccion = new JPanel(new GridLayout(0, 2, 10, 10));
        seleccion.setBorder(BorderFactory.createTitledBorder("Elija cliente y productos"));
        clienteVenta = new JComboBox<>(clientes.toArray(new Cliente[0]));
        productoVenta = new JComboBox<>();
        productoVenta.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> lista, Object valor,
                                                          int indice, boolean seleccionado,
                                                          boolean enfocado) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, enfocado);
                if (valor instanceof Producto) {
                    Producto producto = (Producto) valor;
                    setText(producto.getCodigo() + " - " + producto.getNombre());
                }
                return this;
            }
        });
        cantidadVenta = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        agregarCampo(seleccion, "Cliente:", clienteVenta);
        agregarCampo(seleccion, "Producto:", productoVenta);
        agregarCampo(seleccion, "Cantidad:", cantidadVenta);

        JButton botonAgregar = crearBoton("Agregar al carrito");
        botonAgregar.addActionListener(e -> agregarAlCarrito());
        seleccion.add(new JLabel(""));
        seleccion.add(botonAgregar);

        modeloTablaCarrito = new DefaultTableModel(
                new String[]{"Codigo", "Producto", "Cantidad", "Precio unitario", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloTablaCarrito);
        prepararTabla(tablaCarrito);

        JButton botonFinalizar = crearBoton("Finalizar venta y ver total");
        botonFinalizar.addActionListener(e -> finalizarVenta());

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createTitledBorder("Productos de esta venta"));
        contenido.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        contenido.add(botonFinalizar, BorderLayout.SOUTH);

        panel.add(seleccion, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);
        actualizarListaProductosVenta();
        return panel;
    }

    private void actualizarCamposExtra() {
        datosExtra.removeAll();
        String tipo = (String) tipoProducto.getSelectedItem();
        if ("Libro de papel".equals(tipo)) {
            autor = new JTextField();
            stock = new JTextField();
            agregarCampo(datosExtra, "Autor:", autor);
            agregarCampo(datosExtra, "Unidades en bodega:", stock);
        } else if ("Libro digital".equals(tipo)) {
            autor = new JTextField();
            formato = new JComboBox<>(new String[]{"PDF", "EPUB"});
            agregarCampo(datosExtra, "Autor:", autor);
            agregarCampo(datosExtra, "Formato:", formato);
        } else {
            marca = new JTextField();
            stock = new JTextField();
            agregarCampo(datosExtra, "Marca:", marca);
            agregarCampo(datosExtra, "Unidades disponibles:", stock);
        }
        datosExtra.revalidate();
        datosExtra.repaint();
    }

    private void registrarProducto() {
        String nuevoCodigo = codigo.getText().trim();
        String nuevoNombre = nombre.getText().trim();
        String precioTexto = precio.getText().trim();

        if (nuevoCodigo.isEmpty() || nuevoNombre.isEmpty() || precioTexto.isEmpty()) {
            mostrarError("Complete codigo, nombre y precio. No deje campos en blanco.");
            return;
        }
        if (buscarPorCodigo(nuevoCodigo) != null) {
            mostrarError("Ese codigo ya existe. Escriba otro codigo.");
            return;
        }

        double precioNumero;
        try {
            precioNumero = Double.parseDouble(precioTexto.replace(',', '.'));
        } catch (NumberFormatException error) {
            mostrarError("Escriba un precio usando numeros, por ejemplo 12.50.");
            return;
        }
        if (precioNumero <= 0) {
            mostrarError("El precio debe ser mayor que cero.");
            return;
        }

        Producto productoNuevo;
        String tipo = (String) tipoProducto.getSelectedItem();
        if ("Libro de papel".equals(tipo)) {
            String autorTexto = autor.getText().trim();
            Integer stockNumero = leerStock(stock);
            if (autorTexto.isEmpty() || stockNumero == null) {
                return;
            }
            productoNuevo = new LibroPapel(nuevoCodigo, nuevoNombre, precioNumero, autorTexto, stockNumero);
        } else if ("Libro digital".equals(tipo)) {
            String autorTexto = autor.getText().trim();
            if (autorTexto.isEmpty()) {
                mostrarError("Escriba el autor del libro.");
                return;
            }
            productoNuevo = new LibroDigital(nuevoCodigo, nuevoNombre, precioNumero, autorTexto,
                    (String) formato.getSelectedItem());
        } else {
            String marcaTexto = marca.getText().trim();
            Integer stockNumero = leerStock(stock);
            if (marcaTexto.isEmpty() || stockNumero == null) {
                if (marcaTexto.isEmpty()) {
                    mostrarError("Escriba la marca del producto.");
                }
                return;
            }
            productoNuevo = new Papeleria(nuevoCodigo, nuevoNombre, precioNumero, marcaTexto, stockNumero);
        }

        productos.add(productoNuevo);
        mostrarProductos("");
        actualizarListaProductosVenta();
        codigo.setText("");
        nombre.setText("");
        precio.setText("");
        JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
    }

    private Integer leerStock(JTextField campo) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            mostrarError("Escriba las unidades disponibles.");
            return null;
        }
        try {
            int numero = Integer.parseInt(texto);
            if (numero < 0) {
                mostrarError("Las unidades no pueden ser negativas.");
                return null;
            }
            return numero;
        } catch (NumberFormatException error) {
            mostrarError("Las unidades deben ser un numero entero, como 5.");
            return null;
        }
    }

    private void agregarAlCarrito() {
        Producto producto = (Producto) productoVenta.getSelectedItem();
        if (producto == null) {
            mostrarError("Primero registre o elija un producto.");
            return;
        }
        int cantidad = (Integer) cantidadVenta.getValue();
        int cantidadExistente = 0;
        for (ProductoVenta linea : carrito) {
            if (linea.producto.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                cantidadExistente += linea.cantidad;
            }
        }

        int stockDisponible = obtenerStock(producto);
        if (stockDisponible >= 0 && cantidadExistente + cantidad > stockDisponible) {
            mostrarError("No hay suficientes unidades. Stock disponible: " + stockDisponible + ".");
            return;
        }

        ProductoVenta lineaExistente = null;
        for (ProductoVenta linea : carrito) {
            if (linea.producto.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                lineaExistente = linea;
            }
        }
        if (lineaExistente == null) {
            carrito.add(new ProductoVenta(producto, cantidad));
        } else {
            lineaExistente.cantidad += cantidad;
        }
        mostrarCarrito();
    }

    private void finalizarVenta() {
        if (carrito.isEmpty()) {
            mostrarError("Agregue al menos un producto antes de finalizar.");
            return;
        }

        Cliente cliente = (Cliente) clienteVenta.getSelectedItem();
        double subtotal = 0;
        double descuento = 0;
        double iva = 0;
        StringBuilder detalle = new StringBuilder();

        for (ProductoVenta linea : carrito) {
            Producto producto = linea.producto;
            double importe = redondear(producto.getPrecio() * linea.cantidad);
            double descuentoLinea = 0;
            double ivaLinea = 0;

            if (cliente.socio && (producto instanceof LibroPapel || producto instanceof LibroDigital)) {
                descuentoLinea = redondear(importe * 0.10);
            }
            if (producto instanceof Papeleria) {
                ivaLinea = redondear(importe * 0.15);
            }

            subtotal += importe;
            descuento += descuentoLinea;
            iva += ivaLinea;
            detalle.append(producto.getNombre()).append(" x ").append(linea.cantidad)
                    .append(" | Subtotal: $").append(String.format("%.2f", importe))
                    .append(" | Descuento: $").append(String.format("%.2f", descuentoLinea))
                    .append(" | IVA: $").append(String.format("%.2f", ivaLinea))
                    .append(" | Total: $").append(String.format("%.2f",
                            importe - descuentoLinea + ivaLinea))
                    .append('\n');
        }

        subtotal = redondear(subtotal);
        descuento = redondear(descuento);
        iva = redondear(iva);
        double total = redondear(subtotal - descuento + iva);

        int confirmar = JOptionPane.showConfirmDialog(this,
                detalle + "\nSubtotal: $" + String.format("%.2f", subtotal)
                        + "\nDescuento: $" + String.format("%.2f", descuento)
                        + "\nIVA: $" + String.format("%.2f", iva)
                        + "\nTOTAL: $" + String.format("%.2f", total)
                        + "\n\n¿Desea registrar esta venta?",
                "Resumen de venta", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        for (ProductoVenta linea : carrito) {
            Producto producto = linea.producto;
            if (producto instanceof LibroPapel) {
                LibroPapel libro = (LibroPapel) producto;
                libro.setStock(libro.getStock() - linea.cantidad);
            } else if (producto instanceof Papeleria) {
                Papeleria articulo = (Papeleria) producto;
                articulo.setStock(articulo.getStock() - linea.cantidad);
            }
        }

        carrito.clear();
        mostrarCarrito();
        mostrarProductos("");
        JOptionPane.showMessageDialog(this, "Venta registrada. Total pagado: $"
                + String.format("%.2f", total));
    }

    private void cargarDatosIniciales() {
        productos.add(new LibroPapel("L001", "Cien años de soledad", 18.50, "Gabriel Garcia Marquez", 8));
        productos.add(new LibroPapel("L002", "La casa de los espiritus", 16.00, "Isabel Allende", 5));
        productos.add(new LibroDigital("D001", "El principito digital", 7.25, "Antoine de Saint-Exupery", "PDF"));
        productos.add(new LibroDigital("D002", "Don Quijote digital", 9.50, "Miguel de Cervantes", "EPUB"));
        productos.add(new Papeleria("P001", "Cuaderno universitario", 2.50, "Norma", 20));
        productos.add(new Papeleria("P002", "Esfero azul", 0.75, "Bic", 30));
        clientes.add(new Cliente("Ana Socia", true));
        clientes.add(new Cliente("Luis Cliente", false));
    }

    private void mostrarProductos(String busqueda) {
        modeloTablaProductos.setRowCount(0);
        String texto = busqueda == null ? "" : busqueda.trim();
        for (Producto producto : productos) {
            if (!producto.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                continue;
            }
            String autorMarca = "";
            String formatoTexto = "";
            String stockTexto = "Disponible";
            if (producto instanceof LibroPapel) {
                LibroPapel libro = (LibroPapel) producto;
                autorMarca = libro.getAutor();
                stockTexto = String.valueOf(libro.getStock());
            } else if (producto instanceof LibroDigital) {
                LibroDigital libro = (LibroDigital) producto;
                autorMarca = libro.getAutor();
                formatoTexto = libro.getFormato();
            } else if (producto instanceof Papeleria) {
                Papeleria articulo = (Papeleria) producto;
                autorMarca = articulo.getMarca();
                stockTexto = String.valueOf(articulo.getStock());
            }
            modeloTablaProductos.addRow(new Object[]{
                    producto.getTipo(), producto.getCodigo(), producto.getNombre(),
                    autorMarca, formatoTexto, String.format("%.2f", producto.getPrecio()), stockTexto
            });
        }
    }

    private void mostrarCarrito() {
        modeloTablaCarrito.setRowCount(0);
        for (ProductoVenta linea : carrito) {
            modeloTablaCarrito.addRow(new Object[]{
                    linea.producto.getCodigo(),
                    linea.producto.getNombre(),
                    linea.cantidad,
                    String.format("%.2f", linea.producto.getPrecio()),
                    String.format("%.2f", linea.producto.getPrecio() * linea.cantidad)
            });
        }
    }

    private void actualizarListaProductosVenta() {
        if (productoVenta == null) {
            return;
        }
        productoVenta.removeAllItems();
        for (Producto producto : productos) {
            productoVenta.addItem(producto);
        }
    }

    private Producto buscarPorCodigo(String codigoBuscado) {
        for (Producto producto : productos) {
            if (producto.getCodigo().equalsIgnoreCase(codigoBuscado.trim())) {
                return producto;
            }
        }
        return null;
    }

    private int obtenerStock(Producto producto) {
        if (producto instanceof LibroPapel) {
            return ((LibroPapel) producto).getStock();
        }
        if (producto instanceof Papeleria) {
            return ((Papeleria) producto).getStock();
        }
        return -1;
    }

    private void agregarCampo(JPanel panel, String texto, Component campo) {
        panel.add(new JLabel(texto));
        panel.add(campo);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusable(false);
        return boton;
    }

    private void prepararTabla(JTable tabla) {
        tabla.setFont(new Font("Arial", Font.PLAIN, 15));
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Revise los datos", JOptionPane.WARNING_MESSAGE);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private static class Cliente {
        private String nombre;
        private boolean socio;

        private Cliente(String nombre, boolean socio) {
            this.nombre = nombre;
            this.socio = socio;
        }

        @Override
        public String toString() {
            if (socio) {
                return nombre + " (socio, 10% en libros)";
            }
            return nombre + " (sin tarjeta)";
        }
    }

    private static class ProductoVenta {
        private Producto producto;
        private int cantidad;

        private ProductoVenta(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }
    }
}
