package model;

import java.util.Date;

public abstract class ItemGerenciavel {
    protected int id;
    protected String titulo;
    protected String descricao;
    protected Date dataCriacao;
    protected String status;
    
    // Construtor
    public ItemGerenciavel(int id, String titulo, String descricao, String status) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = new Date(); // Data atual quando o item é criado
        this.status = status;
    }
    
    // Métodos abstratos do diagrama UML
    public abstract String obterDetalhes();
    
    public void atualizarStatus(String novoStatus) {
        this.status = novoStatus;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Date getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "ItemGerenciavel{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", status='" + status + '\'' +
                '}';
    }
}
