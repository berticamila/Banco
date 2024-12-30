import javax.swing.*;
import java.awt.*;

class Cliente {
    private String nome;
    private String sobrenome;
    private String cpf;

    public Cliente(String nome, String sobrenome, String cpf) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getCpf() {
        return cpf;
    }
}

class ContaBancaria {
    private Cliente cliente;
    private double saldo;

    public ContaBancaria(Cliente cliente) {
        this.cliente = cliente;
        this.saldo = 0.0;
    }

    public double consultarSaldo() {
       return Math.round(saldo * 100.0) / 100.0;//Para arrendondar para duas casas decimais
    }

    public void depositar(double valor) {
        saldo += valor;
    }

    public void sacar(double valor) {
        if (valor <= saldo) {
            saldo -= valor;
        } else {
            throw new IllegalArgumentException("Saldo insuficiente!");
        }
    }

    public Cliente getCliente() {
        return cliente;
    }
}

public class Banco{

    private static ContaBancaria conta;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gerenciador Banco");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLayout(new BorderLayout());

            JPanel panelInicial = criarTelaInicial(frame);
            frame.add(panelInicial);

            frame.setVisible(true);
        });
    }

    private static JPanel criarTelaInicial(JFrame frame) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 5, 20, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cria os rótulos e campos de texto
        JLabel labelNome = new JLabel("Nome:");
        JTextField campoNome = new JTextField(15);

        JLabel labelSobrenome = new JLabel("Sobrenome:");
        JTextField campoSobrenome = new JTextField(15);

        JLabel labelCPF = new JLabel("CPF:");
        JTextField campoCPF = new JTextField(15);

        JButton botaoCriarConta = new JButton("Criar Conta");

        // Adiciona o rótulo "Nome"
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        gbc.anchor = GridBagConstraints.EAST; // Alinha à direita
        panel.add(labelNome, gbc);

        // Adiciona o campo "Nome"
        gbc.gridx = 1; // Coluna 1
        gbc.anchor = GridBagConstraints.WEST; // Alinha à esquerda
        panel.add(campoNome, gbc);

        // Adiciona o rótulo "Sobrenome"
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 1; // Linha 1
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(labelSobrenome, gbc);

        // Adiciona o campo "Sobrenome"
        gbc.gridx = 1; // Coluna 1
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoSobrenome, gbc);

        // Adiciona o rótulo "CPF"
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 2; // Linha 2
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(labelCPF, gbc);

        // Adiciona o campo "CPF"
        gbc.gridx = 1; // Coluna 1
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campoCPF, gbc);

        // Adiciona o botão "Criar Conta"
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 3; // Linha 3
        gbc.gridwidth = 2; // O botão ocupa duas colunas
        gbc.anchor = GridBagConstraints.CENTER; // Centraliza o botão
        panel.add(botaoCriarConta, gbc);

        // Configura a ação do botão
        botaoCriarConta.addActionListener(e -> {
            String nome = campoNome.getText();
            String sobrenome = campoSobrenome.getText();
            String cpf = campoCPF.getText();
            
            // Verifica se o nome tem pelo menos dois caracteres e se contém apenas letras
            //(sem números ou caracteres especiais)
            if(!nome.matches("^[\\p{L}]{2,}$")){
                JOptionPane.showMessageDialog(frame, "Nome inválido! O Nome deve ser composto apenas por letras.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
            }
             // Verifica se o sobrenome tem pelo menos dois caracteres e se contém apenas letras
            //(sem números ou caracteres especiais)
             if(!sobrenome.matches("^[\\p{L}]{2,}$")){
                  JOptionPane.showMessageDialog(frame, "Sobrenome inválido! O Sobrenome deve ser composto apenas por letras.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
             }

            if (nome.isEmpty() || sobrenome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verifica se o CPF tem exatamente 11 dígitos numéricos
            if (!cpf.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(frame, "CPF inválido! O CPF deve conter exatamente 11 números.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
            }
            
            Cliente cliente = new Cliente(nome, sobrenome, cpf);
            conta = new ContaBancaria(cliente);
            JOptionPane.showMessageDialog(frame, "Conta criada com sucesso para " + nome + " " + sobrenome + "!");

            frame.getContentPane().removeAll();
            frame.add(criarTelaOperacoes(frame));
            frame.revalidate();
            frame.repaint();
        });

        return panel;
    }

    private static JPanel criarTelaOperacoes(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton botaoConsultarSaldo = new JButton("Consultar Saldo");
        JButton botaoDepositar = new JButton("Depositar");
        JButton botaoSacar = new JButton("Sacar");
        JButton botaoSair = new JButton("Sair");

        botaoConsultarSaldo.addActionListener(e -> {
            double saldo = conta.consultarSaldo();

            JOptionPane.showMessageDialog(frame, "Saldo atual: R$" + saldo, "Saldo", JOptionPane.INFORMATION_MESSAGE);
        });

        botaoDepositar.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog(frame, "Informe o valor a depositar:", "Depósito", JOptionPane.QUESTION_MESSAGE);
            if (valorStr != null) { // Verifica se o usuário não cancelou o diálogo
            valorStr = valorStr.replace(",", "."); // Substitui vírgulas por pontos
            try {
                double valor = Double.parseDouble(valorStr);
                conta.depositar(valor);
                JOptionPane.showMessageDialog(frame, "Depósito realizado com sucesso! Saldo atual: R$" + conta.consultarSaldo());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            }
        });

        botaoSacar.addActionListener(e -> {
            String valorStr = JOptionPane.showInputDialog(frame, "Informe o valor a sacar:", "Saque", JOptionPane.QUESTION_MESSAGE);
            if (valorStr != null) { // Verifica se o usuário não cancelou o diálogo
            valorStr = valorStr.replace(",", "."); // Substitui vírgulas por pontos
            try {
                double valor = Double.parseDouble(valorStr);
                conta.sacar(valor);
                JOptionPane.showMessageDialog(frame, "Saque realizado com sucesso! Saldo atual: R$" + conta.consultarSaldo());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            }
        });

        botaoSair.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Obrigado por utilizar nosso sistema!");
            frame.dispose();
        });

        panel.add(botaoConsultarSaldo);
        panel.add(botaoDepositar);
        panel.add(botaoSacar);
        panel.add(botaoSair);

        return panel;
    }
}
