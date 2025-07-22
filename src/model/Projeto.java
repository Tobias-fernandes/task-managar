package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Projeto extends ItemGerenciavel {
    private Date dataInicio;
    private Date dataFim;
    private List<Tarefa> tarefas;
    
    // Construtor
    public Projeto(int id, String titulo, String descricao, String status, Date dataInicio, Date dataFim) {
        super(id, titulo, descricao, status);
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.tarefas = new ArrayList<>();
    }
    
    // Implementação do método abstrato
    @Override
    public String obterDetalhes() {
        return "Projeto: " + titulo + 
               "\nDescrição: " + descricao + 
               "\nStatus: " + status + 
               "\nData Início: " + dataInicio + 
               "\nData Fim: " + dataFim + 
               "\nNúmero de Tarefas: " + tarefas.size();
    }
    
    // Métodos para gerenciar tarefas (relacionamento 1:N)
    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
    }
    
    public void removerTarefa(Tarefa tarefa) {
        this.tarefas.remove(tarefa);
    }
    
    public List<Tarefa> getTarefas() {
        return tarefas;
    }
    
    // Getters e Setters específicos
    public Date getDataInicio() {
        return dataInicio;
    }
    
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public Date getDataFim() {
        return dataFim;
    }
    
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
    
    @Override
    public String toString() {
        return "Projeto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", status='" + status + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", numTarefas=" + tarefas.size() +
                '}';
    }
}
