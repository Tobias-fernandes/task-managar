package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tarefa extends ItemGerenciavel {
    private Date dataConclusao;
    private String prioridade;
    private List<Categoria> categorias;
    
    // Construtor
    public Tarefa(int id, String titulo, String descricao, String status, String prioridade) {
        super(id, titulo, descricao, status);
        this.prioridade = prioridade;
        this.dataConclusao = null; // Inicialmente null pois não foi concluída
        this.categorias = new ArrayList<>();
    }
    
    // Implementação do método abstrato
    @Override
    public String obterDetalhes() {
        return "Tarefa: " + titulo + 
               "\nDescrição: " + descricao + 
               "\nStatus: " + status + 
               "\nPrioridade: " + prioridade + 
               "\nData Conclusão: " + (dataConclusao != null ? dataConclusao : "Não concluída") +
               "\nCategorias: " + categorias.size();
    }
    
    // Métodos específicos do diagrama UML
    public void marcarComoConcluida() {
        this.dataConclusao = new Date();
        this.status = "Concluída";
    }
    
    public void alterarPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
    
    // Métodos para gerenciar categorias (relacionamento N:N)
    public void adicionarCategoria(Categoria categoria) {
        if (!this.categorias.contains(categoria)) {
            this.categorias.add(categoria);
        }
    }
    
    public void removerCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
    }
    
    public List<Categoria> getCategorias() {
        return categorias;
    }
    
    // Getters e Setters específicos
    public Date getDataConclusao() {
        return dataConclusao;
    }
    
    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
    
    public String getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
    
    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", status='" + status + '\'' +
                ", prioridade='" + prioridade + '\'' +
                ", dataConclusao=" + dataConclusao +
                ", numCategorias=" + categorias.size() +
                '}';
    }
}
