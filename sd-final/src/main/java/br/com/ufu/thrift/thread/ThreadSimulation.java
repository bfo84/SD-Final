package br.com.ufu.thrift.thread;

import java.util.ArrayList;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import br.com.ufu.thrift.graph.Edge;
import br.com.ufu.thrift.graph.Handler;
import br.com.ufu.thrift.graph.Vertex;

public class ThreadSimulation extends Thread {

    TTransport transport;
    TProtocol protocol;
    String ip;
    int porta;
    String nome;
    Handler.Client client;

    public ThreadSimulation(String ip, int porta, String nome) throws TTransportException, TException {
        this.transport = new TSocket(ip, porta);
        this.transport.open();
        this.protocol = new TBinaryProtocol(this.transport);
        this.ip = ip;
        this.porta = porta;
        this.client = new Handler.Client(protocol);
        this.nome = nome;
    }

    public void TestarVertices() throws TException {
        for (int i = 1; i <= 50; i++) {
            Vertex vet = new Vertex(i, i, "v" + i, i);
            System.out.println("Thread " + this.nome + ": Tentando adicionar vértice " + vet.getNome());
            if (client.createVertex(vet)) {
                System.out.println("Thread " + this.nome + ": Vertice " + vet.getNome() + " adicionado!");
            } else {
                System.out.println("Thread " + this.nome + ": Vertice " + vet.getNome() + " não pode ser adicionado!");
            }
        }
    }

    public void TestarEdges() throws TException {
        Edge ar[] = new Edge[10];
        ar[0] = new Edge(0, 1, 2, false, "0,1");
        ar[1] = new Edge(1, 2, 3, true, "1,2");
        ar[2] = new Edge(2, 3, 4, true, "2,3");
        ar[3] = new Edge(1, 0, 3, true, "1,0");
        ar[4] = new Edge(4, 5, 2, false, "4,5");
        ar[5] = new Edge(6, 8, 7, true, "6,8");
        ar[6] = new Edge(7, 3, 6, false, "7,3");
        ar[7] = new Edge(8, 9, 7, true, "8,9");
        ar[8] = new Edge(3, 6, 2, false, "3,6");
        ar[9] = new Edge(9, 7, 1, false, "9,7");

        for (int i = 0; i < 7; i++) {
            System.out.println("Thread " + this.nome + ": Tentando adicionar aresta " + ar[i].getDesc());
            if (client.createEdge(ar[i])) {
                System.out.println("Thread " + this.nome + ": Edge " + ar[i].getDesc() + " adicionada");
            } else {
                System.out.println("Thread " + this.nome + ": Edge " + ar[i].getDesc() + " não pode ser adicionada!");
            }
        }
    }

    public void TestarExclusaoEdges() throws TException {
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                System.out.println("Thread " + this.nome + ": Tentando remover aresta " + i + "," + j + " (caso existir)");
                if (client.deleteEdge(i, j)) {
                    System.out.println("Thread " + this.nome + ": Edge " + i + "," + j + " removida com sucesso!");
                } else {
                    System.out.println("Thread " + this.nome + ": Edge " + i + "," + j + " não pode ser removida!");
                }
            }
        }

    }

    public void TestarExclusaoVertices() throws TException {
        for (int i = 1; i < 60; i++) {
            System.out.println("Thread " + this.nome + ": Tentando remover vértice " + i);
            if (client.deleteVertex(i)) {
                System.out.println("Thread " + this.nome + ": Vértice " + i + " removido com sucesso!");
            } else {
                System.out.println("Thread " + this.nome + ": Vértice " + i + " não pode ser removido!");
            }
        }
    }

    public void TestarBuscaEdgesExclusao() throws TException {
        System.out.println("Thread " + this.nome + ": Tentando listar arestas");
        ArrayList<Edge> ars = (ArrayList<Edge>) client.listEdgesOfGraph();
        System.out.println("Thread " + this.nome + ": Recuperou todas as arestas!");

        for (Edge a : ars) {
            System.out.println("Thread " + this.nome + ": " + a.toString());
            System.out.println("Thread " + this.nome + ": Tentando remover aresta 1,2");
            if (!client.deleteEdge(1, 2)) {
                System.out.println("Thread " + this.nome + ": Edge 1,2 não pode ser removida");
            } else {
                System.out.println("Thread " + this.nome + ": Edge 1,2 removida com sucesso!");
            }

            System.out.println("Thread " + this.nome + ": Tentando remover aresta 2,3");
            if (!client.deleteEdge(2, 3)) {
                System.out.println("Thread " + this.nome + ": Edge 2,3 não pode ser removida");
            } else {
                System.out.println("Thread " + this.nome + ": Edge 2,3 removida com sucesso!");
            }
        }
    }

    @Override
    public void run() {
        try {
            this.TestarVertices();
            System.out.println("####################### THREAD " + this.nome + " TERMINOU DE TESTAR VÉRTICES #######################");
        } catch (TException ex) {
            System.out.println("Thread " + this.nome + ": Erro desconhecido ao adicionar vértices");
            System.out.println(ex.getCause() + " -> " + ex.getMessage());
        }

        try {
            this.TestarEdges();
            System.out.println("####################### THREAD " + this.nome + " TERMINOU DE TESTAR ARESTAS #######################");
        } catch (TException ex) {
            System.out.println("Thread " + this.nome + ": Erro desconhecido ao adicionar arestas");
            System.out.println(ex.getCause() + " -> " + ex.getMessage());
        }

        try {
            this.TestarBuscaEdgesExclusao();
            System.out.println("####################### THREAD " + this.nome + " TERMINOU DE TESTAR BUSCA DE ARESTAS #######################");
        } catch (TException ex) {
            System.out.println("Thread " + this.nome + ": Erro desconhecido ao tentar remover arestas enquanto é feita a listagem");
            System.out.println(ex.getCause() + " -> " + ex.getMessage());
        }

        try {
            this.TestarExclusaoEdges();
            System.out.println("####################### THREAD " + this.nome + " TERMINOU DE TESTAR EXCLUSÃO DE ARESTAS #######################");
        } catch (TException ex) {
            System.out.println("Thread " + this.nome + ": Erro desconhecido ao excluir arestas");
            System.out.println(ex.getCause() + " -> " + ex.getMessage());
        }

        try {
            this.TestarExclusaoVertices();
            System.out.println("####################### THREAD " + this.nome + " TERMINOU DE TESTAR EXCLUSÃO DE VÉRTICES #######################");
        } catch (TException ex) {
            System.out.println("Thread " + this.nome + ": Erro desconhecido ao excluir vértices");
            System.out.println(ex.getCause() + " -> " + ex.getMessage());
        }
    }
}
