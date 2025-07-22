package view;

import dao.GerenciadorDados;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import model.*;
import util.TelaUtil;

public class MenuUsuario {
    private final GerenciadorDados gerenciador;
    private final Scanner scanner;
    private final Usuario usuarioLogado;
    
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
        TelaUtil.exibirCabecalho("SISTEMA DE GESTAO - Bem-vindo, " + usuarioLogado.getNome());
        System.out.println("1. Gerenciar Tarefas");
        System.out.println("2. Gerenciar Projetos");
        System.out.println("3. Gerenciar Categorias");
        System.out.println("4. Relatorios e Status");
        System.out.println("5. Logout");
        System.out.println("============================================================");
        System.out.print("Escolha uma opcao: ");
    }
    
    // ========== GERENCIAMENTO DE TAREFAS ==========
    private void menuGerenciarTarefas() {
        boolean continuar = true;
        
        while (continuar) {
            TelaUtil.exibirCabecalho("GERENCIAMENTO DE TAREFAS");
            System.out.println("1. Criar Nova Tarefa");
            System.out.println("2. Listar Todas as Tarefas");
            System.out.println("3. Editar Tarefa");
            System.out.println("4. Atualizar Status da Tarefa");
            System.out.println("5. Alterar Prioridade");
            System.out.println("6. Marcar como Concluida");
            System.out.println("7. Filtrar por Categoria");
            System.out.println("8. Voltar ao Menu Principal");
            System.out.println("==================================================");
            System.out.print("Escolha uma opcao: ");
            
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    criarNovaTarefa();
                    break;
                case 2:
                    listarTodasTarefas();
                    break;
                case 3:
                    editarTarefa();
                    break;
                case 4:
                    atualizarStatusTarefa();
                    break;
                case 5:
                    alterarPrioridadeTarefa();
                    break;
                case 6:
                    marcarTarefaConcluida();
                    break;
                case 7:
                    filtrarTarefasPorCategoria();
                    break;
                case 8:
                    continuar = false;
                    break;
                default:
                    System.out.println("\n> Opcao invalida! Tente novamente.");
                    TelaUtil.aguardarEnter();
            }
        }
    }
    
    private void criarNovaTarefa() {
        TelaUtil.exibirCabecalho("CRIAR NOVA TAREFA");
        
        System.out.print("Titulo da tarefa: ");
        String titulo = scanner.nextLine().trim();
        
        if (titulo.isEmpty()) {
            System.out.println("\n> Erro: Titulo nao pode estar vazio!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        System.out.print("Descricao da tarefa: ");
        String descricao = scanner.nextLine().trim();
        
        System.out.print("Prioridade (ALTA/MEDIA/BAIXA): ");
        String prioridade = scanner.nextLine().trim().toUpperCase();
        
        if (!prioridade.equals("ALTA") && !prioridade.equals("MEDIA") && !prioridade.equals("BAIXA")) {
            prioridade = "MEDIA";
        }
        
        try {
            Tarefa novaTarefa = new Tarefa(gerenciador.obterProximoIdTarefa(), titulo, descricao, "TODO", prioridade);
            gerenciador.adicionarTarefa(novaTarefa);
            gerenciador.salvarTodosDados();
            
            System.out.println("\n> Tarefa criada com sucesso!");
            System.out.println("ID: " + novaTarefa.getId());
            System.out.println("Titulo: " + novaTarefa.getTitulo());
            System.out.println("Prioridade: " + novaTarefa.getPrioridade());
        } catch (Exception e) {
            System.out.println("\n> Erro ao criar tarefa: " + e.getMessage());
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void listarTodasTarefas() {
        TelaUtil.exibirCabecalho("LISTA DE TAREFAS");
        
        List<Tarefa> tarefas = gerenciador.getTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa encontrada.");
            System.out.println("Crie sua primeira tarefa usando a opcao 'Criar Nova Tarefa'!");
        } else {
            System.out.println("================================================================================");
            System.out.println("                          LISTA DE TAREFAS");
            System.out.println("================================================================================");
            
            for (Tarefa tarefa : tarefas) {
                System.out.println("| [" + tarefa.getId() + "] [" + tarefa.getStatus() + "] [" + tarefa.getPrioridade() + "] " + tarefa.getTitulo());
                System.out.println("|     Desc: " + tarefa.getDescricao());
                System.out.println("|     Criada: " + tarefa.getDataCriacao());
                System.out.println("|------------------------------------------------------------------------------");
            }
        }
        
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    private void editarTarefa() {
        TelaUtil.exibirCabecalho("EDITAR TAREFA");
        
        List<Tarefa> tarefas = gerenciador.getTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa encontrada para editar.");
            System.out.println("Crie uma tarefa primeiro usando a opcao 'Criar Nova Tarefa'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de tarefas
        System.out.println("=== TAREFAS DISPONIVEIS ===");
        for (Tarefa tarefa : tarefas) {
            System.out.println("[" + tarefa.getId() + "] " + tarefa.getTitulo() + " - " + tarefa.getStatus() + " - " + tarefa.getPrioridade());
        }
        
        System.out.print("\nDigite o ID da tarefa que deseja editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tarefa tarefa = gerenciador.buscarTarefaPorId(id);
            
            if (tarefa == null) {
                System.out.println("\n> Tarefa com ID " + id + " nao encontrada!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== EDITANDO TAREFA: " + tarefa.getTitulo() + " ===");
            
            System.out.print("Novo titulo (atual: " + tarefa.getTitulo() + ") ou ENTER para manter: ");
            String novoTitulo = scanner.nextLine().trim();
            if (!novoTitulo.isEmpty()) {
                tarefa.setTitulo(novoTitulo);
            }
            
            System.out.print("Nova descricao (atual: " + tarefa.getDescricao() + ") ou ENTER para manter: ");
            String novaDescricao = scanner.nextLine().trim();
            if (!novaDescricao.isEmpty()) {
                tarefa.setDescricao(novaDescricao);
            }
            
            System.out.print("Nova prioridade (atual: " + tarefa.getPrioridade() + ") [ALTA/MEDIA/BAIXA] ou ENTER para manter: ");
            String novaPrioridade = scanner.nextLine().trim().toUpperCase();
            if (!novaPrioridade.isEmpty() && (novaPrioridade.equals("ALTA") || novaPrioridade.equals("MEDIA") || novaPrioridade.equals("BAIXA"))) {
                tarefa.setPrioridade(novaPrioridade);
            }
            
            System.out.print("Novo status (atual: " + tarefa.getStatus() + ") [TODO/PROG/DONE] ou ENTER para manter: ");
            String novoStatus = scanner.nextLine().trim().toUpperCase();
            if (!novoStatus.isEmpty() && (novoStatus.equals("TODO") || novoStatus.equals("PROG") || novoStatus.equals("DONE"))) {
                tarefa.atualizarStatus(novoStatus);
            }
            
            gerenciador.salvarTodosDados();
            System.out.println("\n> Tarefa atualizada com sucesso!");
            System.out.println("Titulo: " + tarefa.getTitulo());
            System.out.println("Descricao: " + tarefa.getDescricao());
            System.out.println("Prioridade: " + tarefa.getPrioridade());
            System.out.println("Status: " + tarefa.getStatus());
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void atualizarStatusTarefa() {
        TelaUtil.exibirCabecalho("ATUALIZAR STATUS DA TAREFA");
        
        List<Tarefa> tarefas = gerenciador.getTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa encontrada.");
            System.out.println("Crie uma tarefa primeiro usando a opcao 'Criar Nova Tarefa'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de tarefas
        System.out.println("=== TAREFAS DISPONIVEIS ===");
        for (Tarefa tarefa : tarefas) {
            System.out.println("[" + tarefa.getId() + "] " + tarefa.getTitulo() + " - Status atual: " + tarefa.getStatus());
        }
        
        System.out.print("\nDigite o ID da tarefa para alterar o status: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tarefa tarefa = gerenciador.buscarTarefaPorId(id);
            
            if (tarefa == null) {
                System.out.println("\n> Tarefa com ID " + id + " nao encontrada!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== ALTERANDO STATUS DA TAREFA: " + tarefa.getTitulo() + " ===");
            System.out.println("Status atual: " + tarefa.getStatus());
            System.out.println("\nOpcoes de status:");
            System.out.println("1. TODO (A fazer)");
            System.out.println("2. PROG (Em progresso)");
            System.out.println("3. DONE (Concluida)");
            
            System.out.print("\nEscolha o novo status (1-3): ");
            int opcaoStatus = Integer.parseInt(scanner.nextLine().trim());
            
            String novoStatus = "";
            switch (opcaoStatus) {
                case 1:
                    novoStatus = "TODO";
                    break;
                case 2:
                    novoStatus = "PROG";
                    break;
                case 3:
                    novoStatus = "DONE";
                    break;
                default:
                    System.out.println("\n> Opcao invalida!");
                    TelaUtil.aguardarEnter();
                    return;
            }
            
            tarefa.atualizarStatus(novoStatus);
            gerenciador.salvarTodosDados();
            
            System.out.println("\n> Status da tarefa atualizado com sucesso!");
            System.out.println("Tarefa: " + tarefa.getTitulo());
            System.out.println("Novo status: " + tarefa.getStatus());
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void alterarPrioridadeTarefa() {
        TelaUtil.exibirCabecalho("ALTERAR PRIORIDADE DA TAREFA");
        
        List<Tarefa> tarefas = gerenciador.getTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa encontrada.");
            System.out.println("Crie uma tarefa primeiro usando a opcao 'Criar Nova Tarefa'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de tarefas
        System.out.println("=== TAREFAS DISPONIVEIS ===");
        for (Tarefa tarefa : tarefas) {
            System.out.println("[" + tarefa.getId() + "] " + tarefa.getTitulo() + " - Prioridade atual: " + tarefa.getPrioridade());
        }
        
        System.out.print("\nDigite o ID da tarefa para alterar a prioridade: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tarefa tarefa = gerenciador.buscarTarefaPorId(id);
            
            if (tarefa == null) {
                System.out.println("\n> Tarefa com ID " + id + " nao encontrada!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== ALTERANDO PRIORIDADE DA TAREFA: " + tarefa.getTitulo() + " ===");
            System.out.println("Prioridade atual: " + tarefa.getPrioridade());
            System.out.println("\nOpcoes de prioridade:");
            System.out.println("1. ALTA");
            System.out.println("2. MEDIA");
            System.out.println("3. BAIXA");
            
            System.out.print("\nEscolha a nova prioridade (1-3): ");
            int opcaoPrioridade = Integer.parseInt(scanner.nextLine().trim());
            
            String novaPrioridade = "";
            switch (opcaoPrioridade) {
                case 1:
                    novaPrioridade = "ALTA";
                    break;
                case 2:
                    novaPrioridade = "MEDIA";
                    break;
                case 3:
                    novaPrioridade = "BAIXA";
                    break;
                default:
                    System.out.println("\n> Opcao invalida!");
                    TelaUtil.aguardarEnter();
                    return;
            }
            
            tarefa.setPrioridade(novaPrioridade);
            gerenciador.salvarTodosDados();
            
            System.out.println("\n> Prioridade da tarefa atualizada com sucesso!");
            System.out.println("Tarefa: " + tarefa.getTitulo());
            System.out.println("Nova prioridade: " + tarefa.getPrioridade());
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void marcarTarefaConcluida() {
        TelaUtil.exibirCabecalho("MARCAR TAREFA COMO CONCLUIDA");
        
        List<Tarefa> tarefas = gerenciador.getTarefas();
        
        if (tarefas.isEmpty()) {
            System.out.println("\n> Nenhuma tarefa encontrada.");
            System.out.println("Crie uma tarefa primeiro usando a opcao 'Criar Nova Tarefa'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar apenas tarefas não concluídas
        List<Tarefa> tarefasNaoConcluidas = new java.util.ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            if (!tarefa.getStatus().equals("DONE")) {
                tarefasNaoConcluidas.add(tarefa);
            }
        }
        
        if (tarefasNaoConcluidas.isEmpty()) {
            System.out.println("\n> Todas as tarefas ja estao concluidas!");
            System.out.println("Parabens! Voce completou todas as suas tarefas.");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de tarefas não concluídas
        System.out.println("=== TAREFAS PENDENTES ===");
        for (Tarefa tarefa : tarefasNaoConcluidas) {
            System.out.println("[" + tarefa.getId() + "] " + tarefa.getTitulo() + " - " + tarefa.getStatus() + " - " + tarefa.getPrioridade());
        }
        
        System.out.print("\nDigite o ID da tarefa que deseja marcar como concluida: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tarefa tarefa = gerenciador.buscarTarefaPorId(id);
            
            if (tarefa == null) {
                System.out.println("\n> Tarefa com ID " + id + " nao encontrada!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            if (tarefa.getStatus().equals("DONE")) {
                System.out.println("\n> Esta tarefa ja esta marcada como concluida!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== MARCANDO COMO CONCLUIDA ===");
            System.out.println("Tarefa: " + tarefa.getTitulo());
            System.out.println("Status atual: " + tarefa.getStatus());
            System.out.print("\nConfirma a conclusao desta tarefa? (s/N): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacao.equals("s") || confirmacao.equals("sim")) {
                tarefa.atualizarStatus("DONE");
                gerenciador.salvarTodosDados();
                
                System.out.println("\n> Parabens! Tarefa '" + tarefa.getTitulo() + "' marcada como concluida!");
                System.out.println("Continue o otimo trabalho!");
            } else {
                System.out.println("\n> Operacao cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void filtrarTarefasPorCategoria() {
        TelaUtil.exibirCabecalho("FILTRAR TAREFAS POR CATEGORIA");
        
        List<Categoria> categorias = gerenciador.getCategorias();
        
        if (categorias.isEmpty()) {
            System.out.println("\n> Nenhuma categoria encontrada.");
            System.out.println("Crie uma categoria primeiro no menu 'Gerenciar Categorias'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de categorias
        System.out.println("=== CATEGORIAS DISPONIVEIS ===");
        for (Categoria categoria : categorias) {
            System.out.println("[" + categoria.getId() + "] " + categoria.getNome());
        }
        
        System.out.print("\nDigite o ID da categoria para filtrar tarefas: ");
        try {
            int idCategoria = Integer.parseInt(scanner.nextLine().trim());
            Categoria categoria = gerenciador.buscarCategoriaPorId(idCategoria);
            
            if (categoria == null) {
                System.out.println("\n> Categoria com ID " + idCategoria + " nao encontrada!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            List<Tarefa> tarefasCategoria = gerenciador.listarTarefasPorCategoria(idCategoria);
            
            System.out.println("\n================================================================================");
            System.out.println("           TAREFAS DA CATEGORIA: " + categoria.getNome().toUpperCase());
            System.out.println("================================================================================");
            
            if (tarefasCategoria.isEmpty()) {
                System.out.println("\n> Nenhuma tarefa encontrada para a categoria '" + categoria.getNome() + "'.");
                System.out.println("Crie tarefas e associe-as a esta categoria!");
            } else {
                for (Tarefa tarefa : tarefasCategoria) {
                    System.out.println("| [" + tarefa.getId() + "] [" + tarefa.getStatus() + "] [" + tarefa.getPrioridade() + "] " + tarefa.getTitulo());
                    System.out.println("|     Desc: " + tarefa.getDescricao());
                    System.out.println("|     Criada: " + tarefa.getDataCriacao());
                    System.out.println("|------------------------------------------------------------------------------");
                }
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    // ========== GERENCIAMENTO DE PROJETOS ==========
    private void menuGerenciarProjetos() {
        System.out.println(">>> DEBUG: Executando a NOVA versao do menuGerenciarProjetos!");
        boolean continuar = true;
        
        while (continuar) {
            TelaUtil.exibirCabecalho("GERENCIAMENTO DE PROJETOS");
            System.out.println("1. Criar Novo Projeto");
            System.out.println("2. Listar Todos os Projetos");
            System.out.println("3. Editar Projeto");
            System.out.println("4. Excluir Projeto");
            System.out.println("5. Visualizar Detalhes do Projeto");
            System.out.println("6. Voltar ao Menu Principal");
            System.out.println("==================================================");
            System.out.print("Escolha uma opcao: ");
            
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    criarNovoProjeto();
                    break;
                case 2:
                    listarTodosProjetos();
                    break;
                case 3:
                    editarProjeto();
                    break;
                case 4:
                    excluirProjeto();
                    break;
                case 5:
                    visualizarDetalhesProjeto();
                    break;
                case 6:
                    continuar = false;
                    break;
                default:
                    System.out.println("\n> Opcao invalida! Tente novamente.");
                    TelaUtil.aguardarEnter();
            }
        }
    }
    
    private void criarNovoProjeto() {
        TelaUtil.exibirCabecalho("CRIAR NOVO PROJETO");
        
        System.out.print("Titulo do projeto: ");
        String titulo = scanner.nextLine().trim();
        
        if (titulo.isEmpty()) {
            System.out.println("\n> Erro: Titulo nao pode estar vazio!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        System.out.print("Descricao do projeto: ");
        String descricao = scanner.nextLine().trim();
        
        try {
            Projeto novoProjeto = new Projeto(gerenciador.obterProximoIdProjeto(), titulo, descricao, "TODO", new Date(), null);
            gerenciador.adicionarProjeto(novoProjeto);
            gerenciador.salvarTodosDados();
            
            System.out.println("\n> Projeto criado com sucesso!");
            System.out.println("ID: " + novoProjeto.getId());
            System.out.println("Titulo: " + novoProjeto.getTitulo());
        } catch (Exception e) {
            System.out.println("\n> Erro ao criar projeto: " + e.getMessage());
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void listarTodosProjetos() {
        TelaUtil.exibirCabecalho("LISTA DE PROJETOS");
        
        List<Projeto> projetos = gerenciador.getProjetos();
        
        if (projetos.isEmpty()) {
            System.out.println("\n> Nenhum projeto encontrado.");
            System.out.println("Crie seu primeiro projeto usando a opcao 'Criar Novo Projeto'!");
        } else {
            System.out.println("================================================================================");
            System.out.println("                          SEUS PROJETOS");
            System.out.println("================================================================================");
            
            for (Projeto projeto : projetos) {
                System.out.println("| [" + projeto.getId() + "] [" + projeto.getStatus() + "] " + projeto.getTitulo());
                System.out.println("|     Desc: " + projeto.getDescricao());
                System.out.println("|     Criado: " + projeto.getDataCriacao());
                System.out.println("|------------------------------------------------------------------------------");
            }
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void editarProjeto() {
        TelaUtil.exibirCabecalho("EDITAR PROJETO");
        
        List<Projeto> projetos = gerenciador.getProjetos();
        
        if (projetos.isEmpty()) {
            System.out.println("\n> Nenhum projeto encontrado para editar.");
            System.out.println("Crie um projeto primeiro usando a opcao 'Criar Novo Projeto'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de projetos
        System.out.println("=== PROJETOS DISPONIVEIS ===");
        for (Projeto projeto : projetos) {
            System.out.println("[" + projeto.getId() + "] " + projeto.getTitulo() + " - " + projeto.getStatus());
        }
        
        System.out.print("\nDigite o ID do projeto que deseja editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Projeto projeto = gerenciador.buscarProjetoPorId(id);
            
            if (projeto == null) {
                System.out.println("\n> Projeto com ID " + id + " nao encontrado!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== EDITANDO PROJETO: " + projeto.getTitulo() + " ===");
            
            System.out.print("Novo titulo (atual: " + projeto.getTitulo() + ") ou ENTER para manter: ");
            String novoTitulo = scanner.nextLine().trim();
            if (!novoTitulo.isEmpty()) {
                projeto.setTitulo(novoTitulo);
            }
            
            System.out.print("Nova descricao (atual: " + projeto.getDescricao() + ") ou ENTER para manter: ");
            String novaDescricao = scanner.nextLine().trim();
            if (!novaDescricao.isEmpty()) {
                projeto.setDescricao(novaDescricao);
            }
            
            System.out.print("Novo status (atual: " + projeto.getStatus() + ") [TODO/PROG/DONE] ou ENTER para manter: ");
            String novoStatus = scanner.nextLine().trim().toUpperCase();
            if (!novoStatus.isEmpty() && (novoStatus.equals("TODO") || novoStatus.equals("PROG") || novoStatus.equals("DONE"))) {
                projeto.atualizarStatus(novoStatus);
            }
            
            gerenciador.salvarTodosDados();
            System.out.println("\n> Projeto atualizado com sucesso!");
            System.out.println("Titulo: " + projeto.getTitulo());
            System.out.println("Descricao: " + projeto.getDescricao());
            System.out.println("Status: " + projeto.getStatus());
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void excluirProjeto() {
        TelaUtil.exibirCabecalho("EXCLUIR PROJETO");
        
        List<Projeto> projetos = gerenciador.getProjetos();
        
        if (projetos.isEmpty()) {
            System.out.println("\n> Nenhum projeto encontrado para excluir.");
            System.out.println("Crie um projeto primeiro usando a opcao 'Criar Novo Projeto'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de projetos
        System.out.println("=== PROJETOS DISPONIVEIS ===");
        for (Projeto projeto : projetos) {
            System.out.println("[" + projeto.getId() + "] " + projeto.getTitulo() + " - " + projeto.getStatus());
            System.out.println("    Desc: " + projeto.getDescricao());
        }
        
        System.out.print("\nDigite o ID do projeto que deseja excluir: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Projeto projeto = gerenciador.buscarProjetoPorId(id);
            
            if (projeto == null) {
                System.out.println("\n> Projeto com ID " + id + " nao encontrado!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== CONFIRMACAO DE EXCLUSAO ===");
            System.out.println("Projeto: " + projeto.getTitulo());
            System.out.println("Descricao: " + projeto.getDescricao());
            System.out.print("\nTem certeza que deseja excluir este projeto? (s/N): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacao.equals("s") || confirmacao.equals("sim")) {
                boolean removido = gerenciador.removerProjeto(id);
                if (removido) {
                    gerenciador.salvarTodosDados();
                    System.out.println("\n> Projeto '" + projeto.getTitulo() + "' excluido com sucesso!");
                } else {
                    System.out.println("\n> Erro ao excluir o projeto.");
                }
            } else {
                System.out.println("\n> Operacao cancelada. Projeto nao foi excluido.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void visualizarDetalhesProjeto() {
        TelaUtil.exibirCabecalho("DETALHES DO PROJETO");
        
        List<Projeto> projetos = gerenciador.getProjetos();
        
        if (projetos.isEmpty()) {
            System.out.println("\n> Nenhum projeto encontrado.");
            System.out.println("Crie um projeto primeiro usando a opcao 'Criar Novo Projeto'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de projetos
        System.out.println("=== PROJETOS DISPONIVEIS ===");
        for (Projeto projeto : projetos) {
            System.out.println("[" + projeto.getId() + "] " + projeto.getTitulo() + " - " + projeto.getStatus());
        }
        
        System.out.print("\nDigite o ID do projeto para ver detalhes completos: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Projeto projeto = gerenciador.buscarProjetoPorId(id);
            
            if (projeto == null) {
                System.out.println("\n> Projeto com ID " + id + " nao encontrado!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n================================================================================");
            System.out.println("                        DETALHES DO PROJETO");
            System.out.println("================================================================================");
            System.out.println("ID: " + projeto.getId());
            System.out.println("Titulo: " + projeto.getTitulo());
            System.out.println("Descricao: " + projeto.getDescricao());
            System.out.println("Status: " + projeto.getStatus());
            System.out.println("Data de Criacao: " + projeto.getDataCriacao());
            
            if (projeto.getDataInicio() != null) {
                System.out.println("Data de Inicio: " + projeto.getDataInicio());
            } else {
                System.out.println("Data de Inicio: Nao definida");
            }
            
            if (projeto.getDataFim() != null) {
                System.out.println("Data de Fim: " + projeto.getDataFim());
            } else {
                System.out.println("Data de Fim: Nao definida");
            }
            
            // Mostrar tarefas associadas (se houver)
            List<Tarefa> tarefasProjeto = gerenciador.listarTarefasPorProjeto(id);
            System.out.println("\n=== TAREFAS ASSOCIADAS ===");
            if (tarefasProjeto.isEmpty()) {
                System.out.println("Nenhuma tarefa associada a este projeto.");
            } else {
                for (Tarefa tarefa : tarefasProjeto) {
                    System.out.println("- [" + tarefa.getStatus() + "] " + tarefa.getTitulo() + " (Prioridade: " + tarefa.getPrioridade() + ")");
                }
            }
            
            System.out.println("\n" + "=".repeat(80));
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    // ========== GERENCIAMENTO DE CATEGORIAS ==========
    private void menuGerenciarCategorias() {
        boolean continuar = true;
        
        while (continuar) {
            TelaUtil.exibirCabecalho("GERENCIAMENTO DE CATEGORIAS");
            System.out.println("1. Criar Nova Categoria");
            System.out.println("2. Listar Todas as Categorias");
            System.out.println("3. Editar Categoria");
            System.out.println("4. Excluir Categoria");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.println("==================================================");
            System.out.print("Escolha uma opcao: ");
            
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    criarNovaCategoria();
                    break;
                case 2:
                    listarTodasCategorias();
                    break;
                case 3:
                    editarCategoria();
                    break;
                case 4:
                    excluirCategoria();
                    break;
                case 5:
                    continuar = false;
                    break;
                default:
                    System.out.println("\n> Opcao invalida! Tente novamente.");
                    TelaUtil.aguardarEnter();
            }
        }
    }
    
    private void criarNovaCategoria() {
        TelaUtil.exibirCabecalho("CRIAR NOVA CATEGORIA");
        
        System.out.print("Nome da categoria: ");
        String nome = scanner.nextLine().trim();
        
        if (nome.isEmpty()) {
            System.out.println("\n> Erro: Nome nao pode estar vazio!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        try {
            Categoria novaCategoria = new Categoria(gerenciador.obterProximoIdCategoria(), nome);
            gerenciador.adicionarCategoria(novaCategoria);
            gerenciador.salvarTodosDados();
            
            System.out.println("\n> Categoria criada com sucesso!");
            System.out.println("ID: " + novaCategoria.getId());
            System.out.println("Nome: " + novaCategoria.getNome());
        } catch (Exception e) {
            System.out.println("\n> Erro ao criar categoria: " + e.getMessage());
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void listarTodasCategorias() {
        TelaUtil.exibirCabecalho("LISTA DE CATEGORIAS");
        
        List<Categoria> categorias = gerenciador.getCategorias();
        
        if (categorias.isEmpty()) {
            System.out.println("\n> Nenhuma categoria encontrada.");
            System.out.println("Crie sua primeira categoria usando a opcao 'Criar Nova Categoria'!");
        } else {
            System.out.println("================================================================================");
            System.out.println("                          CATEGORIAS DISPONIVEIS");
            System.out.println("================================================================================");
            
            for (Categoria categoria : categorias) {
                System.out.println("| [" + categoria.getId() + "] " + categoria.getNome());
                System.out.println("|------------------------------------------------------------------------------");
            }
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void editarCategoria() {
        TelaUtil.exibirCabecalho("EDITAR CATEGORIA");
        
        List<Categoria> categorias = gerenciador.getCategorias();
        
        if (categorias.isEmpty()) {
            System.out.println("\n> Nenhuma categoria encontrada para editar.");
            System.out.println("Crie uma categoria primeiro usando a opcao 'Criar Nova Categoria'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de categorias
        System.out.println("=== CATEGORIAS DISPONIVEIS ===");
        for (Categoria categoria : categorias) {
            System.out.println("[" + categoria.getId() + "] " + categoria.getNome());
        }
        
        System.out.print("\nDigite o ID da categoria que deseja editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Categoria categoria = gerenciador.buscarCategoriaPorId(id);
            
            if (categoria == null) {
                System.out.println("\n> Categoria com ID " + id + " nao encontrada!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== EDITANDO CATEGORIA: " + categoria.getNome() + " ===");
            
            System.out.print("Novo nome (atual: " + categoria.getNome() + ") ou ENTER para manter: ");
            String novoNome = scanner.nextLine().trim();
            if (!novoNome.isEmpty()) {
                categoria.setNome(novoNome);
                gerenciador.salvarTodosDados();
                System.out.println("\n> Categoria atualizada com sucesso!");
                System.out.println("Novo nome: " + categoria.getNome());
            } else {
                System.out.println("\n> Nenhuma alteracao realizada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    private void excluirCategoria() {
        TelaUtil.exibirCabecalho("EXCLUIR CATEGORIA");
        
        List<Categoria> categorias = gerenciador.getCategorias();
        
        if (categorias.isEmpty()) {
            System.out.println("\n> Nenhuma categoria encontrada para excluir.");
            System.out.println("Crie uma categoria primeiro usando a opcao 'Criar Nova Categoria'!");
            TelaUtil.aguardarEnter();
            return;
        }
        
        // Mostrar lista de categorias
        System.out.println("=== CATEGORIAS DISPONIVEIS ===");
        for (Categoria categoria : categorias) {
            System.out.println("[" + categoria.getId() + "] " + categoria.getNome());
        }
        
        System.out.print("\nDigite o ID da categoria que deseja excluir: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Categoria categoria = gerenciador.buscarCategoriaPorId(id);
            
            if (categoria == null) {
                System.out.println("\n> Categoria com ID " + id + " nao encontrada!");
                TelaUtil.aguardarEnter();
                return;
            }
            
            System.out.println("\n=== CONFIRMACAO DE EXCLUSAO ===");
            System.out.println("Categoria: " + categoria.getNome());
            System.out.print("\nATENCAO: Excluir esta categoria pode afetar tarefas associadas!");
            System.out.print("\nTem certeza que deseja excluir esta categoria? (s/N): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            
            if (confirmacao.equals("s") || confirmacao.equals("sim")) {
                boolean removida = gerenciador.removerCategoria(id);
                if (removida) {
                    gerenciador.salvarTodosDados();
                    System.out.println("\n> Categoria '" + categoria.getNome() + "' excluida com sucesso!");
                } else {
                    System.out.println("\n> Erro ao excluir a categoria.");
                }
            } else {
                System.out.println("\n> Operacao cancelada. Categoria nao foi excluida.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n> ID invalido! Digite apenas numeros.");
        }
        
        TelaUtil.aguardarEnter();
    }
    
    // ========== RELATÓRIOS E STATUS ==========
    private void exibirRelatorios() {
        boolean continuar = true;
        
        while (continuar) {
            TelaUtil.exibirCabecalho("RELATORIOS E STATUS");
            System.out.println("1. Estatisticas Gerais");
            System.out.println("2. Relatorio de Tarefas por Status");
            System.out.println("3. Relatorio de Tarefas por Prioridade");
            System.out.println("4. Relatorio de Projetos");
            System.out.println("5. Relatorio de Produtividade");
            System.out.println("6. Voltar ao Menu Principal");
            System.out.println("==================================================");
            System.out.print("Escolha uma opcao: ");
            
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    exibirEstatisticasGerais();
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    System.out.println("\n> Funcionalidade sera implementada em breve...");
                    TelaUtil.aguardarEnter();
                    break;
                case 6:
                    continuar = false;
                    break;
                default:
                    System.out.println("\n> Opcao invalida! Tente novamente.");
                    TelaUtil.aguardarEnter();
            }
        }
    }
    
    private void exibirEstatisticasGerais() {
        TelaUtil.exibirCabecalho("ESTATISTICAS GERAIS");
        
        List<Tarefa> todasTarefas = gerenciador.getTarefas();
        List<Projeto> todosProjetos = gerenciador.getProjetos();
        List<Categoria> todasCategorias = gerenciador.getCategorias();
        
        System.out.println("================================================================================");
        System.out.println("                        RESUMO GERAL DO SISTEMA");
        System.out.println("================================================================================");
        System.out.println("Total de Projetos: " + todosProjetos.size());
        System.out.println("Total de Tarefas: " + todasTarefas.size());
        System.out.println("Total de Categorias: " + todasCategorias.size());
        System.out.println("");
        System.out.println("Sistema funcionando perfeitamente!");
        
        TelaUtil.aguardarEnter();
    }
    
    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
