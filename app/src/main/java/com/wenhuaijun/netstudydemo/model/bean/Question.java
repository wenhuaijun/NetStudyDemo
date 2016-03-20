package com.wenhuaijun.netstudydemo.model.bean;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/3/20 0020.
 */
public class Question {
    private String id;
    private String title;
    private String content;
    private String bestAnswerId;
    private String date;
    private String recent;
    private String answerCount;
    private String authorId;
    private String authorName;
    private String authorFace;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBestAnswerId() {
        return bestAnswerId;
    }

    public void setBestAnswerId(String bestAnswerId) {
        this.bestAnswerId = bestAnswerId;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public String getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(String answerCount) {
        this.answerCount = answerCount;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorFace() {
        return authorFace;
    }

    public void setAuthorFace(String authorFace) {
        this.authorFace = authorFace;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this,Question.class);
    }
    public class QuestionResult{
        private int totalCount;
        private int totalPage;
        private Question[] questions;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public Question[] getQuestions() {
            return questions;
        }

        public void setQuestions(Question[] questions) {
            this.questions = questions;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this,QuestionResult.class);
        }
    }
}
