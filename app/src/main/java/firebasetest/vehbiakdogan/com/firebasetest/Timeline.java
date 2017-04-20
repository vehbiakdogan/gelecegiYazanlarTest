package firebasetest.vehbiakdogan.com.firebasetest;


public class Timeline {
    private int kategoriId, likeCount;
    private String postId, userName, userImg, postName,postImgPath;

    public Timeline(){}
    public Timeline(String postId, int kategoriId, int likeCount, String userName, String userImg, String postName, String postImgPath) {
        this.postId = postId;
        this.kategoriId = kategoriId;
        this.likeCount = likeCount;
        this.userName = userName;
        this.userImg = userImg;
        this.postName = postName;
        this.postImgPath = postImgPath;

    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostImgPath() {
        return postImgPath;
    }

    public void setPostImgPath(String postImgPath) {
        this.postImgPath = postImgPath;
    }


}
