package dev.cheerfun.pixivic.web.illust.mapper;

import dev.cheerfun.pixivic.common.model.Artist;
import dev.cheerfun.pixivic.common.model.Illustration;
import dev.cheerfun.pixivic.common.model.illust.ArtistPreView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IllustrationBizMapper {
    @Select("select * from illusts where illust_id = #{illustId} ")
    @Results({
            @Result(property = "id", column = "illust_id"),
            @Result(property = "artistPreView", column = "artist", javaType = ArtistPreView.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "tools", column = "tools", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "tags", column = "tags", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "imageUrls", column = "image_urls", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "tags", column = "tags", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class)
    })
    Illustration queryIllustrationByIllustId(String illustId);

    @Select("select * from illusts where artist_id = #{artistId} limit #{currIndex} , #{pageSize}")
    @Results({
            @Result(property = "id", column = "illust_id"),
            @Result(property = "artistPreView", column = "artist", javaType = ArtistPreView.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "tools", column = "tools", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "tags", column = "tags", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "imageUrls", column = "image_urls", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class),
            @Result(property = "tags", column = "tags", javaType = List.class, typeHandler = dev.cheerfun.pixivic.common.handler.JsonTypeHandler.class)
    })
    List<Illustration> queryIllustrationsByArtistId(String artistId, int currIndex, int pageSize);

    @Select("select * from artists where artist_id =#{artistId}")
    @Results({
            @Result(property = "id", column = "artist_id"),
    })
    Artist queryArtistById(String artistId);
}
