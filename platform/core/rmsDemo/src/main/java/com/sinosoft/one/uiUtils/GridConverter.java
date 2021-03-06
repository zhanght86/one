package com.sinosoft.one.uiUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.one.util.reflection.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-10-24
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class GridConverter<T> implements Converter<Gridable> {
    private static final String totalElement = "total";
    private static final String rowsElement = "rows";
    private static final String idElement = "id";
    private static final String cellElement = "cell";

    public String toJson(Gridable gridable) {
        Page page = gridable.getPage();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(totalElement, page.getTotalElements());
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonObject.put(rowsElement, addSubItemObject(gridable.getPage().getContent(), gridable));
        return jsonObject.toString();
    }

    private JSONArray addSubItemObject(Object children, Gridable gridable) {
        JSONArray jsonArray = new JSONArray();
        if (children instanceof Collection) {
            Collection subChildren = (Collection) children;
            for (Object obj : subChildren) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(idElement, BeanUtils.getProperty(obj, gridable.getIdField()));
                    List<String> attributeNames = gridable.getCellField();
                    if (attributeNames != null && attributeNames.size() > 0) {
                        JSONArray cellJsonArray = new JSONArray();
                        for (String attributeName : attributeNames) {
                            cellJsonArray.add(ReflectionUtils.getFieldValue(obj, attributeName));
                        }
                        jsonObject.put(cellElement, cellJsonArray);
                    }
                    jsonArray.add(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }

    public String toXml(Gridable gridable) {
        return null;
    }
}
