package com.sinosoft.one.uiUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-10-12
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class Treeable<T> implements UIable {
    private String idField;
    private String titleField;
    private String classField;
    private String urlField;
    private String childrenField;
    private String stateField;
    private List<T> content;

    public Treeable(List<T> content) {
        this.content = content;
    }

    public Treeable(String idField, String titleField, String classField, String urlField, String childrenField, String stateField) {
        this.idField = idField;
        this.titleField = titleField;
        this.classField = classField;
        this.urlField = urlField;
        this.childrenField = childrenField;
        this.stateField = stateField;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getClassField() {
        return classField;
    }

    public void setClassField(String classField) {
        this.classField = classField;
    }

    public String getStateField() {
        return stateField;
    }

    public void setStateField(String stateField) {
        this.stateField = stateField;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public String getUrlField() {
        return urlField;
    }

    public void setUrlField(String urlField) {
        this.urlField = urlField;
    }

    public String getChildrenField() {
        return childrenField;
    }

    public void setChildrenField(String childrenField) {
        this.childrenField = childrenField;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public Render getRender() {
        return new TreeRender(new TreeConverter(), this);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
