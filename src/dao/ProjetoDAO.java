package dao;

import model.Projeto;
import util.ArquivoUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjetoDAO {
    private static final String NOME_ARQUIVO = "projetos.txt";
    
    // Método para salvar todos os projetos
    public static void salvarProjetos(List<Projeto> projetos) {
        ArquivoUtil.criarDiretorioDados();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO)))) {
            for (Projeto projeto : projetos) {
                String linha = projeto.getId() + ArquivoUtil.getSeparador() + 
                              ArquivoUtil.escaparString(projeto.getTitulo()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.escaparString(projeto.getDescricao()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.dateParaString(projeto.getDataCriacao()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.escaparString(projeto.getStatus()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.dateParaString(projeto.getDataInicio()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.dateParaString(projeto.getDataFim());
                writer.println(linha);
            }
            System.out.println("Projetos salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar projetos: " + e.getMessage());
        }
    }
    
    // Método para carregar todos os projetos
    public static List<Projeto> carregarProjetos() {
        List<Projeto> projetos = new ArrayList<>();
        String caminhoArquivo = ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO);
        
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de projetos não encontrado. Iniciando com lista vazia.");
            return projetos;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(ArquivoUtil.getSeparador());
                if (partes.length >= 7) {
                    int id = Integer.parseInt(partes[0]);
                    String titulo = ArquivoUtil.desescaparString(partes[1]);
                    String descricao = ArquivoUtil.desescaparString(partes[2]);
                    Date dataCriacao = ArquivoUtil.stringParaDate(partes[3]);
                    String status = ArquivoUtil.desescaparString(partes[4]);
                    Date dataInicio = ArquivoUtil.stringParaDate(partes[5]);
                    Date dataFim = ArquivoUtil.stringParaDate(partes[6]);
                    
                    Projeto projeto = new Projeto(id, titulo, descricao, status, dataInicio, dataFim);
                    if (dataCriacao != null) {
                        projeto.setDataCriacao(dataCriacao);
                    }
                    projetos.add(projeto);
                }
            }
            System.out.println("Projetos carregados: " + projetos.size());
        } catch (IOException e) {
            System.err.println("Erro ao carregar projetos: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro no formato dos dados de projeto: " + e.getMessage());
        }
        
        return projetos;
    }
    
    // Método para obter próximo ID disponível
    public static int obterProximoId(List<Projeto> projetos) {
        int maiorId = 0;
        for (Projeto projeto : projetos) {
            if (projeto.getId() > maiorId) {
                maiorId = projeto.getId();
            }
        }
        return maiorId + 1;
    }
    
    // Método para buscar projeto por ID
    public static Projeto buscarPorId(List<Projeto> projetos, int id) {
        for (Projeto projeto : projetos) {
            if (projeto.getId() == id) {
                return projeto;
            }
        }
        return null;
    }
}
