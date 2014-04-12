package com.example.iotd;

import android.graphics.Bitmap;

public class RssItem {
	private String titulo;
	private String descricao;
	private String date;
	private String imagemUrl;
	private Bitmap imagem;
	
	public Bitmap getImagem() {
		return imagem;
	}
	public void setImagem(Bitmap imagem) {
		this.imagem = imagem;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImagemUrl() {
		return imagemUrl;
	}
	public void setImagemUrl(String imagemUrl) {
		this.imagemUrl = imagemUrl;
	}
	
	
	
}
