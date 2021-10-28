package controller;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.URI;

public class Controller extends Component {
    File file = null;
    String fileName = null;

    public void newFile(String text) {
        int saveOrNot = askIfSave(text);
        if (saveOrNot == 0) {
            save(text);
        }
    }

    public int askIfSave(String text) {
        Object[] options = new Object[] { "Yes", "No"};
        return JOptionPane.showOptionDialog( null,"Would you like to save your current text first?", "Save progress?",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "Yes");
    }

    public String openFile() {
        String result = null;
        // elegimos el archivo
        JFileChooser chooser = new JFileChooser();
            // hacemos que solo se trague ficheros, no directorios
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // añadimos la extensión para txt
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Java files (*.java)", "java"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Python files (*.py)", "py"));
            // y hacemos que únicamente se trague archivos txt, y absolutamente nada más, para evitar que reviente si le intentas pasar, por ejemplo, un .pdf
        chooser.setAcceptAllFileFilterUsed(false);
        int option = chooser.showOpenDialog(this);
        file = chooser.getSelectedFile();
        fileName = chooser.getName();
        if (option == JFileChooser.APPROVE_OPTION){
            // copiamos el texto de ese archivo a una cadena de texto con StringBuilder
            StringBuilder sBuilder = new StringBuilder();
            try {
                FileReader fReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fReader);
                String line = br.readLine();
                while (line != null) {
                    sBuilder.append(line + "\n");
                    line = br.readLine();
                }
                result = sBuilder.toString();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        // returneamos nuestra cadena de texto
        return result;
    }

    public void saveAs(String text) {
        JFileChooser chooser = new JFileChooser("~");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Java files (*.java)", "java"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Python files (*.py)", "py"));
        chooser.setAcceptAllFileFilterUsed(false);

        int option = chooser.showSaveDialog(this);
        file = chooser.getSelectedFile();

        if (option == JFileChooser.APPROVE_OPTION) {
            if (!file.getPath().endsWith(".txt") ||
                    !file.getPath().endsWith(".java") ||
                    !file.getPath().endsWith(".py")) {
                // file = new File(noExtension(file.getPath()));
                Object[] options = new Object[] { ".txt", ".java", ".py"};
                int result = JOptionPane.showOptionDialog( null,"Which extension would you prefer?", "Extension needed",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, options,".txt");
                if (result == 0) {
                    file = new File(file.getPath() + ".txt" );
                    fileName = file.getName();
                }
                if (result == 1) {
                    file = new File(file.getPath() + ".java" );
                    fileName = file.getName();
                }
                if (result == 2) {
                    file = new File(file.getPath() + ".py" );
                    fileName = file.getName();
                }
            }
            if (file != null) {
                if (!file.exists()) {
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(text);
                        JOptionPane.showMessageDialog(null, "File saved successfully",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    } catch(IOException ex) {
                        JOptionPane.showMessageDialog(null, "An error has occured during saving process",
                                "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "'" + file.getName() + "' already exists, please select another name.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public void save(String text) {
        if (file != null) {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else saveAs(text);
    }

    public void about() {
        JOptionPane.showMessageDialog(null, "Author: Daniel Rodríguez Muñoz - 2 DAM", "About me", JOptionPane.PLAIN_MESSAGE, null);
    }

    public void contactSupport() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://www.youtube.com/watch?v=LLrIGJEz818&t=27s&ab_channel=Lunareve"));
    }

    public String run(String text) {
        String result = "";
        Process process = null;
        try {
            compile(text);
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                process = Runtime.getRuntime().exec("cmd /c java " + file.getAbsolutePath());
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                result = br.readLine();
            } else {
                compile(text);
                process = Runtime.getRuntime().exec("sh -c java " + file.getAbsolutePath());
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                result = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return process;
        return result;
    }

    public void compile(String text) {
        try {
            save(text);
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                Runtime.getRuntime().exec("cmd /c javac " + file.getAbsolutePath());
            } else {
                Runtime.getRuntime().exec("sh -c javac " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
