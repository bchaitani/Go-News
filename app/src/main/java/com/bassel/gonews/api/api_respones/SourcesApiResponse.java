package com.bassel.gonews.api.api_respones;

import android.os.Parcel;
import android.os.Parcelable;

import com.bassel.gonews.api.model.Source;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by basselchaitani on 2/5/19.
 */
public class SourcesApiResponse implements Parcelable {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("sources")
    private List<Source> sources;

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

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
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
        dest.writeTypedList(this.sources);
    }

    public SourcesApiResponse() {
    }

    protected SourcesApiResponse(Parcel in) {
        this.status = in.readString();
        this.totalResults = in.readInt();
        this.code = in.readString();
        this.message = in.readString();
        this.sources = in.createTypedArrayList(Source.CREATOR);
    }

    public static final Parcelable.Creator<SourcesApiResponse> CREATOR = new Parcelable.Creator<SourcesApiResponse>() {
        @Override
        public SourcesApiResponse createFromParcel(Parcel source) {
            return new SourcesApiResponse(source);
        }

        @Override
        public SourcesApiResponse[] newArray(int size) {
            return new SourcesApiResponse[size];
        }
    };
}
