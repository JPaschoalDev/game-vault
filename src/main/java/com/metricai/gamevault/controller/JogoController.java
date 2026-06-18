package com.metricai.gamevault.controller;

import com.metricai.gamevault.dao.JogoDAO;
import com.metricai.gamevault.model.Jogo;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class JogoController {

    @FXML private FlowPane gridJogos;
    @FXML private TextField campoTitulo;
    @FXML private TextField campoPlataforma;
    @FXML private TextField campoNota;
    @FXML private TextField campoGenero;
    @FXML private TextField campoAnoLancamento;
    @FXML private CheckBox campoZerado;
    @FXML private Label labelCapa;
    @FXML private Label labelStatus;

    private JogoDAO jogoDAO = new JogoDAO();
    private Jogo jogoEditando = null;
    private Jogo jogoSelecionado = null;
    private String capaEscolhida = null;

    @FXML
    public void initialize() {
        carregarJogos();
        labelStatus.setVisible(false);
        labelStatus.setManaged(false);
    }

    // MONTA OS CARDS E COLOCA NO GRID
    private void carregarJogos() {
        gridJogos.getChildren().clear();
        List<Jogo> lista = jogoDAO.listarJogos();

        for (Jogo jogo : lista) {
            VBox card = criarCard(jogo);
            gridJogos.getChildren().add(card);
        }
    }

    // CRIA UM CARD VISUAL PARA CADA JOGO
    private VBox criarCard(Jogo jogo) {
        VBox card = new VBox(5);
        card.getStyleClass().add("card-jogo");
        card.setPrefWidth(200);
        card.setMinWidth(200);
        card.setMaxWidth(200);

        // IMAGEM DA CAPA
        ImageView imageView = new ImageView();
        imageView.setFitWidth(184);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(true);

        // MOLDURA: um StackPane para colocar a imagem e garantir que ela fique dentro de uma moldura arredondada
        javafx.scene.layout.StackPane moldura = new javafx.scene.layout.StackPane(imageView);
        moldura.setPrefSize(184, 250);
        moldura.setMinSize(184, 250);
        moldura.setMaxSize(184, 250);
        moldura.setStyle("-fx-background-color: #2A2A5A; -fx-background-radius: 8px;");

        // CLIP: corta tudo que sai da moldura, garantindo que a imagem fique arredondada
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(184, 250);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        moldura.setClip(clip);

        // PLACEHOLDER
        Label placeholder = new Label("🎮");
        placeholder.getStyleClass().add("card-placeholder");
        placeholder.setPrefSize(184, 250);
        placeholder.setMinSize(184, 250);
        placeholder.setMaxSize(184, 250);

        // TENTA CARREGAR A CAPA
        boolean temCapa = false;
        if (jogo.getCapaPath() != null && !jogo.getCapaPath().isEmpty()) {
            try {
                File arquivo = new File(jogo.getCapaPath());
                if (arquivo.exists()) {
                    Image imagem = new Image(arquivo.toURI().toString());
                    imageView.setImage(imagem);

                    // COVER FIT: recorta o centro da imagem pra preencher tudo
                    double imgW = imagem.getWidth();
                    double imgH = imagem.getHeight();
                    double targetRatio = 184.0 / 250.0;
                    double imgRatio = imgW / imgH;

                    double viewW, viewH, viewX, viewY;
                    if (imgRatio > targetRatio) {
                        // Imagem mais larga: corta os lados
                        viewH = imgH;
                        viewW = imgH * targetRatio;
                        viewX = (imgW - viewW) / 2;
                        viewY = 0;
                    } else {
                        // Imagem mais alta: corta em cima e embaixo
                        viewW = imgW;
                        viewH = imgW / targetRatio;
                        viewX = 0;
                        viewY = (imgH - viewH) / 2;
                    }

                    imageView.setViewport(new javafx.geometry.Rectangle2D(viewX, viewY, viewW, viewH));
                    imageView.setPreserveRatio(false);
                    temCapa = true;
                }
            } catch (Exception e) {
                System.out.println("Erro ao carregar capa: " + e.getMessage());
            }
        }

        // LABELS DE INFORMAÇÃO
        Label labelTitulo = new Label(jogo.getTitulo());
        labelTitulo.getStyleClass().add("card-titulo");
        labelTitulo.setWrapText(true);
        labelTitulo.setMaxWidth(184);
        labelTitulo.setMinHeight(35);
        labelTitulo.setMaxHeight(35);

        Label labelNota = new Label("★ " + jogo.getNota());
        labelNota.getStyleClass().add("card-nota");

        Label labelPlataforma = new Label(jogo.getPlataforma());
        labelPlataforma.getStyleClass().add("card-plataforma");

        card.getChildren().addAll(temCapa ? moldura : placeholder, labelTitulo, labelNota, labelPlataforma);

        // CLIQUE NO CARD = SELECIONA O JOGO
        card.setOnMouseClicked(event -> {
            jogoSelecionado = jogo;
            // Remove seleção visual de todos
            gridJogos.getChildren().forEach(node -> node.getStyleClass().remove("card-selecionado"));
            // Adiciona seleção visual neste
            card.getStyleClass().add("card-selecionado");
        });

        return card;
    }

    // ESCOLHER IMAGEM DE CAPA
    @FXML
    private void escolherCapa() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher capa do jogo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File arquivo = fileChooser.showOpenDialog(gridJogos.getScene().getWindow());
        if (arquivo != null) {
            capaEscolhida = arquivo.getAbsolutePath();
            labelCapa.setText(arquivo.getName());
        }
    }

    // SALVAR (CADASTRAR OU ATUALIZAR)
    @FXML
    private void salvarJogo() {
        try {
            String titulo = campoTitulo.getText();
            if (titulo.trim().isEmpty()) {
                mostrarStatus("⚠️ Título não pode ser vazio!", "#FF2E88");
                return;
            }

            String plataforma = campoPlataforma.getText();
            String genero = campoGenero.getText();
            int ano = Integer.parseInt(campoAnoLancamento.getText());
            double nota = Double.parseDouble(campoNota.getText());
            if(nota<0 || nota >10){
                mostrarStatus("⚠️ Nota deve ser entre 0 e 10!", "#FF2E88");
                return;
            }
            boolean zerado = campoZerado.isSelected();

            if (jogoEditando == null) {
                Jogo novo = new Jogo(titulo, plataforma, genero, ano, nota, zerado);
                novo.setCapaPath(capaEscolhida);
                jogoDAO.inserirJogo(novo);
                mostrarStatus("✅ Jogo cadastrado!", "#4EF3C9");
            } else {
                jogoEditando.setTitulo(titulo);
                jogoEditando.setPlataforma(plataforma);
                jogoEditando.setGenero(genero);
                jogoEditando.setAnoLancamento(ano);
                jogoEditando.setNota(nota);
                jogoEditando.setZerado(zerado);
                if (capaEscolhida != null) {
                    jogoEditando.setCapaPath(capaEscolhida);
                }
                jogoDAO.atualizarJogo(jogoEditando);
                mostrarStatus("✏️ Jogo atualizado!", "#FFD166");
                jogoEditando = null;
            }

            carregarJogos();
            limparCampos();

        } catch (NumberFormatException e) {
            mostrarStatus("⚠ Ano e Nota devem ser números!", "#FF2E88");
        }
    }

    // EDITAR SELECIONADO
    @FXML
    private void editarJogo() {
        if (jogoSelecionado == null) {
            mostrarStatus("⚠️ Nenhum jogo selecionado!", "#FF2E88");
            return;
        }

        jogoEditando = jogoSelecionado;
        campoTitulo.setText(jogoSelecionado.getTitulo());
        campoPlataforma.setText(jogoSelecionado.getPlataforma());
        campoGenero.setText(jogoSelecionado.getGenero());
        campoAnoLancamento.setText(String.valueOf(jogoSelecionado.getAnoLancamento()));
        campoNota.setText(String.valueOf(jogoSelecionado.getNota()));
        campoZerado.setSelected(jogoSelecionado.isZerado());

        if (jogoSelecionado.getCapaPath() != null) {
            labelCapa.setText(new File(jogoSelecionado.getCapaPath()).getName());
            capaEscolhida = jogoSelecionado.getCapaPath();
        }
    }

    // EXCLUIR SELECIONADO
    @FXML
    private void excluirJogo() {
        if (jogoSelecionado == null) {
            mostrarStatus("⚠ Selecione um jogo primeiro!", "#FFD166");
            return;
        }

        jogoDAO.deletarJogo(jogoSelecionado.getId());
        mostrarStatus("❌ Jogo excluído!", "#FF2E88");
        jogoSelecionado = null;
        carregarJogos();
    }

    // LIMPAR CAMPOS
    private void limparCampos() {
        campoTitulo.clear();
        campoPlataforma.clear();
        campoGenero.clear();
        campoAnoLancamento.clear();
        campoNota.clear();
        campoZerado.setSelected(false);
        labelCapa.setText("Nenhuma capa selecionada");
        capaEscolhida = null;
        jogoSelecionado = null;
    }

    // EXIBIR MENSAGEM DE STATUS
    private void mostrarStatus(String mensagem, String cor) {
        labelStatus.setText(mensagem);
        labelStatus.setStyle("-fx-text-fill: " + cor + ";");
        labelStatus.setVisible(true);
        labelStatus.setManaged(true);

        javafx.animation.PauseTransition pausa = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(3)
        );
        pausa.setOnFinished(e -> {
            labelStatus.setText("");
            labelStatus.setVisible(false);
            labelStatus.setManaged(false);
        });
        pausa.play();
    }
}