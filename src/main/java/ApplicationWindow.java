import javax.swing.*;
import java.awt.*;

public class ApplicationWindow extends JFrame {
    private JPanel panel1;
    private JPanel toolbar;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JPanel restoDeCosasIDE;
    private JSplitPane separadorTerminal;
    private JPanel panelNoTerminal;
    private JSplitPane separadorEditorTexto;
    private JTextArea editorTexto;
    private JTree tree1;
    private JTextArea terminal;
    private JPanel panelTerminal;

    public ApplicationWindow () {
        initComponents();
    }

    private void initComponents(){
        JMenuBar menuBar = new JMenuBar();

        // creacion de menus
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu run = new JMenu("Run");
        JMenu help = new JMenu("Help");

        // creacion de submenus
        JMenuItem saveAs = new JMenuItem("Save as...");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem newFile = new JMenuItem("New file");
        JMenuItem openFile = new JMenuItem("Open file");
        JMenuItem close = new JMenuItem("Close");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem runApp = new JMenuItem("Run...");
        JMenuItem compile = new JMenuItem("Compile...");
        JMenuItem tipOfTheDay = new JMenuItem("Tip of the day");
        JMenuItem contactSupport = new JMenuItem("Support from the devs");

        // añadir opciones a los menus
        file.add(newFile);
        file.add(openFile);
        file.add(save);
        file.add(saveAs);
        file.add(close);
        edit.add(undo);
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        run.add(runApp);
        run.add(compile);
        help.add(tipOfTheDay);
        help.add(contactSupport);

        // añadir menus a la barra de herramientas
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(run);
        menuBar.add(help);
        
        // cosas de la ventana
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(getClass().getResource("icon.jpg")).getImage());
        this.setPreferredSize(new Dimension(1280, 720));
        this.setJMenuBar(menuBar);
        this.add(panel1);
        this.pack();
    }
}
