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
            //Variáveis I/O
            Reader l = new Reader();
            Printer i = new Printer();
            int name, nameB, collor;
            String description;
            float weight;
            boolean direction;
            int option;

            //Menu principal
            while (true) {
                try {
                    i.printLn(
                            "\nOperação:\n\n"
                            + " 1) CREATE - Vértice\n"
                            + " 2) READ   - Vértice\n"
                            + " 3) UPDATE - Vértice\n"
                            + " 4) DELETE - Vértice\n"
                            + " 5) CREATE - Edge\n"
                            + " 6) READ   - Edge\n"
                            + " 7) UPDATE - Edge\n"
                            + " 8) DELETE - Edge\n"
                            + " 9) LIST   - Todos os Vértices\n"
                            + "10) LIST   - Todas as Edges\n"
                            + "11) LIST   - Edges de um Vértice\n"
                            + "12) LIST   - Vértices Vizinhos de um Vértice\n"
                            + "13) LIST   - Menor Caminho de A até B\n"
                            + "14) TESTE  - Concorrência com Threads\n"
                            + "15) TESTE  - Bloquear Vértice\n"
                            + "16) TESTE  - Desbloquear Vértice\n"
                            + "17) TESTE  - Distribuição de Vértices\n"
                            + "18) Fechar Cliente");
                    option = l.lerOpcao(1, 18);

                    switch (option) {
                        case 1: {//1) CREATE - Vértice
                            i.print("Nome: ");
                            name = l.readInteger();
                            i.print("Cor: ");
                            collor = l.readInteger();
                            i.print("Descrição: ");
                            description = l.lerTexto();
                            i.print("Peso: ");
                            weight = l.readReal();

                            Vertex v = new Vertex(name, collor, description, weight);
                            if (conectar(args).createVertex(v)) {
                                i.printLn("\nO vértice '" + name + "' foi criado com sucesso.");
                            } else {
                                i.printLn("\nO vértice '" + name + "' não foi criado.");
                            }
                            break;
                        }
                        case 2: {//2) READ   - Vértice
                            i.print("Nome: ");
                            name = l.readInteger();

                            try {
                                Vertex v = conectar(args).readVertex(name);
                                i.printLn(v);
                            } catch (NullException ex) {
                                i.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 3: {//3) UPDATE - Vértice
                            i.print("Nome: ");
                            name = l.readInteger();
                            i.print("Cor: ");
                            collor = l.readInteger();
                            i.print("Descrição: ");
                            description = l.lerTexto();
                            i.print("Peso: ");
                            weight = l.readReal();

                            Vertex v = new Vertex(name, collor, description, weight);
                            if (conectar(args).updateVertex(v)) {
                                i.printLn("\nO vértice '" + name + "' foi atualizado com sucesso.");
                            } else {
                                i.printLn("\nO vértice '" + name + "' não existe.");
                            }
                            break;
                        }
                        case 4: {//4) DELETE - Vértice
                            i.print("Nome: ");
                            name = l.readInteger();

                            if (conectar(args).deleteVertex(name)) {
                                i.printLn("\nO vértice '" + name + "' foi excluído com sucesso.");
                            } else {
                                i.printLn("\nO vértice '" + name + "' não existe.");
                            }
                            break;
                        }
                        case 5: {//5) CREATE - Edge
                            i.print("Nome (Vértice A): ");
                            name = l.readInteger();
                            i.print("Nome (Vértice B): ");
                            nameB = l.readInteger();
                            i.print("Peso: ");
                            weight = l.readReal();
                            i.print("Direcionado: ");
                            direction = l.lerSimNao();
                            i.print("Descrição: ");
                            description = l.lerTexto();

                            Edge a = new Edge(name, nameB, weight, direction, description);
                            if (conectar(args).createEdge(a)) {
                                i.printLn("\nA aresta '" + name + "," + nameB + "' foi criada com sucesso.");
                            } else {
                                i.printLn("\nA aresta '" + name + "," + nameB + "' não foi criada.");
                            }

                            break;
                        }
                        case 6: {//6) READ   -  Edge
                            i.print("Nome (Vértice A): ");
                            name = l.readInteger();
                            i.print("Nome (Vértice B): ");
                            nameB = l.readInteger();

                            try {
                                Edge a = conectar(args).readEdge(name, nameB);
                                i.printLn(a);
                            } catch (NullException ex) {
                                i.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 7: {//7) UPDATE - Edge
                            i.print("Nome (Vértice A): ");
                            name = l.readInteger();
                            i.print("Nome (Vértice B): ");
                            nameB = l.readInteger();
                            i.print("Peso: ");
                            weight = l.readReal();
                            i.print("Descrição: ");
                            description = l.lerTexto();

                            try {
                                Edge a = conectar(args).readEdge(name, nameB);
                                a.setDesc(description);
                                a.setPeso(weight);

                                if (conectar(args).updateEdge(a)) {
                                    i.printLn("\nA aresta '" + name + "," + nameB + "' foi atualizada com sucesso.");
                                } else {
                                    i.printLn("\nA aresta '" + name + "," + nameB + "'não pode ser alterada.");
                                }
                            } catch (NullException e) {
                                i.printLn("\nA aresta '" + name + "," + nameB + "' não existe.");
                            }
                            break;
                        }
                        case 8: {//8) DELETE - Edge
                            i.print("Nome (Vértice A): ");
                            name = l.readInteger();
                            i.print("Nome (Vértice B): ");
                            nameB = l.readInteger();

                            if (conectar(args).deleteEdge(name, nameB)) {
                                i.printLn("\nA aresta '" + name + "," + nameB + "' foi excluída com sucesso.");
                            } else {
                                i.printLn("\nA aresta '" + name + "," + nameB + "' não existe.");
                            }
                            break;
                        }
                        case 9: {//9) LIST   - Todos os Vértices
                            List<Vertex> lista = conectar(args).listVertexsOfGraph();
                            if (lista.isEmpty()) {
                                i.printLn("\nNão há vértices.");
                            } else {
                                i.printLn(lista);
                            }
                            break;
                        }
                        case 10: {//10) LIST   - Todas as Edges
                            List<Edge> lista = conectar(args).listEdgesOfGraph();
                            if (lista.isEmpty()) {
                                i.printLn("\nNão há arestas.");
                            } else {
                                i.printLn(lista);
                            }
                            break;
                        }
                        case 11: {//11) LIST   - Edges de um Vértice
                            i.print("Nome: ");
                            name = l.readInteger();

                            try {
                                List<Edge> lista = conectar(args).listEdgesOfVertex(name);
                                if (lista.isEmpty()) {
                                    i.printLn("\nNão há arestas.");
                                } else {
                                    i.printLn(lista);
                                }
                            } catch (NullException ex) {
                                i.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 12: {//12) LIST   - Vértices Vizinhos de um Vértice
                            i.print("Nome: ");
                            name = l.readInteger();

                            try {
                                List<Vertex> lista = conectar(args).listNeighborhoodOfVertex(name);
                                if (lista.isEmpty()) {
                                    i.printLn("\nNão há vizinhos.");
                                } else {
                                    i.printLn(lista);
                                }
                            } catch (NullException ex) {
                                i.printLn("\n" + ex.mensagem);
                            }
                            break;
                        }
                        case 13: {//13) LIST   - Menor Caminho de A até B
                            i.print("Nome (Vértice A): ");
                            name = l.readInteger();
                            i.print("Nome (Vértice B): ");
                            nameB = l.readInteger();

                            try {
                                List<Vertex> lista = conectar(args).listSmallerPath(name, nameB);
                                if (lista.isEmpty()) {
                                    i.printLn("\nNão há um caminho.");
                                } else {
                                    weight = 0;
                                    for (int j = 0; j < lista.size() - 1; j++) {
                                        weight += conectar(args).readEdge(lista.get(j).getNome(), lista.get(j + 1).getNome()).getPeso();
                                    }
                                    i.printLn(lista);
                                    i.printLn("Peso do caminho: " + weight);
                                }
                            } catch (NullException ex) {
                                i.printLn("\n" + ex.mensagem);
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
                            i.print("Nome: ");
                            name = l.readInteger();
                            if (conectar(args).bloqueiaVertex(name)) {
                                i.printLn("\nVértice " + name + " bloqueado.");
                            } else {
                                i.printLn("\nO vértice " + name + " não existe.");
                            }
                            break;
                        }
                        case 16: {//TESTE  - Desbloquear Vértice
                            i.print("Nome: ");
                            name = l.readInteger();
                            conectar(args).desbloqueiaVertex(name);
                            i.printLn("\nComando para desbloquear o vértice " + name + " enviado.");
                            break;
                        }
                        case 17: {//17) TESTE  - Distribuição de Vértices
                            i.printLn("Criando 64 vértices.");
                            for (int j = 0; j < 64; j++) {
                                conectar(args).createVertex(new Vertex(j, j, "" + j, j));
                            }
                            break;
                        }
                        case 18: {//15) Sair                                                
                            i.printLn("Saindo...");
                            l.close();
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

    // Este método retorna uma conexão ativa para o cliente
    public static Handler.Client conectar(String[] servers) throws ArrayIndexOutOfBoundsException, NumberFormatException, TTransportException, TException {

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

    public int lerOpcao(int min, int max) {
        System.out.print("\nOpção: ");
        int option;

        try {
            option = readInteger();
            if (option < min || option > max) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.print("Opção inválida, digite um número de " + min + " a " + max + ".");
            option = lerOpcao(min, max);
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

    public String lerTexto() {
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
