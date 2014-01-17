package fr.epita.sigl.miwa.application;

public class Promotions {
private Articles article;
private String ref_article;
private String begin;
private String end;
private String pourcentage;
public Articles getArticle() {
	return article;
}
public void setArticle(Articles article) {
	this.article = article;
}

public String getRef_article() {
	return ref_article;
}
public void setRef_article(String ref_article) {
	this.ref_article = ref_article;
}
public String getBegin() {
	return begin;
}
public void setBegin(String begin) {
	this.begin = begin;
}
public String getEnd() {
	return end;
}
public void setEnd(String end) {
	this.end = end;
}
public String getPourcentage() {
	return pourcentage;
}
public void setPourcentage(String pourcentage) {
	this.pourcentage = pourcentage;
}


}
