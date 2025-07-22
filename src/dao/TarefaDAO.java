package dao;

import model.Categoria;
import model.Tarefa;
import util.ArquivoUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TarefaDAO {
    private static final String NOME_ARQUIVO = "tarefas.txt";
    private static final String NOME_ARQUIVO_TAREFA_CATEGORIA = "tarefa_categoria.txt";
    
    // Método para salvar todas as tarefas
    public static void salvarTarefas(List<Tarefa> tarefas) {
        ArquivoUtil.criarDiretorioDados();
        
        // Salvar dados básicos das tarefas
        try (PrintWriter writer = new PrintWriter(new FileWriter(ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO)))) {
            for (Tarefa tarefa : tarefas) {
                String linha = tarefa.getId() + ArquivoUtil.getSeparador() + 
                              ArquivoUtil.escaparString(tarefa.getTitulo()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.escaparString(tarefa.getDescricao()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.dateParaString(tarefa.getDataCriacao()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.escaparString(tarefa.getStatus()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.dateParaString(tarefa.getDataConclusao()) + ArquivoUtil.getSeparador() +
                              ArquivoUtil.escaparString(tarefa.getPrioridade());
                writer.println(linha);
            }
            System.out.println("Tarefas salvas com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar tarefas: " + e.getMessage());
        }
        
        // Salvar relacionamentos tarefa-categoria
        salvarRelacionamentoTarefaCategoria(tarefas);
    }
    
    // Método para salvar relacionamentos N:N entre tarefa e categoria
    private static void salvarRelacionamentoTarefaCategoria(List<Tarefa> tarefas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO_TAREFA_CATEGORIA)))) {
            for (Tarefa tarefa : tarefas) {
                for (Categoria categoria : tarefa.getCategorias()) {
                    String linha = tarefa.getId() + ArquivoUtil.getSeparador() + categoria.getId();
                    writer.println(linha);
                }
            }
            System.out.println("Relacionamentos tarefa-categoria salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar relacionamentos tarefa-categoria: " + e.getMessage());
        }
    }
    
    // Método para carregar todas as tarefas
    public static List<Tarefa> carregarTarefas(List<Categoria> categorias) {
        List<Tarefa> tarefas = new ArrayList<>();
        String caminhoArquivo = ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO);
        
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de tarefas não encontrado. Iniciando com lista vazia.");
            return tarefas;
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
                    Date dataConclusao = ArquivoUtil.stringParaDate(partes[5]);
                    String prioridade = ArquivoUtil.desescaparString(partes[6]);
                    
                    Tarefa tarefa = new Tarefa(id, titulo, descricao, status, prioridade);
                    if (dataCriacao != null) {
                        tarefa.setDataCriacao(dataCriacao);
                    }
                    if (dataConclusao != null) {
                        tarefa.setDataConclusao(dataConclusao);
                    }
                    tarefas.add(tarefa);
                }
            }
            System.out.println("Tarefas carregadas: " + tarefas.size());
        } catch (IOException e) {
            System.err.println("Erro ao carregar tarefas: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro no formato dos dados de tarefa: " + e.getMessage());
        }
        
        // Carregar relacionamentos tarefa-categoria
        carregarRelacionamentoTarefaCategoria(tarefas, categorias);
        
        return tarefas;
    }
    
    // Método para carregar relacionamentos N:N entre tarefa e categoria
    private static void carregarRelacionamentoTarefaCategoria(List<Tarefa> tarefas, List<Categoria> categorias) {
        String caminhoArquivo = ArquivoUtil.obterCaminhoArquivo(NOME_ARQUIVO_TAREFA_CATEGORIA);
        
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo de relacionamentos tarefa-categoria não encontrado.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(ArquivoUtil.getSeparador());
                if (partes.length >= 2) {
                    int tarefaId = Integer.parseInt(partes[0]);
                    int categoriaId = Integer.parseInt(partes[1]);
                    
                    // Encontrar a tarefa
                    Tarefa tarefa = buscarPorId(tarefas, tarefaId);
                    // Encontrar a categoria
                    Categoria categoria = buscarCategoriaPorId(categorias, categoriaId);
                    
                    if (tarefa != null && categoria != null) {
                        tarefa.adicionarCategoria(categoria);
                    }
                }
            }
            System.out.println("Relacionamentos tarefa-categoria carregados!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar relacionamentos tarefa-categoria: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro no formato dos relacionamentos tarefa-categoria: " + e.getMessage());
        }
    }
    
    // Método para obter próximo ID disponível
    public static int obterProximoId(List<Tarefa> tarefas) {
        int maiorId = 0;
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getId() > maiorId) {
                maiorId = tarefa.getId();
            }
        }
        return maiorId + 1;
    }
    
    // Método para buscar tarefa por ID
    public static Tarefa buscarPorId(List<Tarefa> tarefas, int id) {
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getId() == id) {
                return tarefa;
            }
        }
        return null;
    }
    
    // Método auxiliar para buscar categoria por ID
    private static Categoria buscarCategoriaPorId(List<Categoria> categorias, int id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId() == id) {
                return categoria;
            }
        }
        return null;
    }
}
