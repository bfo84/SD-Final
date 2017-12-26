package br.com.ufu.thrift.copycat;

import io.atomix.copycat.Command;

public class DisblockVertex implements Command<Void> {
	public int nome;

	public DisblockVertex(int nome)
	  {
	    this.nome = nome;
	  }
}
