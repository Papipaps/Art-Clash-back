package com.example.demo.utils.mapper;

import com.example.demo.model.data.Comment;
import com.example.demo.model.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "post.id", target = "postId",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    CommentDTO toDTO(Comment comment);

    @Mapping(source = "postId", target = "post.id",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    Comment toEntity(CommentDTO commentDTO);

    List<Comment> toEntities(List<CommentDTO> commentDTOS);

    List<CommentDTO> toDTOs(List<Comment> comments);

}
