package com.jaychang.sa;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialUser implements Parcelable {

  public String userId;
  public String accessToken;
  public String profilePictureUrl;
  public String username;
  public String fullName;
  public String email;
  public String pageLink;

  public SocialUser() {
  }

  public SocialUser(SocialUser other) {
    this.userId = other.userId;
    this.accessToken = other.accessToken;
    this.profilePictureUrl = other.profilePictureUrl;
    this.username = other.username;
    this.fullName = other.fullName;
    this.email = other.email;
    this.pageLink = other.pageLink;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SocialUser that = (SocialUser) o;

    return userId != null ? userId.equals(that.userId) : that.userId == null;
  }

  @Override
  public int hashCode() {
    return userId != null ? userId.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SocialUser {").append("\n\n");
    sb.append("userId=").append(userId).append("\n\n");
    sb.append("username=").append(username).append("\n\n");
    sb.append("fullName=").append(fullName).append("\n\n");
    sb.append("email=").append(email).append("\n\n");
    sb.append("profilePictureUrl=").append(profilePictureUrl).append("\n\n");
    sb.append("pageLink=").append(pageLink).append("\n\n");
    sb.append("accessToken=").append(accessToken).append("\n\n");
    sb.append('}');
    return sb.toString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.userId);
    dest.writeString(this.accessToken);
    dest.writeString(this.profilePictureUrl);
    dest.writeString(this.username);
    dest.writeString(this.fullName);
    dest.writeString(this.email);
    dest.writeString(this.pageLink);
  }

  protected SocialUser(Parcel in) {
    this.userId = in.readString();
    this.accessToken = in.readString();
    this.profilePictureUrl = in.readString();
    this.username = in.readString();
    this.fullName = in.readString();
    this.email = in.readString();
    this.pageLink = in.readString();
  }

  public static final Parcelable.Creator<SocialUser> CREATOR = new Parcelable.Creator<SocialUser>() {
    @Override
    public SocialUser createFromParcel(Parcel source) {
      return new SocialUser(source);
    }

    @Override
    public SocialUser[] newArray(int size) {
      return new SocialUser[size];
    }
  };

}
