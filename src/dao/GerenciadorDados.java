package dao;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class GerenciadorDados {
    private List<Usuario> usuarios;
    private List<Categoria> categorias;
    private List<Projeto> projetos;
    private List<Tarefa> tarefas;
    
    public GerenciadorDados() {
        this.usuarios = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.projetos = new ArrayList<>();
        this.tarefas = new ArrayList<>();
    }
    
    // Método para carregar todos os dados dos arquivos
    public void carregarTodosDados() {
        System.out.println("=== Carregando dados dos arquivos ===");
        
        // Ordem importante: categorias primeiro, depois tarefas (por causa do relacionamento)
        this.categorias = CategoriaDAO.carregarCategorias();
        this.usuarios = UsuarioDAO.carregarUsuarios();
        this.projetos = ProjetoDAO.carregarProjetos();
        this.tarefas = TarefaDAO.carregarTarefas(this.categorias);
        
        // Reconstituir relacionamentos entre usuários e itens gerenciáveis
        reconstituirRelacionamentos();
        
        System.out.println("=== Dados carregados com sucesso! ===\n");
    }
    
    // Método para salvar todos os dados nos arquivos
    public void salvarTodosDados() {
        System.out.println("=== Salvando dados nos arquivos ===");
        
        UsuarioDAO.salvarUsuarios(this.usuarios);
        CategoriaDAO.salvarCategorias(this.categorias);
        ProjetoDAO.salvarProjetos(this.projetos);
        TarefaDAO.salvarTarefas(this.tarefas);
        
        System.out.println("=== Dados salvos com sucesso! ===\n");
    }
    
    // Método para reconstituir relacionamentos entre objetos
    private void reconstituirRelacionamentos() {
        // Associar projetos e tarefas aos usuários
        for (Usuario usuario : usuarios) {
            // Limpar lista atual
            usuario.getItensGerenciaveis().clear();
            
            // Adicionar projetos (seria necessário um campo usuarioId nos arquivos)
            // Por simplicidade, vou assumir que todos os itens pertencem ao primeiro usuário
            if (!usuarios.isEmpty()) {
                Usuario primeiroUsuario = usuarios.get(0);
                for (Projeto projeto : projetos) {
                    primeiroUsuario.adicionarItem(projeto);
                }
                for (Tarefa tarefa : tarefas) {
                    primeiroUsuario.adicionarItem(tarefa);
                }
            }
        }
        
        // Associar tarefas aos projetos (seria necessário um campo projetoId)
        // Por simplicidade, vou assumir que todas as tarefas pertencem ao primeiro projeto
        if (!projetos.isEmpty() && !tarefas.isEmpty()) {
            Projeto primeiroProjeto = projetos.get(0);
            primeiroProjeto.getTarefas().clear();
            for (Tarefa tarefa : tarefas) {
                primeiroProjeto.adicionarTarefa(tarefa);
            }
        }
    }
    
    // Getters
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public List<Categoria> getCategorias() {
        return categorias;
    }
    
    public List<Projeto> getProjetos() {
        return projetos;
    }
    
    public List<Tarefa> getTarefas() {
        return tarefas;
    }
    
    // Métodos para adicionar novos itens
    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }
    
    public void adicionarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
    }
    
    public void adicionarProjeto(Projeto projeto) {
        this.projetos.add(projeto);
    }
    
    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
    }
    
    // Métodos para obter próximos IDs
    public int obterProximoIdUsuario() {
        return UsuarioDAO.obterProximoId(this.usuarios);
    }
    
    public int obterProximoIdCategoria() {
        return CategoriaDAO.obterProximoId(this.categorias);
    }
    
    public int obterProximoIdProjeto() {
        return ProjetoDAO.obterProximoId(this.projetos);
    }
    
    public int obterProximoIdTarefa() {
        return TarefaDAO.obterProximoId(this.tarefas);
    }
    
    // Métodos de busca e operações específicas
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarios.stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }
    
    public Projeto buscarProjetoPorId(int id) {
        return projetos.stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    public Categoria buscarCategoriaPorId(int id) {
        return categorias.stream()
            .filter(c -> c.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    public Tarefa buscarTarefaPorId(int id) {
        return tarefas.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElse(null);
    }
    
    // Métodos para listar por usuário (implementação simplificada)
    public List<Projeto> listarProjetosPorUsuario(int usuarioId) {
        // Por enquanto, retorna todos os projetos
        return new ArrayList<>(projetos);
    }
    
    public List<Tarefa> listarTarefasPorUsuario(int usuarioId) {
        // Por enquanto, retorna todas as tarefas
        return new ArrayList<>(tarefas);
    }
    
    public List<Tarefa> listarTarefasPorProjeto(int projetoId) {
        // Por enquanto, retorna uma lista vazia até implementarmos a relação
        return new ArrayList<>();
    }
    
    public List<Tarefa> listarTarefasPorCategoria(int categoriaId) {
        // Por enquanto, retorna todas as tarefas até implementarmos a relação tarefa-categoria
        return new ArrayList<>(tarefas);
    }
    
    // Métodos para remover itens
    public boolean removerProjeto(int id) {
        return projetos.removeIf(p -> p.getId() == id);
    }
    
    public boolean removerCategoria(int id) {
        return categorias.removeIf(c -> c.getId() == id);
    }
    
    public boolean removerTarefa(int id) {
        return tarefas.removeIf(t -> t.getId() == id);
    }
    
    // Método para criar dados de exemplo
    public void criarDadosExemplo() {
        if (usuarios.isEmpty()) {
            System.out.println("Criando dados de exemplo...");
            
            // Criar usuário de exemplo
            Usuario usuario = new Usuario(1, "Admin", "admin@sistema.com", "123");
            adicionarUsuario(usuario);
            
            // Criar categorias de exemplo
            Categoria catUrgente = new Categoria(1, "Urgente");
            Categoria catDesenvolvimento = new Categoria(2, "Desenvolvimento");
            adicionarCategoria(catUrgente);
            adicionarCategoria(catDesenvolvimento);
            
            // Salvar os dados de exemplo
            salvarTodosDados();
            
            System.out.println("Dados de exemplo criados!");
        }
    }
}
