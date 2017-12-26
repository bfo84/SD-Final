package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.graph.Edge;
import io.atomix.copycat.Command;

public class UpdateEdge implements Command<Boolean> {
	public Edge a;

	public UpdateEdge(Edge a) {
		this.a = a;
	}
}