package view;

public class SistemaMain {
    public static void main(String[] args) {
        System.out.println("> Iniciando Sistema de Gestao de Projetos e Tarefas...\n");
        
        MenuAutenticacao menuAuth = new MenuAutenticacao();
        menuAuth.iniciar();
        
        System.out.println("\n> Sistema encerrado. Ate logo!");
    }
}
