import controller.Controller;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class ApplicationWindow extends JFrame {
    private Controller cont = new Controller();
    private Clipboard clipboard;
    private UndoManager undoManager;
    private JPanel panel1;
    private JPanel toolbar;
    private JButton buttonRun;
    private JButton buttonSave;
    private JButton buttonNewFile;
    private JPanel restoDeCosasIDE;
    private JSplitPane separadorTerminal;
    private JPanel panelNoTerminal;
    private JSplitPane separadorEditorTexto;
    private JTextArea editorTexto;
    private JTree tree1;
    private JPanel panelTerminal;
    private JTextPane terminal;

    public ApplicationWindow () {
        initComponents();
    }

    private void initComponents(){
        JMenuBar menuBar = new JMenuBar();
        undoManager = new UndoManager();
        undoManager.setLimit(5000);
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();


        // creacion de menus
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu run = new JMenu("Run");
        JMenu help = new JMenu("Help");

        // creacion de submenus
        JMenuItem saveAs = new JMenuItem("Save as..."); // hecho
        JMenuItem save = new JMenuItem("Save"); // hecho
        JMenuItem newFile = new JMenuItem("New file"); // hecho
        JMenuItem openFile = new JMenuItem("Open file"); // hecho
        JMenuItem print = new JMenuItem("Print"); // hecho
        JMenuItem close = new JMenuItem("Close"); // hecho
        JMenuItem undo = new JMenuItem("Undo"); // hecho
        JMenuItem redo = new JMenuItem("Redo"); // hecho
        JMenuItem cut = new JMenuItem("Cut"); // hecho
        JMenuItem copy = new JMenuItem("Copy"); // hecho
        JMenuItem paste = new JMenuItem("Paste"); // hecho
        JMenuItem delete = new JMenuItem("Delete"); // hecho
        JMenuItem runApp = new JMenuItem("Run..."); // hecho
        JMenuItem compile = new JMenuItem("Compile..."); // hecho
        JMenuItem about = new JMenuItem("About..."); // hecho
        JMenuItem contactSupport = new JMenuItem("Support from the devs"); // hecho

        // añadir opciones a los menus
        file.add(newFile);
        file.add(openFile);
        file.add(save);
        file.add(saveAs);
        file.add(print);
        file.add(close);
        edit.add(undo);
        edit.add(redo);
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        run.add(runApp);
        run.add(compile);
        help.add(about);
        help.add(contactSupport);

        // añadir menus a la barra de herramientas
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(run);
        menuBar.add(help);

        // action listeners:
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = editorTexto.getText();
                cont.saveAs(text);
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String text = editorTexto.getText();
                cont.save(text);
            }
        });
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = editorTexto.getText();
                cont.save(text);
            }
        });

        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldText = editorTexto.getText();
                cont.newFile(oldText);
            }
        });
        buttonNewFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldText = editorTexto.getText();
                cont.newFile(oldText);
            }
        });

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
               String text = cont.openFile();
               editorTexto.setText(text);
            }
        });

        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cont.print();
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldText = editorTexto.getText();
                if (cont.askIfSave(oldText) == 0) {
                    cont.save(oldText);
                }
                System.exit(0);
            }
        });

        editorTexto.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        // deshacer
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()){
                    undoManager.undo();
                }
            }
        });

        // rehacer
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()){
                    undoManager.redo();
                }
            }
        });

        // copy
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(editorTexto.getSelectedText() != null){
                    StringSelection selection = new StringSelection("" + editorTexto.getSelectedText());
                    clipboard.setContents(selection, selection);
                }
            }
        });

        // paste
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Transferable data = clipboard.getContents(null);
                if (data != null && data.isDataFlavorSupported(DataFlavor.stringFlavor)){
                    try {
                        editorTexto.replaceSelection(""+data.getTransferData(DataFlavor.stringFlavor));
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // igual que el metodo copy, pero reemplazando la seleccion por "" tras copiar en el portapapeles.
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(editorTexto.getSelectedText() != null){
                    StringSelection selection = new StringSelection("" + editorTexto.getSelectedText());
                    clipboard.setContents(selection, selection);
                    editorTexto.replaceSelection("");
                }
            }
        });

        // delete
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorTexto.getSelectedText() != null) {
                    editorTexto.replaceSelection("");
                }
            }
        });

        /*
        En la práctica pone lo siguiente:
        "mostrar la información de autoría del bloc de notas en un JOptionPane o JDialog modal"
        Yo aqui lo que interpreto es que haga un JOptionPane con un texto que ponga "Autor:[nombre]"
        mediante un showMessageDilaog, asique eso haré.
         */
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cont.about();
            }
        });

        contactSupport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cont.contactSupport();
                }
                catch (IOException ex) {
                    System.out.println("A problem has occured, sorry. [IOException]");
                }
            }
        });

        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminal.setText(cont.run(editorTexto.getText()));
                // terminal.setText(cont.run().getOutputStream().write());
                // terminal.setText(cont.run().toString());
            }
        });

        runApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // terminal.setText(cont.run());
                // terminal.setText(cont.run().);
                terminal.setText(cont.run(editorTexto.getText()));
            }
        });

        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cont.compile(editorTexto.getText());
            }
        });

        editorTexto.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char tab = e.getKeyChar();
                if(tab == KeyEvent.VK_TAB) {
                    undoManager.undo();
                    editorTexto.replaceSelection("    ");
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        // cosas de la ventana
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(getClass().getResource("icon.jpg")).getImage());
        this.setPreferredSize(new Dimension(1280, 720));
        this.setJMenuBar(menuBar);
        this.add(panel1);
        this.pack();
    }
}
