package com.movie.plex.movies;

import java.sql.Date;

public class MovieDTO {
	private Long movieId;
	private String movieTitle;
	private String shortPoster;
	private String longPoster;
	private String video;
	private Date releaseDate;
	private String overView;
	private Double popularity;
	
	
	public Long getMovieId() {
		return movieId;
	}
	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public String getShortPoster() {
		return shortPoster;
	}
	public void setShortPoster(String shortPoster) {
		this.shortPoster = shortPoster;
	}
	public String getLongPoster() {
		return longPoster;
	}
	public void setLongPoster(String longPoster) {
		this.longPoster = longPoster;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getOverView() {
		return overView;
	}
	public void setOverView(String overView) {
		this.overView = overView;
	}
	
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public Double getPopularity() {
		return popularity;
	}
	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}
	
}
