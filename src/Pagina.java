/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.util.ArrayList;

/**
 *
 * @author aluno
 */
public class Pagina {
    private String titulo="";
    private String textoCompleto="";
    private String textoTratato="";
    private String textoSemQuebraLinha = "";
    private String textoMinusculo = "";
    private String textoSemAcento = "";
    
    private int tamanho;
    private long data;
    private String relevantes;
    private int quantTermosDistintos;
    private ArrayList termosRelevantes= new ArrayList();
    private ArrayList termos = new ArrayList();

    public Pagina() {
    }
    
    

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTextoCompleto(String texto) {
        this.textoCompleto = texto;
    }

    public ArrayList getTermosRelevantes() {
        return termosRelevantes;
    }

    public void setTermosRelevantes(ArrayList termosRelevantes) {
        this.termosRelevantes = termosRelevantes;
    }

    public ArrayList getTermos() {
        return termos;
    }

    public void setTermos(ArrayList termos) {
        this.termos = termos;
    }

    
    public String getTitulo() {
        return titulo;
    }

    public String getTextoSemQuebraLinha() {
        return textoSemQuebraLinha;
    }

    public void setTextoSemQuebraLinha(String textoSemQuebraLinha) {
        this.textoSemQuebraLinha = textoSemQuebraLinha;
    }

    public String getTextoMinusculo() {
        return textoMinusculo;
    }

    public void setTextoMinusculo(String textoMinusculo) {
        this.textoMinusculo = textoMinusculo;
    }

    public String getTextoSemAcento() {
        return textoSemAcento;
    }

    public void setTextoSemAcento(String textoSemAcento) {
        this.textoSemAcento = textoSemAcento;
    }

    public String getTextoCompleto() {
        return textoCompleto;
    }

    
    public String getTextoTratato() {
        return textoTratato;
    }

    public void setTextoTratato(String textoTratato) {
        this.textoTratato = textoTratato;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getRelevantes() {
        return relevantes;
    }

    public void setRelevantes(String relevantes) {
        this.relevantes = relevantes;
    }

    public int getQuantTermosDistintos() {
        return quantTermosDistintos;
    }

    public void setQuantTermosDistintos(int quantTermosDistintos) {
        this.quantTermosDistintos = quantTermosDistintos;
    }
    
    
    
    
    
    
    
    
}
