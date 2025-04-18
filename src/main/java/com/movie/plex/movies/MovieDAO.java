package com.movie.plex.movies;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.movie.plex.nestcontents.NestContentDTO;
import com.movie.plex.pages.MoviesPager;

@Repository
public class MovieDAO {
	
	@Autowired
	private SqlSession sqlSession;
	
	private final String NAMESPACE = "com.movie.plex.movies.MovieDAO.";
	
	public int addJsonList(List<MovieDTO> dtos) throws Exception{
		return sqlSession.insert(NAMESPACE + "addJsonList", dtos);
	}
	
	public List<MovieDTO> getMainList() throws Exception{
		return sqlSession.selectList(NAMESPACE + "getMainList");
	}
	
	public List<MovieDTO> getList() throws Exception{
		return sqlSession.selectList(NAMESPACE + "getList");
	}
	
	public MovieDTO getMovieTitle(MovieDTO movieDTO) throws Exception{
		return sqlSession.selectOne(NAMESPACE + "getMovieTitle", movieDTO);
	}
	
	public List<MovieDTO> getMoviesList(MoviesPager pager) throws Exception {
		return sqlSession.selectList(NAMESPACE + "getMoviesList", pager);
	}

	public MovieDTO getDetail(MovieDTO movieDTO) throws Exception{
		return sqlSession.selectOne(NAMESPACE + "getDetail", movieDTO);
	}
	
	public Long getTotalMovies(MoviesPager pager) throws Exception{
		return sqlSession.selectOne(NAMESPACE + "getTotalMovies", pager);
	}
}