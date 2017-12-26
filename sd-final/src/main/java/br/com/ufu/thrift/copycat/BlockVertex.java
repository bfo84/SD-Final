package br.com.ufu.thrift.copycat;

import io.atomix.copycat.Command;

public class BlockVertex implements Command<Boolean> {
	public int nome;

	public BlockVertex(int nome) {
		this.nome = nome;
	}
}
