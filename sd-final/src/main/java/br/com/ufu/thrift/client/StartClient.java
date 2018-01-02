package br.com.ufu.thrift.client;

import java.util.List;
import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import br.com.ufu.thrift.graph.Edge;
import br.com.ufu.thrift.graph.Handler;
import br.com.ufu.thrift.graph.NullException;
import br.com.ufu.thrift.graph.Vertex;
import br.com.ufu.thrift.thread.ThreadSimulation;

public class StartClient {

    public static final int MAX = 3; // Número máximo de tentativas de conexão antes de dar erro de conexão

    public static void main(String[] args) {

        try {
            Reader reader = new Reader();
            Printer printer = new Printer();
            int name, nameB, collor;
            String description;
            float weight;
            boolean direction;
            int option;

            //Menu principal
            while (true) {
                try {
                    printer.printLn(
                            "\nOperação:\n\n"
                            + " 1) CRIAR UM VÉRTICE\n"
                            + " 2) LER UM Vértice\n"
                            + " 3) ATUALIZAR UM VÉRTICE\n"
                            + " 4) APAGAR UM VÉRTICE\n"
                            + " 5) CRIAR UMA ARESTA\n"
                            + " 6) LER UMA ARESTA\n"
                            + " 7) ATUALIZAR UMA ARESTA\n"
                            + " 8) APAGAR UMA ARESTA\n"
                            + " 9) LISTAR TODOS OS VÉRTICES\n"
                            + "10) LISTAR TODAS AS ARESTAS\n"
                            + "11) LISTAR AS ARESTAS DE UM VÉRTICE\n"
                            + "12) LISTAR OS VÉRTICES VIZINHOS DE UM VÉRTICE\n"
                            + "13) LISTAR O MENOR CAMINHO DE UM NÓ ATÉ OUTRO\n"
                            + "14) TESTAR CONCORRÊNCIA COM THREADS\n"
                            + "15) TESTAR BLOQUEIO DE VÉRTICE\n"
                            + "16) TESTAR DESBLOQUEAR VÉRTICE\n"
                            + "17) TESTAR DISTRIBUIÇÃO DE VÉRTICES\n"
                            + "18) FECHAR CLIENTE");
                    option = reader.readOption(1, 18);

                    switch (option) {
                        case 1: {//1) CREATE - Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();
                            printer.print("Cor: ");
                            collor = reader.readInteger();
                            printer.print("Descrição: ");
                            description = reader.readText();
                            printer.print("Peso: ");
                            weight = reader.readReal();

                            Vertex v = new Vertex(name, collor, description, weight);
                            if (connectClient(args).createVertex(v)) {
                                printer.printLn("\nO vértice '" + name + "' foi criado com sucesso.");
                            } else {
                                printer.printLn("\nO vértice '" + name + "' não foi criado.");
                            }
                            break;
                        }
                        case 2: {//2) READ   - Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();

                            try {
                                Vertex v = connectClient(args).readVertex(name);
                                printer.printLn(v);
                            } catch (NullException ex) {
                                printer.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 3: {//3) UPDATE - Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();
                            printer.print("Cor: ");
                            collor = reader.readInteger();
                            printer.print("Descrição: ");
                            description = reader.readText();
                            printer.print("Peso: ");
                            weight = reader.readReal();

                            Vertex v = new Vertex(name, collor, description, weight);
                            if (connectClient(args).updateVertex(v)) {
                                printer.printLn("\nO vértice '" + name + "' foi atualizado com sucesso.");
                            } else {
                                printer.printLn("\nO vértice '" + name + "' não existe.");
                            }
                            break;
                        }
                        case 4: {//4) DELETE - Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();

                            if (connectClient(args).deleteVertex(name)) {
                                printer.printLn("\nO vértice '" + name + "' foi excluído com sucesso.");
                            } else {
                                printer.printLn("\nO vértice '" + name + "' não existe.");
                            }
                            break;
                        }
                        case 5: {//5) CREATE - Edge
                            printer.print("Nome (Vértice A): ");
                            name = reader.readInteger();
                            printer.print("Nome (Vértice B): ");
                            nameB = reader.readInteger();
                            printer.print("Peso: ");
                            weight = reader.readReal();
                            printer.print("Direcionado: ");
                            direction = reader.lerSimNao();
                            printer.print("Descrição: ");
                            description = reader.readText();

                            Edge a = new Edge(name, nameB, weight, direction, description);
                            if (connectClient(args).createEdge(a)) {
                                printer.printLn("\nA aresta '" + name + "," + nameB + "' foi criada com sucesso.");
                            } else {
                                printer.printLn("\nA aresta '" + name + "," + nameB + "' não foi criada.");
                            }

                            break;
                        }
                        case 6: {//6) READ   -  Edge
                            printer.print("Nome (Vértice A): ");
                            name = reader.readInteger();
                            printer.print("Nome (Vértice B): ");
                            nameB = reader.readInteger();

                            try {
                                Edge a = connectClient(args).readEdge(name, nameB);
                                printer.printLn(a);
                            } catch (NullException ex) {
                                printer.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 7: {//7) UPDATE - Edge
                            printer.print("Nome (Vértice A): ");
                            name = reader.readInteger();
                            printer.print("Nome (Vértice B): ");
                            nameB = reader.readInteger();
                            printer.print("Peso: ");
                            weight = reader.readReal();
                            printer.print("Descrição: ");
                            description = reader.readText();

                            try {
                                Edge a = connectClient(args).readEdge(name, nameB);
                                a.setDesc(description);
                                a.setPeso(weight);

                                if (connectClient(args).updateEdge(a)) {
                                    printer.printLn("\nA aresta '" + name + "," + nameB + "' foi atualizada com sucesso.");
                                } else {
                                    printer.printLn("\nA aresta '" + name + "," + nameB + "'não pode ser alterada.");
                                }
                            } catch (NullException e) {
                                printer.printLn("\nA aresta '" + name + "," + nameB + "' não existe.");
                            }
                            break;
                        }
                        case 8: {//8) DELETE - Edge
                            printer.print("Nome (Vértice A): ");
                            name = reader.readInteger();
                            printer.print("Nome (Vértice B): ");
                            nameB = reader.readInteger();

                            if (connectClient(args).deleteEdge(name, nameB)) {
                                printer.printLn("\nA aresta '" + name + "," + nameB + "' foi excluída com sucesso.");
                            } else {
                                printer.printLn("\nA aresta '" + name + "," + nameB + "' não existe.");
                            }
                            break;
                        }
                        case 9: {//9) LIST   - Todos os Vértices
                            List<Vertex> lista = connectClient(args).listVertexsOfGraph();
                            if (lista.isEmpty()) {
                                printer.printLn("\nNão há vértices.");
                            } else {
                                printer.printLn(lista);
                            }
                            break;
                        }
                        case 10: {//10) LIST   - Todas as Edges
                            List<Edge> lista = connectClient(args).listEdgesOfGraph();
                            if (lista.isEmpty()) {
                                printer.printLn("\nNão há arestas.");
                            } else {
                                printer.printLn(lista);
                            }
                            break;
                        }
                        case 11: {//11) LIST   - Edges de um Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();

                            try {
                                List<Edge> lista = connectClient(args).listEdgesOfVertex(name);
                                if (lista.isEmpty()) {
                                    printer.printLn("\nNão há arestas.");
                                } else {
                                    printer.printLn(lista);
                                }
                            } catch (NullException ex) {
                                printer.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 12: {//12) LIST   - Vértices Vizinhos de um Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();

                            try {
                                List<Vertex> lista = connectClient(args).listNeighborhoodOfVertex(name);
                                if (lista.isEmpty()) {
                                    printer.printLn("\nNão há vizinhos.");
                                } else {
                                    printer.printLn(lista);
                                }
                            } catch (NullException ex) {
                                printer.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 13: {//13) LIST   - Menor Caminho de A até B
                            printer.print("Nome (Vértice A): ");
                            name = reader.readInteger();
                            printer.print("Nome (Vértice B): ");
                            nameB = reader.readInteger();

                            try {
                                List<Vertex> lista = connectClient(args).listSmallerPath(name, nameB);
                                if (lista.isEmpty()) {
                                    printer.printLn("\nNão há um caminho.");
                                } else {
                                    weight = 0;
                                    for (int j = 0; j < lista.size() - 1; j++) {
                                        weight += connectClient(args).readEdge(lista.get(j).getNome(), lista.get(j + 1).getNome()).getPeso();
                                    }
                                    printer.printLn(lista);
                                    printer.printLn("Peso do caminho: " + weight);
                                }
                            } catch (NullException ex) {
                                printer.printLn("\n" + ex.mensagem);
                            }

                            break;
                        }
                        case 14: {//DEMO   - Demonstração da Concorrência                           

                            ThreadSimulation t1 = new ThreadSimulation(args[0], Integer.parseInt(args[1]), "Um");
                            ThreadSimulation t2 = new ThreadSimulation(args[0], Integer.parseInt(args[1]), "Dois");
                            ThreadSimulation t3 = new ThreadSimulation(args[0], Integer.parseInt(args[1]), "Três");
                            ThreadSimulation t4 = new ThreadSimulation(args[0], Integer.parseInt(args[1]), "Quatro");
                            ThreadSimulation t5 = new ThreadSimulation(args[0], Integer.parseInt(args[1]), "Cinco");

                            try {
                                t1.start();
                                t2.start();
                                t3.start();
                                t4.start();
                                t5.start();
                                t1.join();
                                t2.join();
                                t3.join();
                                t4.join();
                                t5.join();
                            } catch (InterruptedException ex) {
                                System.out.println("Erro no Cliente: Falha ao rodar as threads");
                            }

                            break;
                        }
                        case 15: {//TESTE  - Bloquear Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();
                            if (connectClient(args).bloqueiaVertex(name)) {
                                printer.printLn("\nVértice " + name + " bloqueado.");
                            } else {
                                printer.printLn("\nO vértice " + name + " não existe.");
                            }
                            break;
                        }
                        case 16: {//TESTE  - Desbloquear Vértice
                            printer.print("Nome: ");
                            name = reader.readInteger();
                            connectClient(args).desbloqueiaVertex(name);
                            printer.printLn("\nComando para desbloquear o vértice " + name + " enviado.");
                            break;
                        }
                        case 17: {//17) TESTE  - Distribuição de Vértices
                            printer.printLn("Criando 64 vértices.");
                            for (int j = 0; j < 64; j++) {
                                connectClient(args).createVertex(new Vertex(j, j, "" + j, j));
                            }
                            break;
                        }
                        case 18: {//15) Sair                                                
                            printer.printLn("Saindo...");
                            reader.close();
                            System.exit(0);
                            break;
                        }
                    }
                } catch (TTransportException ex) {
                    if (ex.getMessage().equals("MAX_atingido")) {
                        System.out.println("Número máximo de tentativas de conexão atingido, operação cancelada.");
                    } else {
                        throw ex;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.out.println("Erro nos parâmetros da linha de comando, o sistema não será iniciado. Mensagem de erro: " + ex);
        } catch (TTransportException ex) {
            System.out.println("Erro ao acessar o servidor, o sistema será finalizado. Mensagem de erro: " + ex);
            ex.printStackTrace();
        } catch (TException ex) {
            System.out.println("Houve um erro para a operação, o sistema será finalizado. Mensagem de erro: " + ex);
            ex.printStackTrace();
        }
    }

    /**
     *  Método que retorna uma conexão ativa para o cliente
     * @param servers
     * @return
     * @throws ArrayIndexOutOfBoundsException
     * @throws NumberFormatException
     * @throws TTransportException
     * @throws TException
     */
    public static Handler.Client connectClient(String[] servers) throws ArrayIndexOutOfBoundsException, NumberFormatException, TTransportException, TException {

        int counter = 0;
        TTransport transport;
        TProtocol protocol;
        Handler.Client client = null;

        while (client == null && counter < MAX) {
            for (int i = 0; i < servers.length; i += 2) {
                try {
                    transport = new TSocket(servers[i], Integer.parseInt(servers[i + 1]));
                    transport.open();
                    protocol = new TBinaryProtocol(transport);
                    client = new Handler.Client(protocol);
                    break;
                } catch (TTransportException ex) {
                }
            }
            counter++;
        }
        if (client == null) {
            throw new TTransportException("MAX_atingido");
        } else {
            return client;
        }
    }
}

//Classe com métodos para impressão de objetos
class Printer {

    public void printLn(String texto) {
        System.out.println(texto);
    }

    public void print(String texto) {
        System.out.print(texto);
    }

    public void print(Vertex v) {
        System.out.print("\n" + v.toString().replace(", bloqueado:true", "").replace(", bloqueado:false", ""));
    }

    public void print(Edge a) {
        System.out.print("\n" + a.toString());
    }

    public void printLn(Vertex v) {
        print(v);
        System.out.println("");
    }

    public void printLn(Edge a) {
        print(a);
        System.out.println("");
    }

    public void printLn(List<?> lista) {
        if (lista.size() > 0) {
            if (lista.get(0) instanceof Vertex) {
                for (Vertex v : (List<Vertex>) lista) {
                    print(v);
                }
                printLn("");
            } else if (lista.get(0) instanceof Edge) {
                for (Edge a : (List<Edge>) lista) {
                    print(a);
                }
                printLn("");
            } else {
                System.out.println("\n" + lista.toString());
            }
        }
    }
}

//Classe com métodos para leitura facilidada de entradas de usuário
class Reader {

    private final Scanner read = new Scanner(System.in);

    public int readOption(int min, int max) {
        System.out.print("\nOpção: ");
        int option;

        try {
            option = readInteger();
            if (option < min || option > max) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.print("Opção inválida, digite um número de " + min + " a " + max + ".");
            option = readOption(min, max);
        }

        return option;
    }

    public int readInteger() {
        int option;

        try {
            option = read.nextInt();
            read.nextLine();
        } catch (RuntimeException e) {
            System.out.print("Digite um número: ");
            read.nextLine();
            option = readInteger();
        }

        return option;
    }

    public float readReal() {
        float opcao;

        try {
            opcao = read.nextFloat();
            read.nextLine();
        } catch (RuntimeException e) {
            System.out.print("Digite um número real: ");
            read.nextLine();
            opcao = readReal();
        }
        return opcao;
    }

    public String readText() {
        return read.nextLine();
    }

    public boolean lerSimNao() {
        boolean opcao;

        try {
            opcao = read.nextBoolean();
            read.nextLine();
        } catch (RuntimeException e) {
            System.out.print("Digite 'true' ou 'false': ");
            read.nextLine();
            opcao = lerSimNao();
        }
        return opcao;
    }

    public void close() {
        read.close();
    }
}
