package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.graph.Vertex;
import io.atomix.copycat.Command;

public class UpdateVertex implements Command<Boolean> {
	public Vertex v;

	public UpdateVertex(Vertex v) {
		this.v = v;
	}
}