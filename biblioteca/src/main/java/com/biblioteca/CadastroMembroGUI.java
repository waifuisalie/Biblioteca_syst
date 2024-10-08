package com.biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CadastroMembroGUI extends JFrame {

    public CadastroMembroGUI() {
        // Configurações básicas da janela
        setTitle("Cadastro de Membro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Criando um painel para adicionar componentes
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        // Adicionando componentes para o formulário de cadastro de membro
        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField();

        JLabel numeroMembroLabel = new JLabel("Número de Membro:");
        JTextField numeroMembroField = new JTextField();

        JLabel tipoMembroLabel = new JLabel("Tipo de Membro:");
        JComboBox<String> tipoMembroComboBox = new JComboBox<>(new String[]{"Regular", "Premium", "Estudante", "Professor/Bibliotecário"});

        JButton cadastrarButton = new JButton("Cadastrar");

        JButton cancelarButton = new JButton("Cancelar");

        // Adicionando os componentes ao painel
        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(numeroMembroLabel);
        panel.add(numeroMembroField);
        panel.add(tipoMembroLabel);
        panel.add(tipoMembroComboBox);
        panel.add(new JLabel()); // Espaço vazio para alinhar o botão
        panel.add(new JLabel()); // Espaço vazio para alinhar o botão
        panel.add(cancelarButton);
        panel.add(cadastrarButton);

        // Adicionando o painel à janela
        add(panel);

        // Configure a ação do botão "Cadastrar"
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obter os valores do formulário
                String nome = nomeField.getText();
                int numeroMembro = Integer.parseInt(numeroMembroField.getText());
                String tipoMembro = (String) tipoMembroComboBox.getSelectedItem();

                // Verificar se o membro com o mesmo número já está cadastrado
                if (CsvHandler.verificarExistenciaArquivo("membros.csv") &&
                    CsvHandler.registroJaCadastrado("membros.csv", 1, Integer.toString(numeroMembro))) {
                    JOptionPane.showMessageDialog(null, "Membro com o mesmo número já cadastrado!");
                    return;
                }
                
                // Criar instância do membro correspondente ao tipo selecionado
                Membro membro = criarMembro(nome, numeroMembro, tipoMembro);

                // Verificar e criar o arquivo CSV de membros
                CsvHandler.verificarECriarArquivoCSV("membros.csv");


                // Exibir mensagem de sucesso
                JOptionPane.showMessageDialog(null, "Membro cadastrado com sucesso!");

                // Converter o membro para uma lista de strings
                List<String[]> dadosMembro = converterMembroParaDados(membro, tipoMembro);

                // Escrever as informações do membro no arquivo CSV
                CsvHandler.escreverDados("membros.csv", dadosMembro);

                // Fechar a janela de cadastro após o cadastro
                dispose();
            }
        });

        // Configure a ação do botão "Cancelar"
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fechar a janela de cadastro sem realizar o cadastro
                dispose();
            }
        });
    }

    // Método para converter um objeto Membro em uma lista de strings
    private List<String[]> converterMembroParaDados(Membro membro, String tipoMembro) {
        List<String[]> dados = new ArrayList<>();
        // Adicione os atributos do membro à lista de strings, incluindo o tipo de membro
        dados.add(new String[]{membro.getNome(), String.valueOf(membro.getNumeroMembro()), tipoMembro});
        return dados;
    }
    
    // método que cria uma instância adequada de Membro com base no tipo selecionado
    private Membro criarMembro(String nome, int numeroMembro, String tipoMembro) {
        switch (tipoMembro) {
            case "Regular":
                return new MembroRegular(nome, numeroMembro);
            case "Premium":
                return new MembroPremium(nome, numeroMembro);
            case "Estudante":
                return new MembroEstudante(nome, numeroMembro);
            case "Professor/Bibliotecário":
                return new MembroProfessorBibliotecario(nome, numeroMembro);
            default:
                throw new IllegalArgumentException("Tipo de membro inválido: " + tipoMembro);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CadastroMembroGUI cadastroMembroGUI = new CadastroMembroGUI();
                cadastroMembroGUI.setVisible(true);
            }
        });
    }
}
