package com.metricai.gamevault.dao;

import java.sql.Connection;
import java.sql.Statement;

public class CriarTabelaNuvem {
    public static void main(String[] args) {
        String sql = "CREATE TABLE IF NOT EXISTS jogos (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "titulo VARCHAR(150) NOT NULL," +
                "plataforma VARCHAR(50)," +
                "genero VARCHAR(50)," +
                "ano_lancamento INT," +
                "nota DECIMAL(3,1)," +
                "zerado BOOLEAN DEFAULT FALSE," +
                "capa_path VARCHAR(500) DEFAULT NULL)";

        try (Connection conexao = ConexaoDB.getConexao();
             Statement stmt = conexao.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabela criada na nuvem com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}