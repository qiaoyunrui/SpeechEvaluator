package com.nuc.speechevaluator.db.bean;

import com.nuc.speechevaluator.util.Constant;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 试题
 */
public class Question extends RealmObject implements Serializable {

    public static Question createQuestion() {
        Question question = new Question();
        question.setId(UUID.randomUUID().toString());
        question.setDate(new Date());
        return question;
    }

    @PrimaryKey
    private String id;

    private int languageType;   //语言[中文|英文]

    private int questionType;    //内容类型[（单字，汉语专有）、（词语）、（句子）、（篇章）]

    private String content; //试题内容

    private Date date;    //创建日期

    public String getId() {
        return id;
    }

    public Question setId(String id) {
        this.id = id;
        return this;
    }

    public int getLanguageType() {
        return languageType;
    }

    public Question setLanguageType(int languageType) {
        this.languageType = languageType;
        return this;
    }

    public int getQuestionType() {
        return questionType;
    }

    public Question setQuestionType(int questionType) {
        this.questionType = questionType;
        return this;
    }

    public String getContent() {
        return content;
    }

    public String getContentParam() {
        if (languageType == Constant.QUESTION_LANG_TYPE_ENGLISH
                && questionType == Constant.QUESTION_TYPE_WORD) {
            return "[word]\n" + content;
        }
        return content;
    }

    public Question setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Question setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", languageType=" + languageType +
                ", questionType=" + questionType +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
