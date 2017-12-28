namespace java Grafo

typedef i32 int

struct Vertex
{
	1: int nome,
	2: int cor,
	3: string desc,
	4: double peso,
	5: optional bool bloqueado = false
}

struct Edge
{
	1: int vertice1,
	2: int vertice2,
	3: double peso,
	4: bool direc,
	5: string desc
}

struct Id
{
	1: int nome1,
	2: int nome2
}

struct Grafo
{
	1: map<int,Vertex> vertices = {},
	2: map<Id,Edge> arestas = {}
}

exception NullException
{
	1: string mensagem
}

service Handler
{
	//Métodos comuns para uso pelo Cliente
	bool createVertex(1:Vertex v),
	bool createEdge(1:Edge a),
	Vertex readVertice(1:int nome) throws (1:NullException ne),
	Edge readEdge(1:int nome1, 2:int nome2) throws (1:NullException ne),
	bool updateVertex(1:Vertex v),
	bool updateEdge(1:Edge a),
	bool deleteVertex(1:int nome),
	bool deleteEdge(1:int nome1, 2:int nome2),
	list<Vertex> listVerticesDoGrafo(),
	list<Edge> listEdgesOfGraph(),
	list<Edge> listEdgesOfVertex(1:int nome) throws (1:NullException ne),
	list<Vertex> listVizinhosDoVertice(1:int nome) throws (1:NullException ne),
	list<Vertex> listMenorCaminho(1:int origin, 2:int destination) throws (1:NullException ne),
	//Métodos extras para uso pelos servidores apenas
	int getServerId(),
	oneway void setFingerTable(),
	bool blockVertex(1:int nome),
	oneway void disblockVertex(1:int nome),
	oneway void deleteEdgesOfVertex(1:int nome, 2:int endId),
	list<Vertex> listVertexOfGraphOnRing(1:int endId),
	list<Edge> listEdgesOfGraphOnRing(1:int endId),
	list<Edge> listEdgesOfvertexOnRing(1:int nome, 2:int endId) throws (1:NullException ne),
	list<Vertex> smallestPathDistributed(1:int origem, 2:int destino, list<Vertex> visitados) throws (1:NullException ne)
}
	