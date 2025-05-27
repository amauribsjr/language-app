package com.appidiomas.language;

import com.appidiomas.language.db.DatabaseManager;
import com.appidiomas.language.db.IdiomaService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final IdiomaService idiomaService = new IdiomaService();

    public static void main(String[] args) {
        DatabaseManager.createTables();

        int opcao;
        do {
            exibirMenu();
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarNovoIdioma();
                    break;
                case 2:
                    listarTodosIdiomas();
                    break;
                case 0:
                    System.out.println("Saindo do aplicativo.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("--- Gerenciador de Idiomas ---");
        System.out.println("1. Adicionar novo idioma");
        System.out.println("2. Listar idiomas cadastrados");
        System.out.println("0. Sair");
    }

    private static void adicionarNovoIdioma() {
        System.out.print("Digite o nome do idioma a adicionar: ");
        String nomeIdioma = scanner.nextLine();
        idiomaService.adicionarIdioma(nomeIdioma);
    }

    private static void listarTodosIdiomas() {
        System.out.println("\n--- Idiomas Cadastrados ---");
        List<String> idiomas = idiomaService.listarIdiomas();
        if (idiomas.isEmpty()) {
            System.out.println("Nenhum idioma cadastrado ainda.");
        } else {
            for (String idioma : idiomas) {
                System.out.println("- " + idioma);
            }
        }
    }
}