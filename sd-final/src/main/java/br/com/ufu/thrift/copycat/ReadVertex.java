package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.graph.Vertex;
import io.atomix.copycat.Query;

public class ReadVertex implements Query<Vertex> {
	public int nome;

	public ReadVertex(int nome) {
		this.nome = nome;
	}
}