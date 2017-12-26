package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.graph.Edge;
import io.atomix.copycat.Command;

public class CreateEdge implements Command<Boolean> {
	public Edge a;

	public CreateEdge(Edge a) {
		this.a = a;
	}
}
