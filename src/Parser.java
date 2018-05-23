/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

/**
 *
 * @author aluno
 */
public class Parser {

    private Pagina pagina = new Pagina();
    private String tudo = "";
    private String centroide = "";
    private ArrayList stopList;
    private Centroide c;

    public Parser(String url) throws MalformedURLException, IOException {
        //System.setProperty("http.proxyHost", "10.65.16.2");
        //System.setProperty("http.proxyPort", "3128");
        URL link = new URL(url);
        //System.out.println(link.toString());
        URLConnection conexao = link.openConnection();

        stopList = new ArrayList();

        DataInputStream in = new DataInputStream(conexao.getInputStream());
        String line;
        int i = 0;

        c = new Centroide();

        while ((line = in.readLine()) != null) {

            pagina.setTextoCompleto(pagina.getTextoCompleto() + line + "\n");
            //tudo+=line;
            getTitulo(line);

            i++;
            //System.out.println(line);

        }

        // Tamanho da página
        pagina.setTamanho(conexao.getContentLength());
        // Data da última modificação
        pagina.setData(conexao.getLastModified());
        getTexto();
        Date data = new Date(pagina.getData());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        //System.out.println( getTexto());
        System.out.println("Numero de termos distintos: " + this.getNumeroTermosDistintos());
        //System.out.println("Termos distintos: " +this.getNumeroTermosDistintos());

        System.out.println("Numero de termos: " + getNumeroTermos());
        System.out.println("Data da última atualização: " + df.format(data));
        System.out.println("Tamanho da página: " + pagina.getTamanho());
        //System.out.println("Tamanho da página: " + pagina.getTamanho() + " Bytes");
        this.calculaPeso();
        mostraCentroide();

        //System.out.println (df.format (data));  

        //centroide();
        in.close();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {

        //new Parser("http://www.uefs.br");                                                 	   new Parser(args[0]);



    }

    private void getTitulo(String line) {

        String t = "<title>";
        CharSequence titulo = (CharSequence) t;


        if (line.contains(titulo)) {

            String titilo = line.replaceAll("<title>", "");
            titilo = titilo.replaceAll("</title>", "");
            //System.out.println("Título: " + titilo);
            //String resto;
            //resto = (String) line.split("<")[1];
            //String title = resto.split(">")[1];
            pagina.setTitulo(titilo);
            //System.out.println("Título: " + title);

        }


    }

    //Retorna o texto sem as tags
    private String getTexto() {

        String texto = pagina.getTextoCompleto();
        String textoTratado = texto.replaceAll("\\<[^>]*>", "");
        textoTratado = textoTratado.replace("\\s+", " ");
        textoTratado = textoTratado.replace("", "");
        pagina.setTextoTratato(textoTratado);
        textoTratado = textoTratado.replaceAll("\n", "");
        pagina.setTextoSemQuebraLinha(textoTratado);

        return pagina.getTextoTratato();

    }

    private int getNumeroTermos() {
        return pagina.getTermos().size();
    }

    private int getNumeroTermosDistintos() throws FileNotFoundException {

        //Lê o arquivo
        File arquivo = new File("stoplist.txt");
        Scanner scanner; // FileReader para lê o arquivo de texto.
        scanner = new Scanner(new FileReader(arquivo));
        int j = 0;
        while (scanner.hasNextLine()) {

            String x = scanner.nextLine();
            Termo temp = new Termo();
            temp.setNome(x);
            stopList.add(temp);
            this.centroide = this.centroide + " " + x;
            //System.out.println(((Termo)stopList.get(j)).getNome());
            j++;
        }
        scanner.close();

        //
        String texto = pagina.getTextoSemQuebraLinha();
        String termos[] = texto.split(" ");

        for (int i = 0; i < termos.length; i++) {
            //Testa se o termo é distinto, se for incrementa 
            Termo t = new Termo();
            t.setNome(termos[i]);
            //System.out.println("Termo: " + t.getNome());

            if (c.getTermos().contains(t)) { //se na lista de termos relevantes contem t 
                int index = c.getTermos().indexOf(t);
                Termo termo = ((Termo) c.getTermos().get(index));
                // System.out.println("Já contém");
                //System.out.println(termo.getNome());
                termo.setNumOcorrencias(termo.getNumOcorrencias() + 1);
                //System.out.println("Incrementa valor atual: " + termo.getNumOcorrencias());
                if (!stopList.contains(termo));
                {
                    if (!pagina.getTermosRelevantes().contains(termo)) {
                        t.setNome(t.getNome().toUpperCase());
                        pagina.getTermosRelevantes().add(termo);
                        System.out.println("Termo relevante");
                    }
                }

            } //Se não for adiciona
            else {
                //Termo termo = new Termo();
                c.getTermos().add(t);
                t.setNumOcorrencias(1);
                // System.out.println("Não contém adiciona");
                if (!stopList.contains(t));
                {
                    if (!pagina.getTermosRelevantes().contains(t)) {
                        t.setNome(t.getNome().toLowerCase());
                        pagina.getTermosRelevantes().add(t);
                        //System.out.println("Termo relevante");
                    }
                }

            }
            pagina.getTermos().add(t);

        }

        //pagina.setQuantTermosDistintos(c.getTermos().size());
        return c.getTermos().size();
    }

    public void mostraCentroide() {
        for (int i = 0; i < pagina.getTermosRelevantes().size(); i++) {
            Termo t = (Termo) pagina.getTermosRelevantes().get(i);
            System.out.println(t.getNome() + " , "+ t.getPeso() + " , " + t.getNumOcorrencias());
        }
    }

    public void calculaPeso() {
        
        String texto = this.removeAcento(pagina.getTextoCompleto());
        texto.toLowerCase();

        String titulo = texto.replaceAll("<title>", "");
        titulo = titulo.replaceAll("</title>", "");
        
        String[] termosTitulo = titulo.split(" ");
        for(int i=0; i<termosTitulo.length; i++){
            Termo t = new Termo();
            t.setNome(termosTitulo[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*10);
            }
            
        }
        
        String h1 = pagina.getTextoCompleto().replaceAll("<h1>", "");
        h1 = h1.replaceAll("</h1>", "");
        
        String[] termosH1 = h1.split(" ");
        for(int i=0; i<termosH1.length; i++){
            Termo t = new Termo();
            t.setNome(termosH1[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*7);
            }
            
        }
        
        
        
        String h2 = pagina.getTextoCompleto().replaceAll("<h2>", "");
        h2 = h2.replaceAll("</h2>", "");
        
        String[] termosH2 = h2.split(" ");
        for(int i=0; i<termosH2.length; i++){
            Termo t = new Termo();
            t.setNome(termosH2[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*6);
            }
            
        }
        
        String h3 = pagina.getTextoCompleto().replaceAll("<h3>", "");
        h3 = h3.replaceAll("</h3>", "");
        
        String[] termosH3 = h3.split(" ");
        for(int i=0; i<termosH3.length; i++){
            Termo t = new Termo();
            t.setNome(termosH3[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*5);
            }
            
        }
        
        String h4 = pagina.getTextoCompleto().replaceAll("<h4>", "");
        h4 = h4.replaceAll("</h4>", "");
        
        String[] termosH4 = h4.split(" ");
        for(int i=0; i<termosH4.length; i++){
            Termo t = new Termo();
            t.setNome(termosH4[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*4);
            }
            
        }
        
        String h5 = pagina.getTextoCompleto().replaceAll("<h5>", "");
        h5 = h5.replaceAll("</h5>", "");
        
        String[] termosH5 = h5.split(" ");
        for(int i=0; i<termosH5.length; i++){
            Termo t = new Termo();
            t.setNome(termosH5[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*4);
            }
            
        }
        
        String h6 = pagina.getTextoCompleto().replaceAll("<h6>", "");
        h6 = h6.replaceAll("</h6>", "");
        
        String[] termosH6 = h6.split(" ");
        for(int i=0; i<termosH6.length; i++){
            Termo t = new Termo();
            t.setNome(termosH6[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*4);
            }
            
        }
        
        String a = pagina.getTextoCompleto().replaceAll("<a>", "");
        a = a.replaceAll("</a>", "");
        
        String[] termosA = a.split(" ");
        for(int i=0; i<termosA.length; i++){
            Termo t = new Termo();
            t.setNome(termosA[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*5);
            }
            
        }
        
        String b = pagina.getTextoCompleto().replaceAll("<b>", "");
        b = b.replaceAll("</b>", "");
        
        String[] termosB = b.split(" ");
        for(int i=0; i<termosB.length; i++){
            Termo t = new Termo();
            t.setNome(termosB[i]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String i = pagina.getTextoCompleto().replaceAll("<i>", "");
        i = i.replaceAll("</i>", "");
        
        String[] termosI = i.split(" ");
        for(int j=0; j<termosI.length; j++){
            Termo t = new Termo();
            t.setNome(termosI[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String u = pagina.getTextoCompleto().replaceAll("<u>", "");
        u = u.replaceAll("</u>", "");
        
        String[] termosU = u.split(" ");
        for(int j=0; j<termosU.length; j++){
            Termo t = new Termo();
            t.setNome(termosU[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String big = pagina.getTextoCompleto().replaceAll("<big>", "");
        big = h1.replaceAll("</big>", "");
        
        String[] termosBig = big.split(" ");
        for(int j=0; j<termosBig.length; j++){
            Termo t = new Termo();
            t.setNome(termosBig[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String em = pagina.getTextoCompleto().replaceAll("<em>", "");
        em = h1.replaceAll("</em>","");
        
        String[] termosEm = em.split(" ");
        for(int j=0; j<termosEm.length; j++){
            Termo t = new Termo();
            t.setNome(termosEm[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String strong = pagina.getTextoCompleto().replaceAll("<strong>", "");
        strong = strong.replaceAll("</strong>", "");
        
        String[] termosStrong = strong.split(" ");
        for(int j=0; j<termosStrong.length; j++){
            Termo t = new Termo();
            t.setNome(termosStrong[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String strike = pagina.getTextoCompleto().replaceAll("<strike>", "");
        strike = strike.replaceAll("</strike>", "");
        
        String[] termosStrike = strike.split(" ");
        for(int j=0; j<termosStrike.length; j++){
            Termo t = new Termo();
            t.setNome(termosStrike[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String center = pagina.getTextoCompleto().replaceAll("<center>", "");
        center = center.replaceAll("</center>", "");
        
        String[] termosCenter = center.split(" ");
        for(int j=0; j<termosCenter.length; j++){
            Termo t = new Termo();
            t.setNome(termosCenter[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*3);
            }
            
        }
        
        String small = pagina.getTextoCompleto().replaceAll("<small>", "");
        small = h1.replaceAll("</small>", "");
        
        String[] termosSmall = small.split(" ");
        for(int j=0; j<termosSmall.length; j++){
            Termo t = new Termo();
            t.setNome(termosSmall[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*2);
            }
        }
        
        String sub = pagina.getTextoCompleto().replaceAll("<sub>", "");
        sub = sub.replaceAll("</sub>", "");
        
        String[] termosSub = sub.split(" ");
        for(int j=0; j<termosSub.length; j++){
            Termo t = new Termo();
            t.setNome(termosSub[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*2);
            }
        }
        
        
        String sup = pagina.getTextoCompleto().replaceAll("<sup>", "");
        sup = sup.replaceAll("</sup>", "");
        
        String[] termosSup = sup.split(" ");
        for(int j=0; j<termosSup.length; j++){
            Termo t = new Termo();
            t.setNome(termosSup[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*2);
            }
        }

        String font = pagina.getTextoCompleto().replaceAll("<font>", "");
        font = font.replaceAll("</font>", "");
        
        String[] termosFont = font.split(" ");
        for(int j=0; j<termosFont.length; j++){
            Termo t = new Termo();
            t.setNome(termosFont[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*2);
            }
        }
        
        String address = pagina.getTextoCompleto().replaceAll("<address>", "");
        address = address.replaceAll("</address>", "");
        
        String[] termosAddress = address.split(" ");
        for(int j=0; j<termosAddress.length; j++){
            Termo t = new Termo();
            t.setNome(termosAddress[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*2);
            }
        }
        
        String meta = pagina.getTextoCompleto().replaceAll("<meta>", "");
        meta = meta.replaceAll("</meta>", "");
        
        String[] termosMeta = meta.split(" ");
        for(int j=0; j<termosMeta.length; j++){
            Termo t = new Termo();
            t.setNome(termosMeta[j]);
            if(pagina.getTermosRelevantes().contains(t)){
                int x = pagina.getTermosRelevantes().indexOf(t);
                Termo t2 = (Termo) pagina.getTermosRelevantes().get(x);
                t2.setPeso(t2.getPeso() + t2.getNumOcorrencias()*2);
            }
        }
        
    }

    public String removeAcento(String texto) {
        //Remove a acentuação
        // String texto = pagina.getTextoTratato();
        texto = texto.replaceAll("[èéêë]", "e");
        texto = texto.replaceAll("[ûù]", "u");
        texto = texto.replaceAll("[ïî]", "i");
        texto = texto.replaceAll("[àâ]", "a");
        texto = texto.replaceAll("Ô", "o");
        texto = texto.replaceAll("[ÈÉÊË]", "E");
        texto = texto.replaceAll("[ÛÙ]", "U");
        texto = texto.replaceAll("[Î]", "I");
        texto = texto.replaceAll("[ÀÂ]", "A");
        texto = texto.replaceAll("Ô", "O");
        texto = texto.replaceAll("[óÓ]", "o");
        texto = texto.replaceAll("[Çç]", "c");

        return texto;

        // Transforma todas as letras para minúsculas
        //texto.toLowerCase();
    }
}
