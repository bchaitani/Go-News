package com.bassel.gonews.api.api_respones;

import android.os.Parcel;
import android.os.Parcelable;

import com.bassel.gonews.api.model.Article;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by basselchaitani on 2/5/19.
 */
public class ArticlesApiResponse implements Parcelable {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("articles")
    private List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeInt(this.totalResults);
        dest.writeString(this.code);
        dest.writeString(this.message);
        dest.writeTypedList(this.articles);
    }

    public ArticlesApiResponse() {
    }

    protected ArticlesApiResponse(Parcel in) {
        this.status = in.readString();
        this.totalResults = in.readInt();
        this.code = in.readString();
        this.message = in.readString();
        this.articles = in.createTypedArrayList(Article.CREATOR);
    }

    public static final Parcelable.Creator<ArticlesApiResponse> CREATOR = new Parcelable.Creator<ArticlesApiResponse>() {
        @Override
        public ArticlesApiResponse createFromParcel(Parcel source) {
            return new ArticlesApiResponse(source);
        }

        @Override
        public ArticlesApiResponse[] newArray(int size) {
            return new ArticlesApiResponse[size];
        }
    };
}
