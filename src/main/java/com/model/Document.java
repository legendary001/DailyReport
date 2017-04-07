package com.model;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * <PRE>
 * 作用 : 
 *   
 * 使用 : 
 *   
 * 示例 :
 *   
 * 注意 :
 *   
 * 历史 :
 * -----------------------------------------------------------------------------
 *        VERSION          DATE           BY       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0          2014-5-21        liu_yi          create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public class Document implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private static final Pattern pattern = Pattern.compile("[^0-9]");

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String docId; // 文章id(imcp_id或url)

	 private String title; // 文章标题

	private Date date; // 文章时间

	private String hotLevel; // 文章热度

	private String docType; // 文章类型 (slide/video/doc/hdSlide)

	private String docChannel; // 文章所属频道信息

	private String why; // 解释描述(recommend/additional(数目不够，补充)等)

	private String score;

	private String hotBoost;

	private String readableFeatures;

	private String others;

	private String simId;

	private String revealType;

	private String source;

	private String recomToken;
	private String appVersion;

	public Document() {
	};

	public Document(String id_value, String title, Date date, String hot_level, String doc_type, String docChannel,
					String why, String score, String hotBoost, String readableFeatures, String others) {
		this.docId = id_value;
		this.title = title;
		this.date = date;
		this.hotLevel = hot_level;
		this.docType = doc_type;
		this.docChannel = docChannel;
		this.why = why;
		this.score = score;
		this.hotBoost = hotBoost;
		this.readableFeatures = readableFeatures;
		this.others = others;
	}
	public Document(String id_value, String title, Date date, String hot_level, String doc_type, String docChannel,
					String why, String score, String hotBoost, String readableFeatures, String others, String simId) {
		this.docId = id_value;
		this.title = title;
		this.date = date;
		this.hotLevel = hot_level;
		this.docType = doc_type;
		this.docChannel = docChannel;
		this.why = why;
		this.score = score;
		this.hotBoost = hotBoost;
		this.readableFeatures = readableFeatures;
		this.others = others;
		this.simId=simId;
	}
	public Document(String id_value, String title, Date date, String hot_level, String doc_type, String docChannel,
					String why, String score, String hotBoost, String readableFeatures, String others, String simId, String source) {
		this.docId = id_value;
		this.title = title;
		this.date = date;
		this.hotLevel = hot_level;
		this.docType = doc_type;
		this.docChannel = docChannel;
		this.why = why;
		this.score = score;
		this.hotBoost = hotBoost;
		this.readableFeatures = readableFeatures;
		this.others = others;
		this.simId=simId;
		this.source=source;
	}
	//
	// public String getDocId() {
	// return get(docId);
	// }

	// public String getTitle() {
	// return get(title);
	// }

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getHotLevel() {
		return get(hotLevel);
	}

	public void setHot_level(String hot_level) {
		this.hotLevel = hot_level;
	}

	public String getDocType() {
		return get(docType);
	}

	public void setDoc_type(String doc_type) {
		this.docType = doc_type;
	}

	public String getDocChannel() {
		if (docChannel != null && docChannel.indexOf("null") >= 0) {
			docChannel = "other";
		}
		return get(docChannel);
	}

	public void setDocChannel(String docChannel) {
		this.docChannel = docChannel;
	}

	public String getWhy() {
		return get(why);
	}

	public String getSimId() {
		return get(simId);
	}

	public void setWhy(String why) {
		this.why = why;
	}

	public String getScore() {
		return get(score);
	}

	public String getHotBoost() {
		return get(hotBoost);
	}

	public void setHotBoost(String hotBoost) {
		this.hotBoost = hotBoost;
	}

	public String getOthers() {
		return get(others);
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getReadableFeatures() {
		return get(readableFeatures);
	}

	/**
	 * others字段中第一个|!|前的数字串为被替换的相似id
	 * 
	 * @return
	 */
	public String getSimId(String others) {
		String simidStr = null;
		if (others != null && !others.isEmpty()) {
			if (others.indexOf("simID") > -1) {
				String firstStr = others.split("\\|!\\|")[0];
//				if (!firstStr.isEmpty() && firstStr.indexOf("_") > 0) {
//					simidStr = firstStr.split("_")[1];
//				} else {
//					simidStr = firstStr.split("=")[1];
//				}
				simidStr = firstStr.split("=")[1];
			}
		}
		if (simidStr == null) {
			// 置空,避免二次判断
			simidStr = "";
		}
		return simidStr;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setSimId(String simId) {
		this.simId = simId;
	}

	/**
	 * str不为null返回原值，否则返回""
	 * 
	 * @param str
	 * @return
	 */
	private String get(String str) {
		return (str == null ? "" : str);
	}

//	public static void main(String[] args) {
//		Document d = new Document();
//		System.out.println(d.getSimId(
//				"simID=1078292|!|url\u003dhttp://news.ifeng.com/a/20160803/49709783_0.shtml|!|ifeng ifengpc editor"));
//		System.out.println(d.getSimId(
//				"simID=clus_1078292|!|url\u003dhttp://news.ifeng.com/a/20160803/49709783_0.shtml|!|ifeng ifengpc editor"));
//		System.out.println(
//				d.getSimId("url\u003dhttp://news.ifeng.com/a/20160803/49709783_0.shtml|!|ifeng ifengpc editor"));
//		System.out.println(d.getSimId(""));
//	}

	@Override
	public String toString() {
//		return "Document [docType=" + docType + ", others=" + others + ", simId=" + simId + "]";
		return "Document [title=" + title + ", date=" + date + ", score=" + score + "]";
	}

	public void setReadableFeatures(String readableFeatures) {
		this.readableFeatures = readableFeatures;
	}

	public String getRevealType() {
		return revealType;
	}

	public void setRevealType(String revealType) {
		this.revealType = revealType;
	}

	@Override
	public Document clone(){
		return new Document(this.docId,
		this.title,
		this.date,
		this.hotLevel,
		this.docType,
		this.docChannel,
		this.why,
		this.score,
		this.hotBoost,
		this.readableFeatures,
		this.others, this.simId);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRecomToken() {
		return recomToken;
	}

	public void setRecomToken(String recomToken) {
		this.recomToken = recomToken;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
