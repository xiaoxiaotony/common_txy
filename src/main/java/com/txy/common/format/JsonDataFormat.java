package com.txy.common.format;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.txy.common.bean.AreaInfoBean;
import com.txy.common.bean.BaseBean;
import com.txy.common.bean.ExceptionInfoBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.impl.DateJsonValueProcessor;
import com.txy.common.orm.query.impl.DynamicBean;

public class JsonDataFormat implements DataFormat
{
	
	private static final Logger logger = Logger.getLogger(JsonDataFormat.class);
	
	public JsonDataFormat()
	{
	}
	
	@Override
	public String errorMsg(String msg, String dataType)
	{
		ExceptionInfoBean bean = new ExceptionInfoBean();
		bean.setMsg(msg);
		bean.setSuccess(false);
		return JSONObject.fromObject(bean).toString();
	}
	
	/**
	 * 格式化方法
	 */
	@Override
	public String format(Object data, String dataType)
	{
		
		if (data instanceof TreeInfoBean || data instanceof AreaInfoBean)
		{
			return JSONArray.fromObject(data).toString();
		}
		if (data instanceof List && null != data)
		{
			return com.alibaba.fastjson.JSONArray.toJSONString(data, SerializerFeature.WriteDateUseDateFormat).toString();
		}
		BaseBean baseBean = new BaseBean();
		baseBean.setSuccess(true);
		// 处理pageList
		JsonConfig jf = new JsonConfig();
		jf.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(Constant.JSON_DATE_FORMAT_STR));
		if (data instanceof PageList)
		{
			handPageList(data);
			return JSONSerializer.toJSON(data, jf).toString();
		}
		baseBean.setData(data);
		logger.debug("http方式返回JSON数据:" + JSONSerializer.toJSON(baseBean, jf).toString());
		if(data == null){
			return JSONSerializer.toJSON(data,jf).toString();
		}
		return JSONSerializer.toJSON(baseBean, jf).toString();
	}
	
	/**
	 * 处理pageList
	 * 
	 * @param data
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handPageList(Object data)
	{
		if (((PageList) data).getData() != null)
		{
			List<Object> list = new ArrayList<Object>();
			List<DynamicBean> resultList = ((PageList) data).getData();
			for (int i = 0; i < resultList.size(); i++)
			{
				if (resultList.get(i) instanceof DynamicBean)
				{
					list.add(resultList.get(i).getMap());
				}
				else
				{
					list.add(resultList.get(i));
				}
			}
			((PageList) data).setRows(list);
			((PageList) data).setData(null);
		}
	}
}
