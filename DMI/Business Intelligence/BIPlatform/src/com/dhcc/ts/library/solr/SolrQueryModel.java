package com.dhcc.ts.library.solr;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class SolrQueryModel {
	public String qlbi_id;//主键id--------==索引字段
	public String title_chs;//题名_中文--------==索引字段
	public String title_en;//题名_英文--------==索引字段
	public String author_chs;//作者_中文--------==索引字段
	public String author_en;//作者_英文--------==索引字段
	public String abstract_chs;//摘要_中文--------==索引字段
	public String abstract_en;//摘要_英文--------==索引字段
	public String source_chs;//来源_中文--------==索引字段
	public String source_en;//来源_英文--------==索引字段
	public String descriptor_chs;//关键词_中文--------==索引字段
	public String descriptor_en;//关键词_英文--------==索引字段
	public String publication_time;//出版时间--------==索引字段
	public String resource_type;//资源类型（1：医学百科知识、2：教科书、3：中文期刊、4：外文期刊、5：循证医学、6：影像资料）--------==索引字段
	public String view_count;//浏览次数
	public String bd_update_time;//百度词条更新时间
	public String bd_update_count;//百度词条更新次数
	public String creator;//编辑人
	public Date createtime;//创建时间--------==索引字段
	public String detailes;//内容--------==索引字段
	/*--------20170425新加字段开始--------*/
	public String journal_title;// 期刊名--------==索引字段
	public String document_type;// 文献类型--------==索引字段
	public String language;// 语种--------==索引字段
	/*--------20170425新加字段完毕--------*/
	/*--------20170503新加字段完毕--------*/
	private String publication_year;//出版年份
	/*--------20170503新加字段完毕--------*/
	public String getQlbi_id() {
		return qlbi_id;
	}
	@Field
	public void setQlbi_id(String qlbi_id) {
		this.qlbi_id = qlbi_id;
	}
	public String getTitle_chs() {
		return title_chs;
	}
	@Field
	public void setTitle_chs(String title_chs) {
		this.title_chs = title_chs;
	}
	public String getTitle_en() {
		return title_en;
	}
	@Field
	public void setTitle_en(String title_en) {
		this.title_en = title_en;
	}
	public String getAuthor_chs() {
		return author_chs;
	}
	@Field
	public void setAuthor_chs(String author_chs) {
		this.author_chs = author_chs;
	}
	public String getAuthor_en() {
		return author_en;
	}
	@Field
	public void setAuthor_en(String author_en) {
		this.author_en = author_en;
	}
	public String getAbstract_chs() {
		return abstract_chs;
	}
	@Field
	public void setAbstract_chs(String abstract_chs) {
		this.abstract_chs = abstract_chs;
	}
	public String getAbstract_en() {
		return abstract_en;
	}
	@Field
	public void setAbstract_en(String abstract_en) {
		this.abstract_en = abstract_en;
	}
	public String getSource_chs() {
		return source_chs;
	}
	@Field
	public void setSource_chs(String source_chs) {
		this.source_chs = source_chs;
	}
	public String getSource_en() {
		return source_en;
	}
	@Field
	public void setSource_en(String source_en) {
		this.source_en = source_en;
	}
	public String getDescriptor_chs() {
		return descriptor_chs;
	}
	@Field
	public void setDescriptor_chs(String descriptor_chs) {
		this.descriptor_chs = descriptor_chs;
	}
	public String getDescriptor_en() {
		return descriptor_en;
	}
	@Field
	public void setDescriptor_en(String descriptor_en) {
		this.descriptor_en = descriptor_en;
	}
	public String getPublication_time() {
		return publication_time;
	}
	@Field
	public void setPublication_time(String publication_time) {
		this.publication_time = publication_time;
	}
	public String getResource_type() {
		return resource_type;
	}
	@Field
	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}
	public String getView_count() {
		return view_count;
	}
	@Field
	public void setView_count(String view_count) {
		this.view_count = view_count;
	}
	public String getBd_update_time() {
		return bd_update_time;
	}
	@Field
	public void setBd_update_time(String bd_update_time) {
		this.bd_update_time = bd_update_time;
	}
	public String getBd_update_count() {
		return bd_update_count;
	}
	@Field
	public void setBd_update_count(String bd_update_count) {
		this.bd_update_count = bd_update_count;
	}
	public String getCreator() {
		return creator;
	}
	@Field
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreatetime() {
		return createtime;
	}
	@Field
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getDetailes() {
		return detailes;
	}
	@Field
	public void setDetailes(String detailes) {
		this.detailes = detailes;
	}
	public String getJournal_title() {
		return journal_title;
	}
	@Field
	public void setJournal_title(String journal_title) {
		this.journal_title = journal_title;
	}
	public String getDocument_type() {
		return document_type;
	}
	@Field
	public void setDocument_type(String document_type) {
		this.document_type = document_type;
	}
	public String getLanguage() {
		return language;
	}
	@Field
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPublication_year() {
		return publication_year;
	}
	@Field
	public void setPublication_year(String publication_year) {
		this.publication_year = publication_year;
	}
}