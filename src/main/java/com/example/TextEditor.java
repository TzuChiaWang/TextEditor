package com.example;

import javax.swing.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JFileChooser fileChooser;
    private UndoManager undoManager;

    public TextEditor() {
        // 建立文字編輯區
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setBackground(Color.LIGHT_GRAY);
        textArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(textArea);

        fileChooser = new JFileChooser();

        // 建立選單列
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu formatMenu = new JMenu("Format");
        
        // 建立選單項目
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem closeItem = new JMenuItem("Close");
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        JMenuItem fontSizeItem = new JMenuItem("Format Size");

        // 新增事件監聽器
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        closeItem.addActionListener(this);
        undoItem.addActionListener(this);
        redoItem.addActionListener(this);
        fontSizeItem.addActionListener(this);


        // 新增選單項目到檔案選單
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(closeItem);

        // 新增選單項目到編輯選單
        editMenu.add(undoItem);
        editMenu.add(redoItem);

        // 新增選單項目到格式選單
        formatMenu.add(fontSizeItem);

        // 新增選單到選單列
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);

        // 設定選單列
        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);
        setTitle("Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.blue);
        setLocationRelativeTo(null);

        // 初始化UndoManager
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);
    }

    @Override
    public void actionPerformed(@SuppressWarnings("exports") ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Close":
                textArea.setText("");
                break;
            case "Undo":
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
                break;
            case "Redo":
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
                break;
            case "Format Size":
                setFontSize();
                break;
        }
    }

    private void openFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.read(reader, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveFile() {
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                textArea.write(writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setFontSize() {
        String input = JOptionPane.showInputDialog(this, "Enter Font Size:", "Font Size", JOptionPane.PLAIN_MESSAGE);
        if (input != null) {
            try {
                int fontSize = Integer.parseInt(input);
                textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid font size", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditor editor = new TextEditor();
            editor.setVisible(true);
        });
    }
}