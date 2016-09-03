package com.example.salimo.supermercado.controller;

/**
 * Created by salim on 8/29/2016.
 */
public class Status {
    String nomeProd;
    String validade;
    String preco;
    String imagem;
    String codigoBarras;

    public Status() {
    }

    public Status(String nomeProd, String validade, String preco, String imagem, String codigoBarras) {
        this.nomeProd = nomeProd;
        this.validade = validade;
        this.preco = preco;
        this.imagem = imagem;
        this.codigoBarras = codigoBarras;
    }

    public String getNomeProd() {
        return nomeProd;
    }

    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
}
