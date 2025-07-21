package view;

import dao.GerenciadorDados;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.*;
import util.TelaUtil;

public class MenuUsuario {
    private GerenciadorDados gerenciador;
    private Scanner scanner;
    private Usuario usuarioLogado;
    
    public MenuUsuario(Usuario usuario, GerenciadorDados gerenciador) {
        this.usuarioLogado = usuario;
        this.gerenciador = gerenciador;
        this.scanner = new Scanner(System.in);
    }
    
    public void iniciar() {
        boolean continuar = true;
        
        while (continuar) {
            TelaUtil.limparTela();
            exibirMenuPrincipal();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    TelaUtil.limparTela();
                    menuGerenciarTarefas();
                    break;
                case 2:
                    TelaUtil.limparTela();
                    menuGerenciarProjetos();
                    break;
                case 3:
                    TelaUtil.limparTela();
                    menuGerenciarCategorias();
                    break;
                case 4:
                    TelaUtil.limparTela();
                    exibirRelatorios();
                    break;
                case 5:
                    TelaUtil.limparTela();
                    System.out.println("\n> Salvando dados...");
                    gerenciador.salvarTodosDados();
                    System.out.println("> Logout realizado com sucesso!");
                    continuar = false;
                    break;
                default:
                    System.out.println("\n> Opcao invalida! Tente novamente.");
                    TelaUtil.aguardarEnter();
            }
        }
    }
    
    private void exibirMenuPrincipal() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    SISTEMA DE GESTAO - Bem-vindo, " + usuarioLogado.getNome());
        System.out.println("=".repeat(60));
        System.out.println("1. Gerenciar Tarefas");
        System.out.println("2. Gerenciar Projetos");
        System.out.println("3. Gerenciar Categorias");
        System.out.println("4. Relatorios e Status");
        System.out.println("5. Logout");
        System.out.println("=".repeat(60));
        System.out.print("Escolha uma opcao: ");
    }
    
    // ========== GERENCIAMENTO DE TAREFAS ==========
    private void menuGerenciarTarefas() {
        boolean voltar = false;
        
        while (!voltar) {
            TelaUtil.exibirCabecalho("GERENCIAMENTO DE TAREFAS");
            System.out.println("1. Criar Nova Tarefa");
            System.out.println("2. Listar Todas as Tarefas");
            System.out.println("3. Editar Tarefa");
            System.out.println("4. Atualizar Status da Tarefa");
            System.out.println("5. Alterar Prioridade");
            System.out.println("6. Marcar como Concluida");
            System.out.println("7. Filtrar por Categoria");
            System.out.println("8. Voltar ao Menu Principal");
            System.out.println("=".repeat(50));
            System.out.print("Escolha uma opcao: ");
            
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    TelaUtil.limparTela();
                    criarNovaTarefa();
                    TelaUtil.aguardarEnter();
                    break;
                case 2:
                    TelaUtil.limparTela();
                    listarTarefas();
                    TelaUtil.aguardarEnter();
                    break;
                case 3:
                    TelaUtil.limparTela();
                    editarTarefa();
                    TelaUtil.aguardarEnter();
                    break;
                case 4:
                    TelaUtil.limparTela();
                    atualizarStatusTarefa();
                    TelaUtil.aguardarEnter();
                    break;
                case 5:
                    TelaUtil.limparTela();
                    alterarPrioridadeTarefa();
                    TelaUtil.aguardarEnter();
                    break;
                case 6:
                    TelaUtil.limparTela();
                    marcarTarefaConcluida();
                    TelaUtil.aguardarEnter();
                    break;
                case 7:
                    TelaUtil.limparTela();
                    filtrarTarefasPorCategoria();
                    TelaUtil.aguardarEnter();
                    break;
                case 8:
                    voltar = true;
                    break;
                default:
                    System.out.println("\n> Opcao invalida!");
                    TelaUtil.aguardarEnter();
            }
        }
    }
    
    private void criarNovaTarefa() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         CRIAR NOVA TAREFA");
        System.out.println("=".repeat(40));
        
        System.out.print("Titulo: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isEmpty()) {
            System.out.println("> Titulo nao pode estar vazio!");
            return;
        }
        
        System.out.print("Descricao: ");
        String descricao = scanner.nextLine().trim();
        
        // Selecionar prioridade
        String prioridade = selecionarPrioridade();
        if (prioridade == null) return;
        
        // Criar tarefa
        int novoId = gerenciador.obterProximoIdTarefa();
        Tarefa novaTarefa = new Tarefa(novoId, titulo, descricao, "A Fazer", prioridade);
        
        // Associar categorias
        associarCategorias(novaTarefa);
        
        gerenciador.adicionarTarefa(novaTarefa);
        usuarioLogado.adicionarItem(novaTarefa);
        
        System.out.println("\n> Tarefa criada com sucesso!");
        System.out.println("> " + novaTarefa.obterDetalhes().replace("\n", " | "));
    }
    
    private void listarTarefas() {
        List<Tarefa> tarefas = gerenciador.getTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa cadastrada.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                          LISTA DE TAREFAS");
        System.out.println("=".repeat(80));
        
        for (Tarefa tarefa : tarefas) {
            String statusIcon = getStatusIcon(tarefa.getStatus());
            String prioridadeIcon = getPrioridadeIcon(tarefa.getPrioridade());
            
            System.out.println(String.format("| [%d] %s %s %s", 
                tarefa.getId(), statusIcon, prioridadeIcon, tarefa.getTitulo()));
            System.out.println("|     Desc: " + tarefa.getDescricao());
            System.out.println("|     Criada: " + tarefa.getDataCriacao());
            
            if (!tarefa.getCategorias().isEmpty()) {
                System.out.print("|     Categorias: ");
                for (Categoria cat : tarefa.getCategorias()) {
                    System.out.print(cat.getNome() + " ");
                }
                System.out.println();
            }
            System.out.println("|" + "-".repeat(78));
        }
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private void atualizarStatusTarefa() {
        Tarefa tarefa = selecionarTarefa();
        if (tarefa == null) return;
        
        System.out.println("\n> Status atual: " + tarefa.getStatus());
        System.out.println("\nSelecione o novo status:");
        System.out.println("1. A Fazer");
        System.out.println("2. Em Progresso");
        System.out.println("3. Concluida");
        System.out.print("Opcao: ");
        
        int opcao = lerOpcao();
        String novoStatus;
        
        switch (opcao) {
            case 1: novoStatus = "A Fazer"; break;
            case 2: novoStatus = "Em Progresso"; break;
            case 3: novoStatus = "Concluida"; break;
            default:
                System.out.println("> Opcao invalida!");
                return;
        }
        
        tarefa.atualizarStatus(novoStatus);
        if (novoStatus.equals("Concluida")) {
            tarefa.marcarComoConcluida();
        }
        
        System.out.println("> Status atualizado para: " + novoStatus);
    }
    
    private void alterarPrioridadeTarefa() {
        Tarefa tarefa = selecionarTarefa();
        if (tarefa == null) return;
        
        System.out.println("\n> Prioridade atual: " + tarefa.getPrioridade());
        String novaPrioridade = selecionarPrioridade();
        if (novaPrioridade == null) return;
        
        tarefa.alterarPrioridade(novaPrioridade);
        System.out.println("> Prioridade alterada para: " + novaPrioridade);
    }
    
    private void marcarTarefaConcluida() {
        Tarefa tarefa = selecionarTarefa();
        if (tarefa == null) return;
        
        if (tarefa.getStatus().equals("Concluida")) {
            System.out.println("> Esta tarefa ja esta concluida!");
            return;
        }
        
        tarefa.marcarComoConcluida();
        System.out.println("> Tarefa marcada como concluida!");
        System.out.println("> Data de conclusao: " + tarefa.getDataConclusao());
    }
    
    private void filtrarTarefasPorCategoria() {
        List<Categoria> categorias = gerenciador.getCategorias();
        if (categorias.isEmpty()) {
            System.out.println("\n> Nenhuma categoria cadastrada.");
            return;
        }
        
        System.out.println("\n> Selecione uma categoria:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i).getNome());
        }
        System.out.print("Opcao: ");
        
        int opcao = lerOpcao() - 1;
        if (opcao < 0 || opcao >= categorias.size()) {
            System.out.println("> Categoria invalida!");
            return;
        }
        
        Categoria categoriaSelecionada = categorias.get(opcao);
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        
        for (Tarefa tarefa : gerenciador.getTarefas()) {
            if (tarefa.getCategorias().contains(categoriaSelecionada)) {
                tarefasFiltradas.add(tarefa);
            }
        }
        
        if (tarefasFiltradas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa encontrada na categoria: " + categoriaSelecionada.getNome());
        } else {
            System.out.println("\n> Tarefas na categoria: " + categoriaSelecionada.getNome());
            System.out.println("-".repeat(50));
            for (Tarefa tarefa : tarefasFiltradas) {
                System.out.println("* " + tarefa.getTitulo() + " [" + tarefa.getStatus() + "]");
            }
        }
        
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    // ========== METODOS AUXILIARES ==========
    private String selecionarPrioridade() {
        System.out.println("\n> Selecione a prioridade:");
        System.out.println("1. Alta");
        System.out.println("2. Media");
        System.out.println("3. Baixa");
        System.out.print("Opcao: ");
        
        int opcao = lerOpcao();
        switch (opcao) {
            case 1: return "Alta";
            case 2: return "Media";
            case 3: return "Baixa";
            default:
                System.out.println("> Prioridade invalida!");
                return null;
        }
    }
    
    private void associarCategorias(Tarefa tarefa) {
        List<Categoria> categorias = gerenciador.getCategorias();
        if (categorias.isEmpty()) {
            System.out.println("> Nenhuma categoria disponivel.");
            return;
        }
        
        System.out.println("\n> Categorias disponiveis:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i).getNome());
        }
        System.out.println("0. Pular (nao adicionar categorias)");
        
        System.out.print("Selecione categorias (separadas por virgula): ");
        String input = scanner.nextLine().trim();
        
        if (input.equals("0") || input.isEmpty()) return;
        
        String[] indices = input.split(",");
        for (String indiceStr : indices) {
            try {
                int indice = Integer.parseInt(indiceStr.trim()) - 1;
                if (indice >= 0 && indice < categorias.size()) {
                    tarefa.adicionarCategoria(categorias.get(indice));
                }
            } catch (NumberFormatException e) {
                // Ignora entrada inválida
            }
        }
    }
    
    private Tarefa selecionarTarefa() {
        List<Tarefa> tarefas = gerenciador.getTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa cadastrada.");
            return null;
        }
        
        System.out.println("\n> Selecione uma tarefa:");
        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa tarefa = tarefas.get(i);
            System.out.println((i + 1) + ". " + tarefa.getTitulo() + " [" + tarefa.getStatus() + "]");
        }
        System.out.print("Opcao: ");
        
        int opcao = lerOpcao() - 1;
        if (opcao < 0 || opcao >= tarefas.size()) {
            System.out.println("> Tarefa invalida!");
            return null;
        }
        
        return tarefas.get(opcao);
    }
    
    private String getStatusIcon(String status) {
        switch (status.toLowerCase()) {
            case "a fazer": return "[TODO]";
            case "em progresso": return "[PROG]";
            case "concluida": return "[DONE]";
            default: return "[????]";
        }
    }
    
    private String getPrioridadeIcon(String prioridade) {
        switch (prioridade.toLowerCase()) {
            case "alta": return "[ALTA]";
            case "media": return "[MED]";
            case "baixa": return "[BAIXA]";
            default: return "[???]";
        }
    }
    
    // ========== PLACEHOLDER PARA OUTRAS FUNCIONALIDADES ==========
    private void menuGerenciarProjetos() {
        System.out.println("\n> Menu de Projetos sera implementado na proxima etapa...");
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private void menuGerenciarCategorias() {
        System.out.println("\n> Menu de Categorias sera implementado na proxima etapa...");
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private void exibirRelatorios() {
        System.out.println("\n> Relatorios serao implementados na proxima etapa...");
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private void editarTarefa() {
        System.out.println("\n> Edicao de tarefa sera implementada na proxima etapa...");
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
