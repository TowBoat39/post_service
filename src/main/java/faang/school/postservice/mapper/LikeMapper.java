package faang.school.postservice.mapper;

import faang.school.postservice.dto.LikeEventDto;
import faang.school.postservice.dto.like.LikeDto;
import faang.school.postservice.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {

    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "post.id", target = "postId")
    LikeDto toDto(Like like);

    Like toEntity(LikeDto likeDto);

    @Mapping(source = "post.id", target = "postId")
    LikeEventDto toEventDto(Like like);
}