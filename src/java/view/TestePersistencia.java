package view;

import java.util.Date;
import dao.GerenciadorDados;
import model.*;
import util.ArquivoUtil;

public class TestePersistencia {
    public static void main(String[] args) {
        System.out.println("=== Teste do Sistema de Persistência ===\n");
        
        // Criar gerenciador de dados
        GerenciadorDados gerenciador = new GerenciadorDados();
        
        // Tentar carregar dados existentes
        gerenciador.carregarTodosDados();
        
        // Se não há dados, criar dados de exemplo
        if (gerenciador.getUsuarios().isEmpty()) {
            gerenciador.criarDadosExemplo();
            
            // Criar mais alguns dados para testar
            criarDadosCompletos(gerenciador);
        }
        
        // Exibir dados carregados
        exibirDados(gerenciador);
        
        // Salvar dados
        gerenciador.salvarTodosDados();
        
        System.out.println("=== Teste de Persistência Concluído ===");
        System.out.println("Verifique os arquivos na pasta ../dados/");
    }
    
    private static void criarDadosCompletos(GerenciadorDados gerenciador) {
        System.out.println("Criando dados completos para teste...");
        
        // Obter categorias existentes
        Categoria catUrgente = gerenciador.getCategorias().get(0);
        Categoria catDesenvolvimento = gerenciador.getCategorias().get(1);
        
        // Criar projeto
        Date dataInicio = new Date();
        Date dataFim = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); // +30 dias
        Projeto projeto = new Projeto(
            gerenciador.obterProximoIdProjeto(),
            "Sistema de Gestão",
            "Desenvolvimento de sistema completo",
            "Em Andamento",
            dataInicio,
            dataFim
        );
        gerenciador.adicionarProjeto(projeto);
        
        // Criar tarefas
        Tarefa tarefa1 = new Tarefa(
            gerenciador.obterProximoIdTarefa(),
            "Análise de Requisitos",
            "Levantar todos os requisitos do sistema",
            "Concluída",
            "Alta"
        );
        tarefa1.marcarComoConcluida();
        tarefa1.adicionarCategoria(catUrgente);
        gerenciador.adicionarTarefa(tarefa1);
        
        Tarefa tarefa2 = new Tarefa(
            gerenciador.obterProximoIdTarefa(),
            "Desenvolvimento Backend",
            "Implementar API e regras de negócio",
            "Em Andamento",
            "Alta"
        );
        tarefa2.adicionarCategoria(catDesenvolvimento);
        tarefa2.adicionarCategoria(catUrgente);
        gerenciador.adicionarTarefa(tarefa2);
        
        Tarefa tarefa3 = new Tarefa(
            gerenciador.obterProximoIdTarefa(),
            "Testes Unitários",
            "Criar testes para todas as funcionalidades",
            "Pendente",
            "Média"
        );
        tarefa3.adicionarCategoria(catDesenvolvimento);
        gerenciador.adicionarTarefa(tarefa3);
        
        // Associar tarefas ao projeto
        projeto.adicionarTarefa(tarefa1);
        projeto.adicionarTarefa(tarefa2);
        projeto.adicionarTarefa(tarefa3);
        
        // Associar projeto ao usuário
        Usuario usuario = gerenciador.getUsuarios().get(0);
        usuario.adicionarItem(projeto);
        
        System.out.println("Dados completos criados!");
    }
    
    private static void exibirDados(GerenciadorDados gerenciador) {
        System.out.println("=== Dados Carregados ===");
        
        // Exibir usuários
        System.out.println("\nUsuários (" + gerenciador.getUsuarios().size() + "):");
        for (Usuario usuario : gerenciador.getUsuarios()) {
            System.out.println("- " + usuario);
        }
        
        // Exibir categorias
        System.out.println("\nCategorias (" + gerenciador.getCategorias().size() + "):");
        for (Categoria categoria : gerenciador.getCategorias()) {
            System.out.println("- " + categoria);
        }
        
        // Exibir projetos
        System.out.println("\nProjetos (" + gerenciador.getProjetos().size() + "):");
        for (Projeto projeto : gerenciador.getProjetos()) {
            System.out.println("- " + projeto);
            System.out.println("  Detalhes: " + projeto.obterDetalhes().replace("\n", " | "));
        }
        
        // Exibir tarefas
        System.out.println("\nTarefas (" + gerenciador.getTarefas().size() + "):");
        for (Tarefa tarefa : gerenciador.getTarefas()) {
            System.out.println("- " + tarefa);
            System.out.print("  Categorias: ");
            for (Categoria cat : tarefa.getCategorias()) {
                System.out.print(cat.getNome() + " ");
            }
            System.out.println();
        }
        
        System.out.println();
    }
}
