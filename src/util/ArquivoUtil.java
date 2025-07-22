package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArquivoUtil {
    private static final String CAMINHO_DADOS = "../dados/";
    private static final String SEPARADOR = ";";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    // Método para garantir que o diretório de dados existe
    public static void criarDiretorioDados() {
        File diretorio = new File(CAMINHO_DADOS);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }
    
    // Método para obter o caminho completo do arquivo
    public static String obterCaminhoArquivo(String nomeArquivo) {
        return CAMINHO_DADOS + nomeArquivo;
    }
    
    // Método para converter Date para String
    public static String dateParaString(Date data) {
        if (data == null) return "";
        return DATE_FORMAT.format(data);
    }
    
    // Método para converter String para Date
    public static Date stringParaDate(String dataStr) {
        if (dataStr == null || dataStr.trim().isEmpty()) return null;
        try {
            return DATE_FORMAT.parse(dataStr);
        } catch (Exception e) {
            System.err.println("Erro ao converter data: " + dataStr);
            return null;
        }
    }
    
    // Getter para o separador
    public static String getSeparador() {
        return SEPARADOR;
    }
    
    // Método para escapar strings que contenham o separador
    public static String escaparString(String str) {
        if (str == null) return "";
        return str.replace(SEPARADOR, "\\;");
    }
    
    // Método para desescapar strings
    public static String desescaparString(String str) {
        if (str == null) return "";
        return str.replace("\\;", SEPARADOR);
    }
}
