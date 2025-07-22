package dao;

import model.Categoria;
import util.ArquivoUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private static final String NOME_ARQUIVO = "categorias.txt";
    
    // Método para salvar todas as categorias
    public static void salvarCategorias(List<Categoria> categorias) {
        ArquivoUtil.criarDiretorioDados();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO)))) {
            for (Categoria categoria : categorias) {
                String linha = categoria.getId() + ArquivoUtil.getSeparador() + 
                              ArquivoUtil.escaparString(categoria.getNome());
                writer.println(linha);
            }
            System.out.println("Categorias salvas com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar categorias: " + e.getMessage());
        }
    }
    
    // Método para carregar todas as categorias
    public static List<Categoria> carregarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String caminhoArquivo = ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO);
        
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de categorias não encontrado. Iniciando com lista vazia.");
            return categorias;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(ArquivoUtil.getSeparador());
                if (partes.length >= 2) {
                    int id = Integer.parseInt(partes[0]);
                    String nome = ArquivoUtil.desescaparString(partes[1]);
                    
                    Categoria categoria = new Categoria(id, nome);
                    categorias.add(categoria);
                }
            }
            System.out.println("Categorias carregadas: " + categorias.size());
        } catch (IOException e) {
            System.err.println("Erro ao carregar categorias: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro no formato dos dados de categoria: " + e.getMessage());
        }
        
        return categorias;
    }
    
    // Método para obter próximo ID disponível
    public static int obterProximoId(List<Categoria> categorias) {
        int maiorId = 0;
        for (Categoria categoria : categorias) {
            if (categoria.getId() > maiorId) {
                maiorId = categoria.getId();
            }
        }
        return maiorId + 1;
    }
}
