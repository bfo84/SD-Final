package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.graph.Vertex;
import io.atomix.copycat.Command;

public class CreateVertex implements Command<Boolean> {
	public Vertex v;

	public CreateVertex(Vertex v) {
		this.v = v;
	}
}
