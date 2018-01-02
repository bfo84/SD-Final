/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufu.thrift.copycat;

import br.com.ufu.thrift.grafo.Vertice;
import io.atomix.copycat.Query;

/**
 *
 * @author steff
 */
public class readVerticeQuery implements Query<Vertice> {

    public int nome;

    public readVerticeQuery(int nome) {
        this.nome = nome;
    }
}
