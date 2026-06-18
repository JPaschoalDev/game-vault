package com.metricai.gamevault.dao;

import com.metricai.gamevault.model.Jogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class JogoDAO {

    // CREATE (INSERIR UM NOVO DADO DO BANCO)
    public void inserirJogo(Jogo jogo) {
        String sql = "INSERT INTO jogos (titulo,plataforma,genero,ano_lancamento,nota,zerado,capa_path)" +
                "VALUES (?,?,?,?,?,?,?)";
        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, jogo.getTitulo());
            stmt.setString(2, jogo.getPlataforma());
            stmt.setString(3, jogo.getGenero());
            stmt.setInt(4, jogo.getAnoLancamento());
            stmt.setDouble(5, jogo.getNota());
            stmt.setBoolean(6, jogo.isZerado());
            stmt.setString(7, jogo.getCapaPath());

            stmt.executeUpdate();
            System.out.println("Jogo inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir jogo: " + e.getMessage());
        }
    }

    // READ (LISTAR TODOS OS REGISTROS DO BANCO)
    public List<Jogo> listarJogos() {
        List<Jogo> jogos = new ArrayList<>();
        String sql = "SELECT * FROM jogos";

        try (Connection conexao = ConexaoDB.getConexao();
        PreparedStatement stmt = conexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()){
            while (rs.next()){
                Jogo jogo = new Jogo();
                jogo.setId(rs.getInt("id"));
                jogo.setTitulo(rs.getString("titulo"));
                jogo.setPlataforma(rs.getString("plataforma"));
                jogo.setGenero(rs.getString("genero"));
                jogo.setAnoLancamento(rs.getInt("ano_lancamento"));
                jogo.setNota(rs.getDouble("nota"));
                jogo.setZerado(rs.getBoolean("zerado"));
                jogo.setCapaPath(rs.getString("capa_path"));

                jogos.add(jogo);
            }
            return jogos;
        }catch (SQLException e){
            System.out.println("Erro ao listar jogos: " + e.getMessage());
        }
        return jogos;
    }

    // UPDATE (ATUALIZAR UM REGISTRO EXISTENTE)
    public void atualizarJogo(Jogo jogo){
        String sql = "UPDATE jogos SET titulo=?, plataforma=?, genero=?, ano_lancamento=?, nota=?, zerado=?, capa_path=? WHERE id=?";

        try (Connection conexao = ConexaoDB.getConexao();
        PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setString(1, jogo.getTitulo());
            stmt.setString(2, jogo.getPlataforma());
            stmt.setString(3, jogo.getGenero());
            stmt.setInt(4, jogo.getAnoLancamento());
            stmt.setDouble(5, jogo.getNota());
            stmt.setBoolean(6, jogo.isZerado());
            stmt.setString(7, jogo.getCapaPath());
            stmt.setInt(8, jogo.getId());

            int linhas = stmt.executeUpdate();
            if (linhas > 0){
                System.out.println("Jogo atualizado com sucesso!");
            }else{
                System.out.println("Nenhum jogo com o iD " + jogo.getId() + " foi encontrado!");
            }
        }catch (SQLException e){
            System.out.println("Erro ao atualizar jogo: " + e.getMessage());
        }
    }

    // DELETE (EXCLUIR UM REGISTRO DO BANCO)
    public void deletarJogo(int id){
        String sql = "DELETE FROM jogos WHERE id=?";

        try (Connection conexao = ConexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setInt(1,id);

            int linhas = stmt.executeUpdate();
            if (linhas > 0){
                System.out.println("Jogo deletado com sucesso!");
            }else{
                System.out.println("Nenhum jogo com o iD "+id+" foi encontrado!");
            }

        }catch (SQLException e){
            System.out.println("Erro ao deletar jogo: " + e.getMessage());
        }
        }
    }