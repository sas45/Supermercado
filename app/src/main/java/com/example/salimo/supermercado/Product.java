package com.example.salimo.supermercado;

/**
 * Created by salim on 8/27/2016.
 */
public class Product {

    /**
     * _id : 57c011310e28d91c2848b982
     * nomeProd : Leite
     * validade : 12/09/18
     * preco : 420
     * imagem : https://izyshop.co.mz/images/produtos/01/IMG0119005.png
     * codigoBarras : 09124357584
     * __v : 0
     */

    private String _id;
    private String nomeProd;
    private String validade;
    private double preco;
    private String imagem;
    private String codigoBarras;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
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
