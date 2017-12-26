package br.com.ufu.thrift.server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import br.com.ufu.thrift.copycat.BlockVertex;
import br.com.ufu.thrift.copycat.CreateEdge;
import br.com.ufu.thrift.copycat.CreateVertex;
import br.com.ufu.thrift.copycat.DeleteEdge;
import br.com.ufu.thrift.copycat.DeleteVertex;
import br.com.ufu.thrift.copycat.DisblockVertex;
import br.com.ufu.thrift.copycat.ListEdgeofGraph;
import br.com.ufu.thrift.copycat.ListVertexofGraph;
import br.com.ufu.thrift.copycat.ReadEdges;
import br.com.ufu.thrift.copycat.ReadVertex;
import br.com.ufu.thrift.copycat.SetFingerTable;
import br.com.ufu.thrift.copycat.UpdateEdge;
import br.com.ufu.thrift.copycat.UpdateVertex;
import br.com.ufu.thrift.graph.Edge;
import br.com.ufu.thrift.graph.Graph;
import br.com.ufu.thrift.graph.Handler;
import br.com.ufu.thrift.graph.Id;
import br.com.ufu.thrift.graph.NullException;
import br.com.ufu.thrift.graph.Vertex;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;

public class Server extends StateMachine implements Handler.Iface {

    // Variáveis Comuns
    private final Graph g = new Graph(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());

    // Variáveis para funcionamento P2P
    private int maxNodes;                           // A quantidade máxima de nós é de 2^m e os IDs vão de 0 a 2^m -1
    private int id, behind, next;          			// O ID deste servidor e do servidor anterior a ele e posterior a ele
    private String[] servers;                       // Será a lista com todos servidores (IPs e Portas) passadas no parâmetro, temporário até montar a FT
    private Object[][] fingerTable;                 // Será a Finger Table, terá M nós indexados

    // Variáveis para funcionamento da replicação
    List<Address> members = new LinkedList<>();     // Os endereços dos membros do cluster ao qual este nó pertence
    CopycatClient cluster;                          // Uma conexão com o cluster

    // Construtor e métodos auto-executados
    public Server(String[] args) throws ArrayIndexOutOfBoundsException, NumberFormatException, TException {
        super();

        // Salvando M e validando o tamanho do args (minimo 11, maximo 2^(m+1) + 11)
        maxNodes = Integer.parseInt(args[10]);
        if (args.length < 11 || args.length > (Math.pow(2, maxNodes) - 1) * 6 + 11) {
            throw new ArrayIndexOutOfBoundsException();
        }

        // Criando o client do cluster
        members.add(new Address(args[0], Integer.parseInt(args[2])));
        members.add(new Address(args[4], Integer.parseInt(args[6])));
        members.add(new Address(args[7], Integer.parseInt(args[9])));
        CopycatClient.Builder builder = CopycatClient.builder()
                .withTransport(NettyTransport.builder()
                        .withThreads(4)
                        .build());
        cluster = builder.build();

        // Deixando uma lista com todos os servidores temporariamente no nó, será descartada após montar a Finger Table
        servers = new String[args.length - 11];
        System.arraycopy(args, 11, servers, 0, args.length - 11);
        boolean last = true; // Flag para que o último nó a se conectar comece a montagem da Finger Table

        // Escolhendo um ID aleatório ou copiando o do cluster
        try {
            id = connectNodeFromList(new String[]{args[4], args[5], args[7], args[8]}).getServerId();
        } catch (TTransportException ex) {
            if (Boolean.parseBoolean(args[3])) {
                id = (int) (Math.random() * Math.pow(2, maxNodes));
            } else {
                throw ex;
            }
        }

        // Verificando status e IDs dos demais servidores
        System.out.println("Tentando usar o ID: " + id);
        for (int i = 0; i < servers.length; i += 6) {
            try {
                Handler.Client node = connectNodeFromList(new String[]{servers[i], servers[i + 1], servers[i + 2], servers[i + 3], servers[i + 4], servers[i + 5]});
                System.out.println("Nó informado online e usando o ID " + node.getServerId() + ".");
                if (id == node.getServerId()) {
                    id = (int) (Math.random() * Math.pow(2, maxNodes));
                    i = -6;
                    System.out.println("ID indisponível. Tentando usar novo ID: " + id);
                }
            } catch (TTransportException ex) {
                System.out.println("Nó informado offline.");
                last = false;
            }
        }

        //O último servidor a ficar online avisa ao primeiro para montar a sua FingerTable
        if (last) {
            try {
                openConnectionWithOtherNode(args[4], args[5]);
                openConnectionWithOtherNode(args[7], args[8]);
            } catch (TTransportException ex) {
                last = false;
            }
            if (last) {
                connectNodeFromList(servers).setFingerTable();
            }
        }

    }

    /**
     *  Método necessário para um servidor saber o ID do outro e não repetir
     */
    @Override
    public int getServerId() throws TException {
        return id;
    }

    /**
     *  Método necessário pois a Finger Table só pode ser montada após todos ficarem online e terem seus IDs
     */
    @Override
    public synchronized void setFingerTable() throws TException {
        if (fingerTable == null) {
            if (cluster.state() != CopycatClient.State.CONNECTED) { // Como setFt é o primeiro método invocado, nele é aberta a sessão com o cluster
                cluster.connect(members).join();
            }
            cluster.submit(new SetFingerTable()).join();

            // Solicitando que o sucessor monte sua FT
            try {
                wait(5000); // Espera alguns segundos para montar a FT garantindo que os threads estejam recebendo o comando
            } catch (InterruptedException ex) {
            } finally {
                connectNextNode(next).setFingerTable();
            }
        }
    }

    /**
     * Método responsável pela criação da Finger Table
     * @param commit
     * @throws TException
     */
    public void setFingerTable(Commit<SetFingerTable> commit) throws TException {
        try {
            fingerTable = new Object[maxNodes][2]; //M linhas e 2 colunas (ID, Endereço)

            // Obtendo IDs de todos os servidores listados no parâmetro
            TreeMap<Integer, String[]> temp = new TreeMap<>();
            for (int i = 0; i < servers.length; i += 6) {
                try {
                    temp.put(openConnectionWithOtherNode(servers[i], servers[i + 1]).getServerId(), new String[]{servers[i], servers[i + 1], servers[i + 2], servers[i + 3], servers[i + 4], servers[i + 5]});
                } catch (TTransportException ex) {
                    // Se houver algum erro de conexão e der esta exceção, o servidor com erro ficará fora da montagem da FT
                }
            }

            // Descartar a lista com TODOS os servidores que ficou armazenada temporariamente
            servers = null;

            // Salvando o ID do servidor anterior
            if (temp.floorKey(id) != null) {
                behind = temp.floorKey(id);
            } else {
                behind = temp.lastKey();
            }

            // Montando tabela (FT)
            for (int i = 0; i < maxNodes; i++) {
                int ftpi = id + (int) Math.pow(2, i);// Não é usado 2 ^ i-1 porque i já começa em 0 aqui
                if (ftpi >= Math.pow(2, maxNodes)) {
                    ftpi -= Math.pow(2, maxNodes);
                }
                if (temp.ceilingKey(ftpi) != null) {
                    fingerTable[i][0] = temp.ceilingKey(ftpi);
                } else {
                    fingerTable[i][0] = temp.firstKey();
                }
                fingerTable[i][1] = temp.get((int) fingerTable[i][0]);
            }

            // Salvando o ID do servidor seguinte
            next = (int) fingerTable[0][0];

            // Impressão para conferência
            System.out.println("\nID: " + id + "\nFinger Table:");
            for (int i = 0; i < maxNodes; i++) {
                System.out.println("| " + (i + 1) + " | " + (int) fingerTable[i][0] + " |");
            }
        } finally {
            commit.close();
        }
    }

    /**
     *  Método com a função Hash para K
     * @param k
     * @return
     */
    private int hash(int k) {
        return k % (int) Math.pow(2, maxNodes);
    }

    /**
     *  Método para saber se o nó atual é sucessor de um vértice
     * @param k
     * @return
     */
    private boolean isSuccesorNode(int k) {
        k = hash(k);
        if (behind < id) {
            return behind < k && k <= id;
        } else {
            return !(id < k && k <= behind);
        }
    }

    /**
     *  Método para saber se o nó atual é sucessor de uma aresta
     * @param a
     * @param b
     * @return
     */
    private boolean isSuccessorEdge(int a, int b) {
        return isSuccesorNode(a + b); // Arestas invertidas ficarão no mesmo nó das arestas, Ex: 1,2 e 2,1 ficam no mesmo nó sempre
    }

    /**
     *  Método para abrir conexão com um outro nó, recebe IP e Porta
     * @param ip
     * @param port
     * @return
     * @throws TTransportException
     */
    private Handler.Client openConnectionWithOtherNode(String ip, String port) throws TTransportException {
        TTransport transport = new TSocket(ip, Integer.parseInt(port));
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        return new Handler.Client(protocol);
    }

    /**
     *  Método que tentará se conectar a qualquer nó que responda de uma lista
     * @param servers
     * @return
     * @throws TTransportException
     */
    private Handler.Client connectNodeFromList(String[] servers) throws TTransportException {
        for (int i = 0; i < servers.length; i += 2) {
            try {
                return openConnectionWithOtherNode(servers[i], servers[i + 1]);
            } catch (TTransportException ex) {
            }
        }
        throw new TTransportException();
    }

    /**
     *  Método para se conectar ao nó sucessor de uma aresta, usando somente a Finger Table
     * @param a
     * @param b
     * @return
     * @throws TTransportException
     */
    private Handler.Client connectSuccesorNodeFromEdge(int a, int b) throws TTransportException {
        return connectNextNode(a + b);
    }

    /**
     *  Método para se conectar ao nó sucessor de uma chave K, usando somente a Finger Table
     * @param k
     * @return
     * @throws TTransportException
     */
    private Handler.Client connectNextNode(int k) throws TTransportException {
        k = hash(k);
        String[] node = (String[]) fingerTable[maxNodes - 1][1]; // Por garantia já escolhe o último índice da FT. Não havendo nenhuma substituição, se conectará ao último nó da FT
        if ((id < next && next >= k) || (id > next && (id < k || k <= next))) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " ID " + id + " repassando requisição para ID " + (int) fingerTable[0][0]);
            node = (String[]) fingerTable[0][1]; // Troca para o primeiro nó da FT se atender à condição do IF
        } else {
            int i;
            for (i = 0; i < maxNodes - 1; i++) {
                //repassa para o nó i se id(i) <=k && k <= id(i+1) ou, se caso id(i+1) < id(i), o que significa que deu a volta no anel, então repassa se id(i) <= k && k <= id(i+1) + 2^m
                if (((int) fingerTable[i][0] <= k && k <= (int) fingerTable[i + 1][0]) || (((int) fingerTable[i + 1][0] < (int) fingerTable[i][0]) && (int) fingerTable[i][0] <= k && k <= ((int) Math.pow(2, maxNodes) + (int) fingerTable[i + 1][0]))) {
                    System.out.println(LocalDateTime.now().toLocalTime().toString() + " ID " + id + " repassando requisição para ID " + (int) fingerTable[i][0]);
                    node = (String[]) fingerTable[i][1]; // Troca para o índice i caso em algum momento atenda à condição do algoritimo de repasse da FT: succ <= k <= succ+1
                    break;
                }
            }
            if (i == maxNodes - 1) {
                System.out.println(LocalDateTime.now().toLocalTime().toString() + " ID " + id + " repassando requisição para ID " + (int) fingerTable[maxNodes - 1][0]);
            }
        }
        return connectNodeFromList(node); // Se conecta ao nó correto, ou qualquer cópia deste nó que esteja online
    }

    /**
     *  Método para bloquear vértice independente do servidor que ele esteja, substitui o Syncronized. True quando bloquear, False se não existir tal vértice
     */
    @Override
    public boolean blockVertex(int name) throws TException {
        if (isSuccesorNode(name)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando bloqueiaVertice(" + name + ")");
            return cluster.submit(new BlockVertex(name)).join();
        } else {
            return connectNextNode(name).blockVertex(name);
        }
    }

    public boolean blockVertex(Commit<BlockVertex> commit) throws TException {
        try {
            int name = commit.operation().nome;
            while (true) {
                synchronized (g.vertices.get(name)) {
                    if (!g.vertices.get(name).isBloqueado()) {
                        g.vertices.get(name).setBloqueado(true);
                        return true;
                    }
                }
            }
        } catch (NullPointerException ex) {
            return false;
        } finally {
            commit.close();
        }
    }

    /**
     *  Método para desbloquear um vértice que foi bloqueado em qualquer servidor
     */
    @Override
    public void disblockVertex(int name) throws TException {
        if (isSuccesorNode(name)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando desbloqueiaVertice(" + name + ")");
            cluster.submit(new DisblockVertex(name)).join();
        } else {
            connectNextNode(name).disblockVertex(name);
        }
    }

    public void disblockVertex(Commit<DisblockVertex> commit) throws TException {
        try {
            int name = commit.operation().nome;
            synchronized (g.vertices.get(name)) {
                g.vertices.get(name).setBloqueado(false);
            }
        } catch (NullPointerException ex) {
        } finally {
            commit.close();
        }
    }

    /**
     *  Métodos do Grafo
     *  Criar vértice
     */
    @Override
    public boolean createVertex(Vertex v) throws TException {
        if (v.getNome() < 0) {
            return false;
        }
        if (isSuccesorNode(v.getNome())) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando createVertice(" + v.getNome() + ")");
            return cluster.submit(new CreateVertex(v)).join();
        } else {
            return connectNextNode(v.getNome()).createVertex(v);
        }
    }

    public boolean createVertex(Commit<CreateVertex> commit) {
        try {
            Vertex v = commit.operation().v;
            return g.vertices.putIfAbsent(v.getNome(), v) == null;
        } finally {
            commit.close();
        }
    }

    /**
     *  Criar aresta
     */
    @Override
    public boolean createEdge(Edge edge) throws TException {
        if (edge.getVertice1() == edge.getVertice2()) {
            return false;
        }
        if (isSuccessorEdge(edge.getVertice1(), edge.getVertice2())) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando createAresta(" + edge.getVertice1() + "," + edge.getVertice2() + ")");
            int menor = edge.getVertice1() < edge.getVertice2() ? edge.getVertice1() : edge.getVertice2();
            int maior = edge.getVertice1() > edge.getVertice2() ? edge.getVertice1() : edge.getVertice2();
            try {
                if (blockVertex(menor) & blockVertex(maior)) { // Somente um '&' para obrigar que os dois testes sejam feitos. Com '&&' ele não testa o segundo se o primeiro for FALSE
                    return cluster.submit(new CreateEdge(edge)).join();
                } else {
                    return false;
                }
            } finally {
                disblockVertex(maior); // Se os dois testes não forem feitos acima, há risco de desbloquear indevidamente o vértice maior se ele não foi testado e bloqueado acima
                disblockVertex(menor);
            }
        } else {
            return connectSuccesorNodeFromEdge(edge.getVertice1(), edge.getVertice2()).createEdge(edge);
        }
    }

    public boolean createEdge(Commit<CreateEdge> commit) throws TException {
        try {
            Edge edge = commit.operation().a;
            Id id1 = new Id(edge.getVertice1(), edge.getVertice2());
            Id id2 = new Id(edge.getVertice2(), edge.getVertice1());
            try {
                synchronized (g.arestas.get(id2)) {
                    if (!g.arestas.get(id2).isDirec() || !edge.isDirec()) {
                        return false;
                    } else {
                        throw new NullPointerException();
                    }
                }
            } catch (NullPointerException ey) {
                return g.arestas.putIfAbsent(id1, edge) == null;
            }
        } finally {
            commit.close();
        }
    }

    /**
     *  Ler vértice
     */
    @Override
    public Vertex readVertex(int name) throws NullException, TException {
        if (isSuccesorNode(name)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando readVertice(" + name + ")");
            try {
                if (blockVertex(name)) {
                    return cluster.submit(new ReadVertex(name)).get();
                } else {
                    throw new NullException("O vértice '" + name + "' não existe");
                }
            } catch (InterruptedException | ExecutionException ex) {
                throw new NullException("Erro no servidor");
            } finally {
                disblockVertex(name);
            }
        } else {
            return connectNextNode(name).readVertex(name);
        }
    }

    public Vertex readVertex(Commit<ReadVertex> commit) throws NullException, TException {
        try {
            int name = commit.operation().nome;
            return g.vertices.get(name);
        } finally {
            commit.close();
        }
    }

    /**
     *  Ler aresta
     */
    @Override
    public Edge readEdge(int name1, int name2) throws NullException, TException {
        if (isSuccessorEdge(name1, name2)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando readAresta(" + name1 + "," + name2 + ")");
            try {
                return cluster.submit(new ReadEdges(name1, name2)).get();
            } catch (InterruptedException | ExecutionException ex) {
                throw new NullException("Erro no servidor");
            }

        } else {
            return connectSuccesorNodeFromEdge(name1, name2).readEdge(name1, name2);
        }
    }

    public Edge readEdge(Commit<ReadEdges> commit) throws NullException, TException {
        try {
            int name1 = commit.operation().nome1;
            int name2 = commit.operation().nome2;

            Id id1 = new Id(name1, name2);
            Id id2 = new Id(name2, name1);
            try {
                synchronized (g.arestas.get(id1)) {
                    return g.arestas.get(id1);
                }
            } catch (NullPointerException ex) {
                try {
                    synchronized (g.arestas.get(id2)) {
                        if (!g.arestas.get(id2).isDirec()) {
                            return g.arestas.get(id2);
                        } else {
                            throw new NullPointerException();
                        }
                    }
                } catch (NullPointerException ey) {
                    throw new NullException("A aresta '" + name1 + "," + name2 + "' não existe.");
                }
            }
        } finally {
            commit.close();
        }
    }

    /**
     *  Atualizar vértice
     */
    @Override
    public boolean updateVertex(Vertex vertex) throws TException {
        if (isSuccesorNode(vertex.getNome())) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando updateVertice(" + vertex.getNome() + ")");
            try {
                if (blockVertex(vertex.getNome())) {
                    return cluster.submit(new UpdateVertex(vertex)).join();
                } else {
                    return false;
                }
            } finally {
                disblockVertex(vertex.getNome());
            }
        } else {
            return connectNextNode(vertex.getNome()).updateVertex(vertex);
        }
    }

    public boolean updateVertex(Commit<UpdateVertex> commit) throws TException {
        try {
            Vertex v = commit.operation().v;
            v.setBloqueado(true);
            return g.vertices.replace(v.getNome(), g.vertices.get(v.getNome()), v);
        } finally {
            commit.close();
        }
    }

    /**
     *  Atualizar aresta
     */
    @Override
    public boolean updateEdge(Edge edge) throws TException {
        if (isSuccessorEdge(edge.getVertice1(), edge.getVertice2())) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando updateAresta(" + edge.getVertice1() + "," + edge.getVertice2() + ")");
            return cluster.submit(new UpdateEdge(edge)).join();
        } else {
            return connectSuccesorNodeFromEdge(edge.getVertice1(), edge.getVertice2()).updateEdge(edge);
        }
    }

    public boolean updateEdge(Commit<UpdateEdge> commit) throws TException {
        try {
            Edge a = commit.operation().a;
            Id id1 = new Id(a.getVertice1(), a.getVertice2());
            Id id2 = new Id(a.getVertice2(), a.getVertice1());
            try {
                synchronized (g.arestas.get(id1)) {
                    if (a.isDirec() == g.arestas.get(id1).isDirec()) { // Para consistência não permitimos alterar o direcionamento da aresta.
                        return g.arestas.replace(id1, g.arestas.get(id1), a);
                    } else {
                        return false;
                    }
                }
            } catch (NullPointerException ex) {
                try {
                    synchronized (g.arestas.get(id2)) {
                        if (a.isDirec() == g.arestas.get(id2).isDirec()) { // Para consistência não permitimos alterar o direcionamento da aresta.
                            return g.arestas.replace(id2, g.arestas.get(id2), a);
                        } else {
                            return false;
                        }
                    }
                } catch (NullPointerException ey) {
                    return false;
                }
            }
        } finally {
            commit.close();
        }
    }

    /**
     *  Excluir vértice
     */
    @Override
    public boolean deleteVertex(int name) throws TException {
        if (isSuccesorNode(name)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando deleteVertice(" + name + ")");
            try {
                if (blockVertex(name)) {
                    deleteEdgesOfVertex(name, behind); // Não aguardo o resultado, a operação VAI ser executada
                    return cluster.submit(new DeleteVertex(name)).join();
                } else {
                    return false;
                }
            } finally {
                disblockVertex(name);
            }
        } else {
            return connectNextNode(name).deleteVertex(name);
        }
    }

    public boolean deleteVertex(Commit<DeleteVertex> commit) throws TException {
        try {
            int name = commit.operation().nome;
            return g.vertices.remove(name) != null;
        } finally {
            commit.close();
        }
    }

    /**
     *  Excluir aresta
     */
    @Override
    public boolean deleteEdge(int name1, int name2) throws TException {
        if (isSuccessorEdge(name1, name2)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando deleteAresta(" + name1 + "," + name2 + ")");
            return cluster.submit(new DeleteEdge(name1, name2)).join();
        } else {
            return connectSuccesorNodeFromEdge(name1, name2).deleteEdge(name1, name2);
        }
    }

    public boolean deleteEdge(Commit<DeleteEdge> commit) throws TException {
        try {
            int name1 = commit.operation().nome;
            int name2 = commit.operation().nome2;
            Id id1 = new Id(name1, name2);
            Id id2 = new Id(name2, name1);
            try {
                synchronized (g.arestas.get(id1)) {
                    return g.arestas.remove(id1) != null;
                }
            } catch (NullPointerException ex) {
                try {
                    synchronized (g.arestas.get(id2)) {
                        if (!g.arestas.get(id2).isDirec()) {
                            return g.arestas.remove(id2) != null;
                        } else {
                            throw new NullPointerException();
                        }
                    }
                } catch (NullPointerException ey) {
                    return false;
                }
            }
        } finally {
            commit.close();
        }
    }

    /**
     *  Excluir aresta do vértice de forma distribuída (usado pelo método deleteVertice)
     */
    @Override
    public void deleteEdgesOfVertex(int name, int endId) throws TException {
        if (endId != id) {
            connectNextNode(next).deleteEdgesOfVertex(name, endId);
        }
        List<Edge> list;
        try {
            list = cluster.submit(new ListEdgeofGraph()).get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new TException("Erro no servidor.");
        }
        Iterator<Edge> it = list.iterator();
        Edge a;
        while (it.hasNext()) {
            a = it.next();
            if (a.getVertice1() == name || a.getVertice2() == name) {
                deleteEdge(a.getVertice1(), a.getVertice2());
            }
        }
    }

    /**
     *  Listar todos vértices 
     */
    @Override
    public List<Vertex> listVertexsOfGraph() throws TException {
        List<Vertex> lista = listVertexOfGraphOnRing(behind);
        lista.sort(new Comparator<Vertex>() { // Ordenação apenas
            @Override
            public int compare(final Vertex t, Vertex t1) {
                return t.getNome() - t1.getNome();
            }
        });
        return lista;
    }

    /**
     *  Listar os vértices de todos os nós do anel, parando ao dar a volta e chegar no nó que solicitou a lista
     */
    @Override
    public List<Vertex> listVertexOfGraphOnRing(int endId) throws TException {
        System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando listVerticesDoGrafo()");
        try {
            List<Vertex> lista = cluster.submit(new ListVertexofGraph()).get();
            if (endId != id) {
                lista.addAll(connectNextNode(next).listVertexOfGraphOnRing(endId));
            }
            return lista;
        } catch (InterruptedException | ExecutionException ex) {
            throw new NullException("Erro no servidor");
        }
    }

    public List<Vertex> listVertexOfGraph(Commit<ListVertexofGraph> commit) throws TException {
        try {
            synchronized (g.vertices) {
                return new ArrayList<>(g.vertices.values());
            }
        } finally {
            commit.close();
        }
    }

    /**
     *  Listar todas arestas
     */
    @Override
    public List<Edge> listEdgesOfGraph() throws TException {
        List<Edge> lista = listEdgesOfGraphOnRing(behind);
        lista.sort(new Comparator<Edge>() { // Ordenação apenas
            @Override
            public int compare(Edge t, Edge t1) {
                if (t.getVertice1() - t1.getVertice1() != 0) {
                    return t.getVertice1() - t1.getVertice1();
                } else {
                    return t.getVertice2() - t1.getVertice2();
                }
            }
        });
        return lista;
    }

    /**
     *  Listar as arestas de todos os nós do anel, parando ao dar a volta e chegar no nó que solicitou a lista
     */
    @Override
    public List<Edge> listEdgesOfGraphOnRing(int endId) throws TException {
        System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando listArestasDoGrafo()");
        try {
            List<Edge> lista = cluster.submit(new ListEdgeofGraph()).get();
            if (endId != id) {
                lista.addAll(connectNextNode(next).listEdgesOfGraphOnRing(endId));
            }
            return lista;
        } catch (InterruptedException | ExecutionException ex) {
            throw new NullException("Erro no servidor");
        }
    }

    public List<Edge> listEdgeOfGraph(Commit<ListEdgeofGraph> commit) throws TException {
        try {
            synchronized (g.arestas) {
                return new ArrayList<>(g.arestas.values());
            }
        } finally {
            commit.close();
        }
    }

    /**
     *  Listar as arestas de um determinado vértice
     */
    @Override
    public List<Edge> listEdgesOfVertex(int name) throws NullException, TException {
        readVertex(name);
        List<Edge> lista = listEdgesOfVertexOnRing(name, behind);
        lista.sort(new Comparator<Edge>() { // Ordenação apenas
            @Override
            public int compare(Edge t, Edge t1) {
                if (t.getVertice1() - t1.getVertice1() != 0) {
                    return t.getVertice1() - t1.getVertice1();
                } else {
                    return t.getVertice2() - t1.getVertice2();
                }
            }
        });
        return lista;
    }

    /**
     *  Listar as arestas de um determinado vértice, procurando-as em todos os servidores
     */
    @Override
    public List<Edge> listEdgesOfVertexOnRing(int name, int endId) throws NullException, TException {
        System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando listArestasDoVertice(" + name + ")");
        try {
            List<Edge> lista = cluster.submit(new ListEdgeofGraph()).get();
            Iterator<Edge> it = lista.iterator();
            Edge a;
            while (it.hasNext()) {
                a = it.next();
                if (a.getVertice1() != name && a.getVertice2() != name) {
                    it.remove();
                }
            }
            if (endId != id) {
                lista.addAll(connectNextNode(next).listEdgesOfVertexOnRing(name, endId));
            }
            return lista;
        } catch (InterruptedException | ExecutionException ex) {
            throw new NullException("Erro no servidor");
        }
    }

    /**
     *  Listar vizinhos do vértice
     *  Não é preciso fazer via RAFT pois não lê nenhum dado diretamente, reutiliza outros métodos
     */
    @Override
    public List<Vertex> listNeighborhoodOfVertex(int name) throws NullException, TException {
        if (isSuccesorNode(name)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando listVizinhosDoVertice(" + name + ")");
            List<Vertex> result = new ArrayList<>();
            for (Edge a : listEdgesOfVertex(name)) {
                if (a.isDirec() && a.getVertice2() == name) //significa que o vértice 1 da aresta não é vizinho do vértice 'name'
                {
                    continue;
                }
                Vertex vt1 = readVertex(a.getVertice1());
                Vertex vt2 = readVertex(a.getVertice2());
                if (a.getVertice1() == name && !result.contains(vt1)) {
                    result.add(vt2);
                } else if (a.getVertice2() == name && !result.contains(vt2)) {
                    result.add(vt1);
                }
            }
            return result;
        } else {
            return connectNextNode(name).listNeighborhoodOfVertex(name);
        }
    }

    /**
     *  Listar menor caminho de A até B
     */
    @Override
    public List<Vertex> listSmallerPath(int origin, int destination) throws NullException, TException {
        if (readVertex(origin).getNome() == readVertex(destination).getNome()) {
            List<Vertex> lista = new ArrayList<>();
            lista.add(readVertex(origin));
            return lista;
        } else {
            return smallestPathDistributed(origin, destination, new ArrayList<>());
        }
    }

    /**
     *  Listar menor caminho de A até B, busca por profundidade
     *  Não é preciso fazer via RAFT pois não lê nenhum dado diretamente, reutiliza outros métodos
     */
    @Override
    public List<Vertex> smallestPathDistributed(int origin, int destination, List<Vertex> visited) throws NullException, TException {
        if (isSuccesorNode(origin)) {
            System.out.println(LocalDateTime.now().toLocalTime().toString() + " Executando listMenorCaminho(" + origin + "," + destination + ")");
            List<Vertex> smallerPath = new ArrayList<>();
            List<Vertex> actualPath = new ArrayList<>();
            actualPath.addAll(visited);
            actualPath.add(readVertex(origin));
            for (Vertex v : listNeighborhoodOfVertex(origin)) {
                if (!visited.contains(v)) {
                    List<Vertex> path;
                    if (v.getNome() == destination) {
                        path = actualPath;
                        path.add(v);
                    } else {
                        path = smallestPathDistributed(v.getNome(), destination, actualPath);
                    }
                    try {
                        if (!path.isEmpty()) {
                            double pc = weight(path);
                            double pmc = weight(smallerPath);
                            if (pc == pmc && Math.random() > 0.5) { // Variação para que caminhos com mesmo peso apareçam como resultado aleatoriamente
                                pc = 0;
                            }
                            if (smallerPath.isEmpty() || pc < pmc) {
                                smallerPath = path;
                            }
                        }
                    } catch (NullException ne) {
                        //Se der esta exceção, significa que um vértice ou aresta foi excluído enquanto o caminho era calculado. Assim o caminho todo já é inválido
                    }
                }
            }
            return smallerPath;
        } else {
            return connectNextNode(origin).smallestPathDistributed(origin, destination, visited);
        }
    }

    /**
     *  Método auxiliar para calcular o peso do caminho
     * @param path
     * @return
     * @throws NullException
     * @throws TException
     */
    private double weight(List<Vertex> path) throws NullException, TException {
        double peso = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            peso += readEdge(path.get(i).getNome(), path.get(i + 1).getNome()).getPeso();
        }
        return peso;
    }
}
