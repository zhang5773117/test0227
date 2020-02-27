package com.zb.mapper;
import com.zb.pojo.QgGoodsMessage;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QgGoodsMessageMapper {

	public QgGoodsMessage getQgGoodsMessageById(@Param(value = "id") Long id)throws Exception;

	public List<QgGoodsMessage>	getQgGoodsMessageListByMap(Map<String,Object> param)throws Exception;

	public Integer getQgGoodsMessageCountByMap(Map<String,Object> param)throws Exception;

	public Integer insertQgGoodsMessage(QgGoodsMessage qgGoodsMessage)throws Exception;

	public Integer updateQgGoodsMessage(QgGoodsMessage qgGoodsMessage)throws Exception;

	public Integer updateQgGoodsMessageByUser(QgGoodsMessage qgGoodsMessage)throws Exception;



}
