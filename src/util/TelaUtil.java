package util;

public class TelaUtil {
    
    private static final java.util.Scanner INPUT_SCANNER = new java.util.Scanner(System.in);
    
    /**
     * Limpa a tela do console
     */
    public static void limparTela() {
        try {
            // Para Windows
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Tenta usar o comando cls do Windows
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } 
            // Para Linux/Mac
            else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch (Exception e) {
            // Se não conseguir limpar usando comandos do sistema, usar quebras de linha
            limparTelaFallback();
        }
    }
    
    /**
     * Método alternativo para limpar a tela usando quebras de linha
     */
    private static void limparTelaFallback() {
        // Usa códigos ANSI para limpar a tela (funciona na maioria dos terminais modernos)
        System.out.print("\033[2J\033[H");
        System.out.flush();
        
        // Se os códigos ANSI não funcionarem, usa quebras de linha
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
    /**
     * Aguarda o usuário pressionar Enter para continuar
     */
    public static void aguardarEnter() {
        System.out.print("\nPressione Enter para continuar...");
        try {
            INPUT_SCANNER.nextLine();
        } catch (Exception e) {
            // Ignora exceções
        }
    }
    
    /**
     * Exibe uma mensagem e aguarda o usuário pressionar Enter
     */
    public static void aguardarEnter(String mensagem) {
        System.out.print(mensagem);
        try {
            INPUT_SCANNER.nextLine();
        } catch (Exception e) {
            // Ignora exceções
        }
    }
    
    /**
     * Limpa a tela e exibe um cabeçalho
     */
    public static void exibirCabecalho(String titulo) {
        limparTela();
        System.out.println("=".repeat(60));
        System.out.println("    " + titulo);
        System.out.println("=".repeat(60));
    }
}
