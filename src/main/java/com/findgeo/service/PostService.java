package com.findgeo.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import com.findgeo.dto.PostsListResponseDto;
import com.findgeo.dto.PostsResponseDto;
import com.findgeo.dto.PostsSaveRequestDto;
import com.findgeo.entity.Posts;
import com.findgeo.repository.PostsRepository;
import com.findgeo.dto.PostsUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostsRepository postsRepository;
	
	@Transactional
	public Long save(PostsSaveRequestDto requestDto) {
		return postsRepository.save(requestDto.toPosts()).getBoardid();
	}
	
	@Transactional
	public List<PostsListResponseDto> findAllDesc(){
		return postsRepository.findAllDesc().stream()
				.map(posts -> new PostsListResponseDto(posts))
				.collect(Collectors.toList());
		// postsRepository 결과로 넘어온 posts의 스트림을 map을 통해 postsListResponseDto 변환 > list로 반환하는 코드
	}
	
	public PostsResponseDto findById(Long boardid) {
		Posts posts = postsRepository.findById(boardid)
				.orElseThrow(()->new IllegalArgumentException("해당 게시글이 업습니다. boardid="+ boardid));
		return new PostsResponseDto(posts);
	}
	
	@Transactional
	public Long update(Long boardid, PostsUpdateRequestDto requestDto) {
		Posts posts = postsRepository.findById(boardid)
					.orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. boardid=" + boardid));
		posts.update(requestDto.getBoardtitle(), requestDto.getBoardcontent());
		return boardid;
	}
	
	@Transactional
	public void delete(Long boardid) {
		Posts posts = postsRepository.findById(boardid)
				.orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. boardid=" + boardid));
		postsRepository.delete(posts);
	}
	
	/* Views Counting */
	@Transactional
	public int updateView(Long boardid) {
		return this.postsRepository.updateView(boardid);
	}

}
