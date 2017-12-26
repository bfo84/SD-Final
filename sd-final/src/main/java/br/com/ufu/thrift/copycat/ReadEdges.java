package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.graph.Edge;
import io.atomix.copycat.Query;

public class ReadEdges implements Query<Edge> {
	public int nome1;
	public int nome2;

	public ReadEdges(int nome1, int nome2) {
		this.nome1 = nome1;
		this.nome2 = nome2;
	}
}