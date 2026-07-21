import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
public class InterfazPrincipal extends JFrame {
    private GestionPensum gestor = new GestionPensum();
    private JComboBox<Integer> comboSemestre = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
    
    
    private final Color FONDO_GENERAL = new Color(20, 25, 35);
    private final Color FONDO_TABLA = new Color(35, 40, 50);
    private final Color AZUL_PROFUNDO = new Color(30, 35, 45);
    private final Color ACCENTO_CIAN = new Color(70, 150, 255);
    private final Color TEXTO_CLARO = new Color(240, 240, 240);

    private DefaultTableModel modeloTabla = new DefaultTableModel(new String[]{"Código", "Materia", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    
    private JTable tablaResultados = new JTable(modeloTabla);
    private ArrayList<String> aplazadas = new ArrayList<>();
    private JLabel statusBar = new JLabel(" Sistema listo - Seleccione un semestre para comenzar");
    
    public InterfazPrincipal() {
        setTitle("Planificador De Asignaturas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        
        getContentPane().setBackground(FONDO_GENERAL);
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout(5, 5));

        tablaResultados.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaResultados.setRowHeight(25);
        tablaResultados.setBackground(FONDO_TABLA);
        tablaResultados.setForeground(TEXTO_CLARO);
        tablaResultados.setGridColor(new Color(50, 60, 70));
        
        JScrollPane scroll = new JScrollPane(tablaResultados);
        scroll.getViewport().setBackground(FONDO_TABLA);
        
        tablaResultados.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(FONDO_TABLA);
                if (value.toString().contains("BLOQUEADA")) c.setForeground(new Color(255, 80, 80));
                else c.setForeground(new Color(80, 255, 120));
                return c;
            }
        });

        JPanel panelTop = new JPanel();
        panelTop.setBackground(AZUL_PROFUNDO);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnConsultar = new JButton("Consultar Pensum");
        btnConsultar.setBackground(ACCENTO_CIAN);
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFocusPainted(false);
        
        panelTop.add(new JLabel("Semestre a cursar:") {{ setForeground(TEXTO_CLARO); }});
        panelTop.add(comboSemestre);
        panelTop.add(btnConsultar);

        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(40, 45, 55));
        statusBar.setForeground(TEXTO_CLARO);
        statusBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0), BorderFactory.createEtchedBorder()));

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelTop, BorderLayout.NORTH);
        panelNorte.add(statusBar, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnConsultar.addActionListener(e -> iniciarFlujoConsulta());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void iniciarFlujoConsulta() {
        int sem = (int) comboSemestre.getSelectedItem();
        aplazadas.clear();
        if (sem > 1) {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Tiene materias reprobadas del semestre anterior (" + (sem - 1) + ")?", "Gestión de Reprobadas", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) abrirVentanaSeleccion(sem);
        }
        ejecutarConsulta(sem);
        statusBar.setText(" Consulta realizada correctamente para el Semestre " + sem);
    }

    private void abrirVentanaSeleccion(int semActual) {
        int semAnterior = semActual - 1;
        JDialog dialog = new JDialog(this, "Reprobadas Semestre " + semAnterior, true);
        dialog.setLayout(new BorderLayout(5, 5));
        
        // Unificación de colores en el Dialog
        dialog.getContentPane().setBackground(FONDO_GENERAL);
        
        JPanel panelCheckboxes = new JPanel();
        panelCheckboxes.setLayout(new BoxLayout(panelCheckboxes, BoxLayout.Y_AXIS));
        panelCheckboxes.setBackground(FONDO_GENERAL);
        
        ArrayList<JCheckBox> listaCheckboxes = new ArrayList<>();
        for (Asignatura m : gestor.listaMaterias) {
            if (Integer.parseInt(m.getCodigo().substring(4, 5)) == semAnterior) {
                JCheckBox cb = new JCheckBox(m.getCodigo() + " - " + m.getNombre());
                cb.setForeground(TEXTO_CLARO);
                cb.setBackground(FONDO_GENERAL);
                listaCheckboxes.add(cb);
                panelCheckboxes.add(cb);
            }
        }
        
        JScrollPane scroll = new JScrollPane(panelCheckboxes);
        scroll.getViewport().setBackground(FONDO_GENERAL);
        scroll.setBorder(null);
        dialog.add(scroll, BorderLayout.CENTER);
        
        JButton btnConfirmar = new JButton("Confirmar Selección");
        btnConfirmar.setBackground(ACCENTO_CIAN);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        
        btnConfirmar.addActionListener(e -> {
            for (JCheckBox cb : listaCheckboxes) {
                if (cb.isSelected()) {
                    String cod = cb.getText().substring(0, 7);
                    if (!aplazadas.contains(cod)) aplazadas.add(cod);
                }
            }
            dialog.dispose();
        });
        dialog.add(btnConfirmar, BorderLayout.SOUTH);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void ejecutarConsulta(int sem) {
        modeloTabla.setRowCount(0);
        ArrayList<String> aprobadas = new ArrayList<>();
        for (Asignatura m : gestor.listaMaterias) {
            int s = Integer.parseInt(m.getCodigo().substring(4, 5));
            if (s < sem && !aplazadas.contains(m.getCodigo())) aprobadas.add(m.getCodigo());
        }
        for (Asignatura m : gestor.listaMaterias) {
            if (Integer.parseInt(m.getCodigo().substring(4, 5)) == sem) {
                ArrayList<String> faltantes = new ArrayList<>();
                for (String p : m.getPre()) {
                    if (!aprobadas.contains(p)) {
                        String nombreP = "Desconocida";
                        for (Asignatura mat : gestor.listaMaterias) if (mat.getCodigo().equals(p)) { nombreP = mat.getNombre(); break; }
                        faltantes.add(p + " - " + nombreP);
                    }
                }
                String estado = faltantes.isEmpty() ? "DISPONIBLE" : "BLOQUEADA (Faltan: " + faltantes + ")";
                modeloTabla.addRow(new Object[]{m.getCodigo(), m.getNombre(), estado});
            }
        }
    }
}