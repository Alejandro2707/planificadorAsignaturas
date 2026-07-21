import java.util.ArrayList;

public class Asignatura {
    private String codigo;
    private String nombre;
    private ArrayList<String> pre = new ArrayList<>();

    public Asignatura(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public ArrayList<String> getPre() { return pre; }
    public void agregarPrelacion(String cod) { this.pre.add(cod); }
}