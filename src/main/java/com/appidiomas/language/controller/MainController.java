package com.appidiomas.language.controller;

import com.appidiomas.language.model.Idioma;
import com.appidiomas.language.model.Topico;
import com.appidiomas.language.service.IdiomaService;
import com.appidiomas.language.service.TopicoService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Comparator;
import java.util.Collections;
import javafx.scene.control.ButtonBar.ButtonData;

public class MainController {

    @FXML
    private VBox vboxIdiomas;
    @FXML
    private TextField txtNomeIdioma;
    @FXML
    private ListView<Idioma> listViewIdiomas;

    @FXML
    private VBox vboxTopicos;
    @FXML
    private ComboBox<Idioma> cmbIdiomasTopicos;
    @FXML
    private TextField txtNomeTopico;
    @FXML
    private TextField txtLinkEstudo;
    @FXML
    private ListView<Topico> listViewTopicos;

    private IdiomaService idiomaService;
    private TopicoService topicoService;

    private ObservableList<Idioma> idiomasObservableList;
    private ObservableList<Topico> topicosObservableList;

    private Idioma idiomaAtualParaTopicos;

    public MainController() {
        this.idiomaService = new IdiomaService();
        this.topicoService = new TopicoService();
        this.idiomasObservableList = FXCollections.observableArrayList();
        this.topicosObservableList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        carregarIdiomas();
        listViewIdiomas.setItems(idiomasObservableList);

        listViewIdiomas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNomeIdioma.setText(newValue.getNome());
            } else {
                txtNomeIdioma.clear();
            }
        });

        listViewIdiomas.setCellFactory(lv -> new ListCell<Idioma>() {
            @Override
            protected void updateItem(Idioma idioma, boolean empty) {
                super.updateItem(idioma, empty);
                if (empty || idioma == null) {
                    setText(null);
                } else {
                    setText(idioma.getNome() + " (" + idioma.getTopicosEstudados() + "/" + idioma.getTotalDeTopicos() + ")");
                }
            }
        });

        cmbIdiomasTopicos.setItems(idiomasObservableList);
        listViewTopicos.setItems(topicosObservableList);

        listViewTopicos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNomeTopico.setText(newValue.getNome());
                txtLinkEstudo.setText(newValue.getLinkEstudo() != null ? newValue.getLinkEstudo() : "");
            } else {
                txtNomeTopico.clear();
                txtLinkEstudo.clear();
            }
        });

        listViewTopicos.setCellFactory(lv -> new ListCell<Topico>() {
            private final HBox hbox = new HBox(10);
            private final CheckBox checkBox = new CheckBox();
            private final Label labelNome = new Label();
            private final Label labelOrdem = new Label();
            private final Hyperlink link = new Hyperlink("Abrir Link");

            {
                hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                hbox.getChildren().addAll(checkBox, labelOrdem, labelNome, link);
                HBox.setHgrow(labelNome, Priority.ALWAYS);

                checkBox.setOnAction(event -> {
                    Topico currentTopico = getItem();
                    if (currentTopico != null) {
                        currentTopico.setEstudado(checkBox.isSelected());
                        topicoService.atualizarTopico(currentTopico.getId(), currentTopico.getNome(),
                                currentTopico.getOrdem(), currentTopico.isEstudado(), currentTopico.getLinkEstudo());
                        carregarIdiomas();
                        listViewTopicos.refresh();
                    }
                });

                link.setOnAction(event -> {
                    Topico currentTopico = getItem();
                    if (currentTopico != null && currentTopico.getLinkEstudo() != null && !currentTopico.getLinkEstudo().isEmpty()) {
                        abrirLinkNoNavegador(currentTopico.getLinkEstudo());
                    } else {
                        mostrarAlerta(AlertType.INFORMATION, "Link Não Encontrado", "Não há um link de estudo para este tópico.");
                    }
                });
            }

            @Override
            protected void updateItem(Topico topico, boolean empty) {
                super.updateItem(topico, empty);
                if (empty || topico == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    checkBox.setSelected(topico.isEstudado());
                    labelNome.setText(topico.getNome());
                    labelOrdem.setText("(" + topico.getOrdem() + ")");
                    link.setVisible(topico.getLinkEstudo() != null && !topico.getLinkEstudo().isEmpty());
                    setGraphic(hbox);
                }
            }
        });

        mostrarTelaIdiomas();
    }

    @FXML
    private void gerenciarTopicosDoIdioma() {
        Idioma idiomaSelecionado = listViewIdiomas.getSelectionModel().getSelectedItem();
        if (idiomaSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Idioma Selecionado", "Selecione um idioma na lista para gerenciar seus tópicos.");
            return;
        }
        showTopicsView(idiomaSelecionado);
    }

    @FXML
    private void abrirDialogoAtualizarIdioma() {
        Idioma idiomaSelecionado = listViewIdiomas.getSelectionModel().getSelectedItem();
        if (idiomaSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Idioma Selecionado", "Selecione um idioma para atualizar.");
            return;
        }

        // Changed generic type to ButtonType
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Atualizar Idioma");
        dialog.setHeaderText("Renomear o idioma '" + idiomaSelecionado.getNome() + "'");

        ButtonType updateButtonType = new ButtonType("Atualizar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField novoNomeField = new TextField(idiomaSelecionado.getNome());
        novoNomeField.setPromptText("Novo nome do idioma");

        VBox content = new VBox(10, new Label("Novo nome:"), novoNomeField);
        content.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));
        dialog.getDialogPane().setContent(content);

        final Button btUpdate = (Button) dialog.getDialogPane().lookupButton(updateButtonType);
        if (btUpdate != null) {
            btUpdate.disableProperty().bind(
                    novoNomeField.textProperty().isEmpty().or(
                            novoNomeField.textProperty().isEqualTo(idiomaSelecionado.getNome())
                    )
            );
        }

        Optional<ButtonType> result = dialog.showAndWait(); // Now Optional<ButtonType>

        // Check if the result is present AND if the "Update" button was clicked
        if (result.isPresent() && result.get() == updateButtonType) {
            String nomeTrimmed = novoNomeField.getText().trim();
            if (!nomeTrimmed.isEmpty()) {
                if (idiomaService.atualizarIdioma(idiomaSelecionado.getId(), nomeTrimmed)) {
                    carregarIdiomas();
                    txtNomeIdioma.clear();
                    mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Idioma atualizado para '" + nomeTrimmed + "'!");
                } else {
                    mostrarAlerta(AlertType.ERROR, "Erro ao Atualizar", "Não foi possível atualizar o idioma. Talvez o nome já exista.");
                }
            } else {
                mostrarAlerta(AlertType.WARNING, "Campo Vazio", "O novo nome do idioma não pode ser vazio.");
            }
        }
        // If result is not present or if Cancel was clicked, do nothing.
    }

    @FXML
    private void mostrarTelaIdiomas() {
        vboxIdiomas.setVisible(true);
        vboxIdiomas.setManaged(true);

        vboxTopicos.setVisible(false);
        vboxTopicos.setManaged(false);

        txtNomeTopico.clear();
        txtLinkEstudo.clear();
        listViewTopicos.getSelectionModel().clearSelection();
        topicosObservableList.clear();
    }

    private void showTopicsView(Idioma idioma) {
        if (idioma == null) {
            mostrarAlerta(AlertType.WARNING, "Idioma Inválido", "Selecione um idioma para ver seus tópicos.");
            return;
        }

        idiomaAtualParaTopicos = idioma;
        cmbIdiomasTopicos.getSelectionModel().select(idioma);
        carregarTopicos(idioma.getId());

        vboxIdiomas.setVisible(false);
        vboxIdiomas.setManaged(false);

        vboxTopicos.setVisible(true);
        vboxTopicos.setManaged(true);
    }

    @FXML
    private void adicionarIdioma() {
        String nomeIdioma = txtNomeIdioma.getText().trim();
        if (nomeIdioma.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campo Vazio", "O nome do idioma não pode ser vazio.");
            return;
        }

        if (idiomaService.adicionarIdioma(nomeIdioma)) {
            txtNomeIdioma.clear();
            carregarIdiomas();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Idioma '" + nomeIdioma + "' adicionado com sucesso!");
        } else {
            mostrarAlerta(AlertType.ERROR, "Erro ao Adicionar", "Não foi possível adicionar o idioma '" + nomeIdioma + "'. Talvez já exista.");
        }
    }

    @FXML
    private void removerIdioma() {
        Idioma idiomaSelecionado = listViewIdiomas.getSelectionModel().getSelectedItem();
        if (idiomaSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Idioma Selecionado", "Selecione um idioma para remover.");
            return;
        }

        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar Remoção");
        confirmDialog.setHeaderText("Remover Idioma?");
        confirmDialog.setContentText("Tem certeza que deseja remover o idioma '" + idiomaSelecionado.getNome() + "'? Isso também removerá todos os tópicos associados a ele.");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean sucesso = idiomaService.removerIdioma(idiomaSelecionado.getId());

            if (sucesso) {
                carregarIdiomas();
                topicosObservableList.clear();
                txtNomeIdioma.clear();
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Idioma '" + idiomaSelecionado.getNome() + "' e seus tópicos associados removidos com sucesso!");
            } else {
                mostrarAlerta(AlertType.ERROR, "Erro ao Remover", "Não foi possível remover o idioma.");
            }
        }
    }

    @FXML
    private void handleIdiomaSelection() {
        Idioma idiomaSelecionado = cmbIdiomasTopicos.getSelectionModel().getSelectedItem();
        if (idiomaSelecionado != null) {
            carregarTopicos(idiomaSelecionado.getId());
        } else {
            topicosObservableList.clear();
        }
    }

    @FXML
    private void adicionarTopico() {
        Idioma idiomaSelecionado = cmbIdiomasTopicos.getSelectionModel().getSelectedItem();
        if (idiomaSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Idioma Selecionado", "Selecione um idioma no ComboBox para adicionar um tópico.");
            return;
        }

        String nomeTopico = txtNomeTopico.getText().trim();
        String linkEstudo = txtLinkEstudo.getText().trim();

        if (nomeTopico.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campo Vazio", "O nome do tópico não pode ser vazio.");
            return;
        }

        if (!linkEstudo.isEmpty() && !isValidUrl(linkEstudo)) {
            mostrarAlerta(AlertType.ERROR, "Link Inválido", "Por favor, insira um link de estudo válido (ex: http://exemplo.com ou https://exemplo.com).");
            return;
        }

        int proximaOrdem = topicosObservableList.stream()
                .mapToInt(Topico::getOrdem)
                .max()
                .orElse(0) + 1;

        if (topicoService.adicionarTopico(nomeTopico, idiomaSelecionado.getId(), proximaOrdem, false, linkEstudo)) {
            txtNomeTopico.clear();
            txtLinkEstudo.clear();
            carregarTopicos(idiomaSelecionado.getId());
            carregarIdiomas();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Tópico '" + nomeTopico + "' adicionado com sucesso!");
        } else {
            mostrarAlerta(AlertType.ERROR, "Erro ao Adicionar", "Não foi possível adicionar o tópico '" + nomeTopico + "'. Talvez já exista ou ocorra um erro de DB.");
        }
    }

    @FXML
    private void atualizarTopico() {
        Topico topicoSelecionado = listViewTopicos.getSelectionModel().getSelectedItem();
        if (topicoSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Tópico Selecionado", "Selecione um tópico na lista para atualizar.");
            return;
        }

        String novoNome = txtNomeTopico.getText().trim();
        String novoLinkEstudo = txtLinkEstudo.getText().trim();

        if (novoNome.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campo Vazio", "O novo nome do tópico não pode ser vazio.");
            return;
        }

        if (!novoLinkEstudo.isEmpty() && !isValidUrl(novoLinkEstudo)) {
            mostrarAlerta(AlertType.ERROR, "Link Inválido", "Por favor, insira um link de estudo válido (ex: http://exemplo.com ou https://exemplo.com).");
            return;
        }

        boolean sucesso = topicoService.atualizarTopico(
                topicoSelecionado.getId(),
                novoNome,
                topicoSelecionado.getOrdem(),
                topicoSelecionado.isEstudado(),
                novoLinkEstudo
        );

        if (sucesso) {
            topicoSelecionado.setNome(novoNome);
            topicoSelecionado.setLinkEstudo(novoLinkEstudo);
            listViewTopicos.refresh();
            carregarIdiomas();
            txtNomeTopico.clear();
            txtLinkEstudo.clear();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Tópico atualizado para '" + novoNome + "'!");
        } else {
            mostrarAlerta(AlertType.ERROR, "Erro ao Atualizar", "Não foi possível atualizar o tópico.");
        }
    }

    @FXML
    private void removerTopico() {
        Topico topicoSelecionado = listViewTopicos.getSelectionModel().getSelectedItem();
        if (topicoSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Tópico Selecionado", "Selecione um tópico na lista para remover.");
            return;
        }

        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar Remoção");
        confirmDialog.setHeaderText("Remover Tópico?");
        confirmDialog.setContentText("Tem certeza que deseja remover o tópico '" + topicoSelecionado.getNome() + "'?");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean sucesso = topicoService.removerTopico(topicoSelecionado.getId());

            if (sucesso) {
                carregarIdiomas();
                topicosObservableList.clear();
                txtNomeTopico.clear();
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Tópico '" + topicoSelecionado.getNome() + "' removido com sucesso!");
            } else {
                mostrarAlerta(AlertType.ERROR, "Erro ao Remover", "Não foi possível remover o tópico.");
            }
        }
    }

    @FXML
    private void moverTopicoParaCima() {
        int selectedIndex = listViewTopicos.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            Topico topicoMovido = topicosObservableList.get(selectedIndex);
            Topico topicoTrocado = topicosObservableList.get(selectedIndex - 1);

            int ordemOriginalMovido = topicoMovido.getOrdem();
            int ordemOriginalTrocado = topicoTrocado.getOrdem();

            topicoMovido.setOrdem(ordemOriginalTrocado);
            topicoTrocado.setOrdem(ordemOriginalMovido);

            topicoService.atualizarTopico(topicoMovido.getId(), topicoMovido.getNome(), topicoMovido.getOrdem(), topicoMovido.isEstudado(), topicoMovido.getLinkEstudo());
            topicoService.atualizarTopico(topicoTrocado.getId(), topicoTrocado.getNome(), topicoTrocado.getOrdem(), topicoTrocado.isEstudado(), topicoTrocado.getLinkEstudo());

            Collections.swap(topicosObservableList, selectedIndex, selectedIndex - 1);
            listViewTopicos.getSelectionModel().select(selectedIndex - 1);
            listViewTopicos.refresh();
        } else if (selectedIndex == 0 && !topicosObservableList.isEmpty()) {
            mostrarAlerta(AlertType.INFORMATION, "Ordem", "Este tópico já está no topo da lista.");
        } else {
            mostrarAlerta(AlertType.WARNING, "Nenhum Tópico Selecionado", "Selecione um tópico para mover.");
        }
    }

    @FXML
    private void moverTopicoParaBaixo() {
        int selectedIndex = listViewTopicos.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < topicosObservableList.size() - 1) {
            Topico topicoMovido = topicosObservableList.get(selectedIndex);
            Topico topicoTrocado = topicosObservableList.get(selectedIndex + 1);

            int ordemOriginalMovido = topicoMovido.getOrdem();
            int ordemOriginalTrocado = topicoTrocado.getOrdem();

            topicoMovido.setOrdem(ordemOriginalTrocado);
            topicoTrocado.setOrdem(ordemOriginalMovido);

            topicoService.atualizarTopico(topicoMovido.getId(), topicoMovido.getNome(), topicoMovido.getOrdem(), topicoMovido.isEstudado(), topicoMovido.getLinkEstudo());
            topicoService.atualizarTopico(topicoTrocado.getId(), topicoTrocado.getNome(), topicoTrocado.getOrdem(), topicoTrocado.isEstudado(), topicoTrocado.getLinkEstudo());

            Collections.swap(topicosObservableList, selectedIndex, selectedIndex + 1);
            listViewTopicos.getSelectionModel().select(selectedIndex + 1);
            listViewTopicos.refresh();
        } else if (selectedIndex == topicosObservableList.size() - 1 && !topicosObservableList.isEmpty()) {
            mostrarAlerta(AlertType.INFORMATION, "Ordem", "Este tópico já está no final da lista.");
        } else {
            mostrarAlerta(AlertType.WARNING, "Nenhum Tópico Selecionado", "Selecione um tópico para mover.");
        }
    }

    private void carregarIdiomas() {
        idiomasObservableList.setAll(idiomaService.listarIdiomas());
        Idioma currentSelectedInList = listViewIdiomas.getSelectionModel().getSelectedItem();
        if (currentSelectedInList != null) {
            Optional<Idioma> reloadedIdioma = idiomasObservableList.stream()
                    .filter(i -> i.getId() == currentSelectedInList.getId())
                    .findFirst();
            reloadedIdioma.ifPresent(idioma -> listViewIdiomas.getSelectionModel().select(idioma));
        }

        Idioma currentSelectedInCmb = cmbIdiomasTopicos.getSelectionModel().getSelectedItem();
        if (currentSelectedInCmb != null) {
            Optional<Idioma> reloadedIdioma = idiomasObservableList.stream()
                    .filter(i -> i.getId() == currentSelectedInCmb.getId())
                    .findFirst();
            reloadedIdioma.ifPresent(idioma -> cmbIdiomasTopicos.getSelectionModel().select(idioma));
        }
    }

    private void carregarTopicos(int idiomaId) {
        topicosObservableList.setAll(topicoService.listarTopicosPorIdioma(idiomaId));
        topicosObservableList.sort(Comparator.comparingInt(Topico::getOrdem));
    }

    private void mostrarAlerta(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidUrl(String urlString) {
        try {
            URI uri = new URI(urlString);
            return uri.getScheme() != null && (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private void abrirLinkNoNavegador(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                mostrarAlerta(AlertType.ERROR, "Erro ao Abrir Link", "Não foi possível abrir o link: " + url + "\nErro: " + e.getMessage());
            }
        } else {
            mostrarAlerta(AlertType.ERROR, "Navegador Não Suportado", "Seu sistema não suporta a abertura de links automaticamente.");
        }
    }
}