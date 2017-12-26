package br.com.ufu.thrift.copycat;

import io.atomix.copycat.Command;

public class DeleteVertex implements Command<Boolean> {
	public int nome;

	public DeleteVertex(int nome) {
        this.nome = nome;
    }
}
