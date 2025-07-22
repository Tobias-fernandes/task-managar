package dao;

import model.Usuario;
import util.ArquivoUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final String NOME_ARQUIVO = "usuarios.txt";
    
    // Método para salvar todos os usuários
    public static void salvarUsuarios(List<Usuario> usuarios) {
        ArquivoUtil.criarDiretorioDados();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO)))) {
            for (Usuario usuario : usuarios) {
                String linha = usuario.getId() + ArquivoUtil.getSeparador() + 
                              ArquivoUtil.escaparString(usuario.getNome()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.escaparString(usuario.getEmail()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.escaparString(usuario.getSenha());
                writer.println(linha);
            }
            System.out.println("Usuários salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
    
    // Método para carregar todos os usuários
    public static List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String caminhoArquivo = ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO);
        
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de usuários não encontrado. Iniciando com lista vazia.");
            return usuarios;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(ArquivoUtil.getSeparador());
                if (partes.length >= 4) {
                    int id = Integer.parseInt(partes[0]);
                    String nome = ArquivoUtil.desescaparString(partes[1]);
                    String email = ArquivoUtil.desescaparString(partes[2]);
                    String senha = ArquivoUtil.desescaparString(partes[3]);
                    
                    Usuario usuario = new Usuario(id, nome, email, senha);
                    usuarios.add(usuario);
                }
            }
            System.out.println("Usuários carregados: " + usuarios.size());
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro no formato dos dados de usuário: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    // Método para obter próximo ID disponível
    public static int obterProximoId(List<Usuario> usuarios) {
        int maiorId = 0;
        for (Usuario usuario : usuarios) {
            if (usuario.getId() > maiorId) {
                maiorId = usuario.getId();
            }
        }
        return maiorId + 1;
    }
    
    // Método para buscar usuário por email
    public static Usuario buscarPorEmail(List<Usuario> usuarios, String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }
}
