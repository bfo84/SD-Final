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
public class desbloqueiaVerticeCommand implements Command<Void> {

    public int nome;

    public desbloqueiaVerticeCommand(int nome) {
        this.nome = nome;
    }

}
