package br.com.ufu.thrift.copycat;

import io.atomix.copycat.Command;

public class DeleteEdge implements Command<Boolean> {
	public int nome;
	public int nome2;

	public DeleteEdge(int nome, int nome2) {
		this.nome = nome;
		this.nome2 = nome2;
	}
}
