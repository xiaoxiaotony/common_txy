package com.txy.common.api;

import com.txy.common.bean.Model;
/**
 * 处理接口
 * @author fei
 * @param <T>
 */
public interface Handler<T>
{
	public Object execute(Model model);
}
