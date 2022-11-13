package com.example.demo.utils.mapper;

import com.example.demo.model.data.Post;
import com.example.demo.model.dto.PostDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDTO toDTO(Post post);
    Post toEntity(PostDTO postDTO);

    List<PostDTO> toDTOs(List<Post> all);
    List<Post> toEntities(List<PostDTO> all);
}
