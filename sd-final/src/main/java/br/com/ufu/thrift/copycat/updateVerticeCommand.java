/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.grafo.Vertice;
import io.atomix.copycat.Command;

/**
 *
 * @author steff
 */
public class updateVerticeCommand implements Command<Boolean> {

    public Vertice v;

    public updateVerticeCommand(Vertice v) {
        this.v = v;
    }
}
