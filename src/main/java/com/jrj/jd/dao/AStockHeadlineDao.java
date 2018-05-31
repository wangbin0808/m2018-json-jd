package com.jrj.jd.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.jrj.jd.bean.AStockHeadline;

/**
 * @description A股票头条精华版 -- Dao层
 * @author qiushun.sun
 * @date 2018.03.14
 *
 */
public interface AStockHeadlineDao {

	/**
	 * 获取头条的列表
	 */
	// 测试sql
	/*@Select("SELECT iiid,title,makeDate,paperMediaSource,infoUrl,(SELECT imgUrl FROM INFO_IMG img WHERE img.iiid=item.iiid) AS imgUrl " +
			"from info_item item WHERE item.makeDate<=#{expireDate} ORDER BY item.makeDate DESC")
	*/
	// 上线sql
	@Select("SELECT iiid,title,makeDate,paperMediaSource,infoUrl,(SELECT imgUrl FROM INFO_IMG img WHERE img.iiid=item.iiid) AS imgUrl " +
			"from info_item item WHERE item.makeDate<=#{expireDate} AND ChanNum='010' AND InfoCls='001179' " +
			"ORDER BY item.makeDate DESC")
	Page<AStockHeadline> getHeadlinesDao(@Param("expireDate") String expireDate);

	/**
	 * 获取头条的具体信息 -- 预留接口
	 */
	@Select("")
	void getHeadlineContentDao();
	
	
}
