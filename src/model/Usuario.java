package model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private List<ItemGerenciavel> itensGerenciaveis;
    
    // Construtor
    public Usuario(int id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.itensGerenciaveis = new ArrayList<>();
    }
    
    // Método do diagrama UML
    public boolean verificarLogin(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public List<ItemGerenciavel> getItensGerenciaveis() {
        return itensGerenciaveis;
    }
    
    public void adicionarItem(ItemGerenciavel item) {
        this.itensGerenciaveis.add(item);
    }
    
    public void removerItem(ItemGerenciavel item) {
        this.itensGerenciaveis.remove(item);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
