package view;

import dao.GerenciadorDados;
import dao.UsuarioDAO;
import java.util.Scanner;
import model.Usuario;
import util.TelaUtil;

public class MenuAutenticacao {
    private GerenciadorDados gerenciador;
    private Scanner scanner;
    
    public MenuAutenticacao() {
        this.gerenciador = new GerenciadorDados();
        this.scanner = new Scanner(System.in);
        
        // Carregar dados existentes
        gerenciador.carregarTodosDados();
        
        // Se não há usuários, criar dados básicos
        if (gerenciador.getUsuarios().isEmpty()) {
            gerenciador.criarDadosExemplo();
        }
    }
    
    public void iniciar() {
        TelaUtil.exibirCabecalho("SISTEMA DE GERENCIAMENTO DE PROJETOS");
        
        boolean continuar = true;
        
        while (continuar) {
            exibirMenuPrincipal();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    TelaUtil.limparTela();
                    realizarLogin();
                    break;
                case 2:
                    TelaUtil.limparTela();
                    criarNovaConta();
                    break;
                case 3:
                    TelaUtil.limparTela();
                    System.out.println("\n> Obrigado por usar o sistema!");
                    System.out.println("Salvando dados...");
                    gerenciador.salvarTodosDados();
                    continuar = false;
                    break;
                default:
                    System.out.println("\n> Opcao invalida! Tente novamente.");
                    TelaUtil.aguardarEnter();
            }
        }
        
        scanner.close();
    }
    
    private void exibirMenuPrincipal() {
        System.out.println("\n=================== MENU PRINCIPAL ===================");
        System.out.println("1. Fazer Login");
        System.out.println("2. Criar Nova Conta");
        System.out.println("3. Sair do Sistema");
        System.out.println("======================================================");
        System.out.print("Escolha uma opcao: ");
    }
    
    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Retorna valor inválido para forçar nova tentativa
        }
    }
    
    private void realizarLogin() {
        TelaUtil.exibirCabecalho("LOGIN");
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("> Email nao pode estar vazio!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();
        
        if (senha.isEmpty()) {
            System.out.println("> Senha nao pode estar vazia!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Buscar usuário por email
        Usuario usuario = UsuarioDAO.buscarPorEmail(gerenciador.getUsuarios(), email);
        
        if (usuario != null && usuario.verificarLogin(email, senha)) {
            System.out.println("\n> Login realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");
            TelaUtil.aguardarEnter();
            
            // Chamar o Menu do Usuário
            MenuUsuario menuUsuario = new MenuUsuario(usuario, gerenciador);
            menuUsuario.iniciar();
            
        } else {
            System.out.println("\n> Email ou senha incorretos!");
            System.out.println("Verifique suas credenciais e tente novamente.");
            TelaUtil.aguardarEnter();
        }
    }
    
    private void criarNovaConta() {
        TelaUtil.exibirCabecalho("CRIAR CONTA");
        
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine().trim();
        
        if (nome.isEmpty()) {
            System.out.println("> Nome nao pode estar vazio!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("> Email nao pode estar vazio!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        if (!isEmailValido(email)) {
            System.out.println("> Email deve conter '@' e ter formato valido!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Verificar se email já existe
        Usuario usuarioExistente = UsuarioDAO.buscarPorEmail(gerenciador.getUsuarios(), email);
        if (usuarioExistente != null) {
            System.out.println("> Este email ja esta cadastrado!");
            System.out.println("Tente fazer login ou use outro email.");
            TelaUtil.aguardarEnter();
            return;
        }
        
        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();
        
        if (senha.isEmpty()) {
            System.out.println("> Senha nao pode estar vazia!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        if (senha.length() < 3) {
            System.out.println("> Senha deve ter pelo menos 3 caracteres!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        System.out.print("Confirme a senha: ");
        String confirmaSenha = scanner.nextLine().trim();
        
        if (!senha.equals(confirmaSenha)) {
            System.out.println("> As senhas nao coincidem!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Criar novo usuário
        int novoId = gerenciador.obterProximoIdUsuario();
        Usuario novoUsuario = new Usuario(novoId, nome, email, senha);
        
        gerenciador.adicionarUsuario(novoUsuario);
        gerenciador.salvarTodosDados();
        
        System.out.println("\n> Conta criada com sucesso!");
        System.out.println("Bem-vindo(a), " + nome + "!");
        System.out.println("Agora voce pode fazer login com suas credenciais.");
        TelaUtil.aguardarEnter("\nPressione ENTER para continuar...");
    }
    
    private boolean isEmailValido(String email) {
        return email.contains("@") && email.length() > 3;
    }
    
    // Método main para testar o menu
    public static void main(String[] args) {
        MenuAutenticacao menu = new MenuAutenticacao();
        menu.iniciar();
    }
}
