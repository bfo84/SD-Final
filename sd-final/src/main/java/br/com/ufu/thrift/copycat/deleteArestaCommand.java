/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufu.thrift.copycat;

import io.atomix.copycat.Command;

/**
 *
 * @author steff
 */
public class deleteArestaCommand implements Command<Boolean> {

    public int nome, nome2;

    public deleteArestaCommand(int nome, int nome2) {
        this.nome = nome;
        this.nome2 = nome2;
    }

}
