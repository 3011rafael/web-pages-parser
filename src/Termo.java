/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author Rafael Macedo
 */
public class Termo{
    
    private int numOcorrencias = 0;
    private int peso=0;
    private String nome="";

    public Termo() {
    }
    
    

    public int getNumOcorrencias() {
        return numOcorrencias;
    }

    public void setNumOcorrencias(int numOcorrencias) {
        this.numOcorrencias = numOcorrencias;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public boolean equals(Object o){
        Termo t = (Termo)o;
        if(t.getNome().equals(this.getNome())){
        return true;
        }
        else
            return false;
    }
    
}
