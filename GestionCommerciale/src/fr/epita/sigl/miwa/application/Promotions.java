package fr.epita.sigl.miwa.application;

import java.util.Date;

public class Promotions {
private Articles article;
private Date begin;
private Date end;
private String pourcentage;
public Articles getArticle() {
	return article;
}
public void setArticle(Articles article) {
	this.article = article;
}
public Date getBegin() {
	return begin;
}
public void setBegin(Date begin) {
	this.begin = begin;
}
public Date getEnd() {
	return end;
}
public void setEnd(Date end) {
	this.end = end;
}
public String getPourcentage() {
	return pourcentage;
}
public void setPourcentage(String pourcentage) {
	this.pourcentage = pourcentage;
}


}
