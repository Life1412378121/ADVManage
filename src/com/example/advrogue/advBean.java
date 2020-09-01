package com.example.advrogue;

public class advBean {

	/**
	 * openAdv : {"openState":1,"imgUrl":
	 * "https://i01piccdn.sogoucdn.com/2e7d293791b00f10"
	 * ,"max_img_show_time":5,"videoUrl"
	 * :"http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8"
	 * ,"max_video_show_time":5} scrollTopText :
	 * {"startTime":0,"endTime":23,"colour"
	 * :"#ffffff","textSize":24,"content":"我是上边滚动文字广告"} scrollBottomText :
	 * {"startTime"
	 * :0,"endTime":23,"colour":"#ffffff","textSize":24,"content":"我是下边滚动文字广告"}
	 * top_left_img : {"wideth":200,"height":200,"imgUrl":
	 * "https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png"
	 * ,"showLength":5,"hideLength":5} top_right_img :
	 * {"wideth":200,"height":200,"imgUrl":
	 * "https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png"
	 * ,"showLength":5,"hideLength":5} bottom_left_img :
	 * {"wideth":200,"height":200,"imgUrl":
	 * "https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png"
	 * ,"showLength":5,"hideLength":5} bottom_right_img :
	 * {"wideth":200,"height":200,"imgUrl":
	 * "https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png"
	 * ,"showLength":5,"hideLength":5}
	 */

	public OpenAdvBean openAdv;
	public ScrollTopTextBean scrollTopText;
	public ScrollBottomTextBean scrollBottomText;
	public TopLeftImgBean top_left_img;
	public TopRightImgBean top_right_img;
	public BottomLeftImgBean bottom_left_img;
	public BottomRightImgBean bottom_right_img;

	public OpenAdvBean getOpenAdv() {
		return openAdv;
	}

	public void setOpenAdv(OpenAdvBean openAdv) {
		this.openAdv = openAdv;
	}

	public ScrollTopTextBean getScrollTopText() {
		return scrollTopText;
	}

	public void setScrollTopText(ScrollTopTextBean scrollTopText) {
		this.scrollTopText = scrollTopText;
	}

	public ScrollBottomTextBean getScrollBottomText() {
		return scrollBottomText;
	}

	public void setScrollBottomText(ScrollBottomTextBean scrollBottomText) {
		this.scrollBottomText = scrollBottomText;
	}

	public TopLeftImgBean getTop_left_img() {
		return top_left_img;
	}

	public void setTop_left_img(TopLeftImgBean top_left_img) {
		this.top_left_img = top_left_img;
	}

	public TopRightImgBean getTop_right_img() {
		return top_right_img;
	}

	public void setTop_right_img(TopRightImgBean top_right_img) {
		this.top_right_img = top_right_img;
	}

	public BottomLeftImgBean getBottom_left_img() {
		return bottom_left_img;
	}

	public void setBottom_left_img(BottomLeftImgBean bottom_left_img) {
		this.bottom_left_img = bottom_left_img;
	}

	public BottomRightImgBean getBottom_right_img() {
		return bottom_right_img;
	}

	public void setBottom_right_img(BottomRightImgBean bottom_right_img) {
		this.bottom_right_img = bottom_right_img;
	}

	public static class OpenAdvBean {
		/**
		 * openState : 1 imgUrl :
		 * https://i01piccdn.sogoucdn.com/2e7d293791b00f10 max_img_show_time : 5
		 * videoUrl : http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8
		 * max_video_show_time : 5
		 */

		public int openState;
		public String imgUrl;
		public int max_img_show_time;
		public String videoUrl;
		public int max_video_show_time;
		public String urgentContent;

		public String getUrgentContent() {
			return urgentContent;
		}

		public void setUrgentContent(String urgentContent) {
			this.urgentContent = urgentContent;
		}

		public int getOpenState() {
			return openState;
		}

		public void setOpenState(int openState) {
			this.openState = openState;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getMax_img_show_time() {
			return max_img_show_time;
		}

		public void setMax_img_show_time(int max_img_show_time) {
			this.max_img_show_time = max_img_show_time;
		}

		public String getVideoUrl() {
			return videoUrl;
		}

		public void setVideoUrl(String videoUrl) {
			this.videoUrl = videoUrl;
		}

		public int getMax_video_show_time() {
			return max_video_show_time;
		}

		public void setMax_video_show_time(int max_video_show_time) {
			this.max_video_show_time = max_video_show_time;
		}
	}

	public static class ScrollTopTextBean {
		/**
		 * startTime : 0 endTime : 23 colour : #ffffff textSize : 24 content :
		 * 我是上边滚动文字广告
		 */

		public int startTime;
		public int endTime;
		public String colour;
		public int textSize;
		public String content;
		public int interval;

		public int getInterval() {
			return interval;
		}

		public void setInterval(int interval) {
			this.interval = interval;
		}

		public int getStartTime() {
			return startTime;
		}

		public void setStartTime(int startTime) {
			this.startTime = startTime;
		}

		public int getEndTime() {
			return endTime;
		}

		public void setEndTime(int endTime) {
			this.endTime = endTime;
		}

		public String getColour() {
			return colour;
		}

		public void setColour(String colour) {
			this.colour = colour;
		}

		public int getTextSize() {
			return textSize;
		}

		public void setTextSize(int textSize) {
			this.textSize = textSize;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public static class ScrollBottomTextBean {
		/**
		 * startTime : 0 endTime : 23 colour : #ffffff textSize : 24 content :
		 * 我是下边滚动文字广告
		 */

		public int startTime;
		public int endTime;
		public String colour;
		public int textSize;
		public String content;
		public int interval;

		public int getInterval() {
			return interval;
		}

		public void setInterval(int interval) {
			this.interval = interval;
		}

		public int getStartTime() {
			return startTime;
		}

		public void setStartTime(int startTime) {
			this.startTime = startTime;
		}

		public int getEndTime() {
			return endTime;
		}

		public void setEndTime(int endTime) {
			this.endTime = endTime;
		}

		public String getColour() {
			return colour;
		}

		public void setColour(String colour) {
			this.colour = colour;
		}

		public int getTextSize() {
			return textSize;
		}

		public void setTextSize(int textSize) {
			this.textSize = textSize;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public static class TopLeftImgBean {
		/**
		 * wideth : 200 height : 200 imgUrl :
		 * https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png
		 * showLength : 5 hideLength : 5
		 */

		public int wideth;
		public int height;
		public String imgUrl;
		public int showLength;
		public int hideLength;

		public int getWideth() {
			return wideth;
		}

		public void setWideth(int wideth) {
			this.wideth = wideth;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getShowLength() {
			return showLength;
		}

		public void setShowLength(int showLength) {
			this.showLength = showLength;
		}

		public int getHideLength() {
			return hideLength;
		}

		public void setHideLength(int hideLength) {
			this.hideLength = hideLength;
		}
	}

	public static class TopRightImgBean {
		/**
		 * wideth : 200 height : 200 imgUrl :
		 * https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png
		 * showLength : 5 hideLength : 5
		 */

		public int wideth;
		public int height;
		public String imgUrl;
		public int showLength;
		public int hideLength;

		public int getWideth() {
			return wideth;
		}

		public void setWideth(int wideth) {
			this.wideth = wideth;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getShowLength() {
			return showLength;
		}

		public void setShowLength(int showLength) {
			this.showLength = showLength;
		}

		public int getHideLength() {
			return hideLength;
		}

		public void setHideLength(int hideLength) {
			this.hideLength = hideLength;
		}
	}

	public static class BottomLeftImgBean {
		/**
		 * wideth : 200 height : 200 imgUrl :
		 * https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png
		 * showLength : 5 hideLength : 5
		 */

		public int wideth;
		public int height;
		public String imgUrl;
		public int showLength;
		public int hideLength;

		public int getWideth() {
			return wideth;
		}

		public void setWideth(int wideth) {
			this.wideth = wideth;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getShowLength() {
			return showLength;
		}

		public void setShowLength(int showLength) {
			this.showLength = showLength;
		}

		public int getHideLength() {
			return hideLength;
		}

		public void setHideLength(int hideLength) {
			this.hideLength = hideLength;
		}
	}

	public static class BottomRightImgBean {
		/**
		 * wideth : 200 height : 200 imgUrl :
		 * https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png
		 * showLength : 5 hideLength : 5
		 */

		public int wideth;
		public int height;
		public String imgUrl;
		public int showLength;
		public int hideLength;

		public int getWideth() {
			return wideth;
		}

		public void setWideth(int wideth) {
			this.wideth = wideth;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getShowLength() {
			return showLength;
		}

		public void setShowLength(int showLength) {
			this.showLength = showLength;
		}

		public int getHideLength() {
			return hideLength;
		}

		public void setHideLength(int hideLength) {
			this.hideLength = hideLength;
		}
	}
}
