package model;

public class Categoria {
    private int id;
    private String nome;
    
    // Construtor
    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
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
    
    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria categoria = (Categoria) obj;
        return id == categoria.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
