package bnrc.weibo.crawler.model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import weibo4j.model.User;

public class UserBean {
	
	private long userId;					// 用户UID
	private String screenName;				// 微博昵称
	private String name;					// 友好显示名称，如Bill Gates,名称中间的空格正常显示(此特性暂不支持)
	private String province;				// 省份编码（参考省份编码表）
	private String city;					// 城市编码（参考城市编码表）
	private String location;				// 地址
	private String description;				// 个人描述
	private String url;						// 用户博客地址
	private String profileImageUrl;			// 自定义图像
	private String userDomain;				// 用户个性化URL
	private String gender;					// 性别,m--男，f--女,n--未知
	private int followersCount;				// 粉丝数
	private int friendsCount;				// 关注数
	private int statusesCount;				// 微博数
	private int favouritesCount;			// 收藏数
	private Timestamp createdAt;			// 创建时间
	private boolean following;				// 保留字段,是否已关注(此特性暂不支持)
	private boolean verified;				// 加V标示，是否微博认证用户
	private int verifiedType;				// 认证类型（暂未支持）
	private boolean allowAllActMsg;			// 是否允许所有人给我发私信
	private boolean allowAllComment;		// 是否允许所有人对我的微博进行评论
	private boolean followMe;				// 此用户是否关注我
	private String avatarLarge;				// 用户大头像地址
	private int onlineStatus;				// 用户的在线状态：0不在线、1在线
	private int biFollowersCount;			// 用户的互粉数
	private String remark;					// 用户备注信息，只有查询用户关系时才返回
	private String lang;					// 用户当前的语言版本
	private String verifiedReason;			// 认证原因
	private String weihao;					// 用户的微号
	private boolean geoEnabled;				// 是否允许提供地理位置信息
	private double statusesFrequency;		// 用户当前发布微博的频率（按天计算）
	private int iteration;					// 爬虫访问次数
	private Timestamp updateTime;			// 该条记录的更新时间
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerifiedType() {
		return verifiedType;
	}

	public void setVerifiedType(int verifiedType) {
		this.verifiedType = verifiedType;
	}

	public boolean isAllowAllActMsg() {
		return allowAllActMsg;
	}

	public void setAllowAllActMsg(boolean allowAllActMsg) {
		this.allowAllActMsg = allowAllActMsg;
	}

	public boolean isAllowAllComment() {
		return allowAllComment;
	}

	public void setAllowAllComment(boolean allowAllComment) {
		this.allowAllComment = allowAllComment;
	}

	public boolean isFollowMe() {
		return followMe;
	}

	public void setFollowMe(boolean followMe) {
		this.followMe = followMe;
	}

	public String getAvatarLarge() {
		return avatarLarge;
	}

	public void setAvatarLarge(String avatarLarge) {
		this.avatarLarge = avatarLarge;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public int getBiFollowersCount() {
		return biFollowersCount;
	}

	public void setBiFollowersCount(int biFollowersCount) {
		this.biFollowersCount = biFollowersCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getVerifiedReason() {
		return verifiedReason;
	}

	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}

	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	public boolean isGeoEnabled() {
		return geoEnabled;
	}

	public void setGeoEnabled(boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}

	public double getStatusesFrequency() {
		return statusesFrequency;
	}

	public void setStatusesFrequency(double statusFrequency) {
		this.statusesFrequency = statusFrequency;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	// 将User对象转换成为UserBean对象
	public static UserBean getUserBean(User user) {
		UserBean userBean = new UserBean();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = null;
		
		userBean.setUserId(new BigInteger(user.getId()).longValue());
    	userBean.setScreenName(user.getScreenName());
    	userBean.setName(user.getName());
    	userBean.setProvince(user.getProvince() + "");
    	userBean.setCity(user.getCity() + "");
    	userBean.setLocation(user.getLocation());
    	userBean.setDescription(user.getDescription());
    	userBean.setUrl(user.getUrl());
    	userBean.setProfileImageUrl(user.getProfileImageUrl());
    	userBean.setUserDomain(user.getUserDomain());
    	userBean.setGender(user.getGender());
    	userBean.setFollowersCount(user.getFollowersCount());
    	userBean.setFriendsCount(user.getFriendsCount());
    	userBean.setStatusesCount(user.getStatusesCount());
    	userBean.setFavouritesCount(user.getFavouritesCount());	    	
    	time = simpleDateFormat.format(user.getCreatedAt());
    	userBean.setCreatedAt(Timestamp.valueOf(time));
    	userBean.setFollowing(user.isFollowing());
    	userBean.setVerified(user.isVerified());
    	userBean.setAllowAllActMsg(user.isAllowAllActMsg());
    	userBean.setAllowAllComment(user.isAllowAllComment());
    	userBean.setFollowMe(user.isFollowMe());
    	userBean.setAvatarLarge(user.getAvatarLarge());
    	userBean.setOnlineStatus(user.getOnlineStatus());
    	userBean.setBiFollowersCount(user.getBiFollowersCount());
    	userBean.setRemark(user.getRemark());
    	userBean.setLang(user.getLang());
    	userBean.setVerifiedReason(user.getVerifiedReason());
    	userBean.setWeihao(user.getWeihao());
    	userBean.setGeoEnabled(user.isGeoEnabled());
    	Date currentTime = Calendar.getInstance().getTime();
    	time = simpleDateFormat.format(currentTime);
    	userBean.setUpdateTime(Timestamp.valueOf(time));
    	
    	double timeSpan = (currentTime.getTime() - user.getCreatedAt().getTime()) / 1000 / 60 / 60 / 24 + 1;
    	if (user.getStatusesCount() > 0) {
    		double frequency = user.getStatusesCount() / timeSpan;
    		userBean.setStatusesFrequency(frequency);
    	} else {
    		userBean.setStatusesFrequency(0);
    	}
    	userBean.setIteration(0);
    	
    	return userBean;
	}
	
}
