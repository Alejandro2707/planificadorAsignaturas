import java.util.ArrayList;

public class GestionPensum {
    public ArrayList<Asignatura> listaMaterias = new ArrayList<>();

    public GestionPensum() { cargarPensum(); }

    private void cargarPensum() {
        // --- SEMESTRE 1 ---
        agregar("1472101", "COMPRENSION Y EXPRESION LINGUISTICA", null);
        agregar("1472102", "ORIENTACION Y CULTURA UNIVERSITARIA", null);
        agregar("1472103", "TECNICAS DE ESTUDIO E INVESTIGACION", null);
        agregar("1472104", "MATEMATICA I", null);
        agregar("1472105", "QUIMICA GENERAL", null);
        agregar("1472106", "FUNDAMENTOS DE LA INFORMATICA", null);
        agregar("1472107", "LOGICA COMPUTACIONAL", null);

        // --- SEMESTRE 2 ---
        agregar("1472208", "FISICA I", "1472104");
        agregar("1472209", "MATEMATICA II", "1472104");
        agregar("1472210", "DIBUJO E INTERPRETACION DE PLANOS", "1472104");
        agregar("1472211", "TECNICAS DE PROGRAMACION I", "1472107,1472106");
        agregar("1472212", "INGLES I", null);
        agregar("1472213", "ALGEBRA LINEAL", "1472104");
        agregar("1472214", "INTRODUCCION A LA ADMINISTRACION", "1472104");

        // --- SEMESTRE 3 ---
        agregar("1472315", "DESARROLLO HUMANO", null);
        agregar("1472316", "FISICA II", "1472208");
        agregar("1472317", "LABORATORIO DE FISICA I", "1472208");
        agregar("1472318", "INGLES TECNICO", "1472212");
        agregar("1472319", "MATEMATICA III", "1472209");
        agregar("1472320", "MATEMATICAS DISCRETAS", "1472213,1472209");
        agregar("1472321", "TECNICAS DE PROGRAMACION II", "1472211");

        // --- SEMESTRE 4 ---
        agregar("1472422", "MATEMATICA IV", "1472319");
        agregar("1472423", "ESTADISTICA I", "1472209");
        agregar("1472424", "TECNICAS DE PROGRAMACION III", "1472321");
        agregar("1472425", "CIRCUITOS Y SISTEMAS DIGITALES", "1472316");
        agregar("1472427", "LABORATORIO DE FISICA II", "1472317,1472316");
        agregar("1472428", "ESTRUCTURA DE DATOS", "1472321,1472320");

        // --- SEMESTRE 5 ---
        agregar("1472529", "ESTADISTICA II", "1472423");
        agregar("1472530", "SISTEMAS DE BASES DE DATOS I", "1472428,1472424");
        agregar("1472531", "ARQUITECTURA DEL COMPUTADOR", "1472425");
        agregar("1472532", "CALCULO NUMERICO", "1472422");
        agregar("1472533", "FINANZAS PARA INGENIEROS", "1472214");
        agregar("1472534", "ALGEBRA DE ESTRUCTURAS", "1472320");

        // --- SEMESTRE 6 ---
        agregar("1472634", "INVESTIGACION DE OPERACIONES", "1472534,1472529");
        agregar("1472635", "SISTEMAS DE OPERACION", "1472531,1472428");
        agregar("1472636", "INGENIERIA DEL SOFTWARE I", "1472530");
        agregar("1472637", "SISTEMAS DE BASES DE DATOS II", "1472530");
        agregar("1472638", "INFORMATICA INDUSTRIAL", "1472422,1472531,1472530");
        agregar("1472639", "INNOVACION Y DESARROLLO", "1472533,1472529,1472531");

        // --- SEMESTRE 7 ---
        agregar("2472740", "SEMINARIO DE INVESTIGACION", null);
        agregar("2472742", "REDES DE COMPUTADORAS I", "1472635");
        agregar("2472743", "INGENIERIA DEL SOFTWARE II", "1472637,1472636");
        agregar("2472744", "LENGUAJES Y COMPILADORES", "1472635");
        agregar("2472745", "TENDENCIAS INFORMATICAS", "1472639");

        // --- SEMESTRE 8 ---
        agregar("2472847", "SISTEMAS DE LA CALIDAD", "1472634");
        agregar("2472848", "INGENIERIA ECONOMICA", "1472634,1472533");
        agregar("2472849", "AUDITORIA DE TECNOLOGIAS Y SISTEMAS DE INFORMACION", "2472745,2472743");
        agregar("2472850", "TELECOMUNICACIONES I", "2472742,1472422");
        agregar("2472851", "SISTEMAS DISTRIBUIDOS", "2472744");

        // --- SEMESTRE 9 ---
        agregar("2472954", "SIMULACION INFORMATICA", "1472634");
        agregar("2472955", "SEGURIDAD DE LA INFORMACION", "2472850");
        agregar("2472957", "TRABAJO ESPECIAL DE GRADO", null);
    }

    private void agregar(String cod, String nom, String pre) {
        Asignatura nueva = new Asignatura(cod, nom);
        if (pre != null) {
            for (String p : pre.split(",")) nueva.agregarPrelacion(p);
        }
        listaMaterias.add(nueva);
    }

    public String obtenerResultado(int sem, ArrayList<String> aprobadas) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- RESULTADO PARA SEMESTRE ").append(sem).append(" ---\n\n");
        
        for (Asignatura m : listaMaterias) {
            int semMateria = Integer.parseInt(m.getCodigo().substring(4, 5));
            if (semMateria == sem) {
                ArrayList<String> faltantesInfo = new ArrayList<>();
                
                // Mantenemos la lógica: solo validamos prelaciones si es semestre > 1
                if (sem > 1) {
                    for (String codFaltante : m.getPre()) {
                        if (!aprobadas.contains(codFaltante)) {
                            String nombreFaltante = "Desconocida";
                            for (Asignatura mat : listaMaterias) {
                                if (mat.getCodigo().equals(codFaltante)) {
                                    nombreFaltante = mat.getNombre();
                                    break;
                                }
                            }
                            faltantesInfo.add(codFaltante + " (" + nombreFaltante + ")");
                        }
                    }
                }
                
                if (faltantesInfo.isEmpty()) 
                    sb.append("[DISPONIBLE] ").append(m.getCodigo()).append(" - ").append(m.getNombre()).append("\n");
                else 
                    sb.append("[BLOQUEADA]  ").append(m.getCodigo()).append(" - ").append(m.getNombre()).append(" (Faltan: ").append(faltantesInfo).append(")\n");
            }
        }
        return sb.toString();
    }
}